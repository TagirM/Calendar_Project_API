package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.linearObjects.CableRack;
import ru.tomsknipineft.repositories.CableRackRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "cableRackCache")
public class CableRackService implements EntityProjectService {

    private final CableRackRepository cableRackRepository;

    /**
     * Поиск сущности в базе данных по введенным параметрам сущности из представления
     *
     * @param cableRackFromRequest сущность с введенными параметрами из представления
     * @return искомая в базе данных сущность
     */
    @Cacheable(key = "#cableRackFromRequest.length")
    public CableRack getFindCableRackFromRequest(CableRack cableRackFromRequest) {
        return cableRackRepository.findFirstByLengthGreaterThanEqual(cableRackFromRequest.getLength()).orElseThrow(() ->
                new NoSuchEntityException("Введено некорректное значение длины кабельной эстакады " + cableRackFromRequest.getLength()));
    }

    /**
     * Получение сущности (Кабельная эстакада) из БД
     *
     * @return сущность (Кабельная эстакада)
     */
    public CableRack getFirst() {
        return cableRackRepository.findById(1L).orElseThrow(() ->
                new NoSuchEntityException("Кабельная эстакада в базе данных отсутствует"));
    }
}
