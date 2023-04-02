package be.hogent.springbook.user.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LoginDto {
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email may not be empty")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
}
