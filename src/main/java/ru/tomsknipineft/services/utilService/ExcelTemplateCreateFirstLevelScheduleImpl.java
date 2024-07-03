package ru.tomsknipineft.services.utilService;

import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.Calendar;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Service
public class ExcelTemplateCreateFirstLevelScheduleImpl implements ExcelTemplateCreate {

    private final String nameForFile = "First_Level_Schedule_";

    /**
     * Создание страницы с параметрами колонок в файле Excel
     *
     * @param workbook рабочий лист Excel
     * @return настроенная страница в файле Excel
     */
    public Sheet createSheet(XSSFWorkbook workbook) {
        Sheet sheet = workbook.createSheet("График 1го уровння");
        sheet.setColumnWidth(0, 1350);
        sheet.setColumnWidth(1, 3800);
        sheet.setColumnWidth(2, 3800);
        sheet.setColumnWidth(3, 5700);
        sheet.setColumnWidth(4, 5700);
        sheet.setColumnWidth(5, 5700);
        sheet.setColumnWidth(6, 5000);
        sheet.setColumnWidth(7, 3000);
        sheet.setColumnWidth(8, 3000);
        sheet.setColumnWidth(9, 3000);
        sheet.setColumnWidth(10, 3000);
        sheet.setColumnWidth(11, 3800);
        sheet.setColumnWidth(12, 3800);
        sheet.setColumnWidth(13, 3800);
        sheet.setColumnWidth(14, 7100);
        sheet.setDefaultRowHeightInPoints(18);
        return sheet;
    }

