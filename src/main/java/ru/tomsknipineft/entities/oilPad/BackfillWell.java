package ru.tomsknipineft.entities.oilPad;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.enumEntities.ObjectType;

import java.io.Serializable;

/**
 * Инженерная подготовка кустовой площадки
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "backfill_wells")
public class BackfillWell implements OilPad, EntityProject, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean active = true;

    @Column(name = "object_type")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    //    количество скважин
    @NotNull(message = "Данное поле не может быть пустым")
    @Min(value = 1, message = "Не может быть меньше 1")
    @Max(value = 24, message = "Не может быть больше 24")
    private Integer well;

    //    этап строительства
    @NotNull(message = "Данное поле не может быть пустым")
    @Min(value = 1, message = "Не может быть меньше 1")
    private Integer stage = 1;

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
