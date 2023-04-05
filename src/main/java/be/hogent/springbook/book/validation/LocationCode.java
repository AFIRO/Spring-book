package be.hogent.springbook.book.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LocationCodeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface LocationCode{
    String message() default "Invalid Location format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
