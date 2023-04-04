package be.hogent.springbook.book.controller;

import be.hogent.springbook.book.entity.FavoriteDto;
import be.hogent.springbook.book.entity.dto.BookDto;
import be.hogent.springbook.book.mapper.BookMapper;
import be.hogent.springbook.book.service.BookService;
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

    @GetMapping("/")
    public String getBooks(Model model) {
        log.info("Get all books called by Thymeleaf.");
        List<BookDto> books = bookService.getAll().stream().map(bookMapper::toDto).toList();
        model.addAttribute("books", books);
        return "booklist";
    }

    @GetMapping("/books/favorites")
    public String getBooksSorted(Model model) {
        log.info("Get all books called by Thymeleaf.");
        List<BookDto> books = bookService.getAllSortedByFavorites().stream().map(bookMapper::toDto).toList();
        model.addAttribute("books", books);
        return "booksfavorites";
    }

    @GetMapping("/books/{id}")
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
        return "bookcreate";
    }

    @PostMapping("/markAsFavorite")
    public String markBookAsFavorite(@ModelAttribute("favoriteDto") FavoriteDto favorite) {
        log.info("Mark book {} as favorite for user {} called by Thymeleaf.", favorite.getBookId(), favorite.getUserId());
        bookService.markBookAsFavorite(favorite.getUserId(),favorite.getBookId());
        return "redirect:/";
    }
}
