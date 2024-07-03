package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.entities.DataFormProject;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.areaObjects.Mps;
import ru.tomsknipineft.entities.areaObjects.Sikn;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.entities.enumEntities.ComplexityOfGeology;
import ru.tomsknipineft.entities.enumEntities.ObjectType;
import ru.tomsknipineft.entities.linearObjects.*;
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;
import ru.tomsknipineft.entities.oilPad.OilWellPad;
import ru.tomsknipineft.repositories.CalendarRepository;
import ru.tomsknipineft.services.utilService.*;
import ru.tomsknipineft.utils.exceptions.NoSuchCalendarException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "CalendarsCache")
public class CalendarService {

    private final CalendarRepository calendarRepository;

    private static final Logger logger = LogManager.getLogger(BackfillWellGroupCalendarServiceImpl.class);

    // Константа с количеством дней необходимых изыскателям в поле на дорогу, составление и согласование актов полевых ИИ,
    // а также согласования топографии объекта проеткирования с проектировщиками и изыскателями
    final static int ENGINEERING_SURVEY_CONSTANTS_DAYS = 15;
    // коэффициент уменьшения ресурсов ИГИ если на территории нет ММГ
    final static int WITHOUT_MMG = 2;
    // Константа с количеством дней необходимых проектному офису для сбора и передачи документации заказчику с учетом всех процедур
    final static int PROJECT_OFFICE_DAYS = 2;
    // Константа с количеством дней, необходимых 2м проектным отделам для обмена заданиями в рамках РД
    final static int EXCHANGE_OF_TASKS_RD = 2;
    // Константа с количеством дней, необходимых 2м проектным отделам для обмена заданиями в рамках ПД
    final static int EXCHANGE_OF_TASKS_PD = 3;
    // Дата начала летнего сезона на Байкаловском НГКМ
    final static LocalDate BEGINNING_SUMMER_SEASON_BAIKAL = LocalDate.of(LocalDate.now().getYear(), Month.JUNE, 20);
    // Дата окончания летнего сезона на Байкаловском НГКМ
    final static LocalDate END_SUMMER_SEASON_BAIKAL = LocalDate.of(LocalDate.now().getYear(), Month.SEPTEMBER, 1);
    // Константа с количеством календарных дней необходимых для выдачи и согласования отчета ИЭИ
    final static int ENGINEERING_ENVIRONMENTAL_REPORT_DURATION = 120;
    // Константа с количеством календарных дней необходимых для выдачи и согласования ИКИ
    final static int HISTORICAL_CULTURAL_RESEARCH_DURATION = 120;
    // Константа с количеством календарных дней необходимых для согласования отчета ИИ
    final static int AGREEMENT_ENGINEERING_SURVEY_REPORT_DURATION = 60;
    // Константа с количеством календарных дней необходимых для согласования РД
    final static int AGREEMENT_WORK_DOC_DURATION = 60;
    // Константа с количеством календарных дней необходимых для согласования ПД
    final static int AGREEMENT_PROJECT_DOC_DURATION = 90;
    // Константа с количеством календарных дней необходимых для согласования СД
    final static int AGREEMENT_ESTIMATES_DOC_DURATION = 60;
    // Константа с количеством календарных дней необходимых для ГГЭ ПД
    final static int EXAMINATION_PROJECT_DOC_DURATION = 120;
    // Константа с количеством календарных дней необходимых для разработки ЗУР
    final static int LAND_DURATION = 150;
    // Константа с количеством рабочих дней/ресурсов, необходимых для разработки и утверждения РХР
    final static int RHR_DURATION = 90;
    // Константа с количеством рабочих дней/ресурсов, необходимых для получения материалов заказчиком и подготовки и согласования исполнительныхъ смет для актирования
    final static int PREPARING_MATERIALS_FOR_ACTIVATION = 7;

    /**
     * Получение всего списка календарных планов (различных этапов строительства) по шифру договора
     *
     * @param codeContract шифр договора
     * @return список календарных планов по различным этапам строительства
     */
    @Cacheable(key = "#codeContract", sync = true)
    public List<Calendar> getCalendarByCode(String codeContract) {
        return calendarRepository.findCalendarByCodeContract(codeContract)
                .orElseThrow(() -> new NoSuchCalendarException("Календарь по указанному шифру " + codeContract + " отсутствует в базе данных"));
    }

    /**
     * Получение всего списка наименований и шифров календарных планов из базы данных
     *
     * @return список календарных планов
     */
    public Set<String> getAllCalendars() {
        if (calendarRepository.findAll().size() != 0) {
            Set<String> calendarsName = new HashSet<>();
            List<Calendar> calendars = calendarRepository.findAll();
            for (Calendar calendar :
                    calendars) {
                calendarsName.add("ш." + calendar.getCodeContract() + " \"" + calendar.getProjectName() + "\";");
            }
            return calendarsName;
        } else {
            throw new NoSuchCalendarException("Календари в базе данных отсутствуют");
        }
    }

    /**
     * Метод формирования ExcelFile с календарем по шифру проекта (договора) из базы данных
     *
     * @param calendars календарь проекта (договора)
     * @return ExcelFile с наименованием файла и потоком байт
     */
    @Transactional
    public ExcelFile createFileCalendarExcel(List<Calendar> calendars) {
        return new ExcelFileCreateService().createFile(calendars, new ExcelTemplateCreateCalendarImpl());
    }

    /**
     * Метод формирования ExcelFile с графиком 1го уровня по шифру проекта (договора) из базы данных
     *
     * @param calendars календарь проекта (договора)
     * @return ExcelFile с наименованием файла и потоком байт
     */
    @Transactional
    public ExcelFile createFileFirstLevelScheduleExcel(List<Calendar> calendars) {
        return new ExcelFileCreateService().createFile(calendars, new ExcelTemplateCreateFirstLevelScheduleImpl());
    }

