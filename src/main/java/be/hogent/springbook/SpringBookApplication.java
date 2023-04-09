package be.hogent.springbook;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBookApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBookApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //Ik wil even hier melden dat ik de data injectie niet gedaan heb met de commandline runner, maar
        //met een elegantere Post Construct Dataloader.
        //Gelieve even te kijken naar de Dataloader class in de util package.
    }
}
