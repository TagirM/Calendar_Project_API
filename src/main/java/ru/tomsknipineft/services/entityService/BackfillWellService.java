package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.oilPad.BackfillWell;
import ru.tomsknipineft.repositories.BackfillWellRepository;
import ru.tomsknipineft.services.BackfillWellGroupCalendarServiceImpl;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "backfillWellCache")
public class BackfillWellService implements EntityProjectService {

    private final BackfillWellRepository backfillWellRepository;
//    private final CacheManager cacheManager;

    private static final Logger logger = LogManager.getLogger(BackfillWellService.class);

//    private BackfillWell findBackfillWellFromRequest;

    /**
     * Поиск сущности в базе данных по введенным параметрам сущности из представления
     *
     * @param backfillWellFromRequest сущность с введенными параметрами из представления
     * @return искомая в базе данных сущность
     */
    @Cacheable(key = "#backfillWellFromRequest.well")
    public BackfillWell getFindBackfillWellFromRequest(BackfillWell backfillWellFromRequest) {
//        if (backfillWellFromRequest.isActive()) {
        logger.info("Произошло обращение к методу getFindBackfillWellFromRequest");
        return backfillWellRepository.findFirstByWellGreaterThanEqual(backfillWellFromRequest.getWell()).orElseThrow(() ->
                new NoSuchEntityException("Введено некорректное значение количества скважин " + backfillWellFromRequest.getWell()));
//        }
//        return null;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения полевых ИИ
     *
     * @param backfillWellFromRequest Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
//    @Cacheable(key = "#backfillWellFromRequest.well")
//    public Integer getResourceForEngSurveyBackfillWell(BackfillWell backfillWellFromRequest) {
//        long startTime = System.currentTimeMillis();
//        logger.info("Получение сущности из БД " + getFindBackfillWellFromRequest(backfillWellFromRequest));
//        long executionTime = System.currentTimeMillis() - startTime;
////        logger.info("Получение сущности из БД заняло время " + executionTime);
//        return getFindBackfillWellFromRequest(backfillWellFromRequest).getResourceForEngSurvey();
//    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения ЛИ
     *
     * @param backfillWellFromRequest Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
//    public Integer getResourceForLabResearchBackfillWell(BackfillWell backfillWellFromRequest) {
//        if (getBackfillWellCache(backfillWellFromRequest) == null) {
//            logger.info("Кэш равен null");
//            return getFindBackfillWellFromRequest(backfillWellFromRequest).getResourceForLabResearch();
//        }
//        logger.info("Кэш не равен null");
//        return getBackfillWellCache(backfillWellFromRequest).getResourceForLabResearch();
//    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения отчета ИИ
     *
     * @param backfillWellFromRequest Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
//    public Integer getResourceForEngSurveyReportBackfillWell(BackfillWell backfillWellFromRequest) {
//        if (getBackfillWellCache(backfillWellFromRequest) == null) {
//            return getFindBackfillWellFromRequest(backfillWellFromRequest).getResourceForEngSurveyReport();
//        }
//        return getBackfillWellCache(backfillWellFromRequest).getResourceForEngSurveyReport();
//    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки РД
     *
     * @param backfillWellFromRequest Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
//    public Integer getResourceForWorkDocBackfillWell(BackfillWell backfillWellFromRequest) {
//        if (getBackfillWellCache(backfillWellFromRequest) == null) {
//            return getFindBackfillWellFromRequest(backfillWellFromRequest).getResourceForWorkDoc();
//        }
//        return getBackfillWellCache(backfillWellFromRequest).getResourceForWorkDoc();
//    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки ПД
     *
     * @param backfillWellFromRequest Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
//    public Integer getResourceForProjDocBackfillWell(BackfillWell backfillWellFromRequest) {
//        if (getBackfillWellCache(backfillWellFromRequest) == null) {
//            return getFindBackfillWellFromRequest(backfillWellFromRequest).getResourceForProjDoc();
//        }
//        return getBackfillWellCache(backfillWellFromRequest).getResourceForProjDoc();
//    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки СД
     *
     * @param backfillWellFromRequest Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
//    public Integer getResourceForEstDocBackfillWell(BackfillWell backfillWellFromRequest) {
//        if (getBackfillWellCache(backfillWellFromRequest) == null) {
//            return getFindBackfillWellFromRequest(backfillWellFromRequest).getResourceForEstDoc();
//        }
//        return getBackfillWellCache(backfillWellFromRequest).getResourceForEstDoc();
//    }

    /**
     * Получение сущности (Инженерная подготовка куста) из БД
     *
     * @return сущность (Инженерная подготовка куста)
     */
    public BackfillWell getFirst() {
        return backfillWellRepository.findById(1L).orElseThrow(() ->
                new NoSuchEntityException("Инженерная подготовка куста в базе данных отсутствует"));
    }

    /**
     * Метод получения сущности из кэша, которая ранее была получена из базы данных по введенному параметру в представлении
     *
     * @return сущность из кэша
     */
//    private BackfillWell getBackfillWellCache(BackfillWell backfillWellFromRequest) {
////        long startTime = System.currentTimeMillis();
////        logger.info("Зашли в метод кэша");
////         = new ConcurrentMapCacheManager("backfillWellCache");
////        logger.info("Отработал cacheManager " + cacheManager);
//        Cache cache = cacheManager.getCache("backfillWellCache");
//        Cache.ValueWrapper wrapper = cache.get(backfillWellFromRequest.getWell());
////        Cache.ValueWrapper wrapper = cache.get("#backfillWellFromRequest.well");
////        Cache.ValueWrapper wrapper = Objects.requireNonNull(cache).get("#backfillWellFromRequest.well");
//        logger.info("Отработал wrapper " + wrapper);
//        logger.info("Отработал wrapper.get() " + wrapper.get());
//
////        logger.info("Отработал cache " + cache);
////        logger.info("Отработал cache.get " + cache.get("#backfillWellFromRequest.well"));
////        logger.info("Отработал cache.get.get " + cache.get("backfillWell").get());
////        logger.info("Отработал cache.гет " + Objects.requireNonNull(cache).get("#backfillWellFromRequest.well"));
////        Cache cache1 = (Cache) Objects.requireNonNull(cacheManager.getCache("backfillWellCache")).retrieve("#backfillWellFromRequest.well");
////        logger.info("Отработал cache1 " + cache1);
////        BackfillWell backfillWell1 = (BackfillWell) Objects.requireNonNull(Objects.requireNonNull(cache).get("backfillWell")).get();
////        logger.info("Получение сущности из кэша " + backfillWell1);
////        BackfillWell backfillWell = (BackfillWell) Objects.requireNonNull(Objects.requireNonNull(cache1).get("backfillWell")).get();
//        BackfillWell backfillWell = (BackfillWell) Objects.requireNonNull(wrapper).get();
////                logger.info("Получение сущности из кэша " + backfillWell);
////        long executionTime = System.currentTimeMillis() - startTime;
////        logger.info("Получение сущности из кэша заняло время " + executionTime);
//        return backfillWell;
//    }
}
