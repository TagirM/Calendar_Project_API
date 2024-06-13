package ru.tomsknipineft.entities.enumEntities;

/**
 * Сложность прокладка линейных коммуникаций: сложная, средняя, легкая
 */
public enum ComplexityOfGeology {
    DIFFICULT("Сложная геология"), MEDIUM("Геология средней сложности"), EASY("Простая геология");

    private final String title;

    ComplexityOfGeology(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
