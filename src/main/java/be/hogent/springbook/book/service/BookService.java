package be.hogent.springbook.book.service;

import be.hogent.springbook.book.entity.Author;
import be.hogent.springbook.book.entity.Book;
import be.hogent.springbook.book.entity.dto.BookDto;
import be.hogent.springbook.book.mapper.BookMapper;
import be.hogent.springbook.book.repository.AuthorRepository;
import be.hogent.springbook.book.repository.BookRepository;
import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.repository.UserRepository;
import be.hogent.springbook.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
        newBook.setNumberOfTimesFavorited(0);
        return bookRepository.save(newBook);
    }

    public List<Book> getAllSortedByFavorites(){
        List<Book> allBooks = getAll();
        allBooks.sort((o1, o2) -> {
            if (o1.getNumberOfTimesFavorited() == o2.getNumberOfTimesFavorited()) {
                return o1.getTitle().compareTo(o2.getTitle());
            } else {
                return Integer.compare(o1.getNumberOfTimesFavorited(), o2.getNumberOfTimesFavorited());
            }
        });
        return allBooks;
    }


    public void markBookAsFavorite(String userId, String bookId){
        ApplicationUser potentialApplicationUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("user {} not found in database.", userId);
                    return new IllegalArgumentException(USER_NOT_FOUND);
                });

        Book potentialBook = this.getById(bookId);

        if (!potentialApplicationUser.getFavoriteBooks().contains(potentialBook)){
            potentialApplicationUser.getFavoriteBooks().add(potentialBook);
            potentialBook.setNumberOfTimesFavorited(potentialBook.getNumberOfTimesFavorited()+1);
            userRepository.save(potentialApplicationUser);
        } else {
            throw new IllegalArgumentException();
        }

    }

    public void unmarkBookAsFavorite(String userId, String bookId){
        ApplicationUser potentialApplicationUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("user {} not found in database.", userId);
                    return new IllegalArgumentException(USER_NOT_FOUND);
                });

        Book potentialBook = this.getById(bookId);

        if (potentialApplicationUser.getFavoriteBooks().contains(potentialBook)){
            potentialApplicationUser.getFavoriteBooks().remove(potentialBook);
            potentialBook.setNumberOfTimesFavorited(potentialBook.getNumberOfTimesFavorited()-1);
            userRepository.save(potentialApplicationUser);
        } else {
            throw new IllegalArgumentException();
        }
    }
    }

