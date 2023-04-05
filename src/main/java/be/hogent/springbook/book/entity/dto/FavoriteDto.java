package be.hogent.springbook.book.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
public class FavoriteDto {
    @NotNull
    @NotBlank
    private String userId;
    @NotNull
    @NotBlank
    private String bookId;
}
