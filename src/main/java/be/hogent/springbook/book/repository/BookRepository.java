package be.hogent.springbook.book.repository;

import be.hogent.springbook.book.entity.Author;
import be.hogent.springbook.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findByIsbn(String ISBN);

    List<Book> findAllByAuthorsContaining(Author author);

    boolean existsByIsbn(String isbn);
}
