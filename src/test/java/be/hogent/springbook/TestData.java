package be.hogent.springbook;

import be.hogent.springbook.book.entity.Author;
import be.hogent.springbook.book.entity.Book;
import be.hogent.springbook.book.entity.Location;
import be.hogent.springbook.book.entity.dto.AuthorDto;
import be.hogent.springbook.book.entity.dto.BookDto;
import be.hogent.springbook.book.entity.dto.LocationDto;
import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.entity.UserRole;

import java.util.ArrayList;
import java.util.Arrays;

public class TestData {
    public static final String TEST_ID = "TEST_ID";
    public static final String TEST_NAME = "TEST_NAME";
    private static final Integer TEST_LOCATION_CODE1 = 50;
    private static final Integer TEST_LOCATION_CODE2 = 300;
    private static final String TEST_LOCATION_NAME = "name";
    public static final double TEST_PRICE = 69.69;
    public static final String TEST_ISBN = "9780134685991";
    public static final String TEST_TITLE = "TEST_TITLE";
    public static final String BOOK_NOT_FOUND = "Book not found.";

    public static Author getAuthor() {
        return Author.builder()
                .authorId(TEST_ID)
                .name(TEST_NAME)
                .build();
    }

    public static AuthorDto getAuthorDto() {
        return AuthorDto.builder()
                .authorId(TEST_ID)
                .name(TEST_NAME)
                .build();
    }

    public static Location getLocation() {
        return Location.builder()
                .locationId(TEST_ID)
                .locationCode1(TEST_LOCATION_CODE1)
                .locationCode2(TEST_LOCATION_CODE2)
                .locationName(TEST_LOCATION_NAME)
                .build();
    }

    public static LocationDto getLocationDto() {
        return LocationDto.builder()
                .locationId(TEST_ID)
                .locationCode1(TEST_LOCATION_CODE1)
                .locationCode2(TEST_LOCATION_CODE2)
                .locationName(TEST_LOCATION_NAME)
                .build();
    }

    public static BookDto getBookDto(){
        return BookDto.builder().bookId(TEST_ID)
                .price(TEST_PRICE)
                .isbn(TEST_ISBN)
                .authors(Arrays.asList(getAuthorDto()))
                .locations(Arrays.asList(getLocationDto()))
                .numberOfTimesFavorited(0)
                .title(TEST_TITLE)
                .build();
    }

    public static Book getBook(){
        return Book.builder().bookId(TEST_ID)
                .price(TEST_PRICE)
                .isbn(TEST_ISBN)
                .authors(Arrays.asList(getAuthor()))
                .locations(Arrays.asList(getLocation()))
                .numberOfTimesFavorited(0)
                .title(TEST_TITLE)
                .build();
    }

    public static ApplicationUser getApplicationUser(){
        return ApplicationUser.builder().userId(TEST_ID).favoriteBooks(new ArrayList<>()).role(UserRole.ADMIN).build();
    }
}
