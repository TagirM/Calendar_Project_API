package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.linearObjects.Pipeline;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.repositories.RoadRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class RoadService implements EntityProjectService{

    private final RoadRepository roadRepository;

    private Road findRoadFromRequest;

    /**
     * Поиск в БД количества ресурса необходимого для выполнения полевых ИИ
     * @param road Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyRoad(Road road){
        if (road.isActive()){
            this.findRoadFromRequest = roadRepository.findFirstByCategoryAndLengthGreaterThanEqual(road.getCategory(),
                    road.getLength()).orElseThrow(()-> new NoSuchEntityException("Введены некорректные значения параметров автодороги " +
                    road.getCategory() + " и " + road.getLength()));
            Integer durationRoad = findRoadFromRequest.getResourceForEngSurvey();
            if (road.isBridgeExist()){
                return durationRoad + road.getResourceBridge();
            }
            return durationRoad;
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения ЛИ
     * @param road Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForLabResearchRoad(Road road){
        if (road.isActive()){
            if (findRoadFromRequest == null){
                this.findRoadFromRequest = roadRepository.findFirstByCategoryAndLengthGreaterThanEqual(road.getCategory(),
                        road.getLength()).orElseThrow(()-> new NoSuchEntityException("Введены некорректные значения параметров автодороги " +
                        road.getCategory() + " и " + road.getLength()));
            }
            Integer durationRoad = findRoadFromRequest.getResourceForLabResearch();
            if (road.isBridgeExist()){
                return durationRoad + road.getResourceBridge();
            }
            return durationRoad;
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения отчета ИИ
     * @param road Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyReportRoad(Road road){
        if (road.isActive()){
            if (findRoadFromRequest == null){
                this.findRoadFromRequest = roadRepository.findFirstByCategoryAndLengthGreaterThanEqual(road.getCategory(),
                        road.getLength()).orElseThrow(()-> new NoSuchEntityException("Введены некорректные значения параметров автодороги " +
                        road.getCategory() + " и " + road.getLength()));
            }
            Integer durationRoad = findRoadFromRequest.getResourceForEngSurveyReport();
            if (road.isBridgeExist()){
                return durationRoad + road.getResourceBridge();
            }
            return durationRoad;
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки РД
     * @param road Автодорога
     * @return количество необходимого ресурса
     */
    public Integer getResourceForWorkDocRoad(Road road){
        if (road.isActive()){
            if (findRoadFromRequest == null){
                this.findRoadFromRequest = roadRepository.findFirstByCategoryAndLengthGreaterThanEqual(road.getCategory(),
                        road.getLength()).orElseThrow(()-> new NoSuchEntityException("Введены некорректные значения параметров автодороги " +
                        road.getCategory() + " и " + road.getLength()));
            }
            Integer durationRoad = findRoadFromRequest.getResourceForWorkDoc();
            if (road.isBridgeExist()){
                return durationRoad + road.getResourceBridge();
            }
            return durationRoad;
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки ПД
     * @param road Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForProjDocRoad(Road road){
        if (road.isActive()){
            Integer durationRoad = findRoadFromRequest.getResourceForProjDoc();
            if (road.isBridgeExist()){
                return durationRoad + road.getResourceBridge();
            }
            return durationRoad;
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки СД
     * @param road Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEstDocRoad(Road road){
        if (road.isActive()){
            Integer durationRoad = findRoadFromRequest.getResourceForEstDoc();
            if (road.isBridgeExist()){
                return durationRoad + road.getResourceBridge();
            }
            return durationRoad;
        }
        return 0;
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
