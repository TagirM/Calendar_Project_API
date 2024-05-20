package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;
import ru.tomsknipineft.repositories.BackfillSiteRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "backfillSiteCache")
public class BackfillSiteService implements EntityProjectService {

    private final BackfillSiteRepository backfillSiteRepository;

//    private BackfillSite findBackfillSiteFromRequest;

    /**
     * Поиск сущности в базе данных по введенным параметрам сущности из представления
     *
     * @param backfillSiteFromRequest сущность с введенными параметрами из представления
     * @return искомая в базе данных сущность
     */
    @Cacheable(key = "#backfillSiteFromRequest.square")
    public BackfillSite getFindBackfillSiteFromRequest(BackfillSite backfillSiteFromRequest) {
        if (backfillSiteFromRequest.isActive()) {
            return backfillSiteRepository.findFirstBySquareGreaterThanEqual(backfillSiteFromRequest.getSquare()).orElseThrow(() ->
                    new NoSuchEntityException("Введено некорректное значение площади " + backfillSiteFromRequest.getSquare()));
        }
        return null;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения полевых ИИ
     *
     * @param backfillSiteFromRequest Инженерная подготовка площадки из представления
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyBackfillSite(BackfillSite backfillSiteFromRequest) {
        return getFindBackfillSiteFromRequest(backfillSiteFromRequest).getResourceForEngSurvey();
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения ЛИ
     *
     * @param backfillSiteFromRequest Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForLabResearchBackfillSite(BackfillSite backfillSiteFromRequest) {
        if (getBackfillSiteCache() == null) {
            return getFindBackfillSiteFromRequest(backfillSiteFromRequest).getResourceForLabResearch();
        }
        return getBackfillSiteCache().getResourceForLabResearch();
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения отчета ИИ
     *
     * @param backfillSiteFromRequest Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyReportBackfillSite(BackfillSite backfillSiteFromRequest) {
        if (getBackfillSiteCache() == null) {
            return getFindBackfillSiteFromRequest(backfillSiteFromRequest).getResourceForEngSurveyReport();
        }
        return getBackfillSiteCache().getResourceForEngSurveyReport();
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки РД
     *
     * @param backfillSiteFromRequest Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForWorkDocBackfillSite(BackfillSite backfillSiteFromRequest) {
        if (getBackfillSiteCache() == null) {
            return getFindBackfillSiteFromRequest(backfillSiteFromRequest).getResourceForWorkDoc();
        }
        return getBackfillSiteCache().getResourceForWorkDoc();
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки ПД
     *
     * @param backfillSiteFromRequest Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForProjDocBackfillSite(BackfillSite backfillSiteFromRequest) {
        if (getBackfillSiteCache() == null) {
            return getFindBackfillSiteFromRequest(backfillSiteFromRequest).getResourceForProjDoc();
        }
        return getBackfillSiteCache().getResourceForProjDoc();
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки СД
     *
     * @param backfillSiteFromRequest Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEstDocBackfillSite(BackfillSite backfillSiteFromRequest) {
        if (getBackfillSiteCache() == null) {
            return getFindBackfillSiteFromRequest(backfillSiteFromRequest).getResourceForEstDoc();
        }
        return getBackfillSiteCache().getResourceForEstDoc();
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

    /**
     * Метод получения сущности из кэша, которая ранее была получена из базы данных по введенному параметру в представлении
     *
     * @return сущность из кэша
     */
    private BackfillSite getBackfillSiteCache() {
        CacheManager cacheManager = new ConcurrentMapCacheManager("backfillSiteCache");
        Cache cache = (Cache) Objects.requireNonNull(cacheManager.getCache("backfillSiteCache")).retrieve("#backfillSiteFromRequest.square");
        return (BackfillSite) Objects.requireNonNull(Objects.requireNonNull(cache).get("backfillSite")).get();
    }
}
