package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tomsknipineft.entities.areaObjects.Sikn;
import ru.tomsknipineft.entities.enumEntities.SiknType;

import java.util.Optional;

public interface SiknRepository extends JpaRepository<Sikn, Long> {
    /**
     Поиск сущности с указанным типом и производительностью, соответствующей заданной или ближайшей большей
     */
    Optional<Sikn> findFirstBySiknTypeAndCapacityGreaterThanEqual(SiknType siknType, Double capacity);
}
