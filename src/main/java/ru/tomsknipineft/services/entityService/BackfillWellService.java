package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.oilPad.BackfillWell;
import ru.tomsknipineft.repositories.BackfillWellRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "backfillWellCache")
public class BackfillWellService implements EntityProjectService {

    private final BackfillWellRepository backfillWellRepository;

    private final static long FIRST_ID = 1L;

    private static final Logger logger = LogManager.getLogger(BackfillWellService.class);

    /**
     * Поиск сущности в базе данных по введенным параметрам сущности из представления
     *
     * @param backfillWellFromRequest сущность с введенными параметрами из представления
     * @return искомая в базе данных сущность
     */
    @Cacheable(key = "#backfillWellFromRequest.well")
    public BackfillWell getFindBackfillWellFromRequest(BackfillWell backfillWellFromRequest) {
        return backfillWellRepository
                .findFirstByWellGreaterThanEqual(backfillWellFromRequest.getWell())
                .orElseThrow(() ->
                new NoSuchEntityException("Введено некорректное значение количества скважин " +
                        backfillWellFromRequest.getWell()));
    }

    /**
     * Получение сущности (Инженерная подготовка куста) из БД
     *
     * @return сущность (Инженерная подготовка куста)
     */
    public BackfillWell getFirst() {
        return backfillWellRepository
                .findById(FIRST_ID)
                .orElseThrow(() ->
                new NoSuchEntityException("Инженерная подготовка куста в базе данных отсутствует"));
    }

    /**
     * Метод очистки кэша после отработки данного класса и сохранения ресурсов для последующей проработки календаря проекта
     */
    @CacheEvict(allEntries = true)
    public void evictCacheCalendar(){
        logger.info("Очищен кэш BackfillWell");
    }
}
