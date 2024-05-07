package ru.tomsknipineft;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;
import ru.tomsknipineft.entities.linearObjects.Line;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.entities.oilPad.BackfillWell;
import ru.tomsknipineft.services.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BackfillWellGroupCalendarServiceImplTest {

  BackfillWellService backfillWellService = mock(BackfillWellService.class);
  BackfillSiteService backfillSiteService = mock(BackfillSiteService.class);
  VvpService vvpService = mock(VvpService.class);
  RoadService roadService = mock(RoadService.class);
  LineService lineService = mock(LineService.class);
  CableRackService cableRackService = mock(CableRackService.class);

  BackfillWellGroupCalendarServiceImpl service = new BackfillWellGroupCalendarServiceImpl(backfillWellService, backfillSiteService, vvpService, roadService, lineService, cableRackService);

  @Test
  public void testResourceForEngSurveyStage_BackfillWell() {
    BackfillWell backfillWell = new BackfillWell();
    when(backfillWellService.getResourceForEngSurveyBackfillWell(backfillWell)).thenReturn(10);

    int expected = 10;
    int actual = service.resourceForEngSurveyStage(backfillWell);

    assertEquals(expected, actual);
  }

  @Test
  public void testResourceForLabResearchStage_Road() {
    Road road = new Road();
    when(roadService.getResourceForLabResearchRoad(road)).thenReturn(5);

    int expected = 5;
    int actual = service.resourceForLabResearchStage(road);

    assertEquals(expected, actual);
  }

  @Test
  public void testResourceForEngSurveyReportStage_Line() {
    Line line = new Line();
    when(lineService.getResourceForEngSurveyReportLine(line)).thenReturn(7);

    int expected = 7;
    int actual = service.resourceForEngSurveyReportStage(line);

    assertEquals(expected, actual);
  }

  @Test
  public void testResourceForWorkDocStage_BackfillSite() {
    BackfillSite backfillSite = new BackfillSite();
    when(backfillSiteService.getResourceForWorkDocBackfillSite(backfillSite)).thenReturn(3);

    int expected = 3;
    int actual = service.resourceForWorkDocStage(backfillSite);

    assertEquals(expected, actual);
  }

  @Test
  public void testListActiveEntityProject_BackfillWell() {
    BackfillWell backfillWell = new BackfillWell();
    when(backfillWellService.getFirst()).thenReturn(backfillWell);

    List<EntityProject> entityProjects = new ArrayList<>();
    entityProjects.add(backfillWell);

    List<EntityProject> expectedObjects = new ArrayList<>();
    expectedObjects.add(backfillWell);

    List<EntityProject> actualObjects = service.listActiveEntityProject(entityProjects);

    assertEquals(expectedObjects.size(), actualObjects.size());
    assertEquals(expectedObjects.get(0).getClass(), actualObjects.get(0).getClass());
  }
}

