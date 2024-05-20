package ru.tomsknipineft.entities.enumEntities;

/**
 * Тип СИКН: оперативного или коммерческого учета
 */
public enum SiknType {
    OPERATIONAL("Оперативного учета"), COMMERCIAL("Коммерческого учета");

    private final String title;

    SiknType(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
