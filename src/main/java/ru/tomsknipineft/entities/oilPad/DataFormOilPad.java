package ru.tomsknipineft.entities.oilPad;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.group.GroupSequenceProvider;
import ru.tomsknipineft.entities.DataFormProject;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.entities.linearObjects.CableRack;
import ru.tomsknipineft.entities.linearObjects.Line;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.utils.entityValidator.EngineeringSurveyOilPadGroupSequenceProvider;
import ru.tomsknipineft.utils.entityValidator.OnActiveEngineeringSurvey;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Объект, который включает в себя все сооружения инженерной подготовки кустовой площадки
 */
@GroupSequenceProvider(EngineeringSurveyOilPadGroupSequenceProvider.class)
@Data
public class DataFormOilPad implements DataFormProject, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    //    шифр договора
    @NotNull(message = "Шифр не может быть пустым")
    @Size(min = 4, max = 10, message = "Шифр должен иметь количество символов от 4 до 10")
    String codeContract;

    //    наименование проекта
    @NotNull(message = "Наименование проекта не может быть пустым")
    @Size(min = 10, max = 200, message = "Наименование проекта должно иметь количество символов более 10")
    private String projectName;

    //    начало выполнения работ по договору
    @NotNull(message = "Дата не может быть пустой")
    @FutureOrPresent(message = "Указана прошедшая дата")
    LocalDate startContract;

    @Valid
    private BackfillWell backfillWell = new BackfillWell();

    @Valid
    private Road road = new Road();

    @Valid
    private Line line = new Line();

    @Valid
    private List<BackfillSite> backfillSites = new ArrayList<>();

    @Valid
    private Vvp vvp = new Vvp();

    @Valid
    private CableRack cableRack = new CableRack();

    // есть ли полевые ИИ
    private boolean fieldEngineeringSurvey;

    // есть ли камеральные ИИ
    private boolean engineeringSurveyReport;

    // количество буровых бригад для выполнения ИИ
    @NotNull(message = "Заполните количество буровых бригад", groups = OnActiveEngineeringSurvey.class)
    @Min(value = 1, message = "Количество буровых бригад не может быть меньше 1", groups = OnActiveEngineeringSurvey.class)
    @Max(value = 10, message = "Количество буровых бригад не должно быть больше 10", groups = OnActiveEngineeringSurvey.class)
    private Integer drillingRig;

    // человеческий фактор, непредвиденные обстоятельства, форс-мажор, какие-либо непределенности в выполнении работ
    @NotNull(message = "Заполните человеческий фактор")
    @Min(value = 0, message = "Человеческий фактор не может быть меньше 0")
    @Max(value = 50, message = "Человеческий фактор не должен быть больше 50")
    private Integer humanFactor;

    /**
     * Метод получения всех сущностей (сооружений) из DataFormOilPad
     *
     * @return список сущностей (сооружений)
     */
    public List<EntityProject> getEntityProjects() {
        List<EntityProject> entityProjects = new ArrayList<>(List.of(getBackfillWell(), getRoad(), getLine(), getVvp(), getCableRack()));
        entityProjects.addAll(getBackfillSites());
        return entityProjects;
    }
}
