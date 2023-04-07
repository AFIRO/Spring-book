package be.hogent.springbook.book.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ISBNFormatValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ISBNFormat {
    String message() default "Invalid ISBN format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
