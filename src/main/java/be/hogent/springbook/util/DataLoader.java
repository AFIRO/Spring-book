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
    private void loadData(){
        seedApplicationUsers();
        seedBooks();
        printDB();

    }

    private void printDB() {
        for (Object object : userRepository.findAll()){
            System.out.println(object);
        }

        for (Object object : bookRepository.findAll()){
            System.out.println(object);
        }
    }

    private void seedApplicationUsers() {
        //seed application users
        ApplicationUser admin = ApplicationUser.builder()
                .email("admin@hogent.be")
                .password("$2a$12$Mwbsu7NxN6jCDSmaHahk2eUT0Yhdcg02LZVdQa9tG9josq.rthVr.")
                .role(UserRole.ADMIN)
                .build();

        ApplicationUser user = ApplicationUser.builder()
                .email("user@hogent.be")
                .password("$2a$12$XsB5EQhYuMCWJ41nF6opF.4PV6gGNyzAgJOC.iu.jHE1xuYCXHYiy")
                .role(UserRole.USER)
                .build();

        userRepository.saveAll(Arrays.asList(user, admin));
    }

    private void seedBooks() {
        Book book = Book.builder()
                .authors(List.of(Author.builder().name("Jacques Firenze").build()))
                .isbn("978-0-13-468599-1")
                .locations(List.of(Location.builder().locationName("LOC").locationCode1(50).locationCode2(200).build()))
                .numberOfTimesFavorited(4)
                .price(99.9)
                .title("Uw Moeder")
                .build();

        bookRepository.save(book);
    }


}