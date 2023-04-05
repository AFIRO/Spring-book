package be.hogent.springbook.book.validation;

import be.hogent.springbook.book.entity.dto.LocationDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class LocationCodeValidator implements ConstraintValidator<LocationCode, List<LocationDto>> {
    @Override
    public void initialize(LocationCode constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<LocationDto> locationDtos, ConstraintValidatorContext constraintValidatorContext) {
        if (locationDtos.isEmpty()){
            return false;
        }

        for (LocationDto locationDto : locationDtos) {
            if (locationDto.getLocationCode1() == 0 && locationDto.getLocationCode2() == 0 && locationDto.getLocationName().isEmpty()) {
                continue;
            }

            if (!(locationDto.getLocationCode2() - locationDto.getLocationCode1() >= 50)){
                return false;
            }
        }
        return true;
    }
}
