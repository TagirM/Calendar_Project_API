package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
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

    /**
     * Поиск сущности в базе данных по введенным параметрам сущности из представления
     *
     * @param ktplpFromRequest сущность с введенными параметрами из представления
     * @return искомая в базе данных сущность
     */
    @Cacheable(key = "new org.springframework.cache.interceptor.SimpleKey(#ktplpFromRequest.ktplpType, #ktplpFromRequest.count)")
    public Ktplp getFindKtplpFromRequest(Ktplp ktplpFromRequest) {
        return ktplpRepository.findFirstByKtplpTypeAndCount(ktplpFromRequest.getKtplpType(), ktplpFromRequest.getCount()).orElseThrow(()->
                new NoSuchEntityException("Введены некорректные значения типа " + ktplpFromRequest.getKtplpType() + " или количества " + ktplpFromRequest.getCount()));
    }

    /**
     * Получение сущности (КТПЛП) из БД
     * @return сущность (КТПЛП)
     */
    public Ktplp getFirst(){
        return ktplpRepository.findById(1L).orElseThrow(()->
                new NoSuchEntityException("КТПЛП в базе данных отсутствует"));
    }
}
