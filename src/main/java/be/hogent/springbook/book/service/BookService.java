package be.hogent.springbook.book.service;

import be.hogent.springbook.book.entity.Author;
import be.hogent.springbook.book.entity.Book;
import be.hogent.springbook.book.entity.dto.BookDto;
import be.hogent.springbook.book.entity.dto.LocationDto;
import be.hogent.springbook.book.mapper.BookMapper;
import be.hogent.springbook.book.repository.AuthorRepository;
import be.hogent.springbook.book.repository.BookRepository;
import be.hogent.springbook.book.repository.LocationRepository;
import be.hogent.springbook.security.entity.SecurityUser;
import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final LocationRepository locationRepository;
    private final UserService userService;
    private final BookMapper bookMapper;
    private final MessageSource messageSource;

    public List<Book> getAll() {
        log.info("Get All Books called in Service");
        return bookRepository.findAll();
    }

    public Book getById(String id) {
        log.info("Get By Id for {} Books called in Service", id);
        return bookRepository.findById(id).orElseThrow(() -> {
            log.error("Book {} not found", id);
            return new IllegalArgumentException(messageSource.getMessage("book.not.found", null, LocaleContextHolder.getLocale()));
        });
    }

    public Book getByIsbn(String isbn) {
        log.info("Get By ISBN for {} Books called in Service", isbn);
        return bookRepository.findByIsbn(isbn).orElseThrow(() -> {
            log.error("Book {} not found", isbn);
            return new IllegalArgumentException(messageSource.getMessage("book.not.found", null, LocaleContextHolder.getLocale()));
        });
    }

    public List<Book> getAllByAuthor(String name) {
        log.info("Get By Author for {} Books called in Service", name);

        Author potentialAuthor = authorRepository.findAuthorByName(name)
                .orElseThrow(() -> {
                    log.error("Author with name {} not found", name);
                    return new IllegalArgumentException(messageSource.getMessage("author.not.found", null, LocaleContextHolder.getLocale()));
                });

        return bookRepository.findAllByAuthorsContaining(potentialAuthor);
    }

    public Book createBook(BookDto data) {
        log.info("Create book called in service for book with name {} and isbn {}", data.getTitle(), data.getIsbn());

        if (bookRepository.existsByIsbn(data.getIsbn())) {
            log.error("Data contained duplicate ISBN.");
            throw new IllegalArgumentException(messageSource.getMessage("isbn.not.unique", null, LocaleContextHolder.getLocale()));

        }

        for (LocationDto locationDto : data.getLocations()) {
            if (locationRepository.existsByLocationCode1AndLocationCode2AndLocationName(locationDto.getLocationCode1(), locationDto.getLocationCode2(), locationDto.getLocationName())) {
                log.error("Data contained duplicate location.");
                throw new IllegalArgumentException(messageSource.getMessage("location.not.unique", null, LocaleContextHolder.getLocale()));
            }
        }

        Book newBook = bookMapper.toEntity(data);
        filterDummyData(newBook);
        newBook.setNumberOfTimesFavorited(0);
        return bookRepository.save(newBook);
    }

    public List<Book> getAllSortedByFavorites() {
        log.info("Get All sorted by favorites called in Service.");
        List<Book> allBooks = getAll();
        allBooks.sort((o1, o2) -> {
            if (o1.getNumberOfTimesFavorited() == o2.getNumberOfTimesFavorited()) {
                return -1 * o1.getTitle().compareTo(o2.getTitle());
            } else {
                return -1 * Integer.compare(o1.getNumberOfTimesFavorited(), o2.getNumberOfTimesFavorited());
            }
        });
        return allBooks;
    }


    public String markBookAsFavorite(String userId, String bookId) {
        log.info("(un)Mark book as favorite for book {} and user {} called in service", bookId, userId);
        String feedbackMessage = "";

        ApplicationUser potentialApplicationUser = userService.getById(userId);
        Book potentialBook = this.getById(bookId);

        if (!potentialApplicationUser.getFavoriteBooks().contains(potentialBook)) {
            log.info("Adding book with id {} to favorites of user {}", bookId, userId);
            potentialApplicationUser.getFavoriteBooks().add(potentialBook);
            potentialBook.setNumberOfTimesFavorited(potentialBook.getNumberOfTimesFavorited() + 1);
            userService.saveUser(potentialApplicationUser);
            feedbackMessage = messageSource.getMessage("book", null, LocaleContextHolder.getLocale()) + potentialBook.getTitle() + messageSource.getMessage("been.favorited", null, LocaleContextHolder.getLocale());
        } else {
            log.info("Removing book with id {} from favorites of user {}", bookId, userId);
            potentialApplicationUser.getFavoriteBooks().remove(potentialBook);
            potentialBook.setNumberOfTimesFavorited(potentialBook.getNumberOfTimesFavorited() - 1);
            userService.saveUser(potentialApplicationUser);
            feedbackMessage = messageSource.getMessage("book", null, LocaleContextHolder.getLocale()) + messageSource.getMessage("been.unfavorited", null, LocaleContextHolder.getLocale());
        }

        log.info("Refreshing security context user with new favorites.");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser loggedInUser = (SecurityUser) authentication.getPrincipal();
        loggedInUser.setFavoriteBookIds(potentialApplicationUser.getFavoriteBooks().stream().map(Book::getBookId).toList());
        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(loggedInUser, loggedInUser.getPassword(), loggedInUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
        return feedbackMessage;
    }

    public void deleteBook(String id) {
        log.info("Delete book called in service with id {}", id);
        if (!bookRepository.existsById(id)) {
            log.error("Book not found");
            throw new IllegalArgumentException(messageSource.getMessage("book.not.found", null, LocaleContextHolder.getLocale()));
        }
        Book potentialBook = getById(id);
        bookRepository.delete(potentialBook);
    }

    private static void filterDummyData(Book newBook) {
        log.info("Filtering dummy data from Thymeleaf.");
        newBook.setAuthors(newBook.getAuthors().stream().filter(author -> !author.getName().isBlank()).toList());
        newBook.setLocations(newBook.getLocations().stream().filter(location -> !(location.getLocationCode1() == null || location.getLocationCode2() == null && location.getLocationName().isBlank())).toList());
    }
}

