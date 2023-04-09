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
    public static final String TEST_AUTHOR = "Robin Van Limbergen";
    public static final String TEST_ISBN = "9789057593154";
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
        ex.schedule(this::getAllBooksPrint, 1, TimeUnit.SECONDS);
        ex.schedule(()-> this.getBookAuthorPrint(TEST_AUTHOR), 1, TimeUnit.SECONDS);
        ex.schedule(()-> this.getBookIBSNPrint(TEST_ISBN), 1, TimeUnit.SECONDS);
    }

    private void getAllBooksPrint() {
        out.println("######## GET ALL BOOK TEST #######");
        try {
            List<Book> list = webClient.get().uri(API_URI + "/books").retrieve().bodyToFlux(Book.class).collectList().block();
            assert list != null;
            list.forEach(this::printBookPretty);
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void getBookAuthorPrint(String author) {
        out.println("######## GET BY AUTHOR TEST #######");
        try {
            List<Book> list = webClient.get().uri(API_URI + "/books/author/" + author).retrieve().bodyToFlux(Book.class).collectList().block();
            assert list != null;
            out.println("Getting all books by " + author);
            list.forEach(this::printBookPretty);
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void getBookIBSNPrint(String isbn) {
        out.println("######## GET BY ISBN TEST #######");
        try {
            Book response = webClient.get().uri(API_URI + "/books/isbn/"+ isbn).retrieve().bodyToMono(Book.class).block();
            assert response != null;
            out.println("Getting book with ISBN " + isbn);
            printBookPretty(response);

        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void printBookPretty(Book book){
        out.println("========== PRINTING BOOK INFO ==========");
        out.println("Book id = " + book.getBookId());
        out.println("Book title = " + book.getTitle());
        out.println("Book isbn = " + book.getIsbn());
        out.println("Book favorited = " + book.getNumberOfTimesFavorited());
        out.println("Book price = " + book.getPrice());
        book.getAuthors().forEach(author -> out.println("Author = " + author.getName()));
        book.getLocations().forEach(location -> out.println("Location = " + location.getLocationCode1().toString() + ":" + location.getLocationCode2().toString() + "-" + location.getLocationName()));
    }
}
