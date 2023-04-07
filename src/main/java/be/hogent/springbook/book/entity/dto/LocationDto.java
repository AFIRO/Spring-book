package be.hogent.springbook.book.entity.dto;

import be.hogent.springbook.book.validation.LocationCode;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
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
    @Min(value = 50, message = "{location.code.too.low}")
    @Max(value = 300, message = "{location.code.too.high}")
    private Integer locationCode1;
    @Min(value = 50, message = "{location.code.too.low}")
    @Max(value = 300, message = "{location.code.too.high}")
    private Integer locationCode2;
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "{location.name.letters}")
    private String locationName;
}
