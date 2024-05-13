package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;
import ru.tomsknipineft.entities.oilPad.BackfillWell;
import ru.tomsknipineft.repositories.BackfillWellRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class BackfillWellService implements EntityProjectService{

    private final BackfillWellRepository backfillWellRepository;

    private BackfillWell findBackfillWellFromRequest;

    /**
     * Поиск в БД количества ресурса необходимого для выполнения полевых ИИ
     * @param backfillWell Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyBackfillWell(BackfillWell backfillWell){
        if (backfillWell.isActive()){
            this.findBackfillWellFromRequest = backfillWellRepository.findFirstByWellGreaterThanEqual(backfillWell.getWell()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение количества скважин " + backfillWell.getWell()));
            return findBackfillWellFromRequest.getResourceForEngSurvey();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения ЛИ
     * @param backfillWell Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
    public Integer getResourceForLabResearchBackfillWell(BackfillWell backfillWell){
        if (backfillWell.isActive()){
            if (findBackfillWellFromRequest == null){
                this.findBackfillWellFromRequest = backfillWellRepository.findFirstByWellGreaterThanEqual(backfillWell.getWell()).orElseThrow(()->
                        new NoSuchEntityException("Введено некорректное значение количества скважин " + backfillWell.getWell()));
            }
            return findBackfillWellFromRequest.getResourceForLabResearch();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения отчета ИИ
     * @param backfillWell Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyReportBackfillWell(BackfillWell backfillWell){
        if (backfillWell.isActive()){
            if (findBackfillWellFromRequest == null){
                this.findBackfillWellFromRequest = backfillWellRepository.findFirstByWellGreaterThanEqual(backfillWell.getWell()).orElseThrow(()->
                        new NoSuchEntityException("Введено некорректное значение количества скважин " + backfillWell.getWell()));
            }
            return findBackfillWellFromRequest.getResourceForEngSurveyReport();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки РД
     * @param backfillWell Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
    public Integer getResourceForWorkDocBackfillWell(BackfillWell backfillWell){
        if (backfillWell.isActive()){
            if (findBackfillWellFromRequest == null){
                this.findBackfillWellFromRequest = backfillWellRepository.findFirstByWellGreaterThanEqual(backfillWell.getWell()).orElseThrow(()->
                        new NoSuchEntityException("Введено некорректное значение количества скважин " + backfillWell.getWell()));
            }
            return findBackfillWellFromRequest.getResourceForWorkDoc();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки ПД
     * @param backfillWell Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
    public Integer getResourceForProjDocBackfillWell(BackfillWell backfillWell){
        if (backfillWell.isActive()){
            return findBackfillWellFromRequest.getResourceForProjDoc();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки СД
     * @param backfillWell Инженерная подготовка куста
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEstDocBackfillWell(BackfillWell backfillWell){
        if (backfillWell.isActive()){
            return findBackfillWellFromRequest.getResourceForEstDoc();
        }
        return 0;
    }

    /**
     * Получение сущности (Инженерная подготовка куста) из БД
     * @return сущность (Инженерная подготовка куста)
     */
    public BackfillWell getFirst(){
        return backfillWellRepository.findById(1L).orElseThrow(()->
                new NoSuchEntityException("Инженерная подготовка куста в базе данных отсутствует"));
    }
}
