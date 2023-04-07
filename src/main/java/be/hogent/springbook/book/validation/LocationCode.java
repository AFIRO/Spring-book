package be.hogent.springbook.book.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LocationCodeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LocationCode {
    @Value("${location.code.invalid}")
    String message() default "Location code 2 must be 50 spaces from location code 1.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String locationCode1();

    String locationCode2();

    String locationName();

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        LocationCode[] value();
    }
}
