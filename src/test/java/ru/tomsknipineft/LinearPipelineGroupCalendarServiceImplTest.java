//package ru.tomsknipineft;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.junit4.SpringRunner;
//import ru.tomsknipineft.entities.EntityProject;
//import ru.tomsknipineft.entities.areaObjects.Vvp;
//import ru.tomsknipineft.entities.linearObjects.*;
//import ru.tomsknipineft.services.*;
//import ru.tomsknipineft.services.entityService.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@RunWith(SpringRunner.class)
//public class LinearPipelineGroupCalendarServiceImplTest {
//
//    PipelineService pipelineService = mock(PipelineService.class);
//    VvpService vvpService = mock(VvpService.class);
//    RoadService roadService = mock(RoadService.class);
//    LineService lineService = mock(LineService.class);
//    CableRackService cableRackService = mock(CableRackService.class);
//    KtplpService ktplpService = mock(KtplpService.class);
//
//    SiknService siknService = mock(SiknService.class);
//    LinearPipelineGroupCalendarServiceImpl service = new LinearPipelineGroupCalendarServiceImpl(pipelineService, vvpService, roadService, lineService, cableRackService, ktplpService, siknService);
//
//    @Test
//    public void testResourceForEngSurveyStage_Pipeline() {
//        Pipeline pipeline = new Pipeline();
//        when(pipelineService.getResourceForEngSurveyPipeline(pipeline)).thenReturn(10);
//
//        int expected = 10;
//        int actual = service.resourceForEngGeodeticSurveyStage(pipeline);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testResourceForLabResearchStage_Road() {
//        Road road = new Road();
//        when(roadService.getResourceForLabResearchRoad(road)).thenReturn(5);
//
//        int expected = 5;
//        int actual = service.resourceForLabResearchStage(road);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testResourceForEngSurveyReportStage_Vvp() {
//        Vvp vvp = new Vvp();
//        when(vvpService.getResourceForEngSurveyReportVvp(vvp)).thenReturn(8);
//
//        int expected = 8;
//        int actual = service.resourceForEngSurveyReportStage(vvp);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testResourceForWorkDocStage_CableRack() {
//        CableRack cableRack = new CableRack();
//        when(cableRackService.getResourceForWorkDocCableRack(cableRack)).thenReturn(3);
//
//        int expected = 3;
//        int actual = service.resourceForWorkDocStage(cableRack);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testResourceForProjDocStage_Ktplp() {
//        Ktplp ktplp = new Ktplp();
//        when(ktplpService.getResourceForWorkDocKtplp(ktplp)).thenReturn(3);
//
//        int expected = 3;
//        int actual = service.resourceForWorkDocStage(ktplp);
//
//        assertEquals(expected, actual);
//    }
//
//
//    @Test
//    public void testListActiveEntityProject_Line() {
//        Line line = new Line();
//        line.setActive(true);
//        when(lineService.getFirst()).thenReturn(line);
//
//        List<EntityProject> entityProjects = new ArrayList<>();
//        entityProjects.add(line);
//
//        List<EntityProject> expectedObjects = new ArrayList<>();
//        expectedObjects.add(line);
//
//        List<EntityProject> actualObjects = service.listActiveEntityProject(entityProjects);
//
//        assertEquals(expectedObjects.size(), actualObjects.size());
//        assertEquals(expectedObjects.get(0).getClass(), actualObjects.get(0).getClass());
//    }
//}
