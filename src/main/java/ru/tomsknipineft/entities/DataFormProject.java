package ru.tomsknipineft.entities;

import java.time.LocalDate;

/**
 * Интерфейс, общий для всех объектов проектирования с данными по проекту, используется в записи в файл и восстановлении из файла данных проекта
 */
public interface DataFormProject {

    String getCodeContract();

    LocalDate getStartContract();

    boolean isFieldEngineeringSurvey();

    boolean isEngineeringSurveyReport();

    Integer getDrillingRig();

    Integer getHumanFactor();
}
