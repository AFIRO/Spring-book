package be.hogent.springbook.user.controller;

import be.hogent.springbook.user.entity.dto.ApplicationUserDto;
import be.hogent.springbook.user.entity.dto.LoginDto;
import be.hogent.springbook.user.entity.dto.RegisterDto;
import be.hogent.springbook.user.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final UserService userService;
    private final Validator validator;

    public boolean login(LoginDto data) {
        Set<ConstraintViolation<LoginDto>> violations = validator.validate(data);
        if (!violations.isEmpty()) {
            String errormessage = violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .findFirst()
                    .get();
            log.error(errormessage);
            throw new IllegalArgumentException(errormessage);
        }
        log.info("Login for user {} called from Controller.", data.getEmail());
        return userService.login(data);
    }

    public ApplicationUserDto register(RegisterDto data) {
        Set<ConstraintViolation<RegisterDto>> violations = validator.validate(data);
        if (!violations.isEmpty()) {
            String errormessage = violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .findFirst()
                    .get();
            log.error(errormessage);
            throw new IllegalArgumentException(errormessage);
        }
        log.info("Register for new user {} called from Controller.", data.getEmail());
        return userService.register(data);
    }

    public ApplicationUserDto deleteUser(String id) {
        log.info("Delete for user with id {} called from Controller.", id);
        return userService.deleteUser(id);
    }


}
