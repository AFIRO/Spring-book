package be.hogent.springbook.user.context;

import be.hogent.springbook.user.entity.ApplicationUser;
import lombok.Data;
import org.springframework.stereotype.Component;


@Component()
@Data
public class UserContext {
    private ApplicationUser applicationUser;
}