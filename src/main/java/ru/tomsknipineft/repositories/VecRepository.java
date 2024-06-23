package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tomsknipineft.entities.areaObjects.Vec;

import java.util.Optional;

@Repository
public interface VecRepository extends JpaRepository<Vec, Long> {

    /**
    Поиск сущности с площадью, наличием обустройства, мощностью и наличием склада, соответствующих заданным или ближайшими большими параметрами (если не boolean)
     */
    Optional<Vec> findFirstBySquareGreaterThanEqualAndArrangementVecAndPowerGreaterThanEqualAndStockExist(Double square, boolean arrangement, Integer power, boolean stock);
}
