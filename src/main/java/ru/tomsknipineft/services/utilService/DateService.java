package ru.tomsknipineft.services.utilService;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Класс с методами по обработке дат календарного плана
 */
@Service
public class DateService {

    /**
     * Коллекция выходных дней недели
     */
    private static final Collection<DayOfWeek> weekends = Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    /**
     * Метод, проверяющий, является ли дата праздничным днем
     *
     * @param localDate проверяемая дата
     * @return boolean
     */
    private boolean isHolidays(LocalDate localDate) {
        Collection<LocalDate> holidays = new HashSet<>(List.of(
                LocalDate.of(localDate.getYear(), 1, 1),
                LocalDate.of(localDate.getYear(), 1, 2),
                LocalDate.of(localDate.getYear(), 1, 3),
                LocalDate.of(localDate.getYear(), 1, 4),
                LocalDate.of(localDate.getYear(), 1, 5),
                LocalDate.of(localDate.getYear(), 1, 6),
                LocalDate.of(localDate.getYear(), 1, 7),
                LocalDate.of(localDate.getYear(), 1, 8),
                LocalDate.of(localDate.getYear(), 2, 23),
                LocalDate.of(localDate.getYear(), 3, 8),
                LocalDate.of(localDate.getYear(), 5, 1),
                LocalDate.of(localDate.getYear(), 5, 9),
                LocalDate.of(localDate.getYear(), 6, 12),
                LocalDate.of(localDate.getYear(), 11, 4)));
        return holidays.contains(localDate);
    }

    /**
     * Метод, учитывающий выходные и праздничные дни. При попадании даты на выходной, производится перенос на будний день
     *
     * @param date исходная дата
     * @return будний день
     */
    public LocalDate workDay(LocalDate date) {
        while (weekends.contains(date.getDayOfWeek()) || isHolidays(date)) {
            date = date.plusDays(1);
        }
        return date;
    }

    /**
     * Метод, пересчитывающий ресурсы в календарные дни с учетом выходных и праздничных дней. При попадании крайней даты ресурса
     * на выходной или праздничный день, производится перенос на будний день
     *
     * @param resources     ресурсы, ч/дн
     * @param startResource дата начала работ ресурса
     * @return календарная дата окончания работ
     */
    public LocalDate recalculationResourcesInCalendarDate(int resources, LocalDate startResource) {
        LocalDate result = startResource;
        int addedDays = 0;
        while (addedDays < resources) {
            result = result.plusDays(1);
            if (!(weekends.contains(result.getDayOfWeek()) || isHolidays(result))) {
                ++addedDays;
            }
        }
        result = workDay(result);
        return result;
    }

    /**
     * Метод, учитывающий крайний день календаря 10е число в декабре и 20е число в остальных месяцах для актирования.
     * Так как сроки актирования этапа работ с 1 по 10 число в декабре, с 1 по 20 - в другие месяцы.
     * Если дата выходит за рамка указанных дат, то перенос даты на следующий месяц (например, если 22 марта - то на 7 апреля)
     *
     * @param date исходная дата
     * @return валидный день для актирования
     */
    public LocalDate checkDeadlineForActivation(LocalDate date) {
        if (date != null){
            if ((date.getMonth() != Month.DECEMBER && date.getDayOfMonth() > 20) || (date.getMonth() == Month.DECEMBER && date.getDayOfMonth() > 10)) {
                date = workDay(LocalDate.of(date.getYear(), date.getMonth(), 3).plusMonths(1));
            }
            return date;
        }
        return null;
    }

    /**
     * Метод, возвращающий дату типа LocalDate в формате "dd.MM.yyyy" типа String
     * @param localDate дата
     * @return дата в формате "dd.MM.yyyy" типа String
     */
    public String dateFormatter(LocalDate localDate){
        if (localDate != null){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return localDate.format(dateTimeFormatter);
        }
        return null;
    }
}
