package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.linearObjects.CableRack;
import ru.tomsknipineft.entities.linearObjects.Line;
import ru.tomsknipineft.repositories.LineRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class LineService implements EntityProjectService{

    private final LineRepository lineRepository;

    private Line findLineFromRequest;

    /**
     * Поиск в БД количества ресурса необходимого для выполнения полевых ИИ
     * @param line Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyLine(Line line){
        if (line.isActive()){
            this.findLineFromRequest = lineRepository.findFirstByPowerAndLengthGreaterThanEqual(line.getPower(), line.getLength()).orElseThrow(()->
                    new NoSuchEntityException("Введены некорректные значения параметров ВЛ " + line.getPower() + " и " + line.getLength()));
            return findLineFromRequest.getResourceForEngSurvey();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения ЛИ
     * @param line Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForLabResearchLine(Line line){
        if (line.isActive()){
            if (findLineFromRequest == null){
                this.findLineFromRequest = lineRepository.findFirstByPowerAndLengthGreaterThanEqual(line.getPower(), line.getLength()).orElseThrow(()->
                        new NoSuchEntityException("Введены некорректные значения параметров ВЛ " + line.getPower() + " и " + line.getLength()));
            }
            return findLineFromRequest.getResourceForLabResearch();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения отчета ИИ
     * @param line Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyReportLine(Line line){
        if (line.isActive()){
            if (findLineFromRequest == null){
                this.findLineFromRequest = lineRepository.findFirstByPowerAndLengthGreaterThanEqual(line.getPower(), line.getLength()).orElseThrow(()->
                        new NoSuchEntityException("Введены некорректные значения параметров ВЛ " + line.getPower() + " и " + line.getLength()));
            }
            return findLineFromRequest.getResourceForEngSurveyReport();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки РД
     * @param line ЛЭП
     * @return количество необходимого ресурса
     */
    public Integer getResourceForWorkDocLine(Line line){
        if (line.isActive()){
            if (findLineFromRequest == null){
                this.findLineFromRequest = lineRepository.findFirstByPowerAndLengthGreaterThanEqual(line.getPower(), line.getLength()).orElseThrow(()->
                        new NoSuchEntityException("Введены некорректные значения параметров ВЛ " + line.getPower() + " и " + line.getLength()));
            }
            return findLineFromRequest.getResourceForWorkDoc();
        }

        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки ПД
     * @param line Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForProjDocLine(Line line){
        if (line.isActive()){
            return findLineFromRequest.getResourceForProjDoc();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки СД
     * @param line Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEstDocLine(Line line){
        if (line.isActive()){
            return findLineFromRequest.getResourceForEstDoc();
        }
        return 0;
    }

    /**
     * Получение сущности (ЛЭП) из БД
     * @return сущность (ЛЭП)
     */
    public Line getFirst(){
        return lineRepository.findById(1L).orElseThrow();
    }
}
