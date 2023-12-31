package ca.umd.lms.exam.mapper;

import ca.umd.lms.exam.dto.ExamRealizationDTO;
import ca.umd.lms.exam.dto.SubjectEnrollmentDTO;
import ca.umd.lms.exam.model.ExamRealization;
import ca.utoronto.lms.shared.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
//// the ExamRealizationMapper class defines mappings between ExamRealization entities and ExamRealizationDTO DTOs. /
//It uses annotations to specify how fields should be mapped and includes a custom method for determining if a student has passed an exam realization. 
//The abstract class structure and the use of MapStruct annotations facilitate the automatic generation of mapping code based on these specifications.
public abstract class ExamRealizationMapper
        implements BaseMapper<ExamRealization, ExamRealizationDTO, Long> {
    @Mapping(source = "subjectEnrollmentId", target = "subjectEnrollment")
    @Mapping(source = "examRealization", target = "passed", qualifiedByName = "hasPassed")
    public abstract ExamRealizationDTO toDTO(ExamRealization examRealization);

    @Mapping(source = "subjectEnrollment.id", target = "subjectEnrollmentId")
    public abstract ExamRealization toModel(ExamRealizationDTO examRealizationDTO);

    public abstract SubjectEnrollmentDTO subjectEnrollmentDTOFromId(Long id);

    @Named("hasPassed")
    public Boolean hasPassed(ExamRealization examRealization) {
        Integer score = examRealization.getScore();
        return score != null
                ? score >= examRealization.getExamTerm().getExam().getMinimumScore()
                : null;
    }
}
