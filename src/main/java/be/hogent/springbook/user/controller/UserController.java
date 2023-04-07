package be.hogent.springbook.user.controller;

import be.hogent.springbook.user.entity.UserRole;
import be.hogent.springbook.user.entity.dto.ApplicationUserDto;
import be.hogent.springbook.user.entity.dto.RegisterDto;
import be.hogent.springbook.user.mapper.ApplicationUserMapper;
import be.hogent.springbook.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final ApplicationUserMapper userMapper;

    @GetMapping()
    @Secured(UserRole.Fields.ADMIN)
    public ResponseEntity<List<ApplicationUserDto>> getAll() {
        log.info("Get All called from Controller.");
        try {
            return ResponseEntity.ok(userService.getAll());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Secured(UserRole.Fields.ADMIN)
    public ResponseEntity<ApplicationUserDto> getById(@PathVariable("id") String id) {
        log.info("Get for user with id {} called from Controller.", id);
        try {
            return ResponseEntity.ok(userMapper.toDto(userService.getById(id)));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping()
    @Secured(UserRole.Fields.ADMIN)
    public ResponseEntity<ApplicationUserDto> createUser(@Valid RegisterDto data) {
        log.info("Create for new user {} called from Controller.", data.getEmail());
        try {
            return ResponseEntity.ok()
                    .body(userService.createUser(data));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Secured(UserRole.Fields.ADMIN)
    public ResponseEntity<ApplicationUserDto> deleteUser(@PathVariable("id") String id) {
        log.info("Delete for user with id {} called from Controller.", id);
        try {
            return ResponseEntity.ok(userService.deleteUser(id));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
