package be.hogent.springbook.book.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ISBNFormatValidator implements ConstraintValidator<ISBNFormat, String> {
    @Override
    public void initialize(ISBNFormat constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext constraintValidatorContext) {
        if (isbn == null || isbn.length() != 13) {
            return false;
        }
        isbn = isbn.trim().replaceAll("[^0-9]", "");
        String regex = "^(978|979)\\d{10}$";
        return isbn.matches(regex);
    }
}
