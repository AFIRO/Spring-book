package be.hogent.springbook.book.validation;

import be.hogent.springbook.book.entity.dto.LocationDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;

public class LocationCodeValidator implements ConstraintValidator<LocationCode, LocationDto> {
    private String locationCode1;
    private String locationCode2;
    private String locationName;

    @Override
    public void initialize(LocationCode constraintAnnotation) {
        this.locationCode1 = constraintAnnotation.locationCode1();
        this.locationCode2 = constraintAnnotation.locationCode2();
        this.locationName = constraintAnnotation.locationName();
    }

    @SneakyThrows
    @Override
    public boolean isValid(LocationDto locationDto, ConstraintValidatorContext constraintValidatorContext) {

        if (BeanUtils.getProperty(locationDto, locationCode1) == null && BeanUtils.getProperty(locationDto, locationCode2) == null && BeanUtils.getProperty(locationDto, locationName).isEmpty()) {
            return true;
        }

        if (BeanUtils.getProperty(locationDto, locationCode1) != null && BeanUtils.getProperty(locationDto, locationCode2) != null) {
            Integer locationCodeValue1 = Integer.parseInt(BeanUtils.getProperty(locationDto, locationCode1));
            Integer locationCodeValue2 = Integer.parseInt(BeanUtils.getProperty(locationDto, locationCode2));

            return locationCodeValue2 - locationCodeValue1 >= 50;
        } else
            return true;
    }
}
