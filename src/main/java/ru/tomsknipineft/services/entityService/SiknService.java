package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.areaObjects.Sikn;
import ru.tomsknipineft.repositories.SiknRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "siknCache")
public class SiknService implements EntityProjectService {

    private final SiknRepository siknRepository;

    private final static long FIRST_ID = 1L;

    private static final Logger logger = LogManager.getLogger(SiknRepository.class);

    /**
     * Поиск сущности в базе данных по введенным параметрам сущности из представления
     *
     * @param siknFromRequest сущность с введенными параметрами из представления
     * @return искомая в базе данных сущность
     */
    @Cacheable(key = "new org.springframework.cache.interceptor.SimpleKey(#siknFromRequest.siknType, #siknFromRequest.capacity)")
    public Sikn getFindSiknFromRequest(Sikn siknFromRequest) {
        // как мне кажется такое форматирование лучше, удобнее использовать
        // ПОПРАВИЛ ВЕЗДЕ

        return siknRepository
                .findFirstBySiknTypeAndCapacityGreaterThanEqual(siknFromRequest.getSiknType(),
                        siknFromRequest.getCapacity())
                .orElseThrow(()->
                new NoSuchEntityException("Введены некорректные значения типа " +
                        siknFromRequest.getSiknType() +
                        " или производительности " +
                        siknFromRequest.getCapacity()));
    }

    /**
     * Получение сущности (СИКН) из БД
     * @return сущность (СИКН)
     */
    @Override
    public Sikn getFirst() {
        return siknRepository
                .findById(FIRST_ID)
                .orElseThrow(()->
                new NoSuchEntityException("СИКН в базе данных отсутствует"));
    }

    /**
     * Метод очистки кэша после отработки данного класса и сохранения ресурсов для последующей проработки календаря проекта
     */
    @CacheEvict(allEntries = true)
    public void evictCacheCalendar(){
        logger.info("Очищен кэш Sikn");
    }
}
