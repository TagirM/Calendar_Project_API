package ru.tomsknipineft.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Календарный план со сроками проектирования всех стадий договора (по этапу строительства)
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "calendar")
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //    шифр договора
    @Column(name = "code_contract")
    @NotNull(message = "The code should not be empty")
    @Size(min = 4, max = 10, message = "Code should be between 4 and 10 characters")
    private String codeContract;

    //    наименование проекта
    @Column(name = "project_name")
    @NotNull(message = "The name should not be empty")
    @Size(min = 10, max = 200, message = "Name should be less than 10 characters")
    private String projectName;

    //    начало выполнения работ по договору
    @Column(name = "start_contract")
    @NotNull(message = "The date should not be empty")
    @FutureOrPresent(message = "The date can not be past")
    private LocalDate startContract;

    //    этап строительства
    private Integer stage;

    //    дата окончания полевых ИИ (инженерных изысканий)
    private LocalDate engineeringSurvey;
    //    дата начала ЛИ и далее начала отчета ИИ
    private LocalDate engineeringSurveyLabResearchAndReportStart;
    //    дата выдачи отчета ИИ
    private LocalDate engineeringSurveyReportFinish;
    //    дата согласования отчета ИИ
    private LocalDate agreementEngineeringSurvey;
    //    дата начала сезонных ИИ
    private LocalDate seasonalEngineeringSurveysStart;
    //    дата закрытия ИЭИ
    private LocalDate engineeringAndEnvironmentalSurveysFinish;
    //    дата закрытия ИКИ
    private LocalDate historicalAndCulturalResearchFinish;

    //    дата начала разработки РД (рабочей документации)
    private LocalDate workingStart;
    //    реальная дата выдачи РД (рабочей документации)
    private LocalDate realWorkingFinish;
    //    дата актирования РД (рабочей документации)
    private LocalDate workingFinish;
    //    дата согласования РД
    private LocalDate agreementWorking;

    //    дата начала разработки СД (сметной документации)
    private LocalDate estimatesStart;
    //    дата актирования СД (сметной документации)
    private LocalDate estimatesFinish;
    //    дата согласования СД
    private LocalDate agreementEstimates;

    //    дата начала разработки ПД (проектной документации)
    private LocalDate projectStart;
    //   реальная дата выдачи ПД (проектной документации)
    private LocalDate realProjectFinish;
    //    дата актирования ПД (проектной документации)
    private LocalDate projectFinish;
    //    дата согласования ПД
    private LocalDate agreementProject;
    //    дата заключения эспертизы ПД
    private LocalDate examination;

    //    дата выдачи ППиМТ
    private LocalDate landFinish;

    //    дата утверждения РХР
    private LocalDate rhrFinish;
    //    дата утверждения СЗЗ
    private LocalDate szzFinish;

    //    дата создания календарного плана
    private LocalDateTime dateOfCreated;

    // человеческий фактор, %
    private Integer humanFactor;


    // сохранение в виде файла данных о проекте
    @Lob
//    @Column(name = "bytes_data_project", columnDefinition = "LONGBLOB") // Для БД H2
    @Column(name = "bytes_data_project") // Для БД PostgresSQL
    private byte[] bytesDataProject;

    @PrePersist
    private void init(){
        dateOfCreated = LocalDateTime.now();
    }

    @Transactional
    public byte[] getBytesDataProject() {
        return bytesDataProject;
    }

}
