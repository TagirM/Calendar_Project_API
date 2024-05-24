package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.areaObjects.Sikn;
import ru.tomsknipineft.entities.areaObjects.Vvp;
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
    public Integer resourceForEngSurveyStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            resourceForStage += pipelineService.getFindPipelineFromRequest((Pipeline) entityProjectLinearPipeline).getResourceForEngSurvey();
        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectLinearPipeline).getResourceForEngSurvey();
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectLinearPipeline).getResourceForEngSurvey();
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectLinearPipeline).getResourceForEngSurvey();
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectLinearPipeline).getResourceForEngSurvey();
        } else if (entityProjectLinearPipeline.getClass() == Ktplp.class) {
            resourceForStage += ktplpService.getFindKtplpFromRequest((Ktplp) entityProjectLinearPipeline).getResourceForEngSurvey();
        } else if (entityProjectLinearPipeline.getClass() == Sikn.class) {
            resourceForStage += siknService.getFindSiknFromRequest((Sikn) entityProjectLinearPipeline).getResourceForEngSurvey();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForLabResearchStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
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
            resourceForStage += ktplpService.getFindKtplpFromRequest((Ktplp) entityProjectLinearPipeline).getResourceForLabResearch();
        } else if (entityProjectLinearPipeline.getClass() == Sikn.class) {
            resourceForStage += siknService.getFindSiknFromRequest((Sikn) entityProjectLinearPipeline).getResourceForLabResearch();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForEngSurveyReportStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
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
            resourceForStage += ktplpService.getFindKtplpFromRequest((Ktplp) entityProjectLinearPipeline).getResourceForEngSurveyReport();
        } else if (entityProjectLinearPipeline.getClass() == Sikn.class) {
            resourceForStage += siknService.getFindSiknFromRequest((Sikn) entityProjectLinearPipeline).getResourceForEngSurveyReport();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForWorkDocStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            resourceForStage += pipelineService.getFindPipelineFromRequest((Pipeline) entityProjectLinearPipeline).getResourceForWorkDoc();
        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectLinearPipeline).getResourceForWorkDoc();
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectLinearPipeline).getResourceForWorkDoc();
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectLinearPipeline).getResourceForWorkDoc();
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectLinearPipeline).getResourceForWorkDoc();
        } else if (entityProjectLinearPipeline.getClass() == Ktplp.class) {
            resourceForStage += ktplpService.getFindKtplpFromRequest((Ktplp) entityProjectLinearPipeline).getResourceForWorkDoc();
        } else if (entityProjectLinearPipeline.getClass() == Sikn.class) {
            resourceForStage += siknService.getFindSiknFromRequest((Sikn) entityProjectLinearPipeline).getResourceForWorkDoc();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForProjDocStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
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
            resourceForStage += pipelineService.getFindPipelineFromRequest((Pipeline) entityProjectLinearPipeline).getResourceForEstDoc();
        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectLinearPipeline).getResourceForEstDoc();
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectLinearPipeline).getResourceForEstDoc();
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectLinearPipeline).getResourceForEstDoc();
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectLinearPipeline).getResourceForEstDoc();
        } else if (entityProjectLinearPipeline.getClass() == Ktplp.class) {
            resourceForStage += ktplpService.getFindKtplpFromRequest((Ktplp) entityProjectLinearPipeline).getResourceForEstDoc();
        } else if (entityProjectLinearPipeline.getClass() == Sikn.class) {
            resourceForStage += siknService.getFindSiknFromRequest((Sikn) entityProjectLinearPipeline).getResourceForEstDoc();
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
