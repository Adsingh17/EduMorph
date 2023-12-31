package ca.umd.lms.subject.service;

import ca.utoronto.lms.shared.exception.ForbiddenException;
import ca.utoronto.lms.shared.exception.NotFoundException;
import ca.utoronto.lms.shared.service.ExtendedService;
import ca.umd.lms.subject.client.FacultyFeignClient;
import ca.umd.lms.subject.dto.SubjectDTO;
import ca.umd.lms.subject.dto.SubjectMaterialDTO;
import ca.umd.lms.subject.dto.TeacherDTO;
import ca.umd.lms.subject.mapper.SubjectMaterialMapper;
import ca.umd.lms.subject.model.Subject;
import ca.umd.lms.subject.model.SubjectMaterial;
import ca.umd.lms.subject.repository.SubjectMaterialRepository;
import ca.umd.lms.subject.repository.SubjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static ca.utoronto.lms.shared.security.SecurityUtils.*;

@Service
public class SubjectMaterialService
        extends ExtendedService<SubjectMaterial, SubjectMaterialDTO, Long> {
    private final SubjectMaterialRepository repository;
    private final SubjectMaterialMapper mapper;
    private final SubjectRepository subjectRepository;
    private final FacultyFeignClient facultyFeignClient;

    public SubjectMaterialService(
            SubjectMaterialRepository repository,
            SubjectMaterialMapper mapper,
            SubjectRepository subjectRepository,
            FacultyFeignClient facultyFeignClient) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.subjectRepository = subjectRepository;
        this.facultyFeignClient = facultyFeignClient;
    }

    @Override
    @Transactional
    //Saves a new SubjectMaterialDTO or updates an existing one.
//Checks if the user has the ROLE_TEACHER authority.
//Verifies that the teacher attempting to save the material is associated with the subject.
//If the teacher field is not provided in the DTO, it is set to the teacher making the request.
    public SubjectMaterialDTO save(SubjectMaterialDTO subjectMaterialDTO) {
        if (hasAuthority(ROLE_TEACHER)) {
            TeacherDTO teacher = facultyFeignClient.getTeacher(Set.of(getTeacherId())).get(0);
            SubjectDTO subject = subjectMaterialDTO.getSubject();
            if (!subject.getProfessor().getId().equals(teacher.getId())
                    && !subject.getAssistant().getId().equals(teacher.getId())) {
                throw new ForbiddenException(
                        "You are not allowed to manager this subject material");
            }

            if (subjectMaterialDTO.getTeacher() == null) {
                subjectMaterialDTO.setTeacher(teacher);
            }
        }

        return super.save(subjectMaterialDTO);
    }

    @Override
    @Transactional
    //Deletes subject materials by their IDs.
//Checks if the user has the ROLE_TEACHER authority.
//Verifies that the teacher attempting to delete the materials is associated with the subject of each material.
    public void delete(Set<Long> id) {
        if (hasAuthority(ROLE_TEACHER)) {
            Long teacherId = getTeacherId();
            List<SubjectMaterial> subjectMaterials =
                    (List<SubjectMaterial>) repository.findAllById(id);
            boolean forbidden =
                    subjectMaterials.stream()
                            .anyMatch(
                                    subjectMaterial -> {
                                        Subject subject = subjectMaterial.getSubject();
                                        return !subject.getProfessorId().equals(teacherId)
                                                && !subject.getAssistantId().equals(teacherId);
                                    });
            if (forbidden) {
                throw new ForbiddenException(
                        "You are not allowed to delete these subject materials");
            }
        }

        super.delete(id);
    }

    @Override
    protected List<SubjectMaterialDTO> mapMissingValues(List<SubjectMaterialDTO> subjectMaterials) {
         //Maps missing values in a list of SubjectMaterialDTO by fetching additional details from external services (facultyFeignClient).
//Specifically maps the teacher associated with each subject material.
        map(
                subjectMaterials,
                SubjectMaterialDTO::getTeacher,
                SubjectMaterialDTO::setTeacher,
                facultyFeignClient::getTeacher);

        return subjectMaterials;
    }
//Retrieves subject materials based on the subject ID.
//Throws a NotFoundException if the subject with the given ID is not found.
//Checks for missing values and maps them using the mapMissingValues method.
    public List<SubjectMaterialDTO> findBySubjectId(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new NotFoundException("Subject not found");
        }

        List<SubjectMaterialDTO> subjectMaterials =
                mapper.toDTO(
                        repository.findBySubjectIdAndDeletedFalseOrderByPublicationDateDesc(id));
        return subjectMaterials.isEmpty()
                ? subjectMaterials
                : this.mapMissingValues(subjectMaterials);
    }

    public Page<SubjectMaterialDTO> findBySubjectId(Long id, Pageable pageable, String search) {
        if (!subjectRepository.existsById(id)) {
            throw new NotFoundException("Subject not found");
        }

        Page<SubjectMaterialDTO> subjectMaterials =
                repository
                        .findBySubjectIdContaining(id, pageable, "%" + search + "%")
                        .map(mapper::toDTO);
        return subjectMaterials.getContent().isEmpty()
                ? subjectMaterials
                : new PageImpl<>(
                        this.mapMissingValues(subjectMaterials.getContent()),
                        pageable,
                        subjectMaterials.getTotalElements());
    }
}
//class provides methods for saving, deleting, and retrieving subject materials. 
//It enforces authorization rules based on user roles, ensures the association between teachers and subjects, and maps missing values to enhance the completeness of the data.