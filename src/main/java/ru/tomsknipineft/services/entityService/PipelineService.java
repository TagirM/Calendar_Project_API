package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.linearObjects.Line;
import ru.tomsknipineft.entities.linearObjects.Pipeline;
import ru.tomsknipineft.repositories.PipelineRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class PipelineService {

    private final PipelineRepository pipelineRepository;

    private Pipeline findPipelineFromRequest;

    /**
     * Поиск в БД количества ресурса необходимого для выполнения полевых ИИ
     * @param pipeline Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyPipeline(Pipeline pipeline){
        if (pipeline.isActive()){
            this.findPipelineFromRequest = pipelineRepository.findFirstByPipelineLayingMethodAndUnitsValveGreaterThanEqualAndUnitsSODGreaterThanEqualAndLengthGreaterThanEqual
                            (pipeline.getPipelineLayingMethod(), pipeline.getUnitsValve(), pipeline.getUnitsSOD(), pipeline.getLength())
                    .orElseThrow(()-> new NoSuchEntityException("Введены некорректные значения параметров линейного трубопровода "
                            + ", количество УЗА - " + pipeline.getUnitsValve() + ", количество узлов СОД - " + pipeline.getUnitsSOD()
                            + ", длина - " + pipeline.getLength()));
            return findPipelineFromRequest.getResourceForEngSurvey();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения ЛИ
     * @param pipeline Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForLabResearchPipeline(Pipeline pipeline){
        if (pipeline.isActive()){
            if (findPipelineFromRequest == null){
                this.findPipelineFromRequest = pipelineRepository.findFirstByPipelineLayingMethodAndUnitsValveGreaterThanEqualAndUnitsSODGreaterThanEqualAndLengthGreaterThanEqual
                                (pipeline.getPipelineLayingMethod(), pipeline.getUnitsValve(), pipeline.getUnitsSOD(), pipeline.getLength())
                        .orElseThrow(()-> new NoSuchEntityException("Введены некорректные значения параметров линейного трубопровода "
                                + ", количество УЗА - " + pipeline.getUnitsValve() + ", количество узлов СОД - " + pipeline.getUnitsSOD()
                                + ", длина - " + pipeline.getLength()));
            }
            return findPipelineFromRequest.getResourceForLabResearch();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения отчета ИИ
     * @param pipeline Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyReportPipeline(Pipeline pipeline){
        if (pipeline.isActive()){
            if (findPipelineFromRequest == null){
                this.findPipelineFromRequest = pipelineRepository.findFirstByPipelineLayingMethodAndUnitsValveGreaterThanEqualAndUnitsSODGreaterThanEqualAndLengthGreaterThanEqual
                                (pipeline.getPipelineLayingMethod(), pipeline.getUnitsValve(), pipeline.getUnitsSOD(), pipeline.getLength())
                        .orElseThrow(()-> new NoSuchEntityException("Введены некорректные значения параметров линейного трубопровода "
                                + ", количество УЗА - " + pipeline.getUnitsValve() + ", количество узлов СОД - " + pipeline.getUnitsSOD()
                                + ", длина - " + pipeline.getLength()));
            }
            return findPipelineFromRequest.getResourceForEngSurveyReport();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки РД
     * @param pipeline Линейный трубопровод
     * @return количество необходимого ресурса
     */
    public Integer getResourceForWorkDocPipeline(Pipeline pipeline){
        if (pipeline.isActive()){
            if (findPipelineFromRequest == null){
                this.findPipelineFromRequest = pipelineRepository.findFirstByPipelineLayingMethodAndUnitsValveGreaterThanEqualAndUnitsSODGreaterThanEqualAndLengthGreaterThanEqual
                                (pipeline.getPipelineLayingMethod(), pipeline.getUnitsValve(), pipeline.getUnitsSOD(), pipeline.getLength())
                        .orElseThrow(()-> new NoSuchEntityException("Введены некорректные значения параметров линейного трубопровода "
                                + ", количество УЗА - " + pipeline.getUnitsValve() + ", количество узлов СОД - " + pipeline.getUnitsSOD()
                                + ", длина - " + pipeline.getLength()));
            }
            return findPipelineFromRequest.getResourceForWorkDoc();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки ПД
     * @param pipeline Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForProjDocPipeline(Pipeline pipeline){
        if (pipeline.isActive()){
            return findPipelineFromRequest.getResourceForProjDoc();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки СД
     * @param pipeline Инженерная подготовка площадки
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEstDocPipeline(Pipeline pipeline){
        if (pipeline.isActive()){
            return findPipelineFromRequest.getResourceForEstDoc();
        }
        return 0;
    }

    /**
     * Получение сущности (Линейный трубопровод) из БД
     * @return сущность (Линейный трубопровод)
     */
    public Pipeline getFirst(){
        return pipelineRepository.findById(1L).orElseThrow(()->
                new NoSuchEntityException("Линейный трубопровод в базе данных отсутствует"));
    }
}
