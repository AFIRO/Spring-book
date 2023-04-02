package be.hogent.springbook.book.entity.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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

    public LocationDto(String locationId, int locationCode1, int locationCode2, String locationName) {
        if (!(locationCode1 - locationCode2 >= 50)){
            throw new IllegalArgumentException();
        }
        this.locationId = locationId;
        this.locationCode1 = locationCode1;
        this.locationCode2 = locationCode2;
        this.locationName = locationName;
    }
}
