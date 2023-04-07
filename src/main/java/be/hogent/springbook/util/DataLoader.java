package be.hogent.springbook.util;

import be.hogent.springbook.book.entity.Author;
import be.hogent.springbook.book.entity.Book;
import be.hogent.springbook.book.entity.Location;
import be.hogent.springbook.book.repository.BookRepository;
import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.entity.UserRole;
import be.hogent.springbook.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @PostConstruct
    private void loadData() {
        seedApplicationUsers();
        seedBooks();
        printDB();
    }

    private void printDB() {
        for (Object object : userRepository.findAll()) {
            System.out.println(object);
        }

        for (Object object : bookRepository.findAll()) {
            System.out.println(object);
        }
    }

    private void seedApplicationUsers() {
        //seed application users

        ApplicationUser admin = ApplicationUser.builder()
                .email("admin")
                .password("$2a$12$Mwbsu7NxN6jCDSmaHahk2eUT0Yhdcg02LZVdQa9tG9josq.rthVr.")
                .role(UserRole.ADMIN)
                .maxNumberOfFavorites(2)
                .build();

        ApplicationUser user = ApplicationUser.builder()
                .email("user")
                .password("$2a$12$XsB5EQhYuMCWJ41nF6opF.4PV6gGNyzAgJOC.iu.jHE1xuYCXHYiy")
                .role(UserRole.USER)
                .maxNumberOfFavorites(3)
                .build();

        userRepository.saveAll(Arrays.asList(user, admin));
    }

    private void seedBooks() {


        Book book1 = Book.builder()
                .authors(List.of(Author.builder().name("Dikke Sven").build(),
                        Author.builder().name("Quinte Ridz").build()))
                .isbn("9780134685991")
                .locations(List.of(
                        Location.builder().locationName("BERGING").locationCode1(40).locationCode2(90).build(),
                        Location.builder().locationName("WC").locationCode1(50).locationCode2(200).build(),
                        Location.builder().locationName("SCHUURTJE").locationCode1(60).locationCode2(120).build()))
                .numberOfTimesFavorited(4)
                .price(5.56)
                .title("Belgische Rapcultuur: Een overzicht")
                .build();

        Book book2 = Book.builder()
                .authors(List.of(Author.builder().name("Robin Van Limbergen").build()))
                .isbn("9781234567897")
                .locations(List.of(
                        Location.builder().locationName("VERBODEN").locationCode1(69).locationCode2(300).build()))
                .numberOfTimesFavorited(69)
                .price(29.99)
                .title("Het Leven Zoals Het Is: Boosaardige Clown")
                .build();

        Book book3 = Book.builder()
                .authors(List.of(Author.builder().name("Tasja Collier").build()))
                .isbn("9789057593154")
                .locations(List.of(
                        Location.builder().locationName("WETENSCHAP").locationCode1(70).locationCode2(300).build()))
                .numberOfTimesFavorited(35)
                .price(39.99)
                .title("De Liquiditeitsgraad van de Inhoud van mijn Glas")
                .build();

        Book book4 = Book.builder()
                .authors(List.of(Author.builder().name("Jordan De Deken").build()))
                .isbn("9780747532743")
                .locations(List.of(
                        Location.builder().locationName("FILM").locationCode1(70).locationCode2(300).build()))
                .numberOfTimesFavorited(35)
                .price(99.99)
                .title("Oldborne: een Allegory van Trumpiaanse Politiek")
                .build();


        bookRepository.saveAll(List.of(book1, book2, book3, book4));
    }
}
