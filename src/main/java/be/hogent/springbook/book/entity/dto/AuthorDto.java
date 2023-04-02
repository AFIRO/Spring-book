package be.hogent.springbook.book.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorDto {
    private String authorId;
    @NotNull
    @NotBlank
    private String name;
}
