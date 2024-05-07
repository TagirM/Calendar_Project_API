package ru.tomsknipineft.services;

import ru.tomsknipineft.entities.EntityProject;

import java.util.List;

/**
 * Интерфейс для календарей всех объектов проектирования
 */
public interface GroupObjectCalendarService {

    /**
     * Получение количества ресурса, необходимого для выполнения полевых ИИ сущности (сооружения) объекта проектирования
     *
     * @param entityProject сущность объекта кустовой площадки
     * @return количество ресурса, необходимого для проектирования сущности
     */
    Integer resourceForEngSurveyStage(EntityProject entityProject);

    /**
     * Получение количества ресурса, необходимого для выполнения ЛИ сущности (сооружения) объекта проектирования
     *
     * @param entityProject сущность объекта кустовой площадки
     * @return количество ресурса, необходимого для проектирования сущности
     */
    Integer resourceForLabResearchStage(EntityProject entityProject);

    /**
     * Получение количества ресурса, необходимого для разработки отчета ИИ сущности (сооружения) объекта проектирования
     *
     * @param entityProject сущность объекта кустовой площадки
     * @return количество ресурса, необходимого для проектирования сущности
     */
    Integer resourceForEngSurveyReportStage(EntityProject entityProject);

    /**
     * Получение количества ресурса, необходимого для разработки РД сущности (сооружения) объекта проектирования
     *
     * @param entityProject сущность объекта кустовой площадки
     * @return количество ресурса, необходимого для проектирования сущности
     */
    Integer resourceForWorkDocStage(EntityProject entityProject);

    /**
     * Получение количества ресурса, необходимого для разработки ПД сущности (сооружения) объекта проектирования
     *
     * @param entityProject сущность объекта кустовой площадки
     * @return количество ресурса, необходимого для проектирования сущности
     */
    Integer resourceForProjDocStage(EntityProject entityProject);

    /**
     * Получение количества ресурса, необходимого для разработки СД сущности (сооружения) объекта проектирования
     *
     * @param entityProject сущность объекта кустовой площадки
     * @return количество ресурса, необходимого для проектирования сущности
     */
    Integer resourceForEstDocStage(EntityProject entityProject);

    /**
     * Получение списка только активных сущностей (сооружений) объекта проектирования из представления
     *
     * @param entityProjects сущность (сооружение) объекта проектирования
     * @return список активных сущностей (сооружений)
     */
    List<EntityProject> listActiveEntityProject(List<EntityProject> entityProjects);
}
