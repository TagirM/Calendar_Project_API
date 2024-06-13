package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.linearObjects.Pipeline;
import ru.tomsknipineft.repositories.PipelineRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "pipelineCache")
public class PipelineService implements EntityProjectService {

    private final PipelineRepository pipelineRepository;

    private final static long FIRST_ID = 1L;

    private static final Logger logger = LogManager.getLogger(PipelineService.class);

    /**
     * Поиск сущности в базе данных по введенным параметрам сущности из представления
     *
     * @param pipelineFromRequest сущность с введенными параметрами из представления
     * @return искомая в базе данных сущность
     */
    @Cacheable(key = "new org.springframework.cache.interceptor.SimpleKey(#pipelineFromRequest.pipelineLayingMethod,#pipelineFromRequest.unitsValve, " +
            "#pipelineFromRequest.unitsSOD, #pipelineFromRequest.length)")
    public Pipeline getFindPipelineFromRequest(Pipeline pipelineFromRequest) {
        return pipelineRepository
                .findFirstByPipelineLayingMethodAndUnitsValveGreaterThanEqualAndUnitsSODGreaterThanEqualAndLengthGreaterThanEqual
                        (pipelineFromRequest.getPipelineLayingMethod(),
                                pipelineFromRequest.getUnitsValve(),
                                pipelineFromRequest.getUnitsSOD(),
                                pipelineFromRequest.getLength())
                .orElseThrow(()->
                new NoSuchEntityException("Введены некорректные значения параметров линейного трубопровода: количество УЗА - " +
                        pipelineFromRequest.getUnitsValve() +
                        ", количество узлов СОД - " +
                        pipelineFromRequest.getUnitsSOD() +
                        ", длина - " +
                        pipelineFromRequest.getLength()));
    }

    /**
     * Получение сущности (Линейный трубопровод) из БД
     * @return сущность (Линейный трубопровод)
     */
    public Pipeline getFirst(){
        return pipelineRepository
                .findById(FIRST_ID)
                .orElseThrow(()->
                new NoSuchEntityException("Линейный трубопровод в базе данных отсутствует"));
    }

    /**
     * Метод очистки кэша после отработки данного класса и сохранения ресурсов для последующей проработки календаря проекта
     */
    @CacheEvict(allEntries = true)
    public void evictCacheCalendar(){
        logger.info("Очищен кэш Pipeline");
    }
}
