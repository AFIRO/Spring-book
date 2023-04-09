package be.hogent.springbook.user.service;

import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.entity.dto.ApplicationUserDto;
import be.hogent.springbook.user.entity.dto.RegisterDto;
import be.hogent.springbook.user.mapper.ApplicationUserMapper;
import be.hogent.springbook.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static be.hogent.springbook.user.UserTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ApplicationUserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MessageSource messageSource;
    @Captor
    ArgumentCaptor<ApplicationUser> userCaptor;
    @InjectMocks
    private UserService userService;

    @Test
    void createUser_happyFlow_returnsDto() {
        RegisterDto data = getRegisterDto();
        ApplicationUserDto expected = getApplicationUserDto();
        ApplicationUser noId = getApplicationUserNoId();
        ApplicationUser savedUser = getApplicationUser();

        when(userRepository.existsByEmail(data.getEmail())).thenReturn(false);
        when(userMapper.toEntity(data)).thenReturn(noId);
        when(userRepository.save(noId)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(expected);

        ApplicationUserDto actual = userService.createUser(data);

        assertThat(actual).isEqualTo(expected);
        verify(userRepository, times(1)).existsByEmail(data.getEmail());
    }

    @Test
    void register_alreadyExists_throwsException() {
        RegisterDto data = getRegisterDto();
        ApplicationUser savedUser = getApplicationUser();

        when(userRepository.existsByEmail(data.getEmail())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(data));
        assertThat(exception.getMessage()).isEqualTo(ALREADY_REGISTERED_WITH_THIS_EMAIL);

        verify(userRepository, times(1)).existsByEmail(data.getEmail());
    }

    @Test
    void deleteUser_happyFlow() {
        ApplicationUser deletedUser = getApplicationUser();
        ApplicationUserDto expected = getApplicationUserDto();

        when(userRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(deletedUser));
        when(userMapper.toDto(deletedUser)).thenReturn(expected);

        ApplicationUserDto actual = userService.deleteUser(TEST_ID);
        assertThat(actual).isEqualTo(expected);
        verify(userRepository, times(1)).findById(TEST_ID);
        verify(userRepository, times(1)).deleteById(TEST_ID);
    }

    @Test
    void deleteUser_notFound_throws() {
        when(userRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(TEST_ID));
        assertThat(exception.getMessage()).isEqualTo(NOT_FOUND);
        verify(userRepository, times(1)).findById(TEST_ID);
        verify(userRepository, times(0)).deleteById(TEST_ID);
    }

    @Test
    void saveUser_happyFlow() {
        ApplicationUser startData = getApplicationUser();
        userService.saveUser(startData);
        verify(userRepository, times(1)).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).isEqualTo(startData);
    }

    @Test
    void getById_happyFlow() {
        ApplicationUser expected = getApplicationUser();
        when(userRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(expected));
        ApplicationUser actual = userService.getById(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getById_notFound_Throws() {
        ApplicationUser expected = getApplicationUser();
        when(userRepository.findById(TEST_ID)).thenReturn(Optional.empty());
        when(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale())).thenReturn(NOT_FOUND);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.getById(expected.getUserId()));
        assertThat(exception.getMessage()).isEqualTo(NOT_FOUND);
    }


}