package be.hogent.springbook.book.web;

import be.hogent.springbook.book.entity.dto.BookDto;
import be.hogent.springbook.book.mapper.BookMapper;
import be.hogent.springbook.book.service.BookService;
import be.hogent.springbook.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookRestController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping()
    @Secured(UserRole.Fields.ADMIN)
    private ResponseEntity<List<BookDto>> getAllBooks() {
        log.info("Get All Books called via REST");
        try {
            return ResponseEntity.ok().body(bookService.getAll()
                    .stream()
                    .map(bookMapper::toDto)
                    .toList());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("author/{name}")
    @Secured(UserRole.Fields.ADMIN)
    private ResponseEntity<List<BookDto>> getAllBooksByAuthor(@PathVariable("name") String name) {
        log.info("Get All Books for author {} called via REST", name);
        try {
            return ResponseEntity.ok().body(bookService.getAllByAuthor(name)
                    .stream()
                    .map(bookMapper::toDto)
                    .toList());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("isbn/{isbn}")
    @Secured(UserRole.Fields.ADMIN)
    private ResponseEntity<BookDto> getBookByISBN(@PathVariable("isbn") String isbn) {
        log.info("Get All Books for ISBN {} called via REST", isbn);
        try {
            return ResponseEntity.ok().body(bookMapper.toDto(bookService.getByIsbn(isbn)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }
}
