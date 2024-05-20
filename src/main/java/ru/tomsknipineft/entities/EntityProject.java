package ru.tomsknipineft.entities;

import ru.tomsknipineft.entities.enumEntities.ObjectType;
import ru.tomsknipineft.entities.oilPad.BackfillWell;

/**
 * Интерефейс всех объектов проектирования
 */
public interface EntityProject {

    Integer getStage();

    void setStage(Integer stage);

    ObjectType getObjectType();

    void setObjectType(ObjectType objectType);
    boolean isActive();

    /**
     * Поиск в БД количества ресурса необходимого для выполнения полевых ИИ
     *
     * @return количество необходимого ресурса
     */
    Integer getResourceForEngSurvey();

    /**
     * Поиск в БД количества ресурса необходимого для выполнения ЛИ
     *
     * @return количество необходимого ресурса
     */
    Integer getResourceForLabResearch();

    /**
     * Поиск в БД количества ресурса необходимого для выполнения отчета ИИ
     *
     * @return количество необходимого ресурса
     */
    Integer getResourceForEngSurveyReport();

    /**
     * Поиск в БД количества ресурса необходимого для разработки РД
     *
     * @return количество необходимого ресурса
     */
    Integer getResourceForWorkDoc();

    /**
     * Поиск в БД количества ресурса необходимого для разработки ПД
     *
     * @return количество необходимого ресурса
     */
    Integer getResourceForProjDoc();

    /**
     * Поиск в БД количества ресурса необходимого для разработки СД
     *
     * @return количество необходимого ресурса
     */
    Integer getResourceForEstDoc();

}
