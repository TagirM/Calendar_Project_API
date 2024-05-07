package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;
import ru.tomsknipineft.entities.linearObjects.CableRack;
import ru.tomsknipineft.repositories.CableRackRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class CableRackService implements EntityProjectService{

    private final CableRackRepository cableRackRepository;

    /**
     * Поиск в БД количества ресурса необходимого для выполнения полевых ИИ
     * @param cableRack Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyCableRack(CableRack cableRack){
        if (cableRack.isActive()){
            return cableRackRepository.findFirstByLengthGreaterThanEqual(cableRack.getLength()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение длины кабельной эстакады " + cableRack.getLength())).getResourceForEngSurvey();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения ЛИ
     * @param cableRack Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForLabResearchCableRack(CableRack cableRack){
        if (cableRack.isActive()){
            return cableRackRepository.findFirstByLengthGreaterThanEqual(cableRack.getLength()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение длины кабельной эстакады " + cableRack.getLength())).getResourceForLabResearch();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения отчета ИИ
     * @param cableRack Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyReportCableRack(CableRack cableRack){
        if (cableRack.isActive()){
            return cableRackRepository.findFirstByLengthGreaterThanEqual(cableRack.getLength()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение длины кабельной эстакады " + cableRack.getLength())).getResourceForEngSurveyReport();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки РД
     * @param cableRack Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForWorkDocCableRack(CableRack cableRack){
        if (cableRack.isActive()){
            return cableRackRepository.findFirstByLengthGreaterThanEqual(cableRack.getLength()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение длины кабельной эстакады " + cableRack.getLength())).getResourceForWorkDoc();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки ПД
     * @param cableRack Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForProjDocCableRack(CableRack cableRack){
        if (cableRack.isActive()){
            return cableRackRepository.findFirstByLengthGreaterThanEqual(cableRack.getLength()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение длины кабельной эстакады " + cableRack.getLength())).getResourceForProjDoc();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки СД
     * @param cableRack Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEstDocCableRack(CableRack cableRack){
        if (cableRack.isActive()){
            return cableRackRepository.findFirstByLengthGreaterThanEqual(cableRack.getLength()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение длины кабельной эстакады " + cableRack.getLength())).getResourceForEstDoc();
        }
        return 0;
    }

    /**
     * Получение сущности (Кабельная эстакада) из БД
     * @return сущность (Кабельная эстакада)
     */
    public CableRack getFirst(){
        return cableRackRepository.findById(1L).orElseThrow(()->
                new NoSuchEntityException("Кабельная эстакада в базе данных отсутствует"));
    }
}
