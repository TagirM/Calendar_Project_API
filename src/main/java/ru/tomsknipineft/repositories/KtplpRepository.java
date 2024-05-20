package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tomsknipineft.entities.enumEntities.KtplpType;
import ru.tomsknipineft.entities.linearObjects.Ktplp;

import java.util.Optional;

@Repository
public interface KtplpRepository extends JpaRepository<Ktplp, Long> {
    /**
     Поиск сущности с указанным типом и количеством, соответствующим заданному или ближайшим большим
     */
    Optional<Ktplp> findFirstByKtplpTypeAndCount(KtplpType ktplpType, Integer count);

}