package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
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

    /**
     * Поиск сущности в базе данных по введенным параметрам сущности из представления
     *
     * @param siknFromRequest сущность с введенными параметрами из представления
     * @return искомая в базе данных сущность
     */
    @Cacheable(key = "new org.springframework.cache.interceptor.SimpleKey(#siknFromRequest.siknType, #siknFromRequest.capacity)")
    public Sikn getFindSiknFromRequest(Sikn siknFromRequest) {
        return siknRepository.findFirstBySiknTypeAndCapacityGreaterThanEqual(siknFromRequest.getSiknType(), siknFromRequest.getCapacity()).orElseThrow(()->
                new NoSuchEntityException("Введены некорректные значения типа " + siknFromRequest.getSiknType() + " или производительности " + siknFromRequest.getCapacity()));
    }

    /**
     * Получение сущности (СИКН) из БД
     * @return сущность (СИКН)
     */
    @Override
    public Sikn getFirst() {
        return siknRepository.findById(1L).orElseThrow(()->
                new NoSuchEntityException("СИКН в базе данных отсутствует"));
    }
}
