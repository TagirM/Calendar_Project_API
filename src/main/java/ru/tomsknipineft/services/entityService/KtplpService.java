package ru.tomsknipineft.services.entityService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.linearObjects.Ktplp;
import ru.tomsknipineft.repositories.KtplpRepository;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

@Service
@RequiredArgsConstructor
public class KtplpService {
    private final KtplpRepository ktplpRepository;

    private Ktplp findKtplpFromRequest;

    /**
     * Поиск в БД количества ресурса необходимого для выполнения полевых ИИ
     * @param ktplp КТПЛП
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyKtplp(Ktplp ktplp){
        if (ktplp.isActive()){
            this.findKtplpFromRequest = ktplpRepository.findFirstByKtplpTypeAndCount(ktplp.getKtplpType(), ktplp.getCount()).orElseThrow(()->
                    new NoSuchEntityException("Введены некорректные значения типа " + ktplp.getKtplpType() + " или количества " + ktplp.getCount()));
            return findKtplpFromRequest.getResourceForEngSurvey();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения ЛИ
     * @param ktplp КТПЛП
     * @return количество необходимого ресурса
     */
    public Integer getResourceForLabResearchKtplp(Ktplp ktplp){
        if (ktplp.isActive()){
            if (findKtplpFromRequest == null){
                this.findKtplpFromRequest = ktplpRepository.findFirstByKtplpTypeAndCount(ktplp.getKtplpType(), ktplp.getCount()).orElseThrow(()->
                        new NoSuchEntityException("Введены некорректные значения типа " + ktplp.getKtplpType() + " или количества " + ktplp.getCount()));
            }
            return findKtplpFromRequest.getResourceForLabResearch();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для выполнения отчета ИИ
     * @param ktplp КТПЛП
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEngSurveyReportKtplp(Ktplp ktplp){
        if (ktplp.isActive()){
            if (findKtplpFromRequest == null){
                this.findKtplpFromRequest = ktplpRepository.findFirstByKtplpTypeAndCount(ktplp.getKtplpType(), ktplp.getCount()).orElseThrow(()->
                        new NoSuchEntityException("Введены некорректные значения типа " + ktplp.getKtplpType() + " или количества " + ktplp.getCount()));
            }
            return findKtplpFromRequest.getResourceForEngSurveyReport();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки РД
     * @param ktplp КТПЛП
     * @return количество необходимого ресурса
     */
    public Integer getResourceForWorkDocKtplp(Ktplp ktplp){
        if (ktplp.isActive()){
            if (findKtplpFromRequest == null){
                this.findKtplpFromRequest = ktplpRepository.findFirstByKtplpTypeAndCount(ktplp.getKtplpType(), ktplp.getCount()).orElseThrow(()->
                        new NoSuchEntityException("Введены некорректные значения типа " + ktplp.getKtplpType() + " или количества " + ktplp.getCount()));
            }
            return findKtplpFromRequest.getResourceForWorkDoc();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки ПД
     * @param ktplp КТПЛП
     * @return количество необходимого ресурса
     */
    public Integer getResourceForProjDocKtplp(Ktplp ktplp){
        if (ktplp.isActive()){
            return findKtplpFromRequest.getResourceForProjDoc();
        }
        return 0;
    }

    /**
     * Поиск в БД количества ресурса необходимого для разработки СД
     * @param ktplp КТПЛП
     * @return количество необходимого ресурса
     */
    public Integer getResourceForEstDocKtplp(Ktplp ktplp){
        if (ktplp.isActive()){
            return findKtplpFromRequest.getResourceForEstDoc();
        }
        return 0;
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
