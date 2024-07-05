package ru.tomsknipineft.services.utilService;

import lombok.Data;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.utils.exceptions.NoSuchCalendarException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Data
@Service
public class ExcelFileCreateService {

    private static final String pathFile = "log-service/downloads_calendar/";// для запуска с IDE на компьютере
//    private static final String pathFile = "/log-service/downloads_calendar/";// для запуска приложения на сервере

    /**
     * Метод формирования ExcelFile с параметрами: наименованием файла и потоком байт файла типа ByteArrayResource
     *
     * @param calendars исходный список календарей по одному договору (с учетом этапов строительства)
     * @return ExcelFile с наименованием файла и потоком байт
     */
    public ExcelFile createFile(List<Calendar> calendars, ExcelTemplateCreate excelCreated) {
        if (calendars.size() != 0) {
            write(calendars, excelCreated);
            ExcelFile excelFile = new ExcelFile();
            String fileName = getFileName(calendars, excelCreated);
            ByteArrayResource resource;
            File file = new File(pathFile + fileName);
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                resource = new ByteArrayResource(fileInputStream.readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            excelFile.setFileName(fileName);
            excelFile.setByteResource(resource);
            return excelFile;
        }
        throw new NoSuchCalendarException("Календарь пуст");
    }

    /**
     * Метод создания структуры файла Excel
     */
    private void write(List<Calendar> calendars, ExcelTemplateCreate excelCreated) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = excelCreated.createSheet(workbook);
        excelCreated.createHeader(workbook, sheet, calendars);
        excelCreated.createCells(workbook, sheet, calendars);
        String filename = getFileName(calendars, excelCreated);
//        try (FileOutputStream outputStream = new FileOutputStream(filename)) {
        try (FileOutputStream outputStream = new FileOutputStream(pathFile + filename)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод получения названия файла
     * @param calendars календарь из базы данных
     * @param excelCreated шаблон листа excel
     * @return название файла
     */
    public String getFileName(List<Calendar> calendars, ExcelTemplateCreate excelCreated) {
        return excelCreated.getNameForFile() + cleanedFilename(calendars.get(0).getCodeContract()) + ".xlsx";
    }

    /**
     * Метод, проверяющий недопустимые символы в шифре при наименовании файла и заменящий их на символ нижнего подчеркивания
     *
     * @param codeContract шифр договора
     * @return шифр с символом нижнего подчеркивания вместо недопустимых символов (если они присутствовали)
     */
    private String cleanedFilename(String codeContract) {
        String cleanedCodeContract = codeContract.replaceAll("[\\\\/:*?\"<>|+]", "_");
        cleanedCodeContract = cleanedCodeContract.replaceAll("[\\s.]+$", "_");
        return cleanedCodeContract;
    }
}
