package ca.umd.lms.faculty.service;

import ca.umd.lms.faculty.client.UserFeignClient;
import ca.umd.lms.faculty.dto.TeacherDTO;
import ca.umd.lms.faculty.mapper.TeacherMapper;
import ca.umd.lms.faculty.model.Teacher;
import ca.umd.lms.faculty.repository.TeacherRepository;
import ca.utoronto.lms.shared.dto.RoleDTO;
import ca.utoronto.lms.shared.dto.UserDTO;
import ca.utoronto.lms.shared.dto.UserDetailsDTO;
import ca.utoronto.lms.shared.exception.NotFoundException;
import ca.utoronto.lms.shared.service.ExtendedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.utoronto.lms.shared.security.SecurityUtils.ROLE_TEACHER;
import static ca.utoronto.lms.shared.security.SecurityUtils.ROLE_TEACHER_ID;

@Service
public class TeacherService extends ExtendedService<Teacher, TeacherDTO, Long> {
    private final TeacherRepository repository;
    private final TeacherMapper mapper;
    private final UserFeignClient userFeignClient;

    public TeacherService(
            TeacherRepository repository, TeacherMapper mapper, UserFeignClient userFeignClient) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.userFeignClient = userFeignClient;
    }

    @Override
    @Transactional
    public TeacherDTO save(TeacherDTO teacher) {
         //It checks if the user already exists based on the user ID. If the user doesn't exist (userRequest.getId() == null), 
        //it uses the userFeignClient to create a new user with the specified details, including the username, password, and role.
        UserDTO userRequest = teacher.getUser();
        UserDTO userResponse =
                userRequest.getId() == null
                        ? userFeignClient.createUser(
                                UserDetailsDTO.builder()
                                        .username(userRequest.getUsername())
                                        .password(userRequest.getPassword())
                                        .authorities(
                                                Set.of(
                                                        RoleDTO.builder()
                                                                .id(ROLE_TEACHER_ID)
                                                                .authority(ROLE_TEACHER)
                                                                .build()))
                                        .build())
                        : userFeignClient.patchUser(userRequest.getId(), userRequest);
        teacher.setUser(userResponse);
         //Finally, it calls the super.save(teacher) method to save the teacher entity.
        return super.save(teacher);
    }

    @Override
    @Transactional
    public void delete(Set<Long> id) {
        //responsible for deleting one or more teachers.
        List<Teacher> teachers = (List<Teacher>) repository.findAllById(id);
        Set<Long> userIds = teachers.stream().map(Teacher::getUserId).collect(Collectors.toSet());
        userFeignClient.deleteUser(userIds);
        repository.softDeleteByIds(id);
    }

    @Override
    //responsible for mapping missing values for a list of teachers
    protected List<TeacherDTO> mapMissingValues(List<TeacherDTO> teachers) {
        map(teachers, TeacherDTO::getUser, TeacherDTO::setUser, userFeignClient::getUser);
        return teachers;
    }

    public TeacherDTO findByUserId(Long userId) {
         // retrieves a teacher based on the provided user ID.
        Teacher teacher =
                repository
                        .findByUserId(userId)
                        .orElseThrow(() -> new NotFoundException("User id not found"));
        return mapper.toDTO(teacher);
    }
}
