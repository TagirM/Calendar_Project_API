package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.entities.linearObjects.CableRack;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.repositories.VvpRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class VvpService implements EntityProjectService{

    private final VvpRepository vvpRepository;

    private Vvp findVvpFromRequest;

    /**
     * Поиск в БД количества ресурса необходимого для выполнения полевых ИИ
     * @param vvp Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyVvp(Vvp vvp){
        if (vvp.isActive()){
            this.findVvpFromRequest = vvpRepository.findFirstBySquareGreaterThanEqualAndHelicopterModel(vvp.getSquare(),
                    vvp.getHelicopterModel()).orElseThrow(()->
                    new NoSuchEntityException("Введено некорректное значение площади " + vvp.getSquare() +
                            " и/или модель вертолета " + vvp.getHelicopterModel()));
            return findVvpFromRequest.getResourceForEngSurvey();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения ЛИ
     * @param vvp Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForLabResearchVvp(Vvp vvp){
        if (vvp.isActive()){
            if (findVvpFromRequest == null){
                this.findVvpFromRequest = vvpRepository.findFirstBySquareGreaterThanEqualAndHelicopterModel(vvp.getSquare(),
                        vvp.getHelicopterModel()).orElseThrow(()->
                        new NoSuchEntityException("Введено некорректное значение площади " + vvp.getSquare() +
                                " и/или модель вертолета " + vvp.getHelicopterModel()));
            }
            return findVvpFromRequest.getResourceForLabResearch();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения отчета ИИ
     * @param vvp Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyReportVvp(Vvp vvp){
        if (vvp.isActive()){
            if (findVvpFromRequest == null){
                this.findVvpFromRequest = vvpRepository.findFirstBySquareGreaterThanEqualAndHelicopterModel(vvp.getSquare(),
                        vvp.getHelicopterModel()).orElseThrow(()->
                        new NoSuchEntityException("Введено некорректное значение площади " + vvp.getSquare() +
                                " и/или модель вертолета " + vvp.getHelicopterModel()));
            }
            return findVvpFromRequest.getResourceForEngSurveyReport();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки РД
     * @param vvp Временная вертолетная площадка
     * @return количество необходимого ресурса
     */
    public Integer getResourceForWorkDocVvp(Vvp vvp){
        if (vvp.isActive()){
            if (findVvpFromRequest == null){
                this.findVvpFromRequest = vvpRepository.findFirstBySquareGreaterThanEqualAndHelicopterModel(vvp.getSquare(),
                        vvp.getHelicopterModel()).orElseThrow(()->
                        new NoSuchEntityException("Введено некорректное значение площади " + vvp.getSquare() +
                                " и/или модель вертолета " + vvp.getHelicopterModel()));
            }
            return findVvpFromRequest.getResourceForWorkDoc();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки ПД
     * @param vvp Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForProjDocVvp(Vvp vvp){
        if (vvp.isActive()){
            return findVvpFromRequest.getResourceForProjDoc();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки СД
     * @param vvp Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEstDocVvp(Vvp vvp){
        if (vvp.isActive()){
            return findVvpFromRequest.getResourceForEstDoc();
        }
        return 0;
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
