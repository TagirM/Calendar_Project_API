package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.repositories.RoadRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "roadCache")
public class RoadService implements EntityProjectService {

    private final RoadRepository roadRepository;

    /**
     * Поиск сущности в базе данных по введенным параметрам сущности из представления
     *
     * @param roadFromRequest сущность с введенными параметрами из представления
     * @return искомая в базе данных сущность
     */
    @Cacheable(key = "new org.springframework.cache.interceptor.SimpleKey(#roadFromRequest.category, " +
            "#roadFromRequest.length, #roadFromRequest.count)")
    public Road getFindRoadFromRequest(Road roadFromRequest) {
        return roadRepository.findFirstByCategoryAndLengthGreaterThanEqualAndCountGreaterThanEqual(roadFromRequest.getCategory(),
                roadFromRequest.getLength(), roadFromRequest.getCount()).orElseThrow(()-> new NoSuchEntityException("Введены некорректные значения параметров автодороги " +
                roadFromRequest.getCategory() + " и " + roadFromRequest.getLength()+ " и " + roadFromRequest.getCount()));
    }

    /**
     * Получение сущности (автодорога) из БД
     * @return сущность (автодорога)
     */
    public Road getFirst(){
        return roadRepository.findById(1L).orElseThrow(()->
                new NoSuchEntityException("Автомобильная дорога в базе данных отсутствует"));
    }
}
