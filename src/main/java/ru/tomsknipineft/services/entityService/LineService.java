package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.linearObjects.Line;
import ru.tomsknipineft.repositories.LineRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "lineCache")
public class LineService implements EntityProjectService {

    private final LineRepository lineRepository;

    private final static long FIRST_ID = 1L;

    private static final Logger logger = LogManager.getLogger(LineService.class);

    /**
     * Поиск сущности в базе данных по введенным параметрам сущности из представления
     *
     * @param lineFromRequest сущность с введенными параметрами из представления
     * @return искомая в базе данных сущность
     */
    @Cacheable(key = "new org.springframework.cache.interceptor.SimpleKey(#lineFromRequest.power, #lineFromRequest.length)")
    public Line getFindLineFromRequest(Line lineFromRequest) {
        return lineRepository
                .findFirstByPowerAndLengthGreaterThanEqual(lineFromRequest.getPower(),
                        lineFromRequest.getLength()).orElseThrow(()->
                new NoSuchEntityException("Введены некорректные значения параметров ВЛ " +
                        lineFromRequest.getPower() +
                        " и " +
                        lineFromRequest.getLength()));
    }

    /**
     * Получение сущности (ЛЭП) из БД
     * @return сущность (ЛЭП)
     */
    public Line getFirst(){
        return lineRepository
                .findById(FIRST_ID)
                .orElseThrow(()->
                new NoSuchEntityException("ВЛ в базе данных отсутствует"));
    }

    /**
     * Метод очистки кэша после отработки данного класса и сохранения ресурсов для последующей проработки календаря проекта
     */
    @CacheEvict(allEntries = true)
    public void evictCacheCalendar(){
        logger.info("Очищен кэш Line");
    }
}
