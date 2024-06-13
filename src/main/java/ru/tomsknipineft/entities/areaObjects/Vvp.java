package ru.tomsknipineft.entities.areaObjects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.enumEntities.ObjectType;
import ru.tomsknipineft.entities.oilPad.OilPad;
import ru.tomsknipineft.utils.entityValidator.OnActiveCheck;
import ru.tomsknipineft.utils.entityValidator.VvpGroupSequenceProvider;
import ru.tomsknipineft.utils.entityValidator.VvpModelCheck;

import java.io.Serializable;

/**
 * Временная вертолетная площадка (ВВП)
 */
@GroupSequenceProvider(VvpGroupSequenceProvider.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vvps")
public class Vvp implements OilPad, EntityProject, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean active;

    @Column(name = "object_type")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    //    для посадки какого вертолета предназначена ВВП
    @NotNull(message = "Информация по вертолету не заполнена", groups = VvpModelCheck.class)
    @Size(min = 4, max = 5, message = "наименование модели находится в интервале 4-5 символов", groups = VvpModelCheck.class)
    private String helicopterModel;

    //    необходимость светосигнального оборудования
    @Column(name = "lighting_equipment")
    private boolean lightingEquipment;

    //    наличие зала ожидания
    @Column(name = "hall_exist")
    private boolean hallExist;

    //    площадь отсыпки, га
    @NotNull(message = "Площадь не заполнена", groups = VvpModelCheck.class)
    @Positive(message = "Площадь не может быть отрицательной", groups = VvpModelCheck.class)
    @Max(value = 4, message = "Не может быть больше 4", groups = VvpModelCheck.class)
    private Double square;

    //    этап строительства
    @NotNull(message = "Этап не заполнен", groups = OnActiveCheck.class)
    @Min(value = 1, message = "Не может быть меньше 1", groups = OnActiveCheck.class)
    private Integer stage;

    //    необходимые ресурсы для выполнения геодезических полевых ИИ, чел/дней
    @Column(name = "resource_for_eng_geodetic_survey")
    private Integer resourceForEngGeodeticSurvey;

    //    необходимые ресурсы для выполнения геологических полевых ИИ, чел/дней
    @Column(name = "resource_for_eng_geological_survey")
    private Integer resourceForEngGeologicalSurvey;

    //    необходимые ресурсы для выполнения ЛИ, чел/дней
    @Column(name = "resource_for_lab_research")
    private Integer resourceForLabResearch;

    //    необходимые ресурсы для выполнения отчета ИИ, чел/дней
    @Column(name = "resource_for_eng_survey_report")
    private Integer resourceForEngSurveyReport;

    //    необходимые ресурсы для разработки РД, чел/дней
    @Column(name = "resource_for_work_doc")
    private Integer resourceForWorkDoc;

    //    необходимые ресурсы для разработки ПД, чел/дней
    @Column(name = "resource_for_proj_doc")
    private Integer resourceForProjDoc;

    //    необходимые ресурсы для разработки СД, чел/дней
    @Column(name = "resource_for_est_doc")
    private Integer resourceForEstDoc;
}
