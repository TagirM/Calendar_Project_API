package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.linearObjects.Ktplp;
import ru.tomsknipineft.repositories.KtplpRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "ktplpCache")
public class KtplpService implements EntityProjectService {

    private final KtplpRepository ktplpRepository;

    private final static long FIRST_ID = 1L;

    private static final Logger logger = LogManager.getLogger(KtplpService.class);

    /**
     * Поиск сущности в базе данных по введенным параметрам сущности из представления
     *
     * @param ktplpFromRequest сущность с введенными параметрами из представления
     * @return искомая в базе данных сущность
     */
    @Cacheable(key = "new org.springframework.cache.interceptor.SimpleKey(#ktplpFromRequest.ktplpType, #ktplpFromRequest.count)")
    public Ktplp getFindKtplpFromRequest(Ktplp ktplpFromRequest) {
        return ktplpRepository
                .findFirstByKtplpType(ktplpFromRequest.getKtplpType())
                .orElseThrow(()->
                new NoSuchEntityException("Введены некорректные значения типа " +
                        ktplpFromRequest.getKtplpType() +
                        " или количества " +
                        ktplpFromRequest.getCount()));
    }

    /**
     * Получение сущности (КТПЛП) из БД
     * @return сущность (КТПЛП)
     */
    public Ktplp getFirst(){
        return ktplpRepository
                .findById(FIRST_ID)
                .orElseThrow(()->
                new NoSuchEntityException("КТПЛП в базе данных отсутствует"));
    }

    /**
     * Метод очистки кэша после отработки данного класса и сохранения ресурсов для последующей проработки календаря проекта
     */
    @CacheEvict(allEntries = true)
    public void evictCacheCalendar(){
        logger.info("Очищен кэш Ktplp");
    }
}
