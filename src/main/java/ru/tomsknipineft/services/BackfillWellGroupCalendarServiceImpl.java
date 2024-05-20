package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.entities.linearObjects.CableRack;
import ru.tomsknipineft.entities.linearObjects.Line;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.entities.oilPad.BackfillWell;
import ru.tomsknipineft.services.entityService.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс с бизнес-логикой расчета сроков календарного плана договора из входных данных
 */

@Service
@RequiredArgsConstructor
public class BackfillWellGroupCalendarServiceImpl implements GroupObjectCalendarService{

    private final BackfillWellService backfillWellService;

    private final BackfillSiteService backfillSiteService;

    private final VvpService vvpService;

    private final RoadService roadService;

    private final LineService lineService;

    private final CableRackService cableRackService;

    private static final Logger logger = LogManager.getLogger(BackfillWellGroupCalendarServiceImpl.class);

    @Override
    public Integer resourceForEngSurveyStage(EntityProject entityProjectBackfillWell) {
        int resourceForStage = 0;
        if (entityProjectBackfillWell.getClass() == BackfillWell.class) {
            long startTime = System.currentTimeMillis();
            resourceForStage += backfillWellService.getFindBackfillWellFromRequest((BackfillWell) entityProjectBackfillWell).getResourceForEngSurvey();
            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("Получение сущности заняло время " + executionTime);
//                    getResourceForEngSurveyBackfillWell((BackfillWell) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Road.class) {
            resourceForStage += roadService.getResourceForEngSurveyRoad((Road) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Line.class) {
            resourceForStage += lineService.getResourceForEngSurveyLine((Line) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == BackfillSite.class) {
            resourceForStage += backfillSiteService.getResourceForEngSurveyBackfillSite((BackfillSite) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Vvp.class) {
            resourceForStage += vvpService.getResourceForEngSurveyVvp((Vvp) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getResourceForEngSurveyCableRack((CableRack) entityProjectBackfillWell);
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForLabResearchStage(EntityProject entityProjectBackfillWell) {
        int resourceForStage = 0;
        if (entityProjectBackfillWell.getClass() == BackfillWell.class) {
            long startTime = System.currentTimeMillis();
            resourceForStage += backfillWellService.getFindBackfillWellFromRequest((BackfillWell) entityProjectBackfillWell).getResourceForLabResearch();
            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("Получение сущности заняло время " + executionTime);
//                    .getResourceForLabResearchBackfillWell((BackfillWell) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Road.class) {
            resourceForStage += roadService.getResourceForLabResearchRoad((Road) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Line.class) {
            resourceForStage += lineService.getResourceForLabResearchLine((Line) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == BackfillSite.class) {
            resourceForStage += backfillSiteService.getResourceForLabResearchBackfillSite((BackfillSite) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Vvp.class) {
            resourceForStage += vvpService.getResourceForLabResearchVvp((Vvp) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getResourceForLabResearchCableRack((CableRack) entityProjectBackfillWell);
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForEngSurveyReportStage(EntityProject entityProjectBackfillWell) {
        int resourceForStage = 0;
        if (entityProjectBackfillWell.getClass() == BackfillWell.class) {
            long startTime = System.currentTimeMillis();
            resourceForStage += backfillWellService.getFindBackfillWellFromRequest((BackfillWell) entityProjectBackfillWell).getResourceForEngSurveyReport();
            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("Получение сущности заняло время " + executionTime);
//                    .getResourceForEngSurveyReportBackfillWell((BackfillWell) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Road.class) {
            resourceForStage += roadService.getResourceForEngSurveyReportRoad((Road) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Line.class) {
            resourceForStage += lineService.getResourceForEngSurveyReportLine((Line) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == BackfillSite.class) {
            resourceForStage += backfillSiteService.getResourceForEngSurveyReportBackfillSite((BackfillSite) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Vvp.class) {
            resourceForStage += vvpService.getResourceForEngSurveyReportVvp((Vvp) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getResourceForEngSurveyReportCableRack((CableRack) entityProjectBackfillWell);
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForWorkDocStage(EntityProject entityProjectBackfillWell) {
        int resourceForStage = 0;
        if (entityProjectBackfillWell.getClass() == BackfillWell.class) {
            resourceForStage += backfillWellService.getFindBackfillWellFromRequest((BackfillWell) entityProjectBackfillWell).getResourceForWorkDoc();
//                    .getResourceForWorkDocBackfillWell((BackfillWell) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Road.class) {
            resourceForStage += roadService.getResourceForWorkDocRoad((Road) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Line.class) {
            resourceForStage += lineService.getResourceForWorkDocLine((Line) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == BackfillSite.class) {
            resourceForStage += backfillSiteService.getResourceForWorkDocBackfillSite((BackfillSite) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Vvp.class) {
            resourceForStage += vvpService.getResourceForWorkDocVvp((Vvp) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getResourceForWorkDocCableRack((CableRack) entityProjectBackfillWell);
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForProjDocStage(EntityProject entityProjectBackfillWell) {
        int resourceForStage = 0;
        if (entityProjectBackfillWell.getClass() == BackfillWell.class) {
            resourceForStage += backfillWellService.getFindBackfillWellFromRequest((BackfillWell) entityProjectBackfillWell).getResourceForProjDoc();
//                    .getResourceForProjDocBackfillWell((BackfillWell) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Road.class) {
            resourceForStage += roadService.getResourceForProjDocRoad((Road) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Line.class) {
            resourceForStage += lineService.getResourceForProjDocLine((Line) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == BackfillSite.class) {
            resourceForStage += backfillSiteService.getResourceForProjDocBackfillSite((BackfillSite) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Vvp.class) {
            resourceForStage += vvpService.getResourceForProjDocVvp((Vvp) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getResourceForProjDocCableRack((CableRack) entityProjectBackfillWell);
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForEstDocStage(EntityProject entityProjectBackfillWell) {
        int resourceForStage = 0;
        if (entityProjectBackfillWell.getClass() == BackfillWell.class) {
            resourceForStage += backfillWellService.getFindBackfillWellFromRequest((BackfillWell) entityProjectBackfillWell).getResourceForEstDoc();
//                    .getResourceForEstDocBackfillWell((BackfillWell) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Road.class) {
            resourceForStage += roadService.getResourceForEstDocRoad((Road) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Line.class) {
            resourceForStage += lineService.getResourceForEstDocLine((Line) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == BackfillSite.class) {
            resourceForStage += backfillSiteService.getResourceForEstDocBackfillSite((BackfillSite) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == Vvp.class) {
            resourceForStage += vvpService.getResourceForEstDocVvp((Vvp) entityProjectBackfillWell);
        } else if (entityProjectBackfillWell.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getResourceForEstDocCableRack((CableRack) entityProjectBackfillWell);
        }
        return resourceForStage;
    }

    @Override
    public List<EntityProject> listActiveEntityProject(List<EntityProject> entityProjectsBackfillWell) {
        List<EntityProject> objects = new ArrayList<>();
        for (EntityProject entity :
                entityProjectsBackfillWell) {
            if (entity.isActive()) {
                if (entity.getStage() == null) {
                    entity.setStage(1);
                }
                if (entity.getClass() == BackfillWell.class) {
                    entity.setObjectType(backfillWellService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Road.class) {
                    entity.setObjectType(roadService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Line.class) {
                    entity.setObjectType(lineService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == BackfillSite.class) {
                    entity.setObjectType(backfillSiteService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Vvp.class) {
                    entity.setObjectType(vvpService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == CableRack.class) {
                    entity.setObjectType(cableRackService.getFirst().getObjectType());
                    objects.add(entity);
                }
            }
        }
        return objects;
    }
}