    /**
     * Метод получения данных проекта из базы данных
     *
     * @param calendars календарь проекта
     * @return данные проекта
     */
    @Transactional
    public DataFormProject getDataFormProject(List<Calendar> calendars) {
        if (calendars.size() == 0) {
            throw new NoSuchCalendarException("Календарь в базе данных отсутствует");
        }
        DataFormProjectService dataFormProjectService = new DataFormProjectService();
        DataFormProject dataFormProject;
        try {
            FileOutputStream f = new FileOutputStream(dataFormProjectService.getFilePathRecover());
            // todo нужно добавить проверку что список не пуст
            // поменял параметр на шифр проекта, добавил проверку calendars, который получаем, хотя calendars берется из метода
            // getCalendarByCode() класса CalendarService, в котором есть проверка на то, что календарь может оказаться пустым,
            // см. стр. 68 CalendarService и по идее там должна отрабатывать проверка, но она, что странно почему то, не отработывает... Хлотя должна же...

            f.write(calendars.get(0).getBytesDataProject());
            f.close();
            dataFormProject = dataFormProjectService.dataFormProjectRecover();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dataFormProject;
    }

    /**
     * Создание календарного плана договора с учетом всех этапов строительства
     *
     * @param objects               список сооружений объекта проектирования из контроллера
     * @param objectCalendarService сервис календаря объекта проектирования
     * @param dataFormProject       исходные данные объекта (проекта)
     * @return список календарных планов по всем этапам строительства
     */
    @Transactional
    @CachePut(key = "#dataFormProject.codeContract")
    public List<Calendar> createCalendar(List<EntityProject> objects, GroupObjectCalendarService objectCalendarService, DataFormProject dataFormProject) {
        if (dataFormProject.isFieldEngineeringSurvey()) {
            dataFormProject.setEngineeringSurveyReport(true);
        }
        // возвращаемый список календарей по шфиру со всем набором этапов строительства
        List<Calendar> calendars = new ArrayList<>();
        // сервис расчета дат
        DateService dateService = new DateService();
        // сервис по работе с DataFormProject (данными проекта)
        DataFormProjectService dataFormProjectService = new DataFormProjectService();
        //  Запись в файл данных о проекте
        dataFormProjectService.dataFormProjectSave(dataFormProject);

        // Переменные и даты метода
        // Инициализация переменной, хранящей количество календарных дней смещения начала ЛИ
        int stageOffsetLabII = 0;
        // Инициализация переменной, хранящей количество календарных дней смещения начала отчета ИИ
        int stageOffsetII = 0;
        // Инициализация переменной, хранящей количество календарных дней смещения начала разработки РД относительно предыдущей РД
        int stageOffsetRD = 0;
        // Инициализация переменной, хранящей количество календарных дней смещения начала разработки ПД предыдущей ПД
        int stageOffsetPD = 0;
        // Инициализация переменной, хранящей количество календарных дней смещения начала разработки СД
        int stageOffsetSD = 0;
        // Инициализация переменной, хранящей количество календарных дней смещения начала договора по каждому этапу
        int stageOffsetStartStage = 0;
        // Инициализация переменной, хранящей значение человеческого фактора
        int humanFactor = dataFormProject.getHumanFactor();
        // Инициализация переменной, хранящей дату окончания полевых ИИ
        LocalDate calendarDayFinishEngSurvey = null;
        // Инициализация переменной, хранящей дату начала ЛИ
        LocalDate calendarDayStartLabResearch = null;
        // Инициализация переменной, хранящей дату окончания ЛИ
        LocalDate calendarDayFinishLabResearch = null;
        // Инициализация переменной, хранящей дату начала отчета ИИ
        LocalDate calendarDayStartEngSurveyReport;
        // Инициализация переменной, хранящей дату окончания отчета ИИ
        LocalDate calendarDayFinishEngSurveyReport = null;
        // Инициализация переменной, хранящей дату окончания согласования отчета ИИ
        LocalDate finishAgreementEngineeringSurveyReport = null;
        // Инициализация переменной, хранящей дату начала выполнения ИЭИ и ИКИ
        LocalDate seasonalEngineeringSurveysStart = null;
        // Инициализация переменной, хранящей дату окончания согласования отчета ИЭИ текущего этапа строительства
        LocalDate engineeringAndEnvironmentalSurveysFinish = null;
        // Инициализация переменной, хранящей дату выдачи отчета ИКИ и справки ОКН текущего этапа строительства
        LocalDate historicalAndCulturalResearchFinish = null;
        // Инициализация переменной, хранящей дату начала РД текущего этапа строительства
        LocalDate calendarDayStartWorkDoc;
        // Инициализация переменной, хранящей дату окончания РД текущего этапа строительства
        LocalDate calendarDayFinishWorkDoc;
        // Инициализация переменной, хранящей дату начала ПД текущего этапа строительства
        LocalDate calendarDayStartProjDoc;
        // Инициализация переменной, хранящей дату окончания ПД текущего этапа строительства
        LocalDate calendarDayFinishProjDoc;
        // Инициализация переменной, хранящей дату начала СД текущего этапа строительства
        LocalDate calendarDayStartEstDoc;
        // Инициализация переменной, хранящей дату окончания СД текущего этапа строительства
        LocalDate calendarDayFinishEstDoc = null;
        // Инициализация переменной, хранящей дату утверждения РХР текущего этапа строительства
        LocalDate rhrFinish = null;
        // Инициализация переменной, хранящей дату утверждения СЗЗ текущего этапа строительства
        LocalDate szzFinish = null;

        // начало выполнения работ в соответствие с договором (соответствует началу полевых ИИ), при этом приводим
        // дату начала работ к рабочему дню (если выпал праздничный или выходной день) с помощью метода workDay
        LocalDate startContract = dateService.workDay(dataFormProject.getStartContract());

        // Получение списка только активных сооружений объекта проектирования
        List<EntityProject> activeObjects = objectCalendarService.listActiveEntityProject(objects);

        // Проверка условия, что полевые ИИ выполняются
        Map<Integer, Integer> resourcesEngGeodeticSurvey = null;
        Map<Integer, Integer> resourcesEngGeologicalSurvey = null;
        Map<Integer, Integer> resourcesLabResearch = null;
        if (dataFormProject.isFieldEngineeringSurvey()) {

            // Получение map с ресурсами необходимыми для выполнения полевых геодезических ИИ по каждому этапу строительства
            resourcesEngGeodeticSurvey = getResourcesEngGeodeticSurvey(activeObjects, objectCalendarService);

            // Получение map с ресурсами необходимыми для выполнения полевых геологических ИИ по каждому этапу строительства
            resourcesEngGeologicalSurvey = getResourcesEngGeologicalSurvey(activeObjects, objectCalendarService);

            // Получение map с ресурсами необходимыми для выполнения ЛИ по каждому этапу строительства
            resourcesLabResearch = getResourcesLabResearch(activeObjects, objectCalendarService);
        }

        // Проверка условия, что камеральные ИИ выполняются
        Map<Integer, Integer> resourcesEngSurveyReport = null;
        if (dataFormProject.isEngineeringSurveyReport()) {

            // Получение map с ресурсами необходимыми для разработки отчета ИИ по каждому этапу строительства с помощью метода в данном классе
            resourcesEngSurveyReport = getResourcesEngSurveyReport(activeObjects, objectCalendarService);
        }

        // Получение map с ресурсами необходимыми для РД по каждому этапу строительства
        Map<Integer, Integer> resourcesWorkDoc = getResourcesWorkDoc(activeObjects, objectCalendarService);

        // Получение map с ресурсами необходимыми для ПД по каждому этапу строительства
        Map<Integer, Integer> resourcesProjDoc = getResourcesProjDoc(activeObjects, objectCalendarService);

        // Получение map с ресурсами необходимыми для СД по каждому этапу строительства
        Map<Integer, Integer> resourcesEstDoc = getResourcesEstDoc(activeObjects, objectCalendarService);
        // дата начала работ по текущему этапу строительства
        LocalDate startStage = startContract;
        // Цикл, проходящий каждый этап строительства объекта проектирования
        int stageNumber = 1; // номер этапа строительства = 1
        for (int i = 0; i < resourcesWorkDoc.size(); i++) {

            // определяем текущий номер этапа строительства, если идут не по порядку или не с 1го
            while (resourcesWorkDoc.get(stageNumber) == null) {
                stageNumber++;
            }

            // пересчет необходимых ресурсов для разработки выполнения полевых ИИ и ЛИ с учетом человеческого фактора.
            int resourcesForTotalEngSurveyWithCorrectionsFactors;
            int resourcesForLabResearchWithHumanFactor;
            int resourcesForEngSurveyReportWithHumanFactor;
            if (resourcesEngGeodeticSurvey != null && resourcesEngGeologicalSurvey != null && resourcesLabResearch != null) {
                // Определение дат начала и окончания ИЭИ и ИКИ
                if (startContract.isBefore(LocalDate.of(startContract.getYear(),
                        BEGINNING_SUMMER_SEASON_BAIKAL.getMonth(),
                        BEGINNING_SUMMER_SEASON_BAIKAL.getDayOfMonth()))) {
                    seasonalEngineeringSurveysStart = dateService.workDay(LocalDate.of(startContract.getYear(),
                            BEGINNING_SUMMER_SEASON_BAIKAL.getMonth(),
                            BEGINNING_SUMMER_SEASON_BAIKAL.getDayOfMonth()));
                } else if (startContract.isBefore(LocalDate.of(startContract.getYear(), END_SUMMER_SEASON_BAIKAL.getMonth(), END_SUMMER_SEASON_BAIKAL.getDayOfMonth()))) {
                    seasonalEngineeringSurveysStart = startContract;
                } else {
                    seasonalEngineeringSurveysStart = dateService.workDay(LocalDate.of(startContract.plusYears(1).getYear(),
                            BEGINNING_SUMMER_SEASON_BAIKAL.getMonth(),
                            BEGINNING_SUMMER_SEASON_BAIKAL.getDayOfMonth()));
                }
                engineeringAndEnvironmentalSurveysFinish = dateService.recalculationResourcesInCalendarDate(ENGINEERING_ENVIRONMENTAL_REPORT_DURATION, seasonalEngineeringSurveysStart);
                historicalAndCulturalResearchFinish = dateService.recalculationResourcesInCalendarDate(HISTORICAL_CULTURAL_RESEARCH_DURATION, seasonalEngineeringSurveysStart);

                // При расчете ресурсов для полевых ИИ учитываются ресурсы геодезических и геологических ИИ с поправкой на размер
                // геодезической бригады (getGeodeticTeamType().getValue()), количество геодезических (getGeodeticTeam) и
                //  гелогических (getDrillingRig) бригад, а также с учетом наличия на территории зон с/без ММГ и
                //  с попракой на коэффициент человеческого фактора, форс-мажор, непредвиденных (humanFactor)
                int correctionFactorsForMMG = 1;
                if (!dataFormProject.isMmg()){
                    correctionFactorsForMMG = WITHOUT_MMG;
                }
                resourcesForTotalEngSurveyWithCorrectionsFactors = ((int) ((resourcesEngGeodeticSurvey.get(stageNumber) /
                        dataFormProject.getGeodeticTeamType().getValue() /
                        dataFormProject.getGeodeticTeam()) +
                        (resourcesEngGeologicalSurvey.get(stageNumber) /
                                dataFormProject.getDrillingRig())/correctionFactorsForMMG) *
                        (humanFactor + 100)) / 100 + ENGINEERING_SURVEY_CONSTANTS_DAYS;
                // Расчет ЛИ с попракой на коэффициент человеческого фактора, форс-мажор, непредвиденных (humanFactor)
                resourcesForLabResearchWithHumanFactor = (resourcesLabResearch.get(stageNumber) * (humanFactor + 100)) / 100;

                // Расчет дат окончания этапов работ договора, с учетом пересчета ресурса в календарные дни в каждом этапе строительства с учетом праздничных и
                // выходных дней (в нормальном режиме, без учета сжатых сроков проектирования) с помощью метода recalculationResourcesInCalendarDate

                // Дата окончания полевых ИИ текущего этапа
                calendarDayFinishEngSurvey = dateService.recalculationResourcesInCalendarDate(resourcesForTotalEngSurveyWithCorrectionsFactors, startStage);

                // проверка условия пересечения начала выполнения ЛИ (соответствует окончанию этапа полевых ИИ) текущего этапа строительства с
                // выполнением ЛИ предыдущего этапа, если пересечение есть, то срок сместить, чтобы ЛИ шли последовательно
                if (i > 0 && calendarDayFinishEngSurvey.isBefore(calendarDayFinishLabResearch)) {
                    // количество дней смещения выполнения ЛИ текущего этапа
                    stageOffsetLabII = (int) DAYS.between(calendarDayFinishEngSurvey, calendarDayFinishLabResearch);
                }
                // Дата начала ЛИ текущего этапа
                calendarDayStartLabResearch = dateService.workDay(calendarDayFinishEngSurvey.plusDays(stageOffsetLabII));
                // Дата окончания ЛИ текущего этапа
                calendarDayFinishLabResearch = dateService.recalculationResourcesInCalendarDate(resourcesForLabResearchWithHumanFactor,
                        calendarDayStartLabResearch);

                // проверка условия пересечения начала выполнения отчета ИИ (соответствует окончанию этапа ЛИ) текущего этапа строительства с
                // выполнением отчета ИИ предыдущего этапа, если пересечение есть, то срок сместить, чтобы отчеты ИИ шли последовательно
                if (i > 0 && calendarDayFinishLabResearch.isBefore(Objects.requireNonNull(calendarDayFinishEngSurveyReport))) {
                    // количество дней смещения выполнения отчета ИИ текущего этапа
                    stageOffsetII = (int) DAYS.between(calendarDayFinishLabResearch, calendarDayFinishEngSurveyReport);
                }
                // дата начала разработки отчета ИИ текущего этапа строительства с учетом смещения и проверкой на нерабочий день
                calendarDayStartEngSurveyReport = dateService.workDay(calendarDayFinishLabResearch.plusDays(stageOffsetII));
            } else {
                // если полевых ИИ нет, то камеральные начинаются со старта работ по договору
                calendarDayStartEngSurveyReport = startStage;
                calendarDayStartLabResearch = calendarDayStartEngSurveyReport;
            }
            if (resourcesEngSurveyReport != null) {
                // пересчет необходимых ресурсов для разработки отчета ИИ с учетом человеческого фактора
                resourcesForEngSurveyReportWithHumanFactor = (resourcesEngSurveyReport.get(stageNumber) * (humanFactor + 100)) / 100;
                // дата окончания разработки отчета ИИ текущего этапа строительства
                calendarDayFinishEngSurveyReport = dateService.recalculationResourcesInCalendarDate(resourcesForEngSurveyReportWithHumanFactor,
                        calendarDayStartEngSurveyReport);

                // дата окончания согласования отчета ИИ текущего этапа строительства
                finishAgreementEngineeringSurveyReport = dateService.workDay(calendarDayFinishEngSurveyReport.plusDays
                        (AGREEMENT_ENGINEERING_SURVEY_REPORT_DURATION));
                // дата начала РД текущего этапа строительства
                calendarDayStartWorkDoc = calendarDayFinishEngSurveyReport;
            } else {
                // если полевых и камеральных ИИ нет, то дата начала РД текущего этапа строительства соответствует старту работ по договору
                calendarDayStartWorkDoc = startStage;
            }

            // пересчет необходимых ресурсов для разработки РД с учетом человеческого фактора
            int resourcesForWorkDocWithHumanFactor = (resourcesWorkDoc.get(stageNumber) * (humanFactor + 100)) / 100 + PROJECT_OFFICE_DAYS;

            // пересчет необходимых ресурсов для разработки ПД с учетом человеческого фактора
            int resourcesForProjDocWithHumanFactor = (resourcesProjDoc.get(stageNumber) * (humanFactor + 100)) / 100 + PROJECT_OFFICE_DAYS;

            // пересчет необходимых ресурсов для разработки СД с учетом человеческого фактора
            int resourcesForEstDocWithHumanFactor = (resourcesEstDoc.get(stageNumber) * (humanFactor + 100)) / 100;

            // алгоритм проверки условия пересечения выполнения РД текущего этапа строительства с предыдущими РД
            // мэп с разбивкой типов объектов по стадиям строительства
            Map<Integer, List<ObjectType>> activeObjectTypeByStage = objectTypeByStage(activeObjects);
            // переменная, указывающая на необходимость (или отсуствие таковой) смещения ПСД
            boolean isStageOffsetPSD = false;
            // календарь предыдущих этапов строительства
            Calendar previousCalendar = null;
            int stageBack = 1;
            if (i > 0) {
                // проверяем, тип объекта AREA ли в текущем и предыдущих этапах
                // если типы объектов разные, то смещать не требуется, если встретились одинаковые - смещение возможно, если есть пересечение
                while (stageNumber - stageBack > 0) {
                    if (resourcesWorkDoc.containsKey(stageNumber - stageBack)){
                        if (activeObjectTypeByStage.get(stageNumber - stageBack).contains(ObjectType.AREA) && activeObjectTypeByStage.get(stageNumber).contains(ObjectType.AREA)) {
                            // получаем календарь найденного этапа с типом объекта AREA
                            previousCalendar = calendarRepository.findCalendarByCodeContractAndStage(dataFormProject.getCodeContract(), stageNumber - stageBack).orElseThrow();
                            isStageOffsetPSD = true;
                        }
                        // если пересечение одинаковых типов есть, то начало выполнения РД текущего этапа строительства сместить после окончания РД
                        // предыдущего (или предпред...идущего) этапа (дата очищена от приведения сроков к дате актирования, а также ресурсов необходимых
                        // для актирования)
                        if (isStageOffsetPSD && calendarDayStartWorkDoc.isBefore(previousCalendar.getRealWorkingFinish())) {
                            // количество дней смещения выполнения РД текущего этапа
                            stageOffsetRD = (int) DAYS.between(calendarDayStartWorkDoc, previousCalendar.getRealWorkingFinish());
                        }
                        // завершаем цикл, если нашелся этап с типом объекта AREA
                        if (isStageOffsetPSD) break;
                    }
                    stageBack++;
                }
            }
            // дата начала РД текущего этапа строительства с учетом смещения и проверкой на нерабочий день
            calendarDayStartWorkDoc = dateService.workDay(calendarDayStartWorkDoc.plusDays(stageOffsetRD));

            // расчет даты окончания РД текущего этапа строительства исходя из ресурсов с учетом поправки на сложность геологии в месте строительства
            calendarDayFinishWorkDoc = dateService.
                    recalculationResourcesInCalendarDate(resourcesForWorkDocWithHumanFactor +
                                    correctByComplexityOfGeology(activeObjects, stageNumber, dataFormProject.getComplexityOfGeology()),
                            calendarDayStartWorkDoc);
            // дата окончания согласования РД текущего этапа строительства
            LocalDate agreementWorkingFinish = dateService.workDay(calendarDayFinishWorkDoc.plusDays(AGREEMENT_WORK_DOC_DURATION));

            // дата окончания РД текущего этапа строительства = дате начала разработки смет и дате начала проектной документации текущего этапа
            // если пересечение одинаковых типов есть, то начало выполнения ПД текущего этапа строительства сместить после окончания ПД
            // предыдущего (или предпред..идущего) этапа (дата очищена от приведения сроков к дате актирования, а также ресурсов необходимывх
            // для актирования)
            if (isStageOffsetPSD && calendarDayFinishWorkDoc.isBefore(previousCalendar.getRealProjectFinish())) {
                // количество дней смещения выполнения ПД текущего этапа
                stageOffsetPD = (int) DAYS.between(calendarDayFinishWorkDoc, previousCalendar.getRealProjectFinish());
            }
            // дата начала разработки ПД текущего этапа строительства с учетом смещения (если разработка ПД предыдущего этапа не успела закончиться)
            calendarDayStartProjDoc = dateService.workDay(calendarDayFinishWorkDoc.plusDays(stageOffsetPD));
            // дата окончания разработки ПД текущего этапа строительства с учетом смещения
            calendarDayFinishProjDoc = dateService.recalculationResourcesInCalendarDate(resourcesForProjDocWithHumanFactor,
                    calendarDayStartProjDoc);
            // проверка что ИЭИ на момент выдачи ПД уже есть, иначе смещение срока выдачи ПД после ИЭИ
            if (engineeringAndEnvironmentalSurveysFinish != null && calendarDayFinishProjDoc.isBefore(engineeringAndEnvironmentalSurveysFinish)) {
                int shiftProjDoc = (int) DAYS.between(calendarDayFinishProjDoc, engineeringAndEnvironmentalSurveysFinish);
                calendarDayFinishProjDoc = dateService.workDay(calendarDayFinishProjDoc.plusDays(shiftProjDoc + 5));
            }

            // дата окончания согласования ПД текущего этапа строительства
            LocalDate agreementProjectFinish = dateService.workDay(calendarDayFinishProjDoc.plusDays(AGREEMENT_PROJECT_DOC_DURATION));

            // проверка условия пересечения выполнения СД текущего этапа строительства с предыдущим СД, если пересечение есть, то
            // начало выполнения СД текущего этапа строительства (соответствует окончанию этапа РД) сместить после окончания СД предыдущего этапа
            if (i > 0 && calendarDayFinishWorkDoc.isBefore(calendarDayFinishEstDoc)) {
                // количество дней смещения выполнения СД текущего этапа
                stageOffsetSD = (int) DAYS.between(calendarDayFinishWorkDoc, calendarDayFinishEstDoc);
            }
            // дата начала разработки СД текущего этапа строительства с учетом смещения (если разработка СД предыдущего этапа не успела закончиться)
            calendarDayStartEstDoc = dateService.workDay(calendarDayFinishWorkDoc.plusDays(stageOffsetSD));
            // дата окончания разработки СД текущего этапа строительства
            calendarDayFinishEstDoc = dateService.recalculationResourcesInCalendarDate(resourcesForEstDocWithHumanFactor,
                    calendarDayStartEstDoc);

            // дата окончания согласования СД текущего этапа строительства
            LocalDate agreementEstimatesFinish = dateService.workDay(calendarDayFinishEstDoc.plusDays(AGREEMENT_ESTIMATES_DOC_DURATION));

            // дата окончания разработки ЗУР текущего этапа строительства
            LocalDate landFinish = dateService.workDay(calendarDayFinishProjDoc.plusDays(LAND_DURATION));

            // дата окончания разработки РХР текущего этапа строительства
            if (dataFormProject.isRhrDoc()) {
                rhrFinish = dateService.recalculationResourcesInCalendarDate(RHR_DURATION, calendarDayFinishProjDoc);
            }

            // дата окончания разработки СЗЗ текущего этапа строительства
            if (dataFormProject.getClass() == DataFormOilPad.class) {
                for (EntityProject entity :
                        activeObjects) {
                    if (entity.getClass() == OilWellPad.class || entity.getClass() == Vvp.class) {
                        szzFinish = landFinish;
                        dataFormProject.setNotSzzDoc(true);
                        break;
                    }
                }
            } else if (dataFormProject.getClass() == DataFormLinearObjects.class) {
                for (EntityProject entity :
                        activeObjects) {
                    if (entity.getClass() == Sikn.class || entity.getClass() == Mps.class || entity.getClass() == Ktplp.class || entity.getClass() == Vvp.class) {
                        szzFinish = landFinish;
                        dataFormProject.setNotSzzDoc(true);
                        break;
                    }
                }
            }
            // дата окончания ГГЭ ПД текущего этапа строительства
            LocalDate examinationProjectFinish = dateService.workDay(agreementProjectFinish.plusDays(EXAMINATION_PROJECT_DOC_DURATION));

            // формирование календаря проекта с проверкой попадания даты позже 10го числа в декабре и 20го числа - в остальных месяцах
            // если дата попадает, то переносится на следующий месяц
            Calendar calendar = new Calendar();
            try {
                assert calendarDayFinishEngSurvey != null;
                calendar.setCodeContract(dataFormProject.getCodeContract())
                        .setProjectName(dataFormProject.getProjectName())
                        .setStartContract(startStage)
                        .setStage(stageNumber)
                        .setEngineeringSurvey(dateService.checkDeadlineForActivation(dateService.
                                recalculationResourcesInCalendarDate(PREPARING_MATERIALS_FOR_ACTIVATION, calendarDayFinishEngSurvey)))
                        .setEngineeringSurveyLabResearchAndReportStart(calendarDayStartLabResearch)
                        .setEngineeringSurveyReportFinish(dateService.checkDeadlineForActivation(dateService.
                                        recalculationResourcesInCalendarDate(PREPARING_MATERIALS_FOR_ACTIVATION, calendarDayFinishEngSurveyReport)))
                        .setAgreementEngineeringSurvey(dateService.checkDeadlineForActivation(dateService.
                                        recalculationResourcesInCalendarDate(PREPARING_MATERIALS_FOR_ACTIVATION, finishAgreementEngineeringSurveyReport)))
                        .setSeasonalEngineeringSurveysStart(seasonalEngineeringSurveysStart)
                        .setEngineeringAndEnvironmentalSurveysFinish(dateService.checkDeadlineForActivation(dateService.
                                        recalculationResourcesInCalendarDate(PREPARING_MATERIALS_FOR_ACTIVATION, engineeringAndEnvironmentalSurveysFinish)))
                        .setHistoricalAndCulturalResearchFinish(dateService.checkDeadlineForActivation(dateService.
                                        recalculationResourcesInCalendarDate(PREPARING_MATERIALS_FOR_ACTIVATION, historicalAndCulturalResearchFinish)))
                        .setWorkingStart(calendarDayStartWorkDoc)
                        .setRealWorkingFinish(calendarDayFinishWorkDoc)
                        .setWorkingFinish(dateService.checkDeadlineForActivation(dateService.
                                        recalculationResourcesInCalendarDate(PREPARING_MATERIALS_FOR_ACTIVATION, calendarDayFinishWorkDoc)))
                        .setAgreementWorking(dateService.checkDeadlineForActivation(dateService.
                                        recalculationResourcesInCalendarDate(PREPARING_MATERIALS_FOR_ACTIVATION, agreementWorkingFinish)))
                        .setEstimatesStart(calendarDayStartEstDoc)
                        .setEstimatesFinish(dateService.checkDeadlineForActivation(dateService.
                                        recalculationResourcesInCalendarDate(PREPARING_MATERIALS_FOR_ACTIVATION, calendarDayFinishEstDoc)))
                        .setAgreementEstimates(dateService.checkDeadlineForActivation(dateService.
                                        recalculationResourcesInCalendarDate(PREPARING_MATERIALS_FOR_ACTIVATION, agreementEstimatesFinish)))
                        .setProjectStart(calendarDayStartProjDoc)
                        .setRealProjectFinish(calendarDayFinishProjDoc)
                        .setProjectFinish(dateService.checkDeadlineForActivation(dateService.
                                        recalculationResourcesInCalendarDate(PREPARING_MATERIALS_FOR_ACTIVATION, calendarDayFinishProjDoc)))
                        .setAgreementProject(dateService.checkDeadlineForActivation(dateService.
                                        recalculationResourcesInCalendarDate(PREPARING_MATERIALS_FOR_ACTIVATION, agreementProjectFinish)))
                        .setExamination(dateService.checkDeadlineForActivation(dateService.
                                        recalculationResourcesInCalendarDate(PREPARING_MATERIALS_FOR_ACTIVATION, examinationProjectFinish)))
                        .setLandFinish(dateService.checkDeadlineForActivation(dateService.
                                        recalculationResourcesInCalendarDate(PREPARING_MATERIALS_FOR_ACTIVATION, landFinish)))
                        .setRhrFinish(dateService.checkDeadlineForActivation(dateService.
                                        recalculationResourcesInCalendarDate(PREPARING_MATERIALS_FOR_ACTIVATION, rhrFinish)))
                        .setSzzFinish(dateService.checkDeadlineForActivation(dateService.
                                        recalculationResourcesInCalendarDate(PREPARING_MATERIALS_FOR_ACTIVATION, szzFinish)))
                        .setHumanFactor(dataFormProject.getHumanFactor())
                        .setBytesDataProject(Files.readAllBytes(Paths.get(dataFormProjectService.getFilePathSave())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // проверка наличия в базе предыдущих календарей по данному шифру (на 1м этапе строительства), если есть, то удалить, чтобы не возникало конфликта календарей
            if (i == 0 && calendarRepository.findCalendarByCodeContract(dataFormProject.getCodeContract()).isPresent()) {
                calendarRepository.deleteAll(getCalendarByCode(dataFormProject.getCodeContract()));
            }
            // добавление календаря в общий лист календарей по шифру с разными этапами строительства
            calendars.add(calendarRepository.save(calendar));
            logger.info("Создан новый календарь " + calendar);

            // расчет количества дней смещения начала работ для следующего этапа строительства в зависимости от наличия полевых ИИ и отчета ИИ в договоре
            if (dataFormProject.isFieldEngineeringSurvey()) {
                stageOffsetStartStage = (int) DAYS.between(startStage, calendarDayFinishEngSurvey);
            } else if (dataFormProject.isEngineeringSurveyReport()) {
                stageOffsetStartStage = (int) DAYS.between(startStage, calendarDayFinishEngSurveyReport);
            } else {
                // проверка условия пересечения выполнения РД текущего этапа строительства с последующим РД
                boolean isStageOffsetStartContract = false;
                if (i < resourcesWorkDoc.size() - 1) {
                    // сначала проверяем какой тип объекта в последующем этапе и в текущем или предыдущих этапах.
                    // если типы объектов разные, то смещать начало работ не требуется, если одинаковые - смещение необходимо
                    stageBack = 0;
                    while (stageNumber - stageBack > 0) {
                        if (activeObjectTypeByStage.get(stageNumber - stageBack).contains(ObjectType.AREA) && activeObjectTypeByStage.get(stageNumber + 1).contains(ObjectType.AREA)) {
                            isStageOffsetStartContract = true;
                            break;
                        }
                        stageBack++;
                    }

                    // если пересечение одинкаовых типов AREA есть, то начало выполнения РД следующего этапа строительства сместить
                    // после окончания РД текущего или предыдущего (или предпред...идущего) этапа
                    if (isStageOffsetStartContract) {
                        // количество дней смещения выполнения РД следующего этапа
                        stageOffsetStartStage = (int) DAYS.between(startStage,
                                calendarRepository
                                        .findCalendarByCodeContractAndStage(dataFormProject.getCodeContract(), stageNumber - stageBack)
                                        .orElseThrow()
                                        .getWorkingFinish());
                    } else {
                        // иначе оставляем дату начала работ как для первого этапа строительства
                        startStage = calendarRepository
                                .findCalendarByCodeContractAndStage(dataFormProject.getCodeContract(), 1)
                                .orElseThrow()
                                .getStartContract();
                    }
                }
            }
            // расчет даты начала работ следующего этапа строительства
            startStage = startStage.plusDays(stageOffsetStartStage);
            // обнуление количества дней смещения начала работ, отчета ИИ и ПСД для их отработки в следующем этапе строительства
            stageOffsetLabII = 0;
            stageOffsetII = 0;
            stageOffsetRD = 0;
            stageOffsetPD = 0;
            stageOffsetSD = 0;
            stageOffsetStartStage = 0;
            // переход на следующий этап строительства
            stageNumber++;
        }
        return calendars;
    }

    /**
     * Метод очистки кэша после отработки данного класса и сохранения и выгрузки календаря (и/или графика 1го уровня)
     */
    @CacheEvict(allEntries = true)
    public void evictCacheCalendar() {
        logger.info("Очищен кэш с календарями");
    }

    /**
     * Метод получения мэп с разбивкой типов объектов (значение) по стадиям строительства (ключ)
     *
     * @param entityProjects сооружения проекта
     * @return мэп с разбивкой типов объектов о стадиям строительства
     */
    private Map<Integer, List<ObjectType>> objectTypeByStage(List<EntityProject> entityProjects) {
        Map<Integer, List<ObjectType>> objectTypeByStage = new HashMap<>();
        for (EntityProject entity :
                entityProjects) {
            if (objectTypeByStage.get(entity.getStage()) == null) {
                List<ObjectType> objectTypes = new ArrayList<>();
                objectTypes.add(entity.getObjectType());
                objectTypeByStage.put(entity.getStage(), objectTypes);
            } else {
                objectTypeByStage.get(entity.getStage()).add(entity.getObjectType());
            }
        }
        return objectTypeByStage;
    }

    /**
     * Расчет корректирующих дней к разработке РД с учетом сложности геологии на месте строительства
     *
     * @param entityProjects      набор сооружений в проекте
     * @param stageNumber         этап строительства
     * @param complexityOfGeology сложность геологии
     * @return колечество поправочных ресурсов, чел/дней
     */
    private int correctByComplexityOfGeology(List<EntityProject> entityProjects, int stageNumber, ComplexityOfGeology complexityOfGeology) {
        int correctResourceForWorkDoc = 0;
        for (EntityProject entity :
                entityProjects) {
            if (complexityOfGeology.equals(ComplexityOfGeology.EASY)) break;
            if (entity.getStage() == stageNumber && (entity.getClass() == Line.class || entity.getClass() == CableRack.class || entity.getClass() == Pipeline.class)) {
                if (complexityOfGeology.equals(ComplexityOfGeology.MEDIUM))
                    correctResourceForWorkDoc = correctResourceForWorkDoc + 3;
                else if (complexityOfGeology.equals(ComplexityOfGeology.DIFFICULT))
                    correctResourceForWorkDoc = correctResourceForWorkDoc + 5;
            }
        }
        return correctResourceForWorkDoc;
    }

    /**
     * Получение map с ресурсами необходиммыми для геодезических полевых ИИ по каждому этапу строительства (ключ - этап строительства, значение -
     * необходимый ресурс в ч/дн)
     *
     * @param activeObjects         список активных сооружений объекта проектирования
     * @param objectCalendarService класс-сервис объекта проектирования
     * @return ресурсы необходиммые для полевых ИИ по каждому этапу строительства
     */
    public Map<Integer, Integer> getResourcesEngGeodeticSurvey(List<EntityProject> activeObjects, GroupObjectCalendarService objectCalendarService) {

        Map<Integer, Integer> divisionResourcesEngSurveyByStage = new HashMap<>();
        for (EntityProject entity :
                activeObjects) {
            if (divisionResourcesEngSurveyByStage.containsKey(entity.getStage())) {
                divisionResourcesEngSurveyByStage.put(entity.getStage(), divisionResourcesEngSurveyByStage.get(entity.getStage())
                        + objectCalendarService.resourceForEngGeodeticSurveyStage(entity));
            } else {
                divisionResourcesEngSurveyByStage.put(entity.getStage(), objectCalendarService.resourceForEngGeodeticSurveyStage(entity));
            }
        }
        return divisionResourcesEngSurveyByStage;
    }

    /**
     * Получение map с ресурсами необходиммыми для геологических полевых ИИ по каждому этапу строительства (ключ - этап строительства, значение -
     * необходимый ресурс в ч/дн)
     *
     * @param activeObjects         список активных сооружений объекта проектирования
     * @param objectCalendarService класс-сервис объекта проектирования
     * @return ресурсы необходиммые для полевых ИИ по каждому этапу строительства
     */
    public Map<Integer, Integer> getResourcesEngGeologicalSurvey(List<EntityProject> activeObjects, GroupObjectCalendarService objectCalendarService) {

        Map<Integer, Integer> divisionResourcesEngSurveyByStage = new HashMap<>();
        for (EntityProject entity :
                activeObjects) {
            if (divisionResourcesEngSurveyByStage.containsKey(entity.getStage())) {
                divisionResourcesEngSurveyByStage.put(entity.getStage(), divisionResourcesEngSurveyByStage.get(entity.getStage())
                        + objectCalendarService.resourceForEngGeologicalSurveyStage(entity));
            } else {
                divisionResourcesEngSurveyByStage.put(entity.getStage(), objectCalendarService.resourceForEngGeologicalSurveyStage(entity));
            }
        }
        return divisionResourcesEngSurveyByStage;
    }

    /**
     * Получение map с ресурсами необходиммыми для ЛИ по каждому этапу строительства (ключ - этап строительства, значение - необходимый ресурс в ч/дн)
     *
     * @param activeObjects         список активных сооружений объекта проектирования
     * @param objectCalendarService класс-сервис объекта проектирования
     * @return ресурсы необходиммые для ЛИ по каждому этапу строительства
     */
    public Map<Integer, Integer> getResourcesLabResearch(List<EntityProject> activeObjects, GroupObjectCalendarService objectCalendarService) {
        Map<Integer, Integer> divisionResourcesLabResearchByStage = new HashMap<>();
        for (EntityProject entity :
                activeObjects) {
            if (divisionResourcesLabResearchByStage.containsKey(entity.getStage())) {
                divisionResourcesLabResearchByStage.put(entity.getStage(), divisionResourcesLabResearchByStage.get(entity.getStage())
                        + objectCalendarService.resourceForLabResearchStage(entity));
            } else {
                divisionResourcesLabResearchByStage.put(entity.getStage(), objectCalendarService.resourceForLabResearchStage(entity));
            }
        }
        return divisionResourcesLabResearchByStage;
    }

    /**
     * Получение map с ресурсами необходиммыми для отчета ИИ по каждому этапу строительства (ключ - этап строительства, значение -
     * необходимый ресурс в ч/дн)
     *
     * @param activeObjects         список активных сооружений объекта проектирования
     * @param objectCalendarService класс-сервис объекта проектирования
     * @return ресурсы необходиммые для отчета ИИ по каждому этапу строительства
     */
    public Map<Integer, Integer> getResourcesEngSurveyReport(List<EntityProject> activeObjects, GroupObjectCalendarService objectCalendarService) {
        Map<Integer, Integer> divisionResourcesEngSurveyReportByStage = new HashMap<>();
        for (EntityProject entity :
                activeObjects) {
            if (divisionResourcesEngSurveyReportByStage.containsKey(entity.getStage())) {
                divisionResourcesEngSurveyReportByStage.put(entity.getStage(), divisionResourcesEngSurveyReportByStage.get(entity.getStage()) +
                        (objectCalendarService.resourceForEngSurveyReportStage(entity) / 2));
            } else {
                divisionResourcesEngSurveyReportByStage.put(entity.getStage(), objectCalendarService.resourceForEngSurveyReportStage(entity));
            }
        }
        return divisionResourcesEngSurveyReportByStage;
    }

    /**
     * Получение map с ресурсами необходиммыми для РД по каждому этапу строительства (ключ - этап строительства, значение - необходимый ресурс в ч/дн)
     *
     * @param activeObjects         список активных сооружений объекта проектирования
     * @param objectCalendarService класс-сервис объекта проектирования
     * @return ресурсы необходиммые для РД по каждому этапу строительства
     */
    public Map<Integer, Integer> getResourcesWorkDoc(List<EntityProject> activeObjects, GroupObjectCalendarService objectCalendarService) {
        Map<Integer, Integer> divisionResourcesWorkDocByStage = new HashMap<>();
        for (EntityProject entity :
                activeObjects) {
            if (entity.getObjectType().equals(ObjectType.AREA)) {
                if (divisionResourcesWorkDocByStage.containsKey(entity.getStage())) {
                    divisionResourcesWorkDocByStage.put(entity.getStage(), divisionResourcesWorkDocByStage.get(entity.getStage()) +
                            objectCalendarService.resourceForWorkDocStage(entity));
                } else {
                    divisionResourcesWorkDocByStage.put(entity.getStage(), objectCalendarService.resourceForWorkDocStage(entity));
                }
            }
        }
        for (EntityProject entity :
                activeObjects) {
            if (entity.getObjectType().equals(ObjectType.LINEAR)) {
                if (!divisionResourcesWorkDocByStage.containsKey(entity.getStage())) {
                    divisionResourcesWorkDocByStage.put(entity.getStage(), objectCalendarService.resourceForWorkDocStage(entity));
                } else {
                    if (divisionResourcesWorkDocByStage.get(entity.getStage()) < objectCalendarService.resourceForWorkDocStage(entity)) {
                        divisionResourcesWorkDocByStage.put(entity.getStage(), objectCalendarService.resourceForWorkDocStage(entity));
                    }
                }
            }
        }
        // мэп с разбивкой типов объектов по стадиям строительства
        Map<Integer, List<ObjectType>> activeObjectTypeByStage = objectTypeByStage(activeObjects);
        for (Integer stage :
                activeObjectTypeByStage.keySet()) {
            int addResource = 0;
            int countObjectType = 0;
            if (activeObjectTypeByStage.get(stage).size() > 1 && activeObjectTypeByStage.get(stage).contains(ObjectType.LINEAR)) {
                for (ObjectType objectType :
                        activeObjectTypeByStage.get(stage)) {
                    if (objectType == ObjectType.LINEAR) {
                        countObjectType++;
                        addResource = addResource + EXCHANGE_OF_TASKS_RD * countObjectType;
                    }
                }
            }
            divisionResourcesWorkDocByStage.put(stage, divisionResourcesWorkDocByStage.get(stage) + addResource);
        }
        return divisionResourcesWorkDocByStage;
    }


    /**
     * Получение map с ресурсами необходиммыми для ПД по каждому этапу строительства (ключ - этап строительства, значение - необходимый ресурс в ч/дн)
     *
     * @param activeObjects         список активных сооружений объекта проектирования
     * @param objectCalendarService класс-сервис объекта проектирования
     * @return ресурсы необходиммые для ПД по каждому этапу строительства
     */
    public Map<Integer, Integer> getResourcesProjDoc(List<EntityProject> activeObjects, GroupObjectCalendarService objectCalendarService) {
        Map<Integer, Integer> divisionResourcesProjDocByStage = new HashMap<>();
        for (EntityProject entity :
                activeObjects) {
            if (!divisionResourcesProjDocByStage.containsKey(entity.getStage())) {
                divisionResourcesProjDocByStage.put(entity.getStage(), objectCalendarService.resourceForProjDocStage(entity));
            } else {
                if (divisionResourcesProjDocByStage.get(entity.getStage()) < objectCalendarService.resourceForProjDocStage(entity)) {
                    divisionResourcesProjDocByStage.put(entity.getStage(), objectCalendarService.resourceForProjDocStage(entity));
                }
            }
        }
        // мэп с разбивкой типов объектов по стадиям строительства
        Map<Integer, List<ObjectType>> activeObjectTypeByStage = objectTypeByStage(activeObjects);
        for (Integer stage :
                activeObjectTypeByStage.keySet()) {
            int addResource = 0;
            int countObjectType = 0;
            if (activeObjectTypeByStage.get(stage).size() > 1 && activeObjectTypeByStage.get(stage).contains(ObjectType.LINEAR)) {
                for (ObjectType objectType :
                        activeObjectTypeByStage.get(stage)) {
                    if (objectType == ObjectType.LINEAR) {
                        countObjectType++;
                        addResource = addResource + EXCHANGE_OF_TASKS_PD * countObjectType;
                    }
                }
            }
            divisionResourcesProjDocByStage.put(stage, divisionResourcesProjDocByStage.get(stage) + addResource);
        }
        return divisionResourcesProjDocByStage;
    }

    /**
     * Получение map с ресурсами необходиммыми для СД по каждому этапу строительства (ключ - этап строительства, значение - необходимый ресурс в ч/дн)
     *
     * @param activeObjects         список активных сооружений объекта проектирования
     * @param objectCalendarService класс-сервис объекта проектирования
     * @return ресурсы необходиммые для СД по каждому этапу строительства
     */
    public Map<Integer, Integer> getResourcesEstDoc(List<EntityProject> activeObjects, GroupObjectCalendarService objectCalendarService) {
        Map<Integer, Integer> divisionResourcesEstDocByStage = new HashMap<>();
        for (EntityProject entity :
                activeObjects) {
            if (divisionResourcesEstDocByStage.containsKey(entity.getStage())) {
                divisionResourcesEstDocByStage.put(entity.getStage(), divisionResourcesEstDocByStage.get(entity.getStage()) +
                        objectCalendarService.resourceForEstDocStage(entity));
            } else {
                divisionResourcesEstDocByStage.put(entity.getStage(), objectCalendarService.resourceForEstDocStage(entity));
            }
        }
        return divisionResourcesEstDocByStage;
    }
}
