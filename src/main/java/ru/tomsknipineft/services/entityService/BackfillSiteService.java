package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;
import ru.tomsknipineft.repositories.BackfillSiteRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "backfillSiteCache")
public class BackfillSiteService implements EntityProjectService {

    private final BackfillSiteRepository backfillSiteRepository;

    /**
     * Поиск сущности в базе данных по введенным параметрам сущности из представления
     *
     * @param backfillSiteFromRequest сущность с введенными параметрами из представления
     * @return искомая в базе данных сущность
     */
    @Cacheable(key = "#backfillSiteFromRequest.square")
    public BackfillSite getFindBackfillSiteFromRequest(BackfillSite backfillSiteFromRequest) {
        return backfillSiteRepository.findFirstBySquareGreaterThanEqual(backfillSiteFromRequest.getSquare()).orElseThrow(() ->
                new NoSuchEntityException("Введено некорректное значение площади " + backfillSiteFromRequest.getSquare()));
    }

    /**
     * Получение сущности (Инженерная подготовка площадки) из БД
     *
     * @return сущность (Инженерная подготовка площадки)
     */
    public BackfillSite getFirst() {
        return backfillSiteRepository.findById(1L).orElseThrow(() ->
                new NoSuchEntityException("Площадка в базе данных отсутствует"));
    }
}
