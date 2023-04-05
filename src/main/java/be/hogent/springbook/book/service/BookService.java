package be.hogent.springbook.book.service;

import be.hogent.springbook.book.entity.Author;
import be.hogent.springbook.book.entity.Book;
import be.hogent.springbook.book.entity.Location;
import be.hogent.springbook.book.entity.dto.BookDto;
import be.hogent.springbook.book.mapper.BookMapper;
import be.hogent.springbook.book.repository.AuthorRepository;
import be.hogent.springbook.book.repository.BookRepository;
import be.hogent.springbook.security.entity.SecurityUser;
import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class BookService {
    public static final String USER_NOT_FOUND = "User not found";
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;

    public List<Book> getAll(){
        log.info("Get All Books called in Service");
        return bookRepository.findAll();
    }

    public Book getById(String id){
        log.info("Get By Id for {} Books called in Service", id);
        return bookRepository.findById(id).orElseThrow(()->{
            log.error("Book {} not found", id);
            throw new IllegalArgumentException();
        });
    }

    public Book getByIsbn(String isbn){
        log.info("Get By ISBN for {} Books called in Service", isbn);
        return bookRepository.findByIsbn(isbn).orElseThrow(()->{
            log.error("Book {} not found", isbn);
            throw new IllegalArgumentException();
        });
    }

    public List<Book> getAllByAuthor(String name){
        log.info("Get By Author for {} Books called in Service", name);

        Author potentialAuthor = authorRepository.findAuthorByName(name)
                .orElseThrow(()-> {
                    log.error("Author with name {} not found", name);
                    throw new IllegalArgumentException();
                });

        return bookRepository.findAllByAuthorsContaining(potentialAuthor);
    }

    public Book createBook(BookDto data){
        Book newBook = bookMapper.toEntity(data);

        filterDummyData(newBook);
        newBook.setNumberOfTimesFavorited(0);
        return bookRepository.save(newBook);
    }

    public List<Book> getAllSortedByFavorites(){
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


    public String markBookAsFavorite(String userId, String bookId){
        String feedbackMessage = "";
        ApplicationUser potentialApplicationUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("user {} not found in database.", userId);
                    return new IllegalArgumentException(USER_NOT_FOUND);
                });

        Book potentialBook = this.getById(bookId);

        if (!potentialApplicationUser.getFavoriteBooks().contains(potentialBook)){
            log.info("Adding book to favorites.");
            potentialApplicationUser.getFavoriteBooks().add(potentialBook);
            potentialBook.setNumberOfTimesFavorited(potentialBook.getNumberOfTimesFavorited()+1);
            userRepository.save(potentialApplicationUser);
            feedbackMessage = "Book " + potentialBook.getTitle() + " has been favorited succesfully!";
        } else {
            log.info("Removing book from favorites.");
            potentialApplicationUser.getFavoriteBooks().remove(potentialBook);
            potentialBook.setNumberOfTimesFavorited(potentialBook.getNumberOfTimesFavorited()-1);
            userRepository.save(potentialApplicationUser);
            feedbackMessage = "Book " + potentialBook.getTitle() + " has been unfavorited succesfully!";
        }
        //refresh spring security user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser loggedInUser = (SecurityUser) authentication.getPrincipal();
        loggedInUser.setFavoriteBookIds(potentialApplicationUser.getFavoriteBooks().stream().map(Book::getBookId).toList());
        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(loggedInUser, loggedInUser.getPassword(), loggedInUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
        return feedbackMessage;
    }

    private static void filterDummyData(Book newBook) {
        newBook.setAuthors(newBook.getAuthors().stream().filter(author -> !author.getName().isEmpty()).toList());
        newBook.setLocations(newBook.getLocations().stream().filter(location -> !(location.getLocationCode1() == 0 && location.getLocationCode2() == 0 && location.getLocationName().isEmpty())).toList());
    }

}

