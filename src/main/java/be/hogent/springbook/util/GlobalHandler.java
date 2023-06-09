package be.hogent.springbook.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class GlobalHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public void assertionException(final IllegalArgumentException e) {
        log.error(e.getLocalizedMessage());
    }
}
