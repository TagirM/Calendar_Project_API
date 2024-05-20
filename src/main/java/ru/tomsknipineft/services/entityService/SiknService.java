package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.areaObjects.Sikn;
import ru.tomsknipineft.repositories.SiknRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class SiknService implements EntityProjectService {

    private final SiknRepository siknRepository;

    private Sikn findSiknFromRequest;

    /**
     * Поиск в БД количества ресурса необходимого для выполнения полевых ИИ
     * @param sikn СИКН
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveySikn(Sikn sikn){
        if (sikn.isActive()){
            this.findSiknFromRequest = siknRepository.findFirstBySiknTypeAndCapacityGreaterThanEqual(sikn.getSiknType(), sikn.getCapacity()).orElseThrow(()->
                    new NoSuchEntityException("Введены некорректные значения типа " + sikn.getSiknType() + " или производительности " + sikn.getCapacity()));
            return findSiknFromRequest.getResourceForEngSurvey();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения ЛИ
     * @param sikn СИКН
     * @return количество необходимого ресурса
     */
    public Integer getResourceForLabResearchSikn(Sikn sikn){
        if (sikn.isActive()){
            if (findSiknFromRequest == null) {
                this.findSiknFromRequest = siknRepository.findFirstBySiknTypeAndCapacityGreaterThanEqual(sikn.getSiknType(), sikn.getCapacity()).orElseThrow(() ->
                        new NoSuchEntityException("Введены некорректные значения типа " + sikn.getSiknType() + " или производительности " + sikn.getCapacity()));
            }
                return findSiknFromRequest.getResourceForLabResearch();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения отчета ИИ
     * @param sikn СИКН
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyReportSikn(Sikn sikn){
        if (sikn.isActive()){
            if (findSiknFromRequest == null) {
                this.findSiknFromRequest = siknRepository.findFirstBySiknTypeAndCapacityGreaterThanEqual(sikn.getSiknType(), sikn.getCapacity()).orElseThrow(() ->
                        new NoSuchEntityException("Введены некорректные значения типа " + sikn.getSiknType() + " или производительности " + sikn.getCapacity()));
            }
                return findSiknFromRequest.getResourceForEngSurveyReport();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки РД
     * @param sikn СИКН
     * @return количество необходимого ресурса
     */
    public Integer getResourceForWorkDocSikn(Sikn sikn){
        if (sikn.isActive()){
            if (findSiknFromRequest == null) {
                this.findSiknFromRequest = siknRepository.findFirstBySiknTypeAndCapacityGreaterThanEqual(sikn.getSiknType(), sikn.getCapacity()).orElseThrow(() ->
                        new NoSuchEntityException("Введены некорректные значения типа " + sikn.getSiknType() + " или производительности " + sikn.getCapacity()));
            }
            return findSiknFromRequest.getResourceForWorkDoc();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки ПД
     * @param sikn СИКН
     * @return количество необходимого ресурса
     */
    public Integer getResourceForProjDocSikn(Sikn sikn){
        if (sikn.isActive()){
            return findSiknFromRequest.getResourceForProjDoc();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки СД
     * @param sikn СИКН
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEstDocSikn(Sikn sikn){
        if (sikn.isActive()){
            return findSiknFromRequest.getResourceForEstDoc();
        }
        return 0;
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
