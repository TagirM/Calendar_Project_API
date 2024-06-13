package ru.tomsknipineft.services.utilService;


import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.Calendar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Service
public class ExcelTemplateCreateCalendarImpl implements ExcelTemplateCreate {

    private final String nameForFile = "Calendar_";

    /**
     * Создание страницы с параметрами колонок в файле Excel
     *
     * @param workbook рабочий лист Excel
     * @return настроенная страница в файле Excel
     */
    public Sheet createSheet(XSSFWorkbook workbook) {
        Sheet sheet = workbook.createSheet("Календарный план");
        sheet.setColumnWidth(0, 2100);
        sheet.setColumnWidth(1, 19500);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 6000);
        sheet.setDefaultRowHeightInPoints(18);
        return sheet;
    }

    /**
     * Создание шапки таблицы с календарными сроками проекта
     *
     * @param workbook рабочий лист Excel
     * @param sheet    страница в файле Excel
     */
    public void createHeader(XSSFWorkbook workbook, Sheet sheet, List<Calendar> calendars) {
        int startRowTextHeader = 3; // номер стартовой строчки в файле для заголовков над таблицей
        Row textHeader = sheet.createRow(startRowTextHeader);
        textHeader.setHeightInPoints(28);
        int startRowTableHeader = 11; // номер стартовой строчки шапки таблицы с календарем в файле

        // стили шапки и заголовочных строчек
        XSSFCellStyle headerTextStyle = workbook.createCellStyle();
        headerTextStyle.setAlignment(HorizontalAlignment.CENTER);
        headerTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerTextStyle.setWrapText(true);

        XSSFCellStyle headerTableStyle = workbook.createCellStyle();
        headerTableStyle.setAlignment(HorizontalAlignment.CENTER);
        headerTableStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerTableStyle.setBorderTop(BorderStyle.THIN);
        headerTableStyle.setBorderBottom(BorderStyle.THIN);
        headerTableStyle.setBorderLeft(BorderStyle.THIN);
        headerTableStyle.setBorderRight(BorderStyle.THIN);
        headerTableStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerTableStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerTableStyle.setWrapText(true);

        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        headerTableStyle.setFont(font);
        headerTextStyle.setFont(font);

        XSSFCellStyle orderTextStyle = workbook.createCellStyle();
        headerTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFFont fontOrderText = workbook.createFont();
        fontOrderText.setFontName("Arial");
        fontOrderText.setFontHeightInPoints((short) 11);
        orderTextStyle.setFont(fontOrderText);
        // создание заголовков над таблицей
        if (calendars.size() != 0) {
            Calendar calendar = calendars.get(0);
            String[] textHeaderValues = {"Календарный план", "на выполнение проектных и изыскательских работ",
                    calendar.getProjectName(), "Шифр " + calendar.getCodeContract()};
            for (String textHeaderValue : textHeaderValues) {
                createCellCalendar(0, headerTextStyle, textHeaderValue, textHeader);
                for (int j = 1; j < 4; j++) {
                    createCellCalendar(j, headerTextStyle, "", textHeader);
                }
                sheet.addMergedRegion(new CellRangeAddress(startRowTextHeader, startRowTextHeader, 0, 3));
                textHeader = sheet.createRow(++startRowTextHeader);
                textHeader.setHeightInPoints(28);
            }

            textHeader = sheet.createRow(++startRowTextHeader);
            textHeader.setHeightInPoints(28);
            createCellCalendar(0, orderTextStyle, "Наименование Заказчика", textHeader);
            sheet.addMergedRegion(new CellRangeAddress(startRowTextHeader, startRowTextHeader, 0, 3));
            textHeader = sheet.createRow(++startRowTextHeader);
            textHeader.setHeightInPoints(28);
            createCellCalendar(0, orderTextStyle, "Наименование Подрядчика                                             АО \"ТомскНИПИнефть\"", textHeader);
            sheet.addMergedRegion(new CellRangeAddress(startRowTextHeader, startRowTextHeader, 0, 3));
        }
        // создание шапки таблицы
        String[][] valuesCellsHeaderTable =
                {{"№ этапа", "Наименование работ", "Срок выполнения работ", null},
                        {null, null, "Начало", "Окончание"},
                        {"1", "2", "3", "4"}};

        for (String[] rowHeaderTable : valuesCellsHeaderTable) {
            textHeader = sheet.createRow(startRowTableHeader++);
            textHeader.setHeightInPoints(28);
            for (int j = 0; j < rowHeaderTable.length; j++) {
                createCellCalendar(j, headerTableStyle, rowHeaderTable[j], textHeader);
            }
        }
        sheet.addMergedRegion(new CellRangeAddress(startRowTableHeader - 3, startRowTableHeader - 2, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(startRowTableHeader - 3, startRowTableHeader - 2, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(startRowTableHeader - 3, startRowTableHeader - 3, 2, 3));
    }

    /**
     * Создание ячеек самой таблицы с календарем с расчетными параметрами
     *
     * @param workbook рабочий лист Excel
     * @param sheet    страница в файле Excel
     */
    public void createCells(XSSFWorkbook workbook, Sheet sheet, List<Calendar> calendars) {
        int startRowCells = 14; // номер стартовой строчки в файле для таблицы с календарем после заголовка таблицы (начинается с "Этап строительства")
        XSSFCellStyle styleDate = workbook.createCellStyle();
        XSSFCellStyle styleTextStageCalendar = workbook.createCellStyle();
        XSSFCellStyle chapterStyleDate = workbook.createCellStyle();
        XSSFCellStyle chapterStyleText = workbook.createCellStyle();

        styleDate.setAlignment(HorizontalAlignment.CENTER);
        styleDate.setVerticalAlignment(VerticalAlignment.CENTER);
        styleDate.setBorderTop(BorderStyle.THIN);
        styleDate.setBorderBottom(BorderStyle.THIN);
        styleDate.setBorderLeft(BorderStyle.THIN);
        styleDate.setBorderRight(BorderStyle.THIN);
        styleDate.setWrapText(true);

        styleTextStageCalendar.setBorderTop(BorderStyle.THIN);
        styleTextStageCalendar.setBorderBottom(BorderStyle.THIN);
        styleTextStageCalendar.setBorderLeft(BorderStyle.THIN);
        styleTextStageCalendar.setBorderRight(BorderStyle.THIN);
        styleTextStageCalendar.setVerticalAlignment(VerticalAlignment.CENTER);
        styleTextStageCalendar.setWrapText(true);

        chapterStyleText.setBorderTop(BorderStyle.THIN);
        chapterStyleText.setBorderBottom(BorderStyle.THIN);
        chapterStyleText.setBorderLeft(BorderStyle.THIN);
        chapterStyleText.setBorderRight(BorderStyle.THIN);
        chapterStyleText.setVerticalAlignment(VerticalAlignment.CENTER);
        chapterStyleText.setWrapText(true);
        chapterStyleText.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        chapterStyleText.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        chapterStyleDate.setBorderTop(BorderStyle.THIN);
        chapterStyleDate.setBorderBottom(BorderStyle.THIN);
        chapterStyleDate.setBorderLeft(BorderStyle.THIN);
        chapterStyleDate.setBorderRight(BorderStyle.THIN);
        chapterStyleDate.setVerticalAlignment(VerticalAlignment.CENTER);
        chapterStyleDate.setAlignment(HorizontalAlignment.CENTER);
        chapterStyleDate.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        chapterStyleDate.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont workbookFont = workbook.createFont();
        workbookFont.setFontName("Arial");
        workbookFont.setFontHeightInPoints((short) 11);

        XSSFFont fontChapter = workbook.createFont();
        fontChapter.setFontName("Arial");
        fontChapter.setFontHeightInPoints((short) 11);
        fontChapter.setBold(true);

        styleDate.setFont(workbookFont);
        styleTextStageCalendar.setFont(workbookFont);
        chapterStyleText.setFont(fontChapter);
        chapterStyleDate.setFont(fontChapter);

        int nextIter = 0; // корректируйющий коэффициент для номера раздела следующего этапа строительства
        int chapterCalendar = 1; // номер первого раздела таблицы календаря
        // стили ячеек в календаре
        XSSFCellStyle[] stylesCells = {styleDate, styleTextStageCalendar, styleDate, styleDate};
        // стили ячеек для заголовков этапов календаря
        XSSFCellStyle[] stylesChapter = {chapterStyleDate, chapterStyleText, chapterStyleDate, chapterStyleDate};
        DateService dateService = new DateService();
        // создание календаря
        for (Calendar calendar : calendars) {
            int rowChapterCalendar = 1;
            String[][] valuesCells =
                    {{"Этап строительства " + calendar.getStage(), null, null, null},
                            {null, "Инженерные изыскания",
                                    dateService.dateFormatter(calendar.getStartContract()), dateService.dateFormatter(calendar.getAgreementEngineeringSurvey())},
                            {null, "Инженерные изыскания (полевые работы)",
                                    dateService.dateFormatter(calendar.getStartContract()), dateService.dateFormatter(calendar.getEngineeringSurvey())},
                            {null, "Инженерные изыскания (камеральные  работы) - выдача отчета (60% от общей стоимости камеральных работ) ",
                                    dateService.dateFormatter(calendar.getEngineeringSurveyLabResearchAndReportStart()), dateService.dateFormatter(calendar.getEngineeringSurveyReportFinish())},
                            {null, "Инженерные изыскания (камеральные  работы) согласование отчета (40% от общей стоимости камеральных работ)",
                                    dateService.dateFormatter(calendar.getEngineeringSurveyReportFinish()), dateService.dateFormatter(calendar.getAgreementEngineeringSurvey())},
                            {null, "Инженерно-экологические изыскания",
                                    dateService.dateFormatter(calendar.getSeasonalEngineeringSurveysStart()), dateService.dateFormatter(calendar.getEngineeringAndEnvironmentalSurveysFinish())},
                            {null, "Историко-культурные изыскания",
                                    dateService.dateFormatter(calendar.getSeasonalEngineeringSurveysStart()), dateService.dateFormatter(calendar.getHistoricalAndCulturalResearchFinish())}};
            String[][] valuesCellsRD =
                    {{null, "Рабочая документация", dateService.dateFormatter(calendar.getWorkingStart()), dateService.dateFormatter(calendar.getAgreementEstimates())},
                            {null, "Рабочая документация (60%, выдача документации ОГ)",
                                    dateService.dateFormatter(calendar.getWorkingStart()), dateService.dateFormatter(calendar.getWorkingFinish())},
                            {null, "Рабочая документация (30%, согласование документации с ОГ и УВЭ)",
                                    dateService.dateFormatter(calendar.getWorkingFinish()), dateService.dateFormatter(calendar.getAgreementWorking())},
                            {null, "Рабочая документация  (5%, выдача сметной документации ОГ)",
                                    dateService.dateFormatter(calendar.getEstimatesStart()), dateService.dateFormatter(calendar.getEstimatesFinish())},
                            {null, "Рабочая документация  (5%, согласование сметной документации с ОГ и УВЭ)",
                                    dateService.dateFormatter(calendar.getEngineeringSurveyReportFinish()), dateService.dateFormatter(calendar.getAgreementEstimates())}};
            String[][] valuesCellsPD =
                    {{null, "Проектная документация", dateService.dateFormatter(calendar.getWorkingFinish()), dateService.dateFormatter(calendar.getExamination())},
                            {null, "Проектная документация (60%, выдача документации ОГ)",
                                    dateService.dateFormatter(calendar.getProjectStart()), dateService.dateFormatter(calendar.getProjectFinish())},
                            {null, "Проектная документация (20%, согласование документации с ОГ и УВЭ)",
                                    dateService.dateFormatter(calendar.getProjectFinish()), dateService.dateFormatter(calendar.getAgreementProject())},
                            {null, "Проектная документация (20%, проведение внешних экспертиз)",
                                    dateService.dateFormatter(calendar.getAgreementProject()), dateService.dateFormatter(calendar.getExamination())}};
            String[][] valuesCellsLand =
                    {{null, "Землеустроительные работы", dateService.dateFormatter(calendar.getProjectFinish()), dateService.dateFormatter(calendar.getLandFinish())},
                            {null, "Землеустроительная документация",
                                    dateService.dateFormatter(calendar.getProjectFinish()), dateService.dateFormatter(calendar.getLandFinish())}};
            String[][] valuesCellsOtherWork =
                    {{null, "Прочие работы", dateService.dateFormatter(calendar.getProjectFinish()), dateService.dateFormatter(calendar.getLandFinish())},
                            {null, "Проект санитарно-защитных зон промышленного объекта",
                                    dateService.dateFormatter(calendar.getProjectFinish()), dateService.dateFormatter(calendar.getSzzFinish())},
                            {null, "Разработка рыбохозяйственного раздела",
                                    dateService.dateFormatter(calendar.getProjectFinish()), dateService.dateFormatter(calendar.getRhrFinish())},
                            {"", "ИТОГО: ", dateService.dateFormatter(calendar.getStartContract()), dateService.dateFormatter(calendar.getExamination())}};

            int numberRowCalendarStage = nextIter;
            if (chapterCalendar == 1) {
                numberRowCalendarStage = startRowCells - 1;
            }

            // создание подписи этапа строительства
            if (chapterCalendar > 1) {
                numberRowCalendarStage = createRowCalendar(sheet, null, stylesCells, valuesCells[0], numberRowCalendarStage, 15);
                sheet.addMergedRegion(new CellRangeAddress(numberRowCalendarStage, numberRowCalendarStage, 0, 3));

            }
            numberRowCalendarStage = createRowCalendar(sheet, valuesCells[0][0], stylesChapter, valuesCells[0], numberRowCalendarStage, 27);
            sheet.addMergedRegion(new CellRangeAddress(numberRowCalendarStage, numberRowCalendarStage, 0, 3));

            if (calendar.getEngineeringSurvey() != null || calendar.getEngineeringSurveyReportFinish() != null) {
                // создание заголовка ИИ
                numberRowCalendarStage = createRowCalendar(sheet, String.valueOf(chapterCalendar), stylesChapter, valuesCells[1], numberRowCalendarStage, 33);
                // создание ячеек сроков полевых ИИ
                if (calendar.getEngineeringSurvey() != null) {
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar++, stylesCells, valuesCells[2], numberRowCalendarStage, 42);
                }
                // создание ячеек сроков отчета ИИ
                if (calendar.getEngineeringSurveyReportFinish() != null) {
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar++, stylesCells, valuesCells[3], numberRowCalendarStage, 42);
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar++, stylesCells, valuesCells[4], numberRowCalendarStage, 42);
                }
                // создание ячеек сроков ИЭИ и ИКИ
                if (calendar.getEngineeringAndEnvironmentalSurveysFinish() != null) {
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar++, stylesCells, valuesCells[5], numberRowCalendarStage, 42);
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar, stylesCells, valuesCells[6], numberRowCalendarStage, 42);
                }
                chapterCalendar++;
                rowChapterCalendar = 1;
            }
            // создание ячеек сроков РД и СД
            if (calendar.getWorkingStart() != null) {
                numberRowCalendarStage = createRowCalendar(sheet, String.valueOf(chapterCalendar), stylesChapter, valuesCellsRD[0], numberRowCalendarStage, 33);
                for (int i = 1; i < valuesCellsRD.length; i++) {
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar++, stylesCells, valuesCellsRD[i], numberRowCalendarStage, 42);
                }
                chapterCalendar++;
                rowChapterCalendar = 1;
            }
            // создание ячеек сроков ПД
            if (calendar.getProjectFinish() != null) {
                numberRowCalendarStage = createRowCalendar(sheet, String.valueOf(chapterCalendar), stylesChapter, valuesCellsPD[0], numberRowCalendarStage, 33);
                for (int i = 1; i < valuesCellsPD.length; i++) {
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar++, stylesCells, valuesCellsPD[i], numberRowCalendarStage, 42);
                }
                chapterCalendar++;
                rowChapterCalendar = 1;
            }
            // создание ячеек срока ЗУР
            if (calendar.getProjectFinish() != null) {
                numberRowCalendarStage = createRowCalendar(sheet, String.valueOf(chapterCalendar), stylesChapter, valuesCellsLand[0], numberRowCalendarStage, 33);
                numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar, stylesCells, valuesCellsLand[1], numberRowCalendarStage, 42);
                chapterCalendar++;
            }
            // создание ячеек срока прочих работ
            if (calendar.getRhrFinish() != null || calendar.getSzzFinish() != null) {
                numberRowCalendarStage = createRowCalendar(sheet, String.valueOf(chapterCalendar), stylesChapter, valuesCellsOtherWork[0], numberRowCalendarStage, 33);
                if (calendar.getSzzFinish() != null) {
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar++, stylesCells, valuesCellsOtherWork[1], numberRowCalendarStage, 42);
                }
                if (calendar.getRhrFinish() != null) {
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar, stylesCells, valuesCellsOtherWork[2], numberRowCalendarStage, 42);
                }
                chapterCalendar++;
            }
            numberRowCalendarStage = createRowCalendar(sheet, "", stylesChapter, valuesCellsOtherWork[3], numberRowCalendarStage, 33);
            nextIter = numberRowCalendarStage;
        }

        // создание заключительной строчки календаря
        nextIter = createRowCalendar(sheet, null, stylesCells, new String[]{null, null, null, null}, nextIter, 15);
        sheet.addMergedRegion(new CellRangeAddress(nextIter, nextIter, 0, 3));
        String[] valuesConclusionCells =
                {"", "ВСЕГО по договору: ", dateService.dateFormatter(calendars.get(0).getStartContract()), dateService.dateFormatter(calendars.get(calendars.size() - 1).getExamination())};
        createRowCalendar(sheet, null, stylesChapter, valuesConclusionCells, nextIter, 33);
    }

    /**
     * Метод создание строчки календаря
     *
     * @param sheet       лист в файле
     * @param value       значение первой ячейки строчки
     * @param styles      стили ячеек
     * @param valuesCells значения ячеек
     * @param numberRow   предыдущий номер строчки календаря
     * @param heightRow   высота строки excel
     * @return текущий номер строчки календаря
     */
    private int createRowCalendar(Sheet sheet, String value, XSSFCellStyle[] styles, String[] valuesCells, int numberRow, int heightRow) {
        Row row = sheet.createRow(++numberRow);
        row.setHeightInPoints(heightRow);
        createCellCalendar(0, styles[0], value, row);
        for (int i = 1; i < valuesCells.length; i++) {
            createCellCalendar(i, styles[i], valuesCells[i], row);
        }
        return numberRow;
    }

    /**
     * Метод создание ячейки в строчке календаря
     *
     * @param cellColumnNumber номер ячейки в строчке
     * @param style            стиль ячейки
     * @param value            значение ячейки
     * @param row              строчка, в которой создается ячейка
     */
    private void createCellCalendar(int cellColumnNumber, XSSFCellStyle style, String value, Row row) {
        Cell cell = row.createCell(cellColumnNumber);
        cell.setCellStyle(style);
        cell.setCellValue(value);
    }
}
