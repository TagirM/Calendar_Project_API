package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.areaObjects.Sikn;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.entities.enumEntities.PipelineLayingMethod;
import ru.tomsknipineft.entities.linearObjects.*;
import ru.tomsknipineft.services.entityService.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LinearPipelineGroupCalendarServiceImpl implements GroupObjectCalendarService {

    private final PipelineService pipelineService;

    private final VvpService vvpService;

    private final RoadService roadService;

    private final LineService lineService;

    private final CableRackService cableRackService;

    private final KtplpService ktplpService;

    private final SiknService siknService;

//    private static final Logger logger = LogManager.getLogger(LinearPipelineGroupCalendarServiceImpl.class);

    @Override
    public Integer resourceForEngGeodeticSurveyStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            if (((Pipeline) entityProjectLinearPipeline).getUnitsSOD() > 0 || ((Pipeline) entityProjectLinearPipeline).getUnitsValve() > 0) {
                ((Pipeline) entityProjectLinearPipeline).setUnitExist(true);
            }
            resourceForStage += pipelineService.getFindPipelineFromRequest((Pipeline) entityProjectLinearPipeline).getResourceForEngGeodeticSurvey();
        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectLinearPipeline).getResourceForEngGeodeticSurvey() +
                    (((Road) entityProjectLinearPipeline).getCount() - 1) / 2;
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectLinearPipeline).getResourceForEngGeodeticSurvey();
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectLinearPipeline).getResourceForEngGeodeticSurvey();
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectLinearPipeline).getResourceForEngGeodeticSurvey();
        } else if (entityProjectLinearPipeline.getClass() == Ktplp.class) {
            resourceForStage += ktplpService.getFindKtplpFromRequest((Ktplp) entityProjectLinearPipeline).getResourceForEngGeodeticSurvey() *
                    ((Ktplp) entityProjectLinearPipeline).getCount();
        } else if (entityProjectLinearPipeline.getClass() == Sikn.class) {
            resourceForStage += siknService.getFindSiknFromRequest((Sikn) entityProjectLinearPipeline).getResourceForEngGeodeticSurvey();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForEngGeologicalSurveyStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            if (((Pipeline) entityProjectLinearPipeline).getUnitsSOD() > 0 || ((Pipeline) entityProjectLinearPipeline).getUnitsValve() > 0) {
                ((Pipeline) entityProjectLinearPipeline).setUnitExist(true);
                if (((Pipeline) entityProjectLinearPipeline).getPipelineLayingMethod() == PipelineLayingMethod.UNDERGROUND ||
                        ((Pipeline) entityProjectLinearPipeline).getPipelineLayingMethod() == PipelineLayingMethod.GROUND){
                    resourceForStage += (((Pipeline) entityProjectLinearPipeline).getUnitsValve()  - 1) +
                            ((Pipeline) entityProjectLinearPipeline).getUnitsSOD();
                }
            }
            resourceForStage += pipelineService.getFindPipelineFromRequest((Pipeline) entityProjectLinearPipeline).getResourceForEngGeologicalSurvey();
        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectLinearPipeline).getResourceForEngGeologicalSurvey() +
                    (((Road) entityProjectLinearPipeline).getCount() - 1) / 2;
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectLinearPipeline).getResourceForEngGeologicalSurvey();
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectLinearPipeline).getResourceForEngGeologicalSurvey();
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectLinearPipeline).getResourceForEngGeologicalSurvey();
        } else if (entityProjectLinearPipeline.getClass() == Ktplp.class) {
            resourceForStage += ktplpService.getFindKtplpFromRequest((Ktplp) entityProjectLinearPipeline).getResourceForEngGeologicalSurvey() *
                    ((Ktplp) entityProjectLinearPipeline).getCount();
        } else if (entityProjectLinearPipeline.getClass() == Sikn.class) {
            resourceForStage += siknService.getFindSiknFromRequest((Sikn) entityProjectLinearPipeline).getResourceForEngGeologicalSurvey();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForLabResearchStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            if (((Pipeline) entityProjectLinearPipeline).getUnitsSOD() > 0 || ((Pipeline) entityProjectLinearPipeline).getUnitsValve() > 0) {
                ((Pipeline) entityProjectLinearPipeline).setUnitExist(true);
                if (((Pipeline) entityProjectLinearPipeline).getPipelineLayingMethod() == PipelineLayingMethod.UNDERGROUND ||
                        ((Pipeline) entityProjectLinearPipeline).getPipelineLayingMethod() == PipelineLayingMethod.GROUND) {
                    resourceForStage += (((Pipeline) entityProjectLinearPipeline).getUnitsValve() - 1) / 4 +
                            ((Pipeline) entityProjectLinearPipeline).getUnitsSOD() / 4;
                }
            }
            resourceForStage += pipelineService.getFindPipelineFromRequest((Pipeline) entityProjectLinearPipeline).getResourceForLabResearch();
        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectLinearPipeline).getResourceForLabResearch();
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectLinearPipeline).getResourceForLabResearch();
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectLinearPipeline).getResourceForLabResearch();
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectLinearPipeline).getResourceForLabResearch();
        } else if (entityProjectLinearPipeline.getClass() == Ktplp.class) {
            resourceForStage += ktplpService.getFindKtplpFromRequest((Ktplp) entityProjectLinearPipeline).getResourceForLabResearch() +
                    ((Ktplp) entityProjectLinearPipeline).getCount() / 4;
        } else if (entityProjectLinearPipeline.getClass() == Sikn.class) {
            resourceForStage += siknService.getFindSiknFromRequest((Sikn) entityProjectLinearPipeline).getResourceForLabResearch();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForEngSurveyReportStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            if (((Pipeline) entityProjectLinearPipeline).getUnitsSOD() > 0 || ((Pipeline) entityProjectLinearPipeline).getUnitsValve() > 0) {
                ((Pipeline) entityProjectLinearPipeline).setUnitExist(true);
            }
            resourceForStage += pipelineService.getFindPipelineFromRequest((Pipeline) entityProjectLinearPipeline).getResourceForEngSurveyReport();
        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectLinearPipeline).getResourceForEngSurveyReport();
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectLinearPipeline).getResourceForEngSurveyReport();
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectLinearPipeline).getResourceForEngSurveyReport();
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectLinearPipeline).getResourceForEngSurveyReport();
        } else if (entityProjectLinearPipeline.getClass() == Ktplp.class) {
            resourceForStage += ktplpService.getFindKtplpFromRequest((Ktplp) entityProjectLinearPipeline).getResourceForEngSurveyReport() +
                    ((Ktplp) entityProjectLinearPipeline).getCount() / 2;
        } else if (entityProjectLinearPipeline.getClass() == Sikn.class) {
            resourceForStage += siknService.getFindSiknFromRequest((Sikn) entityProjectLinearPipeline).getResourceForEngSurveyReport();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForWorkDocStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            if (((Pipeline) entityProjectLinearPipeline).getUnitsSOD() > 0 || ((Pipeline) entityProjectLinearPipeline).getUnitsValve() > 0) {
                ((Pipeline) entityProjectLinearPipeline).setUnitExist(true);
                if (((Pipeline) entityProjectLinearPipeline).getPipelineLayingMethod() == PipelineLayingMethod.UNDERGROUND ||
                        ((Pipeline) entityProjectLinearPipeline).getPipelineLayingMethod() == PipelineLayingMethod.GROUND) {
                    resourceForStage += (((Pipeline) entityProjectLinearPipeline).getUnitsValve() - 1) +
                            ((Pipeline) entityProjectLinearPipeline).getUnitsSOD() * 2;
                } else {
                    resourceForStage += (((Pipeline) entityProjectLinearPipeline).getUnitsValve() - 1) +
                            ((Pipeline) entityProjectLinearPipeline).getUnitsSOD() * 3;
                }
            }
            resourceForStage += pipelineService.getFindPipelineFromRequest((Pipeline) entityProjectLinearPipeline).getResourceForWorkDoc();
        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectLinearPipeline).getResourceForWorkDoc() +
                    (((Road) entityProjectLinearPipeline).getCount() - 1);
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectLinearPipeline).getResourceForWorkDoc();
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectLinearPipeline).getResourceForWorkDoc();
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectLinearPipeline).getResourceForWorkDoc();
        } else if (entityProjectLinearPipeline.getClass() == Ktplp.class) {
            resourceForStage += ktplpService.getFindKtplpFromRequest((Ktplp) entityProjectLinearPipeline).getResourceForWorkDoc() +
                    ((Ktplp) entityProjectLinearPipeline).getCount() * 2;
        } else if (entityProjectLinearPipeline.getClass() == Sikn.class) {
            resourceForStage += siknService.getFindSiknFromRequest((Sikn) entityProjectLinearPipeline).getResourceForWorkDoc();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForProjDocStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            if (((Pipeline) entityProjectLinearPipeline).getUnitsSOD() > 0 || ((Pipeline) entityProjectLinearPipeline).getUnitsValve() > 0) {
                ((Pipeline) entityProjectLinearPipeline).setUnitExist(true);
            }
            resourceForStage += pipelineService.getFindPipelineFromRequest((Pipeline) entityProjectLinearPipeline).getResourceForProjDoc();

        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectLinearPipeline).getResourceForProjDoc();
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectLinearPipeline).getResourceForProjDoc();
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectLinearPipeline).getResourceForProjDoc();
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectLinearPipeline).getResourceForProjDoc();
        } else if (entityProjectLinearPipeline.getClass() == Ktplp.class) {
            resourceForStage += ktplpService.getFindKtplpFromRequest((Ktplp) entityProjectLinearPipeline).getResourceForProjDoc();
        } else if (entityProjectLinearPipeline.getClass() == Sikn.class) {
            resourceForStage += siknService.getFindSiknFromRequest((Sikn) entityProjectLinearPipeline).getResourceForProjDoc();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForEstDocStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            if (((Pipeline) entityProjectLinearPipeline).getUnitsSOD() > 0 || ((Pipeline) entityProjectLinearPipeline).getUnitsValve() > 0) {
                ((Pipeline) entityProjectLinearPipeline).setUnitExist(true);
                if (((Pipeline) entityProjectLinearPipeline).getPipelineLayingMethod() == PipelineLayingMethod.UNDERGROUND ||
                        ((Pipeline) entityProjectLinearPipeline).getPipelineLayingMethod() == PipelineLayingMethod.GROUND) {
                    resourceForStage += (((Pipeline) entityProjectLinearPipeline).getUnitsValve() - 1) +
                            ((Pipeline) entityProjectLinearPipeline).getUnitsSOD() * 2;
                } else {
                    resourceForStage += (((Pipeline) entityProjectLinearPipeline).getUnitsValve() - 1) +
                            ((Pipeline) entityProjectLinearPipeline).getUnitsSOD();
                }
            }
            resourceForStage += pipelineService.getFindPipelineFromRequest((Pipeline) entityProjectLinearPipeline).getResourceForEstDoc();
            pipelineService.evictCacheCalendar();
        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectLinearPipeline).getResourceForEstDoc() +
                    (((Road) entityProjectLinearPipeline).getCount() - 1) / 2;
            roadService.evictCacheCalendar();
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectLinearPipeline).getResourceForEstDoc();
            lineService.evictCacheCalendar();
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectLinearPipeline).getResourceForEstDoc();
            vvpService.evictCacheCalendar();
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectLinearPipeline).getResourceForEstDoc();
            cableRackService.evictCacheCalendar();
        } else if (entityProjectLinearPipeline.getClass() == Ktplp.class) {
            resourceForStage += ktplpService.getFindKtplpFromRequest((Ktplp) entityProjectLinearPipeline).getResourceForEstDoc() +
                    ((Ktplp) entityProjectLinearPipeline).getCount();
            ktplpService.evictCacheCalendar();
        } else if (entityProjectLinearPipeline.getClass() == Sikn.class) {
            resourceForStage += siknService.getFindSiknFromRequest((Sikn) entityProjectLinearPipeline).getResourceForEstDoc();
            siknService.evictCacheCalendar();
        }
        return resourceForStage;
    }

    @Override
    public List<EntityProject> listActiveEntityProject(List<EntityProject> entityProjectsLinearPipeline) {
        List<EntityProject> objects = new ArrayList<>();
        for (EntityProject entity :
                entityProjectsLinearPipeline) {
            if (entity.isActive()) {
                if (entity.getStage() == null) {
                    entity.setStage(1);
                }
                if (entity.getClass() == Pipeline.class) {
                    entity.setObjectType(pipelineService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Road.class) {
                    entity.setObjectType(roadService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Line.class) {
                    entity.setObjectType(lineService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Vvp.class) {
                    entity.setObjectType(vvpService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == CableRack.class) {
                    entity.setObjectType(cableRackService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Ktplp.class) {
                    entity.setObjectType(ktplpService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Sikn.class) {
                    entity.setObjectType(siknService.getFirst().getObjectType());
                    objects.add(entity);
                }
            }
        }
        return objects;
    }
}
