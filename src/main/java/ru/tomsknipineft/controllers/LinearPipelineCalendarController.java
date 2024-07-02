package ru.tomsknipineft.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.entities.linearObjects.DataFormLinearObjects;
import ru.tomsknipineft.services.CalendarService;
import ru.tomsknipineft.services.LinearPipelineGroupCalendarServiceImpl;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/linear_object/field_pipeline")
public class LinearPipelineCalendarController {

    private final LinearPipelineGroupCalendarServiceImpl linearObjectGroupCalendarService;
    private final CalendarService calendarService;

    private static final Logger logger = LogManager.getLogger(BackfillWellCalendarController.class);

    /**
     * Страница с вводом данных по линейному трубопроводу для формирования календарного плана договора
     */
    @GetMapping
    public String linearPipelinePage(Model model){
        model.addAttribute("dataFormLinearObjects", new DataFormLinearObjects());
        return "input_page/field-pipeline";
    }

    /**
     * Получение данных из страницы ввода данных для формирования календарного плана
     * @param dataFormLinearObjects класс, содержащий основные данные по объекту инженерная подготовка кустовой площадки
     * @return перенаправление на страницу вывода календарного плана договора
     */
    @PostMapping("/create")
    public String createCalendar(@Valid @ModelAttribute("dataFormLinearObjects") DataFormLinearObjects dataFormLinearObjects,
                                 BindingResult bindingResult, HttpSession session){
        if (bindingResult.hasErrors()){
            return "input_page/field-pipeline";
        }
        session.setAttribute("codeContract", dataFormLinearObjects.getCodeContract());
        calendarService.createCalendar(dataFormLinearObjects.getEntityProjects(), linearObjectGroupCalendarService, dataFormLinearObjects);
        return "redirect:/linear_object/field_pipeline/calendar";
    }

    /**
     * Страница с выводом календарного плана договора
     */
    @GetMapping("/calendar")
    public String resultCalendar(Model model, HttpSession session){
        String codeContract = (String) session.getAttribute("codeContract");
        List<Calendar> calendars = calendarService.getCalendarByCode(codeContract);
        DataFormLinearObjects dataFormLinearObjects = (DataFormLinearObjects) calendarService.getDataFormProject(calendars);
        logger.info("Календарь по шифру " + codeContract + " выведен - " + calendars);
        model.addAttribute("calendars", calendars);
        model.addAttribute("codeContract", codeContract);
        model.addAttribute("dataFormLinearObjects", dataFormLinearObjects);
        model.addAttribute("fieldEngineeringSurvey", dataFormLinearObjects.isFieldEngineeringSurvey());
        model.addAttribute("engineeringSurveyReport", dataFormLinearObjects.isEngineeringSurveyReport());
        model.addAttribute("mmg", dataFormLinearObjects.isMmg());
//        model.addAttribute("notRhrDoc", dataFormLinearObjects.isRhrDoc());
//        model.addAttribute("notSzzDoc", dataFormLinearObjects.isNotSzzDoc());
        return "result_calendar/linear-object-result-calendar";
    }
}
