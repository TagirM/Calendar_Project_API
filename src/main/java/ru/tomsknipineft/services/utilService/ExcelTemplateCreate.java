package ru.tomsknipineft.services.utilService;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.tomsknipineft.entities.Calendar;

import java.util.List;

public interface ExcelTemplateCreate {

    Sheet createSheet(XSSFWorkbook workbook);

    void createHeader(XSSFWorkbook workbook, Sheet sheet, List<Calendar> calendars);

    void createCells(XSSFWorkbook workbook, Sheet sheet, List<Calendar> calendars);

    String getNameForFile();

//    String getFileName(List<Calendar> calendars);
}
