package be.hogent.springbook.book.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ISBNChecksumValidator implements ConstraintValidator<ISBNChecksum, String> {
    @Override
    public void initialize(ISBNChecksum constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext constraintValidatorContext) {
        if (isbn == null || isbn.length() != 13) {
            return false;
        }
        isbn = isbn.trim().replaceAll("[^0-9]", "");
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checksum = (10 - (sum % 10)) % 10;
        int lastDigit = Character.getNumericValue(isbn.charAt(12));

        return checksum == lastDigit;
    }
}
