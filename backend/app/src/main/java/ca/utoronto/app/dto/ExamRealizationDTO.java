package ca.utoronto.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExamRealizationDTO extends BaseDTO<Long> {
    private Integer score;
    private ExamTermDTO examTerm;
    private SubjectEnrollmentDTO subjectEnrollment;
}
