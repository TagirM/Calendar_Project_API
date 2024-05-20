package ru.tomsknipineft.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.services.CalendarService;
import ru.tomsknipineft.services.utilService.ExcelCreatedService;
import ru.tomsknipineft.utils.exceptions.NoSuchCalendarException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CalendarRestController {

    private final CalendarService calendarService;

//    private final ExcelCreatedService excelCreatedService;

    private static final Logger logger = LogManager.getLogger(CalendarRestController.class);

    /**
     * Метод контроллера, принимающий команду на скачивание календаря проекта в формате excel
     * @param codeContract шифр договора
     * @return http-ответ
     */
    @Transactional
    @GetMapping("/calendar/to_desktop")
    public ResponseEntity<Resource> uploadingCalendar(@RequestParam("codeContract") String codeContract){
        long startTime = System.currentTimeMillis();
        List<Calendar> calendars = calendarService.getCalendarByCode(codeContract);
        long executionTime = System.currentTimeMillis() - startTime;
        logger.info("Получение календаря из БД заняло время " + executionTime);
        ExcelCreatedService excelCreatedService = new ExcelCreatedService();
//        excelCreatedService.setCalendars(calendars);
        excelCreatedService.write(calendars);

        logger.info("Скачан календарь по шифру " + codeContract + " - " + calendars);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + excelCreatedService.getFileName(calendars))
                .contentLength(excelCreatedService.resource(calendars).contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelCreatedService.resource(calendars));
    }
}
