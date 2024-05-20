package ru.tomsknipineft.services.entityService;

import ru.tomsknipineft.entities.EntityProject;

/**
 * Интерфейс для всех сервисов сущностей (сооружений) объекта проектирования
 */
public interface EntityProjectService {

    /**
     * Получение сущности (сооружения) объекта проектирования из БД
     * @return сущность (сооружение) объекта
     */
    EntityProject getFirst();

}
