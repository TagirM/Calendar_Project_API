package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.entities.DataFormProject;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.enumEntities.ObjectType;
import ru.tomsknipineft.entities.linearObjects.DataFormLinearObjects;
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;
import ru.tomsknipineft.repositories.CalendarRepository;
import ru.tomsknipineft.services.utilService.DataFormProjectService;
import ru.tomsknipineft.services.utilService.DateService;
import ru.tomsknipineft.services.utilService.ExcelCreatedService;
import ru.tomsknipineft.services.utilService.ExcelFile;
import ru.tomsknipineft.utils.exceptions.NoSuchCalendarException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "calendarsCache")
public class CalendarService {

    private final CalendarRepository calendarRepository;

    private static final Logger logger = LogManager.getLogger(BackfillWellGroupCalendarServiceImpl.class);

    // Константа с количеством дней необходимых проектному офису для сбора и передачи документации заказчику с учетом всех процедур
    final static int PROJECT_OFFICE_DAYS = 2;
    // Константа с количеством календарных дней необходимых для согласования отчета ИИ
    final static int AGREEMENT_ENGINEERING_SURVEY_REPORT_DURATION = 60;
    // Константа с количеством календарных дней необходимых для согласования РД
    final static int AGREEMENT_WORK_DOC_DURATION = 60;
    // Константа с количеством календарных дней необходимых для согласования ПД
    final static int AGREEMENT_PROJECT_DOC_DURATION = 60;
    // Константа с количеством календарных дней необходимых для согласования СД
    final static int AGREEMENT_ESTIMATES_DOC_DURATION = 60;
    // Константа с количеством календарных дней необходимых для ГГЭ ПД
    final static int EXAMINATION_PROJECT_DOC_DURATION = 120;
    // Константа с количеством календарных дней необходимых для разработки ЗУР
    final static int LAND_DURATION = 120;

    /**
     * Получение всего списка календарных планов (различных этапов строительства) по шифру договора
     *
     * @param code шифр договора
     * @return список календарных планов по различным этапам строительства
     */
    @Cacheable(key = "#code")
    public List<Calendar> getCalendarByCode(String code) {
//        logger.info("Зашли в метод получения календаря по шифру");
        return calendarRepository.findCalendarByCodeContract(code)
                .orElseThrow(() -> new NoSuchCalendarException("Календарь по указанному шифру " + code + " отсутствует в базе данных"));
    }

    /**
     * Получение всего списка календарных планов из базы данных
     *
     * @return список календарных планов
     */
    public List<Calendar> getAllCalendars() {
        if (calendarRepository.findAll().size() != 0) {
            return calendarRepository.findAll();
        } else {
            throw new NoSuchCalendarException("Календари в базе данных отсутствуют");
        }
    }

    /**
     * Метод формирования ExcelFile по шифру проекта (договора) из базы данных
     *
     * @param codeContract шифр проекта (договора)
     * @return ExcelFile с наименованием файла и потоком байт
     */
    @Transactional
    public ExcelFile createFile(String codeContract) {
        ExcelCreatedService excelCreatedService = new ExcelCreatedService();
//        logger.info("Кэшируется получения календаря по шифру");
        List<Calendar> calendars = getCalendarByCode(codeContract);
        return excelCreatedService.createFile(calendars);
    }

