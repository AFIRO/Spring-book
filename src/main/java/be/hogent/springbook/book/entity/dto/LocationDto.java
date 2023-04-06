package be.hogent.springbook.book.entity.dto;

import be.hogent.springbook.book.validation.LocationCode;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@LocationCode(locationCode1 = "locationCode1", locationCode2 = "locationCode2", locationName = "locationName")
public class LocationDto {
    private String locationId;
    @Min(50)
    @Max(300)
    private Integer locationCode1;
    @Min(50)
    @Max(300)
    private Integer locationCode2;
    @Pattern(regexp = "^[A-Za-z]+$",message = "You may only pass letters.")
    private String locationName;
}