    /**
     * Создание заголовоков и шапки таблицы графика
     *
     * @param workbook рабочий лист Excel
     * @param sheet    страница в файле Excel
     */
    public void createHeader(XSSFWorkbook workbook, Sheet sheet, List<Calendar> calendars) {
        int startRowTextHeader = 0; // номер стартовой строчки в файле для основного заголовка в файле
        int startRowHeaderInfo = 2; // номер стартовой строчки заголовочной информации над таблицей в файле
        int startRowTableHeader = 11; // номер стартовой строчки шапки таблицы с графиком в файле

        // стили заголовочных строчек и шапки таблицы
        XSSFCellStyle headerTextStyle = workbook.createCellStyle();
        headerTextStyle.setAlignment(HorizontalAlignment.CENTER);
        headerTextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerTextStyle.setWrapText(true);
        headerTextStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        headerTextStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont fontHeaderText = workbook.createFont();
        fontHeaderText.setFontName("Arial");
        fontHeaderText.setFontHeightInPoints((short) 14);
        fontHeaderText.setBold(true);
        headerTextStyle.setFont(fontHeaderText);

        XSSFCellStyle headerInfoLeftColumnStyle = workbook.createCellStyle();
        headerInfoLeftColumnStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerInfoLeftColumnStyle.setBorderTop(BorderStyle.MEDIUM);
        headerInfoLeftColumnStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerInfoLeftColumnStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerInfoLeftColumnStyle.setBorderRight(BorderStyle.MEDIUM);
        headerInfoLeftColumnStyle.setWrapText(true);

        XSSFCellStyle headerInfoRightColumnStyle = workbook.createCellStyle();
        headerInfoRightColumnStyle.setAlignment(HorizontalAlignment.CENTER);
        headerInfoRightColumnStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerInfoRightColumnStyle.setBorderTop(BorderStyle.MEDIUM);
        headerInfoRightColumnStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerInfoRightColumnStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerInfoRightColumnStyle.setBorderRight(BorderStyle.MEDIUM);
        headerInfoRightColumnStyle.setWrapText(true);

        XSSFCellStyle headerTableStyle = workbook.createCellStyle();
        headerTableStyle.setAlignment(HorizontalAlignment.CENTER);
        headerTableStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerTableStyle.setBorderTop(BorderStyle.THIN);
        headerTableStyle.setBorderBottom(BorderStyle.THIN);
        headerTableStyle.setBorderLeft(BorderStyle.THIN);
        headerTableStyle.setBorderRight(BorderStyle.THIN);
        headerTableStyle.setWrapText(true);

        XSSFCellStyle lastHeaderTableStyle = workbook.createCellStyle();
        lastHeaderTableStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        lastHeaderTableStyle.setBorderTop(BorderStyle.THIN);
        lastHeaderTableStyle.setBorderBottom(BorderStyle.THIN);
        lastHeaderTableStyle.setBorderLeft(BorderStyle.THIN);
        lastHeaderTableStyle.setBorderRight(BorderStyle.THIN);

        XSSFFont fontInfoLeftColumn = workbook.createFont();
        fontInfoLeftColumn.setFontName("Arial");
        fontInfoLeftColumn.setFontHeightInPoints((short) 10);
        fontInfoLeftColumn.setBold(true);
        headerInfoLeftColumnStyle.setFont(fontInfoLeftColumn);

        XSSFFont fontHeaderTable = workbook.createFont();
        fontHeaderTable.setFontName("Arial");
        fontHeaderTable.setFontHeightInPoints((short) 10);
        headerInfoRightColumnStyle.setFont(fontHeaderTable);
        headerTableStyle.setFont(fontHeaderTable);
        lastHeaderTableStyle.setFont(fontHeaderTable);

        // создание заголовка над таблицей
        Row textHeader = sheet.createRow(startRowTextHeader);
        textHeader.setHeightInPoints(35);
        createCellCalendar(1, headerTextStyle,
                "Оперативный календарно-сетевой график выполнения ПИР/ Исполнение плана Оперативного календарно-сетевого графика выполнения ПИР", textHeader);
        for (int j = 2; j < 11; j++) {
            createCellCalendar(j, headerTextStyle, null, textHeader);
        }
        sheet.addMergedRegion(new CellRangeAddress(startRowTextHeader, startRowTextHeader, 1, 10));

        if (calendars.size() != 0) {
            Calendar calendar = calendars.get(0);
            // создание заголовочной информации над таблицей
            String[][] valuesCellsHeaderInfo =
                    {{"Период планирования:", null, null, null},
                            {"Отчетный период:", null, null, null},
                            {"Текущая дата (дата предоставления отчета):", null, calendar.getStartContract().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), null},
                            {"Объект капитального строительства:", null, calendar.getProjectName(), null},
                            {null, null, null, null},
                            {"Подрядная организация:", null, "АО \"ТомскНИПИнефть\"", null},
                            {"Договор:", null, null, null}};
            XSSFCellStyle[] stylesHeaderInfo = {headerInfoLeftColumnStyle, headerInfoLeftColumnStyle, headerInfoRightColumnStyle, headerInfoRightColumnStyle};

            startRowHeaderInfo--;
            for (int i = 0; i < valuesCellsHeaderInfo.length; i++) {
                startRowHeaderInfo = createRowCalendar(sheet, valuesCellsHeaderInfo[i][0], stylesHeaderInfo, valuesCellsHeaderInfo[i], startRowHeaderInfo, 30, 1);
                if (i < 3 || i > 4) {
                    sheet.addMergedRegion(new CellRangeAddress(startRowHeaderInfo, startRowHeaderInfo, 1, 2));
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(startRowHeaderInfo - 3, startRowHeaderInfo - 2, 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(startRowHeaderInfo - 3, startRowHeaderInfo - 2, 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(startRowHeaderInfo - 4, startRowHeaderInfo - 4, 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(startRowHeaderInfo - 1, startRowHeaderInfo - 1, 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(startRowHeaderInfo, startRowHeaderInfo, 3, 4));

            // создание шапки таблицы
            String[][] valuesCellsHeaderTable =
                    {{"Идентификатор работы", "Ссылка на номер позиции этапа Календарного плана", "Наименование работы", "Номер договора с субподряной организацией",
                            "Субподрядная организация", "Статус работы*", "Дата начала", "", "Дата окончания", "", "Физический % выполнения", "Причины отклонения",
                            "Мероприятия по ликвидации отклонения", "Диаграмма Ганта (шкала времени: месяц по неделям)"},
                            {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                            {null, null, null, null, null, null, "Факт", "Ожидаемая", "Факт", "Ожидаемая", null, null, null, null},
                            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"},
                            {"Наименование комплекса работ", null, null, null, null, null, null, null, null, null, null, null, null, null}};
            XSSFCellStyle[] stylesHeaderTable = {headerTableStyle, headerTableStyle, headerTableStyle, headerTableStyle, lastHeaderTableStyle};

            for (int i = 0; i < valuesCellsHeaderTable.length; i++) {
                textHeader = sheet.createRow(startRowTableHeader++);
                textHeader.setHeightInPoints(40 - (6 * i));
                for (int j = 1; j < valuesCellsHeaderTable[i].length + 1; j++) {
                    createCellCalendar(j, stylesHeaderTable[i], valuesCellsHeaderTable[i][j - 1], textHeader);
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(startRowTableHeader - 1, startRowTableHeader - 1, 1, 14));
            for (int i = 1; i < 15; i++) {
                if (i < 7 || i > 10) {
                    sheet.addMergedRegion(new CellRangeAddress(startRowTableHeader - 5, startRowTableHeader - 3, i, i));
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(startRowTableHeader - 5, startRowTableHeader - 4, 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(startRowTableHeader - 5, startRowTableHeader - 4, 9, 10));
        }
    }

    /**
     * Создание ячеек самой таблицы с графиков с расчетными параметрами
     *
     * @param workbook рабочий лист Excel
     * @param sheet    страница в файле Excel
     */
    public void createCells(XSSFWorkbook workbook, Sheet sheet, List<Calendar> calendars) {
        int startRowCells = 16; // номер стартовой строчки в файле для таблицы с графиком после заголовка таблицы (начинается с "Этап строительства")
        XSSFCellStyle styleNumberStageCalendar = workbook.createCellStyle();
        XSSFCellStyle styleTextStageCalendar = workbook.createCellStyle();
        XSSFCellStyle styleDate = workbook.createCellStyle();
        XSSFCellStyle chapterStyleText = workbook.createCellStyle();

        styleNumberStageCalendar.setAlignment(HorizontalAlignment.CENTER);
        styleNumberStageCalendar.setVerticalAlignment(VerticalAlignment.CENTER);
        styleNumberStageCalendar.setBorderTop(BorderStyle.THIN);
        styleNumberStageCalendar.setBorderBottom(BorderStyle.THIN);
        styleNumberStageCalendar.setBorderLeft(BorderStyle.THIN);
        styleNumberStageCalendar.setBorderRight(BorderStyle.THIN);
        styleNumberStageCalendar.setWrapText(true);

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

        styleDate.setBorderTop(BorderStyle.THIN);
        styleDate.setBorderBottom(BorderStyle.THIN);
        styleDate.setBorderLeft(BorderStyle.THIN);
        styleDate.setBorderRight(BorderStyle.THIN);
        styleDate.setVerticalAlignment(VerticalAlignment.CENTER);
        styleDate.setAlignment(HorizontalAlignment.CENTER);

        XSSFFont workbookFont = workbook.createFont();
        workbookFont.setFontName("Arial");
        workbookFont.setFontHeightInPoints((short) 10);

        XSSFFont fontChapter = workbook.createFont();
        fontChapter.setFontName("Arial");
        fontChapter.setFontHeightInPoints((short) 10);
        fontChapter.setBold(true);

        styleNumberStageCalendar.setFont(workbookFont);
        styleTextStageCalendar.setFont(workbookFont);
        chapterStyleText.setFont(fontChapter);
        styleDate.setFont(fontChapter);

        int nextIter = 0; // корректируйющий коэффициент для номера раздела следующего этапа строительства
        int chapterCalendar = 1; // номер первого раздела первого этапа таблицы графика 1го уровня
        DateService dateService = new DateService();
        // стили ячеек в графике 1го уровня
        XSSFCellStyle[] stylesCells = {styleNumberStageCalendar, styleNumberStageCalendar, styleTextStageCalendar, styleNumberStageCalendar, styleNumberStageCalendar,
                styleNumberStageCalendar, styleNumberStageCalendar, styleDate, styleNumberStageCalendar, styleDate, styleNumberStageCalendar,
                styleNumberStageCalendar, styleNumberStageCalendar, styleNumberStageCalendar};
        // стили ячеек для заголовков этапов графика 1го уровня
        XSSFCellStyle[] stylesChapter = {chapterStyleText, styleDate, styleDate, styleDate, styleDate, styleDate, styleDate, styleDate,
                styleDate, styleDate, styleDate, styleDate, styleDate, styleDate};
        // создание графика 1го уровня
        for (Calendar calendar : calendars) {
            int rowChapterCalendar = 1;
            String[][] valuesCells =
                    {{"Этап строительства " + calendar.getStage(), null, null, null, null, null, null, null, null, null, null, null, null, null},
                            {"Инженерные изыскания", null, null, null, null, null, null, null, null, null, null, null, null, null},
                            {null, null, "Инженерные изыскания (полевые работы)", null, null, null, null,
                                    dateService.dateFormatter(calendar.getStartContract()), null,
                                    dateService.dateFormatter(calendar.getEngineeringSurvey()), null, null, null, null},
                            {null, null, "Инженерные изыскания (камеральные работы) - выдача отчета", null, null, null, null,
                                    dateService.dateFormatter(calendar.getEngineeringSurveyLabResearchAndReportStart()), null,
                                    dateService.dateFormatter(calendar.getEngineeringSurveyReportFinish()), null, null, null, null},
                            {null, null, "Инженерные изыскания (камеральные работы) согласование отчета", null, null, null, null,
                                    dateService.dateFormatter(calendar.getEngineeringSurveyReportFinish()), null,
                                    dateService.dateFormatter(calendar.getAgreementEngineeringSurvey()), null, null, null, null},
                            {null, null, "Инженерно-экологические изыскания", null, null, null, null,
                                    dateService.dateFormatter(calendar.getSeasonalEngineeringSurveysStart()), null,
                                    dateService.dateFormatter(calendar.getEngineeringAndEnvironmentalSurveysFinish()), null, null, null, null},
                            {null, null, "Историко-культурные изыскания", null, null, null, null,
                                    dateService.dateFormatter(calendar.getSeasonalEngineeringSurveysStart()), null,
                                    dateService.dateFormatter(calendar.getHistoricalAndCulturalResearchFinish()), null, null, null, null}};
            String[][] valuesCellsRD =
                    {{"Рабочая документация", null, null, null, null, null, null, null, null, null, null, null, null, null},
                            {null, null, "Рабочая документация (60%, выдача документации ОГ)", null, null, null, null,
                                    dateService.dateFormatter(calendar.getWorkingStart()), null,
                                    dateService.dateFormatter(calendar.getWorkingFinish()), null, null, null, null},
                            {null, null, "Рабочая документация (30%, согласование документации с ОГ и УВЭ)", null, null, null, null,
                                    dateService.dateFormatter(calendar.getWorkingFinish()), null,
                                    dateService.dateFormatter(calendar.getAgreementWorking()), null, null, null, null},
                            {null, null, "Рабочая документация  (5%, выдача сметной документации ОГ)", null, null, null, null,
                                    dateService.dateFormatter(calendar.getEstimatesStart()), null,
                                    dateService.dateFormatter(calendar.getEstimatesFinish()), null, null, null, null},
                            {null, null, "Рабочая документация  (5%, согласование сметной документации с ОГ и УВЭ)", null, null, null, null,
                                    dateService.dateFormatter(calendar.getEstimatesFinish()), null,
                                    dateService.dateFormatter(calendar.getAgreementEstimates()), null, null, null, null}};
            String[][] valuesCellsPD =
                    {{"Проектная документация", null, null, null, null, null, null, null, null, null, null, null, null, null},
                            {null, null, "Проектная документация (60%, выдача документации ОГ)", null, null, null, null,
                                    dateService.dateFormatter(calendar.getProjectStart()), null,
                                    dateService.dateFormatter(calendar.getProjectFinish()), null, null, null, null},
                            {null, null, "Проектная документация (20%, согласование документации с ОГ и УВЭ)", null, null, null, null,
                                    dateService.dateFormatter(calendar.getProjectFinish()), null,
                                    dateService.dateFormatter(calendar.getAgreementProject()), null, null, null, null},
                            {null, null, "Проектная документация (20%, проведение внешних экспертиз)", null, null, null, null,
                                    dateService.dateFormatter(calendar.getAgreementProject()), null,
                                    dateService.dateFormatter(calendar.getExamination()), null, null, null, null}};
            String[][] valuesCellsLand =
                    {{"Землеустроительные работы", null, null, null, null, null, null, null, null, null, null, null, null, null},
                            {null, null, "Землеустроительная документация", null, null, null, null,
                                    dateService.dateFormatter(calendar.getProjectFinish()), null,
                                    dateService.dateFormatter(calendar.getLandFinish()), null, null, null, null}};
            String[][] valuesCellsOtherWork =
                    {{"Прочие работы", null, null, null, null, null, null, null, null, null, null, null, null, null},
                            {null, null, "Проект санитарно-защитных зон промышленного объекта", null, null, null, null,
                                    dateService.dateFormatter(calendar.getProjectFinish()), null,
                                    dateService.dateFormatter(calendar.getSzzFinish()), null, null, null, null},
                            {null, null, "Разработка рыбохозяйственного раздела", null, null, null, null,
                                    dateService.dateFormatter(calendar.getProjectFinish()), null,
                                    dateService.dateFormatter(calendar.getRhrFinish()), null, null, null, null}};

            int numberRowCalendarStage = nextIter;
            if (chapterCalendar == 1) {
                numberRowCalendarStage = startRowCells - 1;
            }

            // создание подписи этапа строительства
            if (chapterCalendar > 1) {
                numberRowCalendarStage = createRowCalendar(sheet, null, stylesCells, valuesCells[0], numberRowCalendarStage, 15, 1);
                sheet.addMergedRegion(new CellRangeAddress(numberRowCalendarStage, numberRowCalendarStage, 1, 14));

            }
            numberRowCalendarStage = createRowCalendar(sheet, valuesCells[0][0], stylesChapter, valuesCells[0], numberRowCalendarStage, 22, 1);
            sheet.addMergedRegion(new CellRangeAddress(numberRowCalendarStage, numberRowCalendarStage, 1, 14));

            if (calendar.getEngineeringSurvey() != null || calendar.getEngineeringSurveyReportFinish() != null) {
                // создание заголовка ИИ
                numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + ". " + valuesCells[1][0], stylesChapter, valuesCells[1], numberRowCalendarStage, 25, 1);
                sheet.addMergedRegion(new CellRangeAddress(numberRowCalendarStage, numberRowCalendarStage, 1, 14));
                // создание ячеек сроков полевых ИИ
                if (calendar.getEngineeringSurvey() != null) {
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar++, stylesCells, valuesCells[2], numberRowCalendarStage, 45, 2);
                }
                // создание ячеек сроков отчета ИИ
                if (calendar.getEngineeringSurveyReportFinish() != null) {
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar++, stylesCells, valuesCells[3], numberRowCalendarStage, 45, 2);
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar++, stylesCells, valuesCells[4], numberRowCalendarStage, 45, 2);
                }
                // создание ячеек сроков ИЭИ и ИКИ
                if (calendar.getEngineeringAndEnvironmentalSurveysFinish() != null) {
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar++, stylesCells, valuesCells[5], numberRowCalendarStage, 45, 2);
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar, stylesCells, valuesCells[6], numberRowCalendarStage, 45, 2);
                }
                chapterCalendar++;
                rowChapterCalendar = 1;
            }
            // создание ячеек сроков РД и СД
            if (calendar.getWorkingStart() != null) {
                numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + ". " + valuesCellsRD[0][0], stylesChapter, valuesCellsRD[0], numberRowCalendarStage, 25, 1);
                sheet.addMergedRegion(new CellRangeAddress(numberRowCalendarStage, numberRowCalendarStage, 1, 14));
                for (int i = 2; i < valuesCellsRD.length + 1; i++) {
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar++, stylesCells, valuesCellsRD[i - 1], numberRowCalendarStage, 50, 2);
                }
                chapterCalendar++;
                rowChapterCalendar = 1;
            }
            // создание ячеек сроков ПД
            if (calendar.getProjectFinish() != null) {
                numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + ". " + valuesCellsPD[0][0], stylesChapter, valuesCellsPD[0], numberRowCalendarStage, 25, 1);
                sheet.addMergedRegion(new CellRangeAddress(numberRowCalendarStage, numberRowCalendarStage, 1, 14));
                for (int i = 2; i < valuesCellsPD.length + 1; i++) {
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar++, stylesCells, valuesCellsPD[i - 1], numberRowCalendarStage, 50, 2);
                }
                chapterCalendar++;
                rowChapterCalendar = 1;
            }
            // создание ячеек срока ЗУР
            if (calendar.getProjectFinish() != null) {
                numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + ". " + valuesCellsLand[0][0], stylesChapter, valuesCellsLand[0], numberRowCalendarStage, 25, 1);
                sheet.addMergedRegion(new CellRangeAddress(numberRowCalendarStage, numberRowCalendarStage, 1, 14));
                numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar, stylesCells, valuesCellsLand[1], numberRowCalendarStage, 45, 2);
                chapterCalendar++;
            }
            // создание ячеек срока прочих работ
            if (calendar.getRhrFinish() != null || calendar.getSzzFinish() != null) {
                numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + ". " + valuesCellsOtherWork[0][0], stylesChapter, valuesCellsOtherWork[0], numberRowCalendarStage, 25, 1);
                sheet.addMergedRegion(new CellRangeAddress(numberRowCalendarStage, numberRowCalendarStage, 1, 14));
                if (calendar.getSzzFinish() != null) {
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar++, stylesCells, valuesCellsOtherWork[1], numberRowCalendarStage, 45, 2);
                }
                if (calendar.getRhrFinish() != null) {
                    numberRowCalendarStage = createRowCalendar(sheet, chapterCalendar + "." + rowChapterCalendar, stylesCells, valuesCellsOtherWork[2], numberRowCalendarStage, 45, 2);
                }
                chapterCalendar++;
            }
            nextIter = numberRowCalendarStage;
        }
    }

    /**
     * Метод создание строчки графика 1го уровня
     *
     * @param sheet                      лист в файле
     * @param value                      значение первой ячейки строчки
     * @param styles                     стили ячеек
     * @param valuesCells                значения ячеек
     * @param numberRow                  предыдущий номер строчки графика 1го уровня
     * @param heightRow                  высота строки excel
     * @param firstColumnNumberWithValue первая ячейка (колонка) строки со значением value
     * @return текущий номер строчки графика 1го уровня
     */
    private int createRowCalendar(Sheet sheet, String value, XSSFCellStyle[] styles, String[] valuesCells, int numberRow, int heightRow, int firstColumnNumberWithValue) {
        Row row = sheet.createRow(++numberRow);
        row.setHeightInPoints(heightRow);
        if (firstColumnNumberWithValue == 2) {
            createCellCalendar(firstColumnNumberWithValue - 1, styles[firstColumnNumberWithValue - 2], null, row);
        }
        createCellCalendar(firstColumnNumberWithValue, styles[firstColumnNumberWithValue - 1], value, row);
        for (int i = firstColumnNumberWithValue + 1; i < valuesCells.length + 1; i++) {
            createCellCalendar(i, styles[i - 1], valuesCells[i - 1], row);
        }
        return numberRow;
    }

    /**
     * Метод создание ячейки в строчке графика 1го уровня
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
