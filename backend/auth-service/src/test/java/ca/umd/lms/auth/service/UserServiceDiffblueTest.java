package ca.umd.lms.auth.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import ca.umd.lms.auth.mapper.UserMapper;
import ca.umd.lms.auth.model.User;
import ca.umd.lms.auth.repository.UserRepository;
import ca.umd.lms.auth.security.TokenGenerator;
import ca.utoronto.lms.shared.dto.UserDetailsDTO;
import ca.utoronto.lms.shared.exception.NotFoundException;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
class UserServiceDiffblueTest {
  @MockBean
  private AuthenticationManager authenticationManager;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @MockBean
  private TokenGenerator tokenGenerator;

  @MockBean
  private UserDetailsService userDetailsService;

  @MockBean
  private UserMapper userMapper;

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private UserService userService;
  /**
  * Method under test: {@link UserService#update(UserDetailsDTO)}
  */
  @Test
  void testUpdate() {
    User user = new User();
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setAuthorities(new HashSet<>());
    user.setCredentialsNonExpired(true);
    user.setDeleted(true);
    user.setEnabled(true);
    user.setId(1L);
    user.setPassword("iloveyou");
    user.setUsername("janedoe");
    Optional<User> ofResult = Optional.of(user);

    User user2 = new User();
    user2.setAccountNonExpired(true);
    user2.setAccountNonLocked(true);
    user2.setAuthorities(new HashSet<>());
    user2.setCredentialsNonExpired(true);
    user2.setDeleted(true);
    user2.setEnabled(true);
    user2.setId(1L);
    user2.setPassword("iloveyou");
    user2.setUsername("janedoe");
    when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
    when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
    UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
    when(userMapper.toDTO(Mockito.<User>any())).thenReturn(userDetailsDTO);
    UserDetailsDTO actualUpdateResult = userService.update(new UserDetailsDTO());
    verify(userMapper).toDTO(Mockito.<User>any());
    verify(userRepository).findById(Mockito.<Long>any());
    verify(userRepository).save(Mockito.<User>any());
    assertSame(userDetailsDTO, actualUpdateResult);
  }

  /**
   * Method under test: {@link UserService#update(UserDetailsDTO)}
   */
  @Test
  void testUpdate2() {
    User user = new User();
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setAuthorities(new HashSet<>());
    user.setCredentialsNonExpired(true);
    user.setDeleted(true);
    user.setEnabled(true);
    user.setId(1L);
    user.setPassword("iloveyou");
    user.setUsername("janedoe");
    Optional<User> ofResult = Optional.of(user);

    User user2 = new User();
    user2.setAccountNonExpired(true);
    user2.setAccountNonLocked(true);
    user2.setAuthorities(new HashSet<>());
    user2.setCredentialsNonExpired(true);
    user2.setDeleted(true);
    user2.setEnabled(true);
    user2.setId(1L);
    user2.setPassword("iloveyou");
    user2.setUsername("janedoe");
    when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
    when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
    when(userMapper.toDTO(Mockito.<User>any())).thenThrow(new NotFoundException("An error occurred"));
    assertThrows(NotFoundException.class, () -> userService.update(new UserDetailsDTO()));
    verify(userMapper).toDTO(Mockito.<User>any());
    verify(userRepository).findById(Mockito.<Long>any());
    verify(userRepository).save(Mockito.<User>any());
  }
}
