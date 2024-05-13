package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tomsknipineft.entities.enumEntities.KtplpType;
import ru.tomsknipineft.entities.linearObjects.Ktplp;

import java.util.Optional;

@Repository
public interface KtplpRepository extends JpaRepository<Ktplp, Long> {
    /**
     Поиск сущности с видом, соответствующих заданным или ближайшими большими
     */
    Optional<Ktplp> findByKtplpType(KtplpType ktplpType);

}