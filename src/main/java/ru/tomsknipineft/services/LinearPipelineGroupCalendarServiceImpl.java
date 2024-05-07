package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.entities.linearObjects.CableRack;
import ru.tomsknipineft.entities.linearObjects.Line;
import ru.tomsknipineft.entities.linearObjects.Pipeline;
import ru.tomsknipineft.entities.linearObjects.Road;

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

//    private static final Logger logger = LogManager.getLogger(LinearPipelineGroupCalendarServiceImpl.class);

    @Override
    public Integer resourceForEngSurveyStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            resourceForStage += pipelineService.getResourceForEngSurveyPipeline((Pipeline) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getResourceForEngSurveyRoad((Road) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getResourceForEngSurveyLine((Line) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getResourceForEngSurveyVvp((Vvp) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getResourceForEngSurveyCableRack((CableRack) entityProjectLinearPipeline);
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForLabResearchStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            resourceForStage += pipelineService.getResourceForLabResearchPipeline((Pipeline) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getResourceForLabResearchRoad((Road) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getResourceForLabResearchLine((Line) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getResourceForLabResearchVvp((Vvp) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getResourceForLabResearchCableRack((CableRack) entityProjectLinearPipeline);
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForEngSurveyReportStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            resourceForStage += pipelineService.getResourceForEngSurveyReportPipeline((Pipeline) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getResourceForEngSurveyReportRoad((Road) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getResourceForEngSurveyReportLine((Line) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getResourceForEngSurveyReportVvp((Vvp) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getResourceForEngSurveyReportCableRack((CableRack) entityProjectLinearPipeline);
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForWorkDocStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            resourceForStage += pipelineService.getResourceForWorkDocPipeline((Pipeline) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getResourceForWorkDocRoad((Road) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getResourceForWorkDocLine((Line) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getResourceForWorkDocVvp((Vvp) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getResourceForWorkDocCableRack((CableRack) entityProjectLinearPipeline);
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForProjDocStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            resourceForStage += pipelineService.getResourceForProjDocPipeline((Pipeline) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getResourceForProjDocRoad((Road) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getResourceForProjDocLine((Line) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getResourceForProjDocVvp((Vvp) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getResourceForProjDocCableRack((CableRack) entityProjectLinearPipeline);
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForEstDocStage(EntityProject entityProjectLinearPipeline) {
        int resourceForStage = 0;
        if (entityProjectLinearPipeline.getClass() == Pipeline.class) {
            resourceForStage += pipelineService.getResourceForEstDocPipeline((Pipeline) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Road.class) {
            resourceForStage += roadService.getResourceForEstDocRoad((Road) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Line.class) {
            resourceForStage += lineService.getResourceForEstDocLine((Line) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == Vvp.class) {
            resourceForStage += vvpService.getResourceForEstDocVvp((Vvp) entityProjectLinearPipeline);
        } else if (entityProjectLinearPipeline.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getResourceForEstDocCableRack((CableRack) entityProjectLinearPipeline);
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
                }
            }
        }
        return objects;
    }
}
