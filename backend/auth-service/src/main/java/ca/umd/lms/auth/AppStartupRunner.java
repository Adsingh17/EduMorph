package ca.umd.lms.auth;

import ca.umd.lms.auth.service.RoleService;
import ca.umd.lms.auth.service.UserService;
import ca.utoronto.lms.shared.dto.RoleDTO;
import ca.utoronto.lms.shared.dto.UserDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

import static ca.utoronto.lms.shared.security.SecurityUtils.*;

@Component
@RequiredArgsConstructor//the AppStartupRunner class is used to ensure that essential roles and a default admin user are available in the system when the Spring Boot application starts.
// This is particularly useful for initializing the system with default data, ensuring a smooth start for the application.
// The use of the forceSave methods suggests that the roles and the user are created if they do not already exist in the database.
public class AppStartupRunner implements ApplicationRunner {
    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        RoleDTO root = RoleDTO.builder().id(ROLE_ROOT_ID).authority(ROLE_ROOT).build();
        RoleDTO admin = RoleDTO.builder().id(ROLE_ADMIN_ID).authority(ROLE_ADMIN).build();
        RoleDTO teacher = RoleDTO.builder().id(ROLE_TEACHER_ID).authority(ROLE_TEACHER).build();
        RoleDTO student = RoleDTO.builder().id(ROLE_STUDENT_ID).authority(ROLE_STUDENT).build();

        UserDetailsDTO user =
                UserDetailsDTO.builder()
                        .id(ROOT_USER_ID)
                        .username("admin@umd.edu")
                        .password(passwordEncoder.encode("password"))
                        .authorities(Set.of(root, admin))
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .enabled(true)
                        .build();

        if (roleService.findAll().isEmpty()) {
            roleService.forceSave(root);
            roleService.forceSave(admin);
            roleService.forceSave(teacher);
            roleService.forceSave(student);
        }

        if (userService.findAll().isEmpty()) {
            userService.forceSave(user);
        }
    }
}
