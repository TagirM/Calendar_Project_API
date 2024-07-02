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
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;
import ru.tomsknipineft.services.BackfillWellGroupCalendarServiceImpl;
import ru.tomsknipineft.services.CalendarService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/oil_pad_object/backfill_well")
public class BackfillWellCalendarController {

    private final BackfillWellGroupCalendarServiceImpl backFillWellCalendarServiceImpl;

    private final CalendarService calendarService;

    private static final Logger logger = LogManager.getLogger(BackfillWellCalendarController.class);

    /**
     * Страница с вводом данных по инженерной подготовке куста для формирования календарного плана договора
     */
    @GetMapping
    public String backfillWellPage(Model model) {
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
        // todo c 64 по 74 строки напрашивается выделение в метод сервиса
        // Строчка session.setAttribute это чисто контроллерный код по передаче аттрибута в другой метод контроллера, это параметр контроллера, в сервис это не переносится.
        // Строчку String codeContract сократил.
        // Строчку List<EntityProject> entityProjects и entityProjects.addAll перенес в dataFormOilPad в метод getEntityProjects.
        // Зачем переносить строчку calendarService.createCalendar? Это вызов метода сервиса, т.е. то, что и должен делать контроллер.
        // Подсчет времени делал просто для себя, измерял время с кэшем и без, закомментировал.
        // В итоге оставил только команды нужные для контроллера, без бизнес-логики, аналогично сделал в LinearPipelineCalendarController.

        session.setAttribute("codeContract", dataFormOilPad.getCodeContract());
        calendarService.createCalendar(dataFormOilPad.getEntityProjects(), backFillWellCalendarServiceImpl, dataFormOilPad);
        return "redirect:/oil_pad_object/backfill_well/calendar";
    }

    /**
     * Страница с выводом календарного плана договора
     */
    @GetMapping("/calendar")
    public String resultCalendar(Model model, HttpSession session) {
        // todo с 84 по 99 мне кажется правильнее сделать метод сервиса
        // Строчка String codeContract - это получение аттрибута сессии, которая в параметре, исключить нельзя.
        // Подсчет времени делал просто для себя, измерял время с кэшем и без, закомментировал
        // Строчки List<Calendar> calendars и DataFormOilPad dataFormOilPad это просто переменные по передаче результата отработки
        // сервиса в представление (model), их можно вообще убрать, просто код тогда будет чуть хуже читаем, хоть и короче...
        // Строчка logger.info - это просто логи, в сервис неудобно было переносить, т.к. в методе getDataFormProject сразу return,
        // если переносить, тогда надо увеличивать код и сначала получать из репозитория календарь, делать лог, а потом возвращать calendars.
        // а в контроллере, получается, нельзя оставлять логи?
        // Строчки, идущие ниже logger.info - это аттрибуты модели в представлении, их не убрать. Можно сократить последние 2 строчки и
        // в представлении уже доставать isFieldEngineeringSurvey и isEngineeringSurveyReport из dataFormOilPad, но это менее удобно,
        // значительно увеличиться код в представлении, т.к. повторяющаяся часть вынесена в блок. Это необходимо?

        String codeContract = (String) session.getAttribute("codeContract");
        List<Calendar> calendars = calendarService.getCalendarByCode(codeContract);
        DataFormOilPad dataFormOilPad = (DataFormOilPad) calendarService.getDataFormProject(calendars);
        logger.info("Календарь по шифру " + codeContract + " выведен - " + calendars);
        model.addAttribute("calendars", calendars);
        model.addAttribute("codeContract", codeContract);
        model.addAttribute("dataFormOilPad", dataFormOilPad);
        model.addAttribute("fieldEngineeringSurvey", dataFormOilPad.isFieldEngineeringSurvey());
        model.addAttribute("engineeringSurveyReport", dataFormOilPad.isEngineeringSurveyReport());
        model.addAttribute("mmg", dataFormOilPad.isMmg());
//        model.addAttribute("notRhrDoc", dataFormOilPad.isRhrDoc());
//        model.addAttribute("notSzzDoc", dataFormOilPad.isNotSzzDoc());
        return "result_calendar/oil-pad-result-calendar";
    }
}

