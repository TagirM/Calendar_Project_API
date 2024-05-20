package ru.tomsknipineft.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;
import ru.tomsknipineft.services.BackfillWellGroupCalendarServiceImpl;
import ru.tomsknipineft.services.CalendarService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oil_pad_object/backfill_well")
public class BackfillWellCalendarController {

    private final BackfillWellGroupCalendarServiceImpl backFillWellCalendarServiceImpl;

    private final CalendarService calendarService;

//    private String codeContract;

//    private DataFormOilPad dataFormOilPad;


//    protected List<Calendar> calendars;

    private static final Logger logger = LogManager.getLogger(BackfillWellCalendarController.class);

    /**
     * Страница с вводом данных по инженерной подготовке куста для формирования календарного плана договора
     */
    @GetMapping
    public String backfillWellPage(Model model) {
//        calendars = null;
        model.addAttribute("dataFormOilPad", new DataFormOilPad());
        return "input_page/backfill-well";
    }

    /**
     * Получение данных из страницы ввода данных для формирования календарного плана
     *
     * @param dataFormOilPad класс, содержащий основные данные по объекту инженерная подготовка кустовой площадки
     * @return перенаправление на страницу вывода календарного плана договора
     */
    @PostMapping("/create")
    public String createCalendar(@Valid @ModelAttribute("dataFormOilPad") DataFormOilPad dataFormOilPad, BindingResult bindingResult,
                                 HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "input_page/backfill-well";
        }
        List<EntityProject> entityProjects = new ArrayList<>(List.of(dataFormOilPad.getBackfillWell(), dataFormOilPad.getRoad(),
                dataFormOilPad.getLine(), dataFormOilPad.getVvp(), dataFormOilPad.getCableRack()));
        entityProjects.addAll(dataFormOilPad.getBackfillSites());

        String codeContract = dataFormOilPad.getCodeContract();
        session.setAttribute("codeContract", codeContract);
//        this.dataFormOilPad = dataFormOilPad;
        long startTime = System.currentTimeMillis();
        calendarService.createCalendar(entityProjects, backFillWellCalendarServiceImpl, dataFormOilPad);
        long executionTime = System.currentTimeMillis() - startTime;
        logger.info("Создание календаря заняло время " + executionTime);
        return "redirect:/oil_pad_object/backfill_well/calendar";
    }

    /**
     * Страница с выводом календарного плана договора
     */
    @GetMapping("/calendar")
    public String resultCalendar(Model model, HttpSession session) {
        String codeContract = (String) session.getAttribute("codeContract");
//        logger.info("Календарь по шифру " + codeContract);
//        if (codeFromRequest != null) {
//            String codeContract = codeFromRequest;
        long startTime = System.currentTimeMillis();
        List<Calendar> calendars = calendarService.getCalendarByCode(codeContract);
        long executionTime = System.currentTimeMillis() - startTime;
        logger.info("Получение календаря из БД заняло время " + executionTime);
        DataFormOilPad dataFormOilPad = (DataFormOilPad) calendarService.getDataFormProject(calendars);
//        }
        logger.info("Календарь по шифру " + codeContract + " выведен - " + calendars);
        model.addAttribute("calendars", calendars);
        model.addAttribute("codeContract", codeContract);
        model.addAttribute("dataFormOilPad", dataFormOilPad);
        model.addAttribute("fieldEngineeringSurvey", dataFormOilPad.isFieldEngineeringSurvey());
        model.addAttribute("engineeringSurveyReport", dataFormOilPad.isEngineeringSurveyReport());
        return "result_calendar/oil-pad-result-calendar";
    }
}

