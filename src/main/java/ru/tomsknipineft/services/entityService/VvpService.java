package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.repositories.VvpRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "vvpCache")
public class VvpService implements EntityProjectService {

    private final VvpRepository vvpRepository;

    /**
     * Поиск сущности в базе данных по введенным параметрам сущности из представления
     *
     * @param vvpFromRequest сущность с введенными параметрами из представления
     * @return искомая в базе данных сущность
     */
    @Cacheable(key = "new org.springframework.cache.interceptor.SimpleKey(#vvpFromRequest.square, #vvpFromRequest.helicopterModel)")
    public Vvp getFindVvpFromRequest(Vvp vvpFromRequest) {
        if (vvpFromRequest.getHelicopterModel().equals("МИ-8")){
            vvpFromRequest.setSquare(1.0);
        } else if (vvpFromRequest.getHelicopterModel().equals("МИ-26")) {
            vvpFromRequest.setSquare(2.0);
        }
        System.out.println(vvpFromRequest);
        return vvpRepository.findFirstBySquareGreaterThanEqual(vvpFromRequest.getSquare()).orElseThrow(()->
                new NoSuchEntityException("Введено некорректное значение площади " + vvpFromRequest.getSquare() +
                        " или модель вертолета " + vvpFromRequest.getHelicopterModel()));
    }

    /**
     * Получение сущности (Временная вертолетная площадка) из БД
     * @return сущность (Временная вертолетная площадка)
     */
    public Vvp getFirst(){
        return vvpRepository.findById(1L).orElseThrow(()->
                new NoSuchEntityException("Временная вертолетная площадка в базе данных отсутствует"));
    }
}
