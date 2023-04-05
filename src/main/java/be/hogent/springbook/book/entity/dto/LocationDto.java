package be.hogent.springbook.book.entity.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private String locationId;
    @NotNull
    @Min(50)
    @Max(300)
    private int locationCode1;
    @NotNull
    @Min(50)
    @Max(300)
    private int locationCode2;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[A-Za-z]+$")
    private String locationName;
}
