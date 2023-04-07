package be.hogent.springbook.book.controller;

import be.hogent.springbook.book.entity.dto.BookDto;
import be.hogent.springbook.book.entity.dto.FavoriteDto;
import be.hogent.springbook.book.mapper.BookMapper;
import be.hogent.springbook.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;
    private final MessageSource messageSource;

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

    @GetMapping("/books/create")
    public String showCreateBookPage(Model model) {
        log.info("Show Create Book Page called by Thymeleaf.");
        model.addAttribute("bookDto", BookDto.generateDefault());
        return "createbook";
    }

    @PostMapping("/books/create")
    public String createBook(Model model, @Valid @ModelAttribute("bookDto") BookDto data, BindingResult bindingResult) {
        log.info("Create book called by Thymeleaf.");
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(log::error);
            return "createbook";
        }
        if (data.getBookId() != null) {
            log.error("Passed BookDto contains an Id.");
            throw new IllegalArgumentException(messageSource.getMessage("book.id.in.create", null, LocaleContextHolder.getLocale()));
        }
        BookDto book;
        try {
            book = bookMapper.toDto(bookService.createBook(data));
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "createbook";

        }
        return "redirect:/?success=" + book.getTitle() + " has been created succesfully.";
    }

    @PostMapping("/markAsFavorite")
    public String markBookAsFavorite(@Valid @ModelAttribute("favoriteDto") FavoriteDto favorite) {
        log.info("Mark book {} as favorite for user {} called by Thymeleaf.", favorite.getBookId(), favorite.getUserId());
        String feedback = bookService.markBookAsFavorite(favorite.getUserId(), favorite.getBookId());
        return "redirect:/?success=" + feedback;
    }

    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable String id) {
        log.info("Delete book for id {} called by Thymeleaf.", id);
        bookService.deleteBook(id);
        return "redirect:/?success=Book has been deleted succesfully.";
    }

}
