package ru.tomsknipineft.entities;

import ru.tomsknipineft.entities.enumEntities.ComplexityOfGeology;
import ru.tomsknipineft.entities.enumEntities.GeodeticTeamType;

import java.time.LocalDate;

/**
 * Интерфейс, общий для всех объектов проектирования с данными по проекту, используется в записи в файл и восстановлении из файла данных проекта
 */
public interface DataFormProject {

    String getCodeContract();

    String getProjectName();

    LocalDate getStartContract();

    boolean isFieldEngineeringSurvey();

    boolean isEngineeringSurveyReport();

    boolean isMmg();

    boolean isRhrDoc();

    void setNotSzzDoc(boolean notSzzDoc);

    boolean isNotSzzDoc();

    void setEngineeringSurveyReport(boolean flag);

    GeodeticTeamType getGeodeticTeamType();

    Integer getGeodeticTeam();

    Integer getDrillingRig();

    Integer getHumanFactor();

    ComplexityOfGeology getComplexityOfGeology();
}
