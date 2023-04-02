package be.hogent.springbook.book.controller;

import be.hogent.springbook.book.entity.dto.BookDto;
import be.hogent.springbook.book.mapper.BookMapper;
import be.hogent.springbook.book.service.BookService;
import be.hogent.springbook.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping("/books")
    public String getBooks(Model model) {
        log.info("Get all books called by Thymeleaf.");
        List<BookDto> books = bookService.getAll().stream().map(bookMapper::toDto).toList();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/books/favorites")
    public String getBooksSorted(Model model) {
        log.info("Get all books called by Thymeleaf.");
        List<BookDto> books = bookService.getAllSortedByFavorites().stream().map(bookMapper::toDto).toList();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/book/{id}")
    public String getBookById(Model model, @PathVariable("id") String id) {
        log.info("Get book by Id {} called by Thymeleaf.", id);
        BookDto book = bookMapper.toDto(bookService.getById(id));
        model.addAttribute("book", book);
        return "book";
    }

    @PostMapping("/book/{id}")
    public String createBook(Model model, @ModelAttribute BookDto data) {
        log.info("Create book called by Thymeleaf.");
        if (data.getBookId() != null){
            throw new IllegalArgumentException();
        }
        BookDto book = bookMapper.toDto(bookService.createBook(data));
        model.addAttribute("book", book);
        return "book";
    }

    @PostMapping("/favorite/{userId}/{bookId}")
    public String markBookAsFavorite(@PathVariable("userId") String userId, @PathVariable("bookId") String bookId ) {
        log.info("Mark book {} as favorite for user {} called by Thymeleaf.", bookId, userId);
        bookService.markBookAsFavorite(userId,bookId);
        return "favorite";
    }

    @PostMapping("/unfavorite/{userId}/{bookId}")
    public String unmarkBookAsFavorite(@PathVariable("userId") String userId, @PathVariable("bookId") String bookId ) {
        log.info("Unmark book {} as favorite for user {} called by Thymeleaf.", bookId, userId);
        bookService.unmarkBookAsFavorite(userId,bookId);
        return "unfavorite";
    }
}
