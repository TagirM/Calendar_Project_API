package ru.tomsknipineft.entities.enumEntities;

/**
 * Мощность ВЛ: 6 кВ, 10 кВ, 35 кВ, 110 кВ
 */

public enum LinePower {
    LINE6("6 кВ"), LINE10("10 кВ"), LINE35("35 кВ"), LINE110("110 кВ");

    private final String title;

    LinePower(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
