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
import ru.tomsknipineft.services.utilService.ExcelFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CalendarRestController {

    private final CalendarService calendarService;

    private static final Logger logger = LogManager.getLogger(CalendarRestController.class);

    /**
     * Метод контроллера, принимающий команду на скачивание календаря проекта в формате excel
     * @param codeContract шифр договора
     * @return http-ответ
     */
    @Transactional
    @GetMapping("/calendar/to_desktop")
    public ResponseEntity<Resource> uploadingCalendar(@RequestParam("codeContract") String codeContract){
        // todo обычно строки от 40 до 46 это метод сервиса
        // подсчет времени делал просто для себя, измерял время с кэшем и без, закомментировал
        // код перенес в calendarService, оставил только excelFile и логи

//        long startTime = System.currentTimeMillis();
//        List<Calendar> calendars = calendarService.getCalendarByCode(codeContract);
//        long executionTime = System.currentTimeMillis() - startTime;
//        logger.info("Получение календаря из БД заняло время " + executionTime);
        List<Calendar> calendars = calendarService.getCalendarByCode(codeContract);
        ExcelFile excelFile = calendarService.createFileCalendarExcel(calendars);
        logger.info("Скачан календарь по шифру " + codeContract);
        calendarService.evictCacheCalendar();
        // todo правильнее объединить методы excelCreatedService.getFileName(calendars) excelCreatedService.resource(calendars)
        // с тем чтоб результатом был объект состоящий из двух полей строка и ByteArrayResource
        // грубо говоря public ExcelFile getExcelFile(calendars){
        // ExcelFile fileResult = new ExcelFile(excelCreatedService.getFileName(calendars), excelCreatedService.resource(calendars))
        // return fileResult;
        // }
        // СУТЬ ИДЕИ ПОНЯЛ, СДЕЛАЛ НОВЫЙ КЛАСС И МЕТОД, КОТОРЫЙ ОБЪЕДИНЯЕТ ПЕРЕДАВАЕМЫЕ СВОЙСТВА ФАЙЛА. МЕТОД resource ЗАКОММЕНТИРОВАЛ
        // А МЕТОД getFileName СДЕЛАЛ private, Т.К. ИСПОЛЬЗУЕТСЯ НЕСКОЛЬКО РАЗ В САМОМ КЛАССЕ. ЗДЕСЬ ПОПРАВИЛ
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + excelFile.getFileName())
                .contentLength(excelFile.getByteResource().contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile.getByteResource());
    }

    /**
     * Метод контроллера, принимающий команду на скачивание графика 1го уровня проекта в формате excel
     * @param codeContract шифр договора
     * @return http-ответ
     */
    @Transactional
    @GetMapping("/schedule/to_desktop")
    public ResponseEntity<Resource> uploadingFirstLevelSchedule(@RequestParam("codeContract") String codeContract){
        List<Calendar> calendars = calendarService.getCalendarByCode(codeContract);
        ExcelFile excelFile = calendarService.createFileFirstLevelScheduleExcel(calendars);
        logger.info("Скачан график 1го уровня по шифру " + codeContract);
        calendarService.evictCacheCalendar();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + excelFile.getFileName())
                .contentLength(excelFile.getByteResource().contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile.getByteResource());
    }
}
