package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
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

    /**
     * Поиск сущности в базе данных по введенным параметрам сущности из представления
     *
     * @param lineFromRequest сущность с введенными параметрами из представления
     * @return искомая в базе данных сущность
     */
    @Cacheable(key = "new org.springframework.cache.interceptor.SimpleKey(#lineFromRequest.power, #lineFromRequest.length)")
    public Line getFindLineFromRequest(Line lineFromRequest) {
        return lineRepository.findFirstByPowerAndLengthGreaterThanEqual(lineFromRequest.getPower(), lineFromRequest.getLength()).orElseThrow(()->
                new NoSuchEntityException("Введены некорректные значения параметров ВЛ " + lineFromRequest.getPower() + " и " + lineFromRequest.getLength()));
    }

    /**
     * Получение сущности (ЛЭП) из БД
     * @return сущность (ЛЭП)
     */
    public Line getFirst(){
        return lineRepository.findById(1L).orElseThrow();
    }
}
