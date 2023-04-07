package be.hogent.springbook.user.service;

import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.entity.dto.ApplicationUserDto;
import be.hogent.springbook.user.entity.dto.RegisterDto;
import be.hogent.springbook.user.mapper.ApplicationUserMapper;
import be.hogent.springbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    public static final String USER_NOT_FOUND = "User not found";
    public static final String ALREADY_REGISTERED_WITH_THIS_EMAIL = "User already registered with this email.";
    private final UserRepository userRepository;
    private final ApplicationUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    public List<ApplicationUserDto> getAll() {
        log.info("Get All called from Service");
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    public ApplicationUserDto createUser(RegisterDto data) {
        log.info("Create for new user {} called from Service.", data.getEmail());
        if (!userRepository.existsByEmail(data.getEmail())) {
            data.setPassword(passwordEncoder.encode(data.getPassword()));
            ApplicationUser savedApplicationUser = userRepository.save(userMapper.toEntity(data));
            log.info("Setting user with email {} as active application user", data.getEmail());
            return userMapper.toDto(savedApplicationUser);
        } else {
            log.error(ALREADY_REGISTERED_WITH_THIS_EMAIL);
            throw new IllegalArgumentException(ALREADY_REGISTERED_WITH_THIS_EMAIL);
        }
    }

    public ApplicationUser getById(String id) {
        log.info("Get by id for user with id {} called from Service.", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("user {} not found in database.", id);
                    return new IllegalArgumentException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
                });
    }

    public ApplicationUserDto deleteUser(String id) {
        log.info("Delete for user with id {} called from Service.", id);
        ApplicationUser deletedApplicationUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("user with id {} not found in database.", id);
                    return new IllegalArgumentException(USER_NOT_FOUND);
                });
        userRepository.deleteById(id);
        log.info("User with id {} deleted.", id);
        return userMapper.toDto(deletedApplicationUser);
    }

    public void saveUser(ApplicationUser user) {
        userRepository.save(user);
    }

}
