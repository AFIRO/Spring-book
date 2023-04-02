package be.hogent.springbook.user.service;

import be.hogent.springbook.book.entity.Book;
import be.hogent.springbook.book.repository.BookRepository;
import be.hogent.springbook.book.service.BookService;
import be.hogent.springbook.user.context.UserContext;
import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.entity.dto.ApplicationUserDto;
import be.hogent.springbook.user.entity.dto.LoginDto;
import be.hogent.springbook.user.entity.dto.RegisterDto;
import be.hogent.springbook.user.mapper.ApplicationUserMapper;
import be.hogent.springbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
    private final UserContext userContext;
    private final ApplicationUserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public boolean login(LoginDto data) {
        log.info("Login for user {} called from Service.", data.getEmail());
        ApplicationUser potentialApplicationUser = userRepository.findByEmail(data.getEmail())
                .orElseThrow(() -> {
                    log.error("user {} not found in database.", data.getEmail());
                    return new IllegalArgumentException(USER_NOT_FOUND);
                });
        if (passwordEncoder.matches(data.getPassword(),potentialApplicationUser.getPassword())) {
            log.info("Setting user with email {} as active application user", potentialApplicationUser.getEmail());
            userContext.setApplicationUser(potentialApplicationUser);
            return true;
        } else {
            return false;
        }
    }

    public ApplicationUserDto register(RegisterDto data) {
        log.info("Register for new user {} called from Service.", data.getEmail());
        if (!userRepository.existsByEmail(data.getEmail())) {
            data.setPassword(passwordEncoder.encode(data.getPassword()));
            ApplicationUser savedApplicationUser = userRepository.save(userMapper.toEntity(data));
            log.info("Setting user with email {} as active application user", data.getEmail());
            userContext.setApplicationUser(savedApplicationUser);
            return userMapper.toDto(savedApplicationUser);
        } else {
            log.error(ALREADY_REGISTERED_WITH_THIS_EMAIL);
            throw new IllegalArgumentException(ALREADY_REGISTERED_WITH_THIS_EMAIL);
        }
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

}
