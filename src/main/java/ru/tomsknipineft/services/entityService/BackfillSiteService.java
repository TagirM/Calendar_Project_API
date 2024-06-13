package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;
import ru.tomsknipineft.repositories.BackfillSiteRepository;
import ru.tomsknipineft.services.BackfillWellGroupCalendarServiceImpl;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "backfillSiteCache")
public class BackfillSiteService implements EntityProjectService {

    private final BackfillSiteRepository backfillSiteRepository;

    private final static long FIRST_ID = 1L;

    private static final Logger logger = LogManager.getLogger(BackfillSiteService.class);

    /**
     * Поиск сущности в базе данных по введенным параметрам сущности из представления
     *
     * @param backfillSiteFromRequest сущность с введенными параметрами из представления
     * @return искомая в базе данных сущность
     */
    @Cacheable(key = "#backfillSiteFromRequest.square")
    public BackfillSite getFindBackfillSiteFromRequest(BackfillSite backfillSiteFromRequest) {
        return backfillSiteRepository
                .findFirstBySquareGreaterThanEqual(backfillSiteFromRequest.getSquare())
                .orElseThrow(() ->
                new NoSuchEntityException("Введено некорректное значение площади " +
                        backfillSiteFromRequest.getSquare()));
    }

    /**
     * Получение сущности (Инженерная подготовка площадки) из БД
     *
     * @return сущность (Инженерная подготовка площадки)
     */
    public BackfillSite getFirst() {
        // константы вроде 1L лучше выносить в final static long FIRST_ID и везде его использовать
        // есть даже такой термин - магические константы
        // ПОПРАВИЛ ВЕЗДЕ

        return backfillSiteRepository
                .findById(FIRST_ID)
                .orElseThrow(() ->
                new NoSuchEntityException("Площадка в базе данных отсутствует"));
    }

    /**
     * Метод очистки кэша после отработки данного класса и сохранения ресурсов для последующей проработки календаря проекта
     */
    @CacheEvict(allEntries = true)
    public void evictCacheCalendar(){
        logger.info("Очищен кэш BackfillSite");
    }
}
