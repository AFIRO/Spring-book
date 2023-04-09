package be.hogent.springbook.book.service;

import be.hogent.springbook.book.entity.Author;
import be.hogent.springbook.book.entity.Book;
import be.hogent.springbook.book.entity.dto.BookDto;
import be.hogent.springbook.book.mapper.BookMapper;
import be.hogent.springbook.book.repository.AuthorRepository;
import be.hogent.springbook.book.repository.BookRepository;
import be.hogent.springbook.book.repository.LocationRepository;
import be.hogent.springbook.security.entity.SecurityUser;
import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static be.hogent.springbook.TestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    public static final String ISBN_NOT_UNIQUE = "ISBN not unique";
    public static final String LOCATION_NOT_UNIQUE = "Location not unique";
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private UserService userService;
    @Mock
    private MessageSource messageSource;
    @Mock
    private Authentication auth;
    @InjectMocks
    private BookService bookService;
    @Captor
    private ArgumentCaptor<ApplicationUser> applicationUserArgumentCaptor;

    @Test
    void getAll_happyFlow(){
        Book listContents = getBook();
        List<Book> expected = Arrays.asList(listContents);
        when(bookRepository.findAll()).thenReturn(expected);
        List<Book> actual = bookService.getAll();

        assertThat(actual.get(0)).isEqualTo(listContents);
        verify(bookRepository,times(1)).findAll();
    }

    @Test
    void getById_happyFlow(){
        Book expected = getBook();
        when(bookRepository.findById(TEST_ID)).thenReturn(Optional.ofNullable(expected));
        Book actual = bookService.getById(TEST_ID);

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository,times(1)).findById(TEST_ID);
    }

    @Test
    void getById_notFound_throws(){
        when(bookRepository.findById(TEST_ID)).thenReturn(Optional.empty());
        when(messageSource.getMessage("book.not.found", null, LocaleContextHolder.getLocale())).thenReturn(BOOK_NOT_FOUND);
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> bookService.getById(TEST_ID));
        assertThat(exception.getMessage()).isEqualTo(BOOK_NOT_FOUND);
        verify(bookRepository,times(1)).findById(TEST_ID);
    }

    @Test
    void getByIsbn_happyFlow(){
        Book expected = getBook();
        when(bookRepository.findByIsbn(TEST_ISBN)).thenReturn(Optional.ofNullable(expected));
        Book actual = bookService.getByIsbn(TEST_ISBN);

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository,times(1)).findByIsbn(TEST_ISBN);
    }

    @Test
    void getByIsbn_notFound_throws(){
        when(bookRepository.findByIsbn(TEST_ISBN)).thenReturn(Optional.empty());
        when(messageSource.getMessage("book.not.found", null, LocaleContextHolder.getLocale())).thenReturn(BOOK_NOT_FOUND);
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> bookService.getByIsbn(TEST_ISBN));
        assertThat(exception.getMessage()).isEqualTo(BOOK_NOT_FOUND);
        verify(bookRepository,times(1)).findByIsbn(TEST_ISBN);
    }

    @Test
    void getAllByAuthor_happyFlow(){
        Author startData = getAuthor();
        Book listContents = getBook();
        List<Book> expected = Arrays.asList(listContents);

        when(authorRepository.findAuthorByName(startData.getName())).thenReturn(Optional.ofNullable(startData));
        when(bookRepository.findAllByAuthorsContaining(startData)).thenReturn(expected);
        List<Book> actual = bookService.getAllByAuthor(startData.getName());

        assertThat(actual.get(0)).isEqualTo(listContents);
        verify(authorRepository,times(1)).findAuthorByName(startData.getName());
        verify(bookRepository,times(1)).findAllByAuthorsContaining(startData);
    }

    @Test
    void getAllByAuthor_authorNotFound_throws(){
        Author startData = getAuthor();

        when(authorRepository.findAuthorByName(startData.getName())).thenReturn(Optional.empty());
        when(messageSource.getMessage("author.not.found", null, LocaleContextHolder.getLocale())).thenReturn("Author not found.");
        Exception exception = assertThrows(IllegalArgumentException.class, ()-> bookService.getAllByAuthor(startData.getName()));
        assertThat(exception.getMessage()).isEqualTo("Author not found.");

        verify(authorRepository,times(1)).findAuthorByName(startData.getName());
        verify(bookRepository,times(0)).findAllByAuthorsContaining(startData);
    }

    @Test
    void createBook_Happyflow(){
        BookDto startData = getBookDto();
        Book expected = getBook();
        when(bookRepository.existsByIsbn(startData.getIsbn())).thenReturn(false);
        when(locationRepository.existsByLocationCode1AndLocationCode2AndLocationName(startData.getLocations().get(0).getLocationCode1(),startData.getLocations().get(0).getLocationCode2(),startData.getLocations().get(0).getLocationName())).thenReturn(false);
        when(bookMapper.toEntity(startData)).thenReturn(expected);
        when(bookRepository.save(expected)).thenReturn(expected);
        Book actual = bookService.createBook(startData);

        assertThat(actual).isEqualTo(expected);

        verify(bookRepository,times(1)).existsByIsbn(startData.getIsbn());
        verify(locationRepository,times(1)).existsByLocationCode1AndLocationCode2AndLocationName(startData.getLocations().get(0).getLocationCode1(),startData.getLocations().get(0).getLocationCode2(),startData.getLocations().get(0).getLocationName());
        verify(bookMapper,times(1)).toEntity(startData);
        verify(bookRepository,times(1)).save(expected);
    }

    @Test
    void createBook_ISBNalreadyExists_Throws(){
        BookDto startData = getBookDto();
        Book expected = getBook();
        when(bookRepository.existsByIsbn(startData.getIsbn())).thenReturn(true);
        when(messageSource.getMessage("isbn.not.unique", null, LocaleContextHolder.getLocale())).thenReturn(ISBN_NOT_UNIQUE);
        Exception exception = assertThrows(IllegalArgumentException.class,()->bookService.createBook(startData));

        assertThat(exception.getMessage()).isEqualTo(ISBN_NOT_UNIQUE);
        verify(bookRepository,times(1)).existsByIsbn(startData.getIsbn());
        verify(locationRepository,times(0)).existsByLocationCode1AndLocationCode2AndLocationName(startData.getLocations().get(0).getLocationCode1(),startData.getLocations().get(0).getLocationCode2(),startData.getLocations().get(0).getLocationName());
        verify(bookMapper,times(0)).toEntity(startData);
        verify(bookRepository,times(0)).save(expected);
    }

    @Test
    void createBook_locationAlreadyExists_Throws(){
        BookDto startData = getBookDto();
        Book expected = getBook();
        when(bookRepository.existsByIsbn(startData.getIsbn())).thenReturn(false);
        when(locationRepository.existsByLocationCode1AndLocationCode2AndLocationName(startData.getLocations().get(0).getLocationCode1(),startData.getLocations().get(0).getLocationCode2(),startData.getLocations().get(0).getLocationName())).thenReturn(true);
        when(messageSource.getMessage("location.not.unique", null, LocaleContextHolder.getLocale())).thenReturn(LOCATION_NOT_UNIQUE);
        Exception exception = assertThrows(IllegalArgumentException.class,()->bookService.createBook(startData));

        assertThat(exception.getMessage()).isEqualTo(LOCATION_NOT_UNIQUE);
        verify(bookRepository,times(1)).existsByIsbn(startData.getIsbn());
        verify(locationRepository,times(1)).existsByLocationCode1AndLocationCode2AndLocationName(startData.getLocations().get(0).getLocationCode1(),startData.getLocations().get(0).getLocationCode2(),startData.getLocations().get(0).getLocationName());
        verify(bookMapper,times(0)).toEntity(startData);
        verify(bookRepository,times(0)).save(expected);
    }

    @Test
    void getAllSorted_happyflow(){
        List<Book> expected = Arrays.asList(getBook(),getBook());
        when(bookRepository.findAll()).thenReturn(expected);
        List<Book> actual = bookService.getAllSortedByFavorites();

        assertThat(actual.get(0)).isEqualTo(expected.get(0));
        assertThat(actual.get(1)).isEqualTo(expected.get(1));
        verify(bookRepository,times(1)).findAll();

    }

    @Test
    void deleteBook_happyFlow(){
        Book startData = getBook();
        when(bookRepository.existsById(startData.getBookId())).thenReturn(true);
        when(bookRepository.findById(startData.getBookId())).thenReturn(Optional.of(startData));
        bookService.deleteBook(startData.getBookId());

        verify(bookRepository,times(1)).existsById(startData.getBookId());
        verify(bookRepository,times(1)).findById(startData.getBookId());
        verify(bookRepository,times(1)).delete(startData);
    }

    @Test
    void deleteBook_doesNotExist_Throws(){
        Book startData = getBook();
        when(bookRepository.existsById(startData.getBookId())).thenReturn(false);
        when(messageSource.getMessage("book.not.found", null, LocaleContextHolder.getLocale())).thenReturn(BOOK_NOT_FOUND);
        Exception exception = assertThrows(IllegalArgumentException.class,()->bookService.deleteBook(startData.getBookId()));

        assertThat(exception.getMessage()).isEqualTo(BOOK_NOT_FOUND);

        verify(bookRepository,times(1)).existsById(startData.getBookId());
        verify(bookRepository,times(0)).findById(startData.getBookId());
        verify(bookRepository,times(0)).delete(startData);
    }

    @Test
    void favoriteBook_happyFlow(){
        Book startBook = getBook();
        ApplicationUser startUser = getApplicationUser();
        startUser.getFavoriteBooks().add(startBook);
        when(userService.getById(startUser.getUserId())).thenReturn(startUser);
        when(bookRepository.findById(startBook.getBookId())).thenReturn(Optional.of(startBook));
        when(auth.getPrincipal()).thenReturn(new SecurityUser(startUser));
        SecurityContextHolder.getContext().setAuthentication(auth);
        bookService.markBookAsFavorite(startUser.getUserId(),startBook.getBookId());
        verify(userService,times(1)).saveUser(applicationUserArgumentCaptor.capture());
        ApplicationUser actual = applicationUserArgumentCaptor.getValue();

        assertThat(actual.getFavoriteBooks().isEmpty()).isTrue();
        verify(userService,times(1)).getById(startUser.getUserId());
        verify(bookRepository,times(1)).findById(startBook.getBookId());
    }

    @Test
    void unfavoriteBook_happyFlow(){
        Book startBook = getBook();
        ApplicationUser startUser = getApplicationUser();
        when(userService.getById(startUser.getUserId())).thenReturn(startUser);
        when(bookRepository.findById(startBook.getBookId())).thenReturn(Optional.of(startBook));
        when(auth.getPrincipal()).thenReturn(new SecurityUser(startUser));
        SecurityContextHolder.getContext().setAuthentication(auth);
        bookService.markBookAsFavorite(startUser.getUserId(),startBook.getBookId());
        verify(userService,times(1)).saveUser(applicationUserArgumentCaptor.capture());
        ApplicationUser actual = applicationUserArgumentCaptor.getValue();

        assertThat(actual.getFavoriteBooks().get(0)).isEqualTo(startBook);
        assertThat(actual.getFavoriteBooks().get(0).getNumberOfTimesFavorited()).isEqualTo(1);
        verify(userService,times(1)).getById(startUser.getUserId());
        verify(bookRepository,times(1)).findById(startBook.getBookId());
    }
}