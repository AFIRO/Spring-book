package be.hogent.springbook.book.service;

import be.hogent.springbook.book.entity.Author;
import be.hogent.springbook.book.entity.Book;
import be.hogent.springbook.book.repository.AuthorRepository;
import be.hogent.springbook.book.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import static be.hogent.springbook.TestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private BookService bookService;

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



}