    /**
     * Метод получения данных проекта из базы данных
     *
     * @param codeContract шифр проекта
     * @return данные проекта
     */
    @Transactional
    public DataFormProject getDataFormProject(String codeContract) {
//        logger.info("Кэшируется получения календаря по шифру");
        List<Calendar> calendars = getCalendarByCode(codeContract);
        if (calendars.size() == 0) {
            throw new NoSuchCalendarException("Календарь по шифру " + codeContract + " в базе данных отсутствует");
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
    @CachePut(key = "#dataFormProject.codeContract")
    @Transactional
    public List<Calendar> createCalendar(List<EntityProject> objects, GroupObjectCalendarService objectCalendarService, DataFormProject dataFormProject) {
        if (dataFormProject.isFieldEngineeringSurvey()) {
            dataFormProject.setEngineeringSurveyReport(true);
        }
        List<Calendar> calendars = new ArrayList<>();
        DateService dateService = new DateService();
        DataFormProjectService dataFormProjectService = new DataFormProjectService();
        //  Запись в файл данных о проекте
        dataFormProjectService.dataFormProjectSave(dataFormProject);

        // Переменные и даты метода
        // Инициализация переменной, хранящей количество календарных дней смещения начала ЛИ
        int stageOffsetLabII = 0;
        // Инициализация переменной, хранящей количество календарных дней смещения начала отчета ИИ
        int stageOffsetII = 0;
        // Инициализация переменной, хранящей количество календарных дней смещения начала разработки РД
        int stageOffsetRD = 0;
        // Инициализация переменной, хранящей количество календарных дней смещения начала разработки ПД
        int stageOffsetPD = 0;
        // Инициализация переменной, хранящей количество календарных дней смещения начала разработки СД
        int stageOffsetSD = 0;
        // Инициализация переменной, хранящей количество календарных дней смещения начала договора по каждому этапу
        int stageOffsetStartContract = 0;
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
        // Инициализация переменной, хранящей дату начала РД текущего этапа строительства
        LocalDate calendarDayStartWorkDoc;
        // Инициализация переменной, хранящей дату окончания РД текущего этапа строительства
        LocalDate calendarDayFinishWorkDoc = null;
        // Инициализация переменной, хранящей дату окончания ПД текущего этапа строительства
        LocalDate calendarDayFinishProjDoc = null;
        // Инициализация переменной, хранящей дату окончания СД текущего этапа строительства
        LocalDate calendarDayFinishEstDoc = null;

        // начало выполнения работ в соответствие с договором (соответствует началу полевых ИИ), при этом приводим
        // дату начала работ к рабочему дню (если выпал праздничный или выходной день) с помощью метода workDay
        LocalDate startContract = dateService.workDay(dataFormProject.getStartContract());

        // Получение списка только активных сооружений объекта проектирования
        List<EntityProject> activeObjects = objectCalendarService.listActiveEntityProject(objects);

        // Проверка условия, что полевые ИИ выполняются
        Map<Integer, Integer> resourcesEngSurvey = null;
        Map<Integer, Integer> resourcesLabResearch = null;
        if (dataFormProject.isFieldEngineeringSurvey()) {

            // Получение map с ресурсами необходиммыми для выполнения полевых ИИ по каждому этапу строительства с помощью метода в данном классе
            resourcesEngSurvey = getResourcesEngSurvey(activeObjects, objectCalendarService);

            // Получение map с ресурсами необходиммыми для выполнения ЛИ по каждому этапу строительства с помощью метода в данном классе
            resourcesLabResearch = getResourcesLabResearch(activeObjects, objectCalendarService);
        }

        // Проверка условия, что камеральные ИИ выполняются
        Map<Integer, Integer> resourcesEngSurveyReport = null;
        if (dataFormProject.isEngineeringSurveyReport()) {

            // Получение map с ресурсами необходимыми для разработки отчета ИИ по каждому этапу строительства с помощью метода в данном классе
            resourcesEngSurveyReport = getResourcesEngSurveyReport(activeObjects, objectCalendarService);
        }

        // Получение map с ресурсами необходиммыми для РД по каждому этапу строительства с помощью метода в данном классе
        Map<Integer, Integer> resourcesWorkDoc = getResourcesWorkDoc(activeObjects, objectCalendarService);

        // Получение map с ресурсами необходиммыми для ПД по каждому этапу строительства с помощью метода в данном классе
        Map<Integer, Integer> resourcesProjDoc = getResourcesProjDoc(activeObjects, objectCalendarService);

        // Получение map с ресурсами необходиммыми для СД по каждому этапу строительства с помощью метода в данном классе
        Map<Integer, Integer> resourcesEstDoc = getResourcesEstDoc(activeObjects, objectCalendarService);

        // Цикл, проходящий каждый этап строительства объекта проектирования
        int stageNumber = 1;
        for (int i = 0; i < resourcesWorkDoc.size(); i++) {

            // определяем номер этапа строительства, если идут не по порядку или не с 1го
//            int stageNumber = i + 1;
            while (resourcesWorkDoc.get(stageNumber) == null) {
                stageNumber++;
            }

            // пересчет необходимых ресурсов для разработки выполнения полевых ИИ и ЛИ с учетом человеческого фактора.
            // При расчете ресурсов для полевых ИИ также учитывается количество буровых машин (getDrillingRig)
            int resourcesForEngSurveyWithHumanFactor;
            int resourcesForLabResearchWithHumanFactor;
            if (resourcesEngSurvey != null && resourcesLabResearch != null && resourcesEngSurveyReport != null) {
                logger.info("Ресурсы без ч/ф " + resourcesEngSurvey.get(stageNumber));
                resourcesForEngSurveyWithHumanFactor = ((int) ((resourcesEngSurvey.get(stageNumber) / drillingCorrectionFactor(dataFormProject))) *
                        (humanFactor + 100)) / 100;
                logger.info("Ресурсы с ч/ф и коэф на буровые " + resourcesForEngSurveyWithHumanFactor);
                resourcesForLabResearchWithHumanFactor = (resourcesLabResearch.get(stageNumber) * (humanFactor + 100)) / 100;

                // расчет дат окончания этапов работ договора, с учетом пересчета ресурса в календарные дни в каждом этапе строительства с учетом праздничных и
                // выходных дней (в нормальном режиме, без учета сжатых сроков проектирования) с помощью метода recalculationResourcesInCalendarDate

                // Дата окончания полевых ИИ текущего этапа
                calendarDayFinishEngSurvey = dateService.recalculationResourcesInCalendarDate(resourcesForEngSurveyWithHumanFactor, startContract);

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
                if (i > 0 && calendarDayFinishLabResearch.isBefore(calendarDayFinishEngSurveyReport)) {
                    // количество дней смещения выполнения отчета ИИ текущего этапа
                    stageOffsetII = (int) DAYS.between(calendarDayFinishLabResearch, calendarDayFinishEngSurveyReport);
                }
                // дата начала разработки отчета ИИ текущего этапа строительства с учетом смещения и проверкой на нерабочий день
                calendarDayStartEngSurveyReport = dateService.workDay(calendarDayFinishLabResearch.plusDays(stageOffsetII));
            } else {
//                calendarDayFinishEngSurvey = null;
                calendarDayStartEngSurveyReport = startContract;
            }

            // пересчет необходимых ресурсов для разработки отчета ИИ с учетом человеческого фактора
            int resourcesForEngSurveyReportWithHumanFactor;
            if (resourcesEngSurveyReport != null) {
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
//                calendarDayFinishEngSurveyReport = null;
//                finishAgreementEngineeringSurveyReport = null;
                // дата начала РД текущего этапа строительства
                calendarDayStartWorkDoc = startContract;
            }

            // пересчет необходимых ресурсов для разработки РД с учетом человеческого фактора
            int resourcesForWorkDocWithHumanFactor = (resourcesWorkDoc.get(stageNumber) * (humanFactor + 100)) / 100 + PROJECT_OFFICE_DAYS;

            // пересчет необходимых ресурсов для разработки ПД с учетом человеческого фактора
            int resourcesForProjDocWithHumanFactor = (resourcesProjDoc.get(stageNumber) * (humanFactor + 100)) / 100 + PROJECT_OFFICE_DAYS;

            // пересчет необходимых ресурсов для разработки СД с учетом человеческого фактора
            int resourcesForEstDocWithHumanFactor = (resourcesEstDoc.get(stageNumber) * (humanFactor + 100)) / 100;


            // проверка условия пересечения выполнения РД текущего этапа строительства с предыдущим РД
            boolean isStageOffsetPSD = false;
            if (i > 0) {
                // сначала проверяем какой тип объекта в предыдущем этапе и в текущем
                // если типы объектов разные, то смещать не требуется, если одинаковые - смещение необходимо
                Set<ObjectType> previousStages = new HashSet<>();
                Set<ObjectType> currentStages = new HashSet<>();
                for (EntityProject entity :
                        activeObjects) {
                    if (entity.getStage() == stageNumber - 1) {
                        previousStages.add(entity.getObjectType());
                    }
                    else if (entity.getStage() == stageNumber) {
                        currentStages.add(entity.getObjectType());
                    }
                    if (previousStages.contains(ObjectType.AREA) && currentStages.contains(ObjectType.AREA)){
                        isStageOffsetPSD = true;
                        break;
                    }
                }
                // если пересечение одинкаовых типов есть, то начало выполнения РД текущего этапа строительства сместить после окончания РД предыдущего этапа
                if (isStageOffsetPSD && calendarDayStartWorkDoc.isBefore(calendarDayFinishWorkDoc)) {
                    // количество дней смещения выполнения РД текущего этапа
                    stageOffsetRD = (int) DAYS.between(calendarDayStartWorkDoc, calendarDayFinishWorkDoc);
                }
            }
            // дата начала РД текущего этапа строительства с учетом смещения и проверкой на нерабочий день
            calendarDayStartWorkDoc = dateService.workDay(calendarDayStartWorkDoc.plusDays(stageOffsetRD));
            // дата окончания РД текущего этапа строительства = дате начала разработки смет и дате начала проектной документации текущего этапа
            calendarDayFinishWorkDoc = dateService.recalculationResourcesInCalendarDate(resourcesForWorkDocWithHumanFactor,
                    calendarDayStartWorkDoc);

            // дата окончания согласования РД текущего этапа строительства
            LocalDate agreementWorkingFinish = dateService.workDay(calendarDayFinishWorkDoc.plusDays(AGREEMENT_WORK_DOC_DURATION));

            // проверка условия пересечения выполнения ПД текущего этапа строительства с предыдущим ПД, если пересечение есть, то
            // начало выполнения ПД текущего этапа строительства (соответствует окончанию этапа РД) сместить после окончания ПД предыдущего этапа
            if (i > 0 && isStageOffsetPSD && calendarDayFinishWorkDoc.isBefore(calendarDayFinishProjDoc)) {
                // количество дней смещения выполнения ПД текущего этапа
                stageOffsetPD = (int) DAYS.between(calendarDayFinishWorkDoc, calendarDayFinishProjDoc);
            }

            // дата окончания разработки ПД текущего этапа строительства
            calendarDayFinishProjDoc = dateService.recalculationResourcesInCalendarDate(resourcesForProjDocWithHumanFactor,
                    calendarDayFinishWorkDoc.plusDays(stageOffsetPD));

            // дата окончания согласования ПД текущего этапа строительства
            LocalDate agreementProjectFinish = dateService.workDay(calendarDayFinishProjDoc.plusDays(AGREEMENT_PROJECT_DOC_DURATION));

            // проверка условия пересечения выполнения СД текущего этапа строительства с предыдущим СД, если пересечение есть, то
            // начало выполнения СД текущего этапа строительства (соответствует окончанию этапа РД) сместить после окончания СД предыдущего этапа
            if (i > 0 && calendarDayFinishWorkDoc.isBefore(calendarDayFinishEstDoc)) {
                // количество дней смещения выполнения СД текущего этапа
                stageOffsetSD = (int) DAYS.between(calendarDayFinishWorkDoc, calendarDayFinishEstDoc);
            }

            // дата окончания разработки СД текущего этапа строительства
            calendarDayFinishEstDoc = dateService.recalculationResourcesInCalendarDate(resourcesForEstDocWithHumanFactor,
                    calendarDayFinishWorkDoc.plusDays(stageOffsetSD));

            // дата окончания согласования СД текущего этапа строительства
            LocalDate agreementEstimatesFinish = dateService.workDay(calendarDayFinishEstDoc.plusDays(AGREEMENT_ESTIMATES_DOC_DURATION));

            // дата окончания разработки ЗУР текущего этапа строительства
            LocalDate landFinish = dateService.workDay(calendarDayFinishProjDoc.plusDays(LAND_DURATION));

            // дата окончания ГГЭ ПД текущего этапа строительства
            LocalDate examinationProjectFinish = dateService.workDay(agreementProjectFinish.plusDays(EXAMINATION_PROJECT_DOC_DURATION));

            // формирование календаря проекта с проверкой попадания даты позже 10го числа в декабре и 20го числа - в остальных месяцах
            // если дата попадает, то переносится на следующий месяц
            Calendar calendar = new Calendar();
            try {
                assert calendarDayFinishEngSurvey != null;
                calendar.setCodeContract(dataFormProject.getCodeContract())
                        .setProjectName(dataFormProject.getProjectName())
                        .setStartContract(startContract)
                        .setStage(stageNumber)
                        .setEngineeringSurvey(dateService.checkDeadlineForActivation(calendarDayFinishEngSurvey))
                        .setEngineeringSurveyLabResearchAndReportStart(calendarDayStartLabResearch)
                        .setEngineeringSurveyReportFinish(dateService.checkDeadlineForActivation(calendarDayFinishEngSurveyReport))
                        .setAgreementEngineeringSurvey(dateService.checkDeadlineForActivation(finishAgreementEngineeringSurveyReport))
                        .setWorkingStart(calendarDayStartWorkDoc)
                        .setWorkingFinish(dateService.checkDeadlineForActivation(calendarDayFinishWorkDoc))

                        .setEstimatesFinish(dateService.checkDeadlineForActivation(calendarDayFinishEstDoc))
                        .setProjectFinish(dateService.checkDeadlineForActivation(calendarDayFinishProjDoc))
                        .setLandFinish(dateService.checkDeadlineForActivation(landFinish))
                        .setAgreementWorking(dateService.checkDeadlineForActivation(agreementWorkingFinish))
                        .setAgreementProject(dateService.checkDeadlineForActivation(agreementProjectFinish))
                        .setAgreementEstimates(dateService.checkDeadlineForActivation(agreementEstimatesFinish))
                        .setExamination(dateService.checkDeadlineForActivation(examinationProjectFinish))
                        .setHumanFactor(dataFormProject.getHumanFactor())
                        .setBytesDataProject(Files.readAllBytes(Paths.get(dataFormProjectService.getFilePathSave())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // проверка наличия в базе предыдущих календарей по данному шифру, если есть, то удалить, чтобы не возникало конфликта календарей
            if (i == 0 && calendarRepository.findCalendarByCodeContract(dataFormProject.getCodeContract()).isPresent()) {
                calendarRepository.deleteAll(getCalendarByCode(dataFormProject.getCodeContract()));
            }
            calendars.add(calendarRepository.save(calendar));
            logger.info("Создан новый календарь " + calendar);

            // расчет количества дней смещения начала работ для следующего этапа строительства в зависимости от наличия полевых ИИ и отчета ИИ в договоре
            if (dataFormProject.isFieldEngineeringSurvey()) {
                stageOffsetStartContract = (int) DAYS.between(startContract, calendarDayFinishEngSurvey);
            } else if (dataFormProject.isEngineeringSurveyReport()) {
                stageOffsetStartContract = (int) DAYS.between(startContract, calendarDayFinishEngSurveyReport);
            } else {
                // проверка условия пересечения выполнения РД текущего этапа строительства с последующим РД
                boolean isStageOffsetStartContract = false;
                if (i<resourcesWorkDoc.size()-1){
                    // сначала проверяем какой тип объекта в последующем этапе и в текущем
                    // если типы объектов разные, то смещать начало работ не требуется, если одинаковые - смещение необходимо
                    Set<ObjectType> currentStages = new HashSet<>();
                    Set<ObjectType> nextStages = new HashSet<>();
                    for (EntityProject entity :
                            activeObjects) {
                        if (entity.getStage() == stageNumber) {
                            currentStages.add(entity.getObjectType());
                        }
                        else if (entity.getStage() == stageNumber +  1) {
                            nextStages.add(entity.getObjectType());
                        }
                        if (nextStages.contains(ObjectType.AREA) && currentStages.contains(ObjectType.AREA)){
                            isStageOffsetStartContract = true;
                            break;
                        }
                    }
                    // если пересечение одинкаовых типов есть, то начало выполнения РД следующего этапа строительства сместить после окончания РД текущего этапа
                    if (isStageOffsetStartContract) {
                        // количество дней смещения выполнения РД следующего этапа
                        stageOffsetStartContract = (int) DAYS.between(startContract, calendarDayFinishWorkDoc);
                    }
                }
            }
            // дата начала работ следующего этапа строительства
            startContract = startContract.plusDays(stageOffsetStartContract);
            // обнуление количества дней смещения начала работ, отчета ИИ и ПСД для их отработки в следующем этапе строительства
            stageOffsetLabII = 0;
            stageOffsetII = 0;
            stageOffsetRD = 0;
            stageOffsetPD = 0;
            stageOffsetSD = 0;
            stageOffsetStartContract = 0;
            // переход на следующий этап строительства
            stageNumber++;
        }
        return calendars;
    }

    /**
     * Метод рассчитывающий корректирующий коэффициент на полевые ИИ с учетом количества буровых машин
     *
     * @param dataFormProject тип объекта проектирования
     * @return корректирующий коэффициент на полевые ИИ
     */
    private double drillingCorrectionFactor(DataFormProject dataFormProject) {
        double drillingRig = 0;
        if (dataFormProject.getClass() == DataFormOilPad.class) {
            drillingRig = dataFormProject.getDrillingRig() * 0.85;
        } else if (dataFormProject.getClass() == DataFormLinearObjects.class) {
            drillingRig = dataFormProject.getDrillingRig() * 0.7;
        }
        return drillingRig;
    }

    /**
     * Получение map с ресурсами необходиммыми для полевых ИИ по каждому этапу строительства (ключ - этап строительства, значение -
     * необходимый ресурс в ч/дн)
     *
     * @param activeObjects         список активных сооружений объекта проектирования
     * @param objectCalendarService класс-сервис объекта проектирования
     * @return ресурсы необходиммые для полевых ИИ по каждому этапу строительства
     */
    public Map<Integer, Integer> getResourcesEngSurvey(List<EntityProject> activeObjects, GroupObjectCalendarService objectCalendarService) {

        Map<Integer, Integer> divisionResourcesEngSurveyByStage = new HashMap<>();
        for (EntityProject entity :
                activeObjects) {
            if (divisionResourcesEngSurveyByStage.containsKey(entity.getStage())) {
                divisionResourcesEngSurveyByStage.put(entity.getStage(), divisionResourcesEngSurveyByStage.get(entity.getStage())
                        + objectCalendarService.resourceForEngSurveyStage(entity));
            } else {
                divisionResourcesEngSurveyByStage.put(entity.getStage(), objectCalendarService.resourceForEngSurveyStage(entity));
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
