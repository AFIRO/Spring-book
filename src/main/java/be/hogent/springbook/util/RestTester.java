package be.hogent.springbook.util;

import be.hogent.springbook.book.entity.Book;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

@Component
public class RestTester {
    private final String API_URI = "http://localhost:8080/api/";
    private final WebClient webClient;

    public RestTester() {
        this.webClient = WebClient.builder()
                .defaultHeaders(header -> header.setBasicAuth("admin", "admin"))
                .build();
    }

    @PostConstruct
    private void callRestTests() {
        ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
        ex.schedule(this::getAllBooksPrint, 5, TimeUnit.SECONDS);
        ex.schedule(this::getBookAuthorPrint, 5, TimeUnit.SECONDS);
        ex.schedule(this::getBookIBSNPrint, 5, TimeUnit.SECONDS);
    }

    private void getAllBooksPrint() {
        try {
            List<Book> list = webClient.get().uri(API_URI + "/books").retrieve().bodyToFlux(Book.class).collectList().block();
            assert list != null;
            out.println("######## GET ALL BOOK TEST #######");
            list.forEach(book -> {
                out.println("========== PRINTING BOOK INFO ==========");
                out.println("Book id = " + book.getBookId());
                out.println("Book title = " + book.getTitle());
                out.println("Book isbn = " + book.getIsbn());
                out.println("Book favorited = " + book.getNumberOfTimesFavorited());
                out.println("Book price = " + book.getPrice());
                book.getAuthors().forEach(author -> out.println("Author = " + author.getName()));
                book.getLocations().forEach(location -> out.println("Location = " + location.getLocationCode1().toString() + ":" + location.getLocationCode2().toString() + "-" + location.getLocationName()));
            });
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void getBookAuthorPrint() {
        try {
            List<Book> list = webClient.get().uri(API_URI + "/books/author/Robin Van Limbergen").retrieve().bodyToFlux(Book.class).collectList().block();
            assert list != null;
            out.println("######## GET BY AUTHOR TEST #######");
            out.println("Getting all books by Robin Van Limbergen");
            list.forEach(book -> {
                out.println("========== PRINTING BOOK INFO ==========");
                out.println("Book id = " + book.getBookId());
                out.println("Book title = " + book.getTitle());
                out.println("Book isbn = " + book.getIsbn());
                out.println("Book favorited = " + book.getNumberOfTimesFavorited());
                out.println("Book price = " + book.getPrice());
                book.getAuthors().forEach(author -> out.println("Author = " + author.getName()));
                book.getLocations().forEach(location -> out.println("Location = " + location.getLocationCode1().toString() + ":" + location.getLocationCode2().toString() + "-" + location.getLocationName()));
            });
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void getBookIBSNPrint() {
        try {
            Book response = webClient.get().uri(API_URI + "/books/isbn/9789057593154").retrieve().bodyToMono(Book.class).block();
            assert response != null;
            out.println("######## GET BY ISBN TEST #######");
            out.println("Getting book with ISBN 9789057593154");
            out.println("========== PRINTING BOOK INFO ==========");
            out.println("Book id = " + response.getBookId());
            out.println("Book title = " + response.getTitle());
            out.println("Book isbn = " + response.getIsbn());
            out.println("Book favorited = " + response.getNumberOfTimesFavorited());
            out.println("Book price = " + response.getPrice());
            response.getAuthors().forEach(author -> out.println("Author = " + author.getName()));
            response.getLocations().forEach(location -> out.println("Location = " + location.getLocationCode1().toString() + ":" + location.getLocationCode2().toString() + "-" + location.getLocationName()));

        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }
}
