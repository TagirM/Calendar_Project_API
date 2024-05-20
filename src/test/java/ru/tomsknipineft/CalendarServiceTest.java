package ru.tomsknipineft;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.entities.DataFormProject;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.enumEntities.ObjectType;
import ru.tomsknipineft.repositories.CalendarRepository;
import ru.tomsknipineft.services.CalendarService;
import ru.tomsknipineft.services.utilService.DataFormProjectService;
import ru.tomsknipineft.services.utilService.DateService;
import ru.tomsknipineft.services.GroupObjectCalendarService;

import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CalendarServiceTest {

    private final CalendarRepository calendarRepository = mock(CalendarRepository.class);
    private final DateService dateService = mock(DateService.class);
    private final DataFormProjectService dataFormProjectService = mock(DataFormProjectService.class);
    private final GroupObjectCalendarService objectCalendarService = mock(GroupObjectCalendarService.class);
    private final CalendarService service = new CalendarService(calendarRepository, dateService, dataFormProjectService);

    @Test
    public void testGetCalendarByCode() {
        when(calendarRepository.findCalendarByCodeContract(anyString())).thenReturn(Optional.of(new ArrayList<>()));
        List<Calendar> result = service.getCalendarByCode("code");
        assertNotNull(result);
    }

    @Test
    public void testGetDataFormProject() {
        Calendar calendar = new Calendar();
        calendar.setBytesDataProject(new byte[10]);

        DataFormProject mockDataFormProject = mock(DataFormProject.class);
        when(dataFormProjectService.getFilePathRecover()).thenReturn("testPath");
        when(dataFormProjectService.dataFormProjectRecover()).thenReturn(mockDataFormProject);

        DataFormProject result = service.getDataFormProject(Collections.singletonList(calendar));

        assertNotNull(result);
        assertEquals(mockDataFormProject, result);
    }

    @Test
    public void testCreateCalendar() {
        List<EntityProject> objects = new ArrayList<>();
        DataFormProject dataFormProject = mock(DataFormProject.class);

        when(dataFormProjectService.getFilePathSave()).thenReturn("testFilePath");

        List<Calendar> calendars = service.createCalendar(objects, objectCalendarService, dataFormProject);

        assertNotNull(calendars);
        assertEquals(0, calendars.size()); // Проверка размера полученного списка
    }

    @Test
    public void testGetResourcesEngSurvey() {
        EntityProject entity = mock(EntityProject.class);
        List<EntityProject> activeObjects = Collections.singletonList(entity);

        when(objectCalendarService.resourceForEngSurveyStage(entity)).thenReturn(5);

        Map<Integer, Integer> result = service.getResourcesEngSurvey(activeObjects, objectCalendarService);

        assertNotNull(result);
    }

    @Test
    public void testGetResourcesLabResearch() {
        EntityProject entity = mock(EntityProject.class);
        List<EntityProject> activeObjects = Collections.singletonList(entity);

        when(objectCalendarService.resourceForLabResearchStage(entity)).thenReturn(10);

        Map<Integer, Integer> result = service.getResourcesLabResearch(activeObjects, objectCalendarService);

        assertNotNull(result);
    }

    @Test
    public void testGetResourcesEngSurveyReport() {
        EntityProject entity = mock(EntityProject.class);
        List<EntityProject> activeObjects = Collections.singletonList(entity);

        when(objectCalendarService.resourceForEngSurveyReportStage(entity)).thenReturn(15);

        Map<Integer, Integer> result = service.getResourcesEngSurveyReport(activeObjects, objectCalendarService);

        assertNotNull(result);
    }

    @Test
    public void testGetResourcesWorkDoc() {
        EntityProject entity = mock(EntityProject.class);
        when(entity.getObjectType()).thenReturn(ObjectType.AREA); // Предположим, что есть проверка на конкретный ObjectType

        List<EntityProject> activeObjects = Collections.singletonList(entity);

        when(objectCalendarService.resourceForWorkDocStage(entity)).thenReturn(20);

        Map<Integer, Integer> result = service.getResourcesWorkDoc(activeObjects, objectCalendarService);

        assertNotNull(result);
        assertEquals(20, (int) result.get(0)); // Проверка, что возвращенное значение соответствует ожидаемому
    }

    @Test
    public void testGetResourcesProjDoc() {
        EntityProject entity = mock(EntityProject.class);
        List<EntityProject> activeObjects = Collections.singletonList(entity);

        when(objectCalendarService.resourceForProjDocStage(entity)).thenReturn(25);

        Map<Integer, Integer> result = service.getResourcesProjDoc(activeObjects, objectCalendarService);

        assertNotNull(result);
    }

    @Test
    public void testGetResourcesEstDoc() {
        EntityProject entity = mock(EntityProject.class);
        List<EntityProject> activeObjects = Collections.singletonList(entity);

        when(objectCalendarService.resourceForEstDocStage(entity)).thenReturn(30);

        Map<Integer, Integer> result = service.getResourcesEstDoc(activeObjects, objectCalendarService);

        assertNotNull(result);
    }
}

