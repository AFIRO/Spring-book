package be.hogent.springbook.book.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

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
