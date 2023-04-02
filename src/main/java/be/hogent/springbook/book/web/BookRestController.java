package be.hogent.springbook.book.web;

import be.hogent.springbook.book.entity.dto.BookDto;
import be.hogent.springbook.book.mapper.BookMapper;
import be.hogent.springbook.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookRestController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping("/author/{name}")
    private List<BookDto> getAllBooksByAuthor(@PathVariable("name") String name){
        return bookService.getAllByAuthor(name)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @GetMapping("/isbn/{isbn}")
    private BookDto getBookByISBN(@PathVariable("isbn") String isbn){
        return bookMapper.toDto(bookService.getByIsbn(isbn));
    }
}
