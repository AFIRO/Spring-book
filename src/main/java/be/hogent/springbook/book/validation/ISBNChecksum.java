package be.hogent.springbook.book.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ISBNChecksumValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ISBNChecksum {
    String message() default "Invalid ISBN Checksum";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
