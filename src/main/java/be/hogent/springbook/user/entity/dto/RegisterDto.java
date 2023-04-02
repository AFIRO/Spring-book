package be.hogent.springbook.user.entity.dto;

import be.hogent.springbook.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class RegisterDto {
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email may not be empty")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Role is required")
    private UserRole role;
}
