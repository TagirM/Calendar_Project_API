package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.entities.DataFormProject;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.enumEntities.ObjectType;
import ru.tomsknipineft.repositories.CalendarRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchCalendarException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    private final DateService dateService;

    private final DataFormProjectService dataFormProjectService;

    private static final Logger logger = LogManager.getLogger(BackfillWellGroupCalendarServiceImpl.class);

    // Константа с количеством дней необходимых проектному офису для сбора и передачи документации заказчику с учетом всех процедур
    final int PROJECT_OFFICE_DAYS = 2;
    // Константа с количеством календарных дней необходимых для согласования отчета ИИ
    final int AGREEMENT_ENGINEERING_SURVEY_REPORT_DURATION = 60;
    // Константа с количеством календарных дней необходимых для согласования РД
    final int AGREEMENT_WORK_DOC_DURATION = 60;
    // Константа с количеством календарных дней необходимых для согласования ПД
    final int AGREEMENT_PROJECT_DOC_DURATION = 60;
    // Константа с количеством календарных дней необходимых для согласования СД
    final int AGREEMENT_ESTIMATES_DOC_DURATION = 60;
    // Константа с количеством календарных дней необходимых для ГГЭ ПД
    final int EXAMINATION_PROJECT_DOC_DURATION = 120;
    // Константа с количеством календарных дней необходимых для разработки ЗУР
    final int LAND_DURATION = 120;

    /**
     * Получение всего списка календарных планов (различных этапов строительства) по шифру договора
     *
     * @param code шифр договора
     * @return список календарных планов по различным этапам строительства
     */
    public List<Calendar> getCalendarByCode(String code) {
        return calendarRepository.findCalendarByCodeContract(code)
                .orElseThrow(() -> new NoSuchCalendarException("Календарь по указанному шифру " + code + " отсутствует в базе данных"));
    }

    /**
     * Метод получения данных проекта из базы данных
     *
     * @param calendars календарь проекта
     * @return данные проекта
     */
    @Transactional
    public DataFormProject getDataFormProject(List<Calendar> calendars) {
        DataFormProject dataFormProject;
        try {
            FileOutputStream f = new FileOutputStream(dataFormProjectService.getFilePathRecover());
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
    public List<Calendar> createCalendar(List<EntityProject> objects, GroupObjectCalendarService objectCalendarService, DataFormProject dataFormProject) {
        List<Calendar> calendars = new ArrayList<>();

        //  Запись в файл данных о проекте
        this.dataFormProjectService.dataFormProjectSave(dataFormProject);

        // Переменные и даты метода        
        // Инициализация переменной, хранящей количество календарных дней смещения начала отчета ИИ
        int stageOffsetII = 0;
        // Инициализация переменной, хранящей количество календарных дней смещения начала разработки ПСД
        int stageOffsetPSD = 0;
        // Инициализация переменной, хранящей значение человеческого фактора
        int humanFactor = dataFormProject.getHumanFactor();
        // Инициализация переменной, хранящей дату окончания полевых ИИ
        LocalDate calendarDayFinishEngSurvey;
        // Инициализация переменной, хранящей дату окончания ЛИ
        LocalDate calendarDayFinishLabResearch;
        // Инициализация переменной, хранящей дату окончания отчета ИИ
        LocalDate calendarDayFinishEngSurveyReport = null;
        // Инициализация переменной, хранящей дату окончания согласования отчета ИИ
        LocalDate finishAgreementEngineeringSurveyReport;
        // Инициализация переменной, хранящей дату окончания РД
        LocalDate calendarDayFinishWorkDoc = null;

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
        for (int i = 0; i < resourcesWorkDoc.size(); i++) {

            // определяем номер этапа строительства, если идут не по порядку или не с 1го
            int stageNumber = i + 1;
            while (resourcesWorkDoc.get(stageNumber) == null) {
                stageNumber++;
            }

            // пересчет необходимых ресурсов для разработки выполнения полевых ИИ и ЛИ с учетом человеческого фактора.
            // При расчете ресурсов для полевых ИИ также учитывается количество буровых машин (getDrillingRig)
            int resourcesForEngSurveyWithHumanFactor;
            int resourcesForLabResearchWithHumanFactor;
            if (resourcesEngSurvey != null && resourcesLabResearch != null) {
                resourcesForEngSurveyWithHumanFactor = ((resourcesEngSurvey.get(stageNumber) / dataFormProject.getDrillingRig()) * (humanFactor + 100)) / 100;
                resourcesForLabResearchWithHumanFactor = (resourcesLabResearch.get(stageNumber) * (humanFactor + 100)) / 100;

                // расчет дат окончания этапов работ договора, с учетом пересчета ресурса в календарные дни в каждом этапе строительства с учетом праздничных и выходных дней
                // (в нормальном режиме, без учета сжатых сроков проектирования) с помощью метода recalculationResourcesInCalendarDate

                // Дата окончания полевых ИИ текущего этапа
                calendarDayFinishEngSurvey = dateService.recalculationResourcesInCalendarDate(resourcesForEngSurveyWithHumanFactor, startContract);

                // Дата окончания ЛИ текущего этапа
                calendarDayFinishLabResearch = dateService.recalculationResourcesInCalendarDate(resourcesForLabResearchWithHumanFactor, calendarDayFinishEngSurvey);

                // проверка условия пересечения начала выполнения отчета ИИ (соответствует окончанию этапа ЛИ) текущего этапа строительства с
                // выполнением отчета ИИ предыдущего этапа, если пересечение есть, то срок сместить, чтобы отчетов ИИ шли последовательно
                if (i > 0 && calendarDayFinishLabResearch.isBefore(calendarDayFinishEngSurveyReport)) {
                    // количество дней смещения выполнения отчета ИИ текущего этапа
                    stageOffsetII = (int) DAYS.between(calendarDayFinishLabResearch, calendarDayFinishEngSurveyReport);
                }
            } else {
                calendarDayFinishEngSurvey = startContract;
                calendarDayFinishLabResearch = calendarDayFinishEngSurvey;
            }

            // пересчет необходимых ресурсов для разработки отчета ИИ с учетом человеческого фактора
            int resourcesForEngSurveyReportWithHumanFactor;
            if (resourcesEngSurveyReport != null) {
                resourcesForEngSurveyReportWithHumanFactor = (resourcesEngSurveyReport.get(stageNumber) * (humanFactor + 100)) / 100;
                // дата окончания разработки отчета ИИ текущего этапа строительства с учетом смещения
                calendarDayFinishEngSurveyReport = dateService.recalculationResourcesInCalendarDate(resourcesForEngSurveyReportWithHumanFactor,
                        calendarDayFinishLabResearch.plusDays(stageOffsetII));

                // дата окончания согласования отчета ИИ текущего этапа строительства
                finishAgreementEngineeringSurveyReport = dateService.workDay(calendarDayFinishEngSurveyReport.plusDays(AGREEMENT_ENGINEERING_SURVEY_REPORT_DURATION));
            } else {
                calendarDayFinishEngSurveyReport = calendarDayFinishLabResearch;
                finishAgreementEngineeringSurveyReport = calendarDayFinishEngSurveyReport;
            }

            // пересчет необходимых ресурсов для разработки РД с учетом человеческого фактора
            int resourcesForWorkDocWithHumanFactor = (resourcesWorkDoc.get(stageNumber) * (humanFactor + 100)) / 100 + PROJECT_OFFICE_DAYS;

            // пересчет необходимых ресурсов для разработки ПД с учетом человеческого фактора
            int resourcesForProjDocWithHumanFactor = (resourcesProjDoc.get(stageNumber) * (humanFactor + 100)) / 100 + PROJECT_OFFICE_DAYS;

            // пересчет необходимых ресурсов для разработки СД с учетом человеческого фактора
            int resourcesForEstDocWithHumanFactor = (resourcesEstDoc.get(stageNumber) * (humanFactor + 100)) / 100;

            // дата начала РД текущего этапа строительства
            LocalDate startWorking = calendarDayFinishEngSurveyReport;
            // проверка условия пересечения выполнения РД текущего этапа строительства с предыдущим РД, если пересечение есть, то
            // начало выполнения РД текущего этапа строительства сместить после окончания РД предыдущего этапа
            if (i > 0 && startWorking.isBefore(calendarDayFinishWorkDoc)) {
                // количество дней смещения выполнения РД текущего этапа
                stageOffsetPSD = (int) DAYS.between(startWorking, calendarDayFinishWorkDoc);
            }
            // дата окончания РД текущего этапа строительства  с учетом смещения = дате начала разработки смет и дате начала проектной документации текущего этапа
            calendarDayFinishWorkDoc = dateService.recalculationResourcesInCalendarDate(resourcesForWorkDocWithHumanFactor, calendarDayFinishEngSurveyReport.plusDays(stageOffsetPSD));

            // дата окончания согласования РД текущего этапа строительства
            LocalDate agreementWorkingFinish = dateService.workDay(calendarDayFinishWorkDoc.plusDays(AGREEMENT_WORK_DOC_DURATION));

            // дата окончания разработки ПД текущего этапа строительства
            LocalDate calendarDayFinishProjDoc = dateService.recalculationResourcesInCalendarDate(resourcesForProjDocWithHumanFactor, calendarDayFinishWorkDoc);

            // дата окончания согласования ПД текущего этапа строительства
            LocalDate agreementProjectFinish = dateService.workDay(calendarDayFinishProjDoc.plusDays(AGREEMENT_PROJECT_DOC_DURATION));

            // дата окончания разработки СД текущего этапа строительства
            LocalDate calendarDayFinishEstDoc = dateService.recalculationResourcesInCalendarDate(resourcesForEstDocWithHumanFactor, calendarDayFinishWorkDoc);

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
                calendar.setCodeContract(dataFormProject.getCodeContract()).setStartContract(startContract)
                        .setStage(stageNumber)
                        .setEngineeringSurvey(dateService.checkDeadlineForActivation(calendarDayFinishEngSurvey))
                        .setEngineeringSurveyReport(dateService.checkDeadlineForActivation(calendarDayFinishEngSurveyReport))
                        .setAgreementEngineeringSurvey(dateService.checkDeadlineForActivation(finishAgreementEngineeringSurveyReport))
                        .setWorkingStart(dateService.checkDeadlineForActivation(startWorking))
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

            // расчет смещения начала работ для следующего этапа строительства в зависимости от наличия полевых ИИ и отчета ИИ в договоре
            if (dataFormProject.isFieldEngineeringSurvey()) {
                startContract = calendarDayFinishEngSurvey;
            } else if (dataFormProject.isEngineeringSurveyReport()) {
                startContract = calendarDayFinishEngSurveyReport;
            } else {
                startContract = calendarDayFinishWorkDoc;
            }
            // обнуления количества дней смещения отчета ИИ и РД за счет наложения этапов, для следующего этапа строительства
            stageOffsetII = 0;
            stageOffsetPSD = 0;
        }
        return calendars;
    }

    /**
     * Получение map с ресурсами необходиммыми для полевых ИИ по каждому этапу строительства (ключ - этап строительства, значение - необходимый ресурс в ч/дн)
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
     * Получение map с ресурсами необходиммыми для отчета ИИ по каждому этапу строительства (ключ - этап строительства, значение - необходимый ресурс в ч/дн)
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
