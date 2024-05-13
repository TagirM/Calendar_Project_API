package ru.tomsknipineft.entities.enumEntities;

/**
 * Тип КТПЛП: 6/0,4 кВ, 10/0,4 кВ, 35/0,4 кВ
 */
public enum KtplpType {
    KTPLP6_04("6/0,4 кВ"), KTPLP10_04("10/0,4 кВ"), KTPLP35_04("35/0,4 кВ");

    private final String title;

    KtplpType(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
