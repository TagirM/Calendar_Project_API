package ru.tomsknipineft.entities.enumEntities;

/**
 * Тип бригады: стандартная, увеличенная
 */
public enum GeodeticTeamType {
    STANDARD("стандартного", 1), ENLARGED("расширенного", 1.5);

    private final String title;
    private final double value;

    GeodeticTeamType(String title, double value){
        this.title = title;
        this.value = value;
    }

    public String getTitle(){
        return title;
    }

    public double getValue() {
        return value;
    }
}
