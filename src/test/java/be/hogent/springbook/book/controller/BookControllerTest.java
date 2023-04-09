package be.hogent.springbook.book.controller;

import be.hogent.springbook.book.entity.Book;
import be.hogent.springbook.book.entity.dto.BookDto;
import be.hogent.springbook.book.mapper.BookMapper;
import be.hogent.springbook.book.service.BookService;
import be.hogent.springbook.security.entity.SecurityUser;
import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static be.hogent.springbook.TestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    @MockBean
    private BookMapper bookMapper;

    @Test
    void getBooks_happyFlow() throws Exception {
        List<Book> books = Arrays.asList(getBook());
        List<BookDto> bookDtos = Arrays.asList(getBookDto());
        when(bookService.getAll()).thenReturn(books);
        when(bookMapper.toDto(books.get(0))).thenReturn(bookDtos.get(0));
        mockMvc.perform(get("/").with(user(getAdminUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("booklist"))
                .andExpect(model().attribute("books", bookDtos));
    }

    @Test
    void getFavoriteBooks_happyFlow() throws Exception {
        List<Book> books = Arrays.asList(getBook());
        List<BookDto> bookDtos = Arrays.asList(getBookDto());
        when(bookService.getAllSortedByFavorites()).thenReturn(books);
        when(bookMapper.toDto(books.get(0))).thenReturn(bookDtos.get(0));
        mockMvc.perform(get("/books/favorites").with(user(getAdminUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("booksfavorites"))
                .andExpect(model().attribute("books", bookDtos));
    }

    @Test
    void getById_happyFlow() throws Exception {
        Book book = getBook();
        BookDto bookDto = getBookDto();
        when(bookService.getById(book.getBookId())).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        mockMvc.perform(get("/books/"+ book.getBookId()).with(user(getAdminUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("book"))
                .andExpect(model().attribute("book", bookDto));
    }

    @Test
    void showCreate_happyFlow() throws Exception {
        mockMvc.perform(get("/books/create").with(user(getAdminUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("createbook"))
                .andExpect(model().attribute("bookDto", BookDto.generateDefault()));
    }

    @Test
    void createBook_happyFlow() throws Exception {
        Book book = getBook();
        BookDto bookDto = getBookDto();
        when(bookService.createBook(bookDto)).thenReturn(book);
        when(bookMapper.toDto(any())).thenReturn(bookDto);
        mockMvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("isbn",bookDto.getIsbn())
                        .param("title",bookDto.getTitle())
                        .param("price", String.valueOf(bookDto.getPrice()))
                        .param("numberOfTimesFavorited","0")
                        .param("authors[0].name", bookDto.getAuthors().get(0).getName())
                        .param("locations[0].locationCode1", String.valueOf(bookDto.getLocations().get(0).getLocationCode1()))
                        .param("locations[0].locationCode2", String.valueOf(bookDto.getLocations().get(0).getLocationCode2()))
                        .param("locations[0].locationName", (bookDto.getLocations().get(0).getLocationName()))
                        .with(user(getAdminUser()))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/?success="+bookDto.getTitle()+" has been created succesfully."));
    }

    @Test
    void createBook_validationError_returnToCreate() throws Exception {
        Book book = getBook();
        BookDto bookDto = getBookDto();
        when(bookService.createBook(bookDto)).thenReturn(book);
        when(bookMapper.toDto(any())).thenReturn(bookDto);
        mockMvc.perform(post("/books/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("isbn",bookDto.getIsbn())
                        .param("title",bookDto.getTitle())
                        .param("price", String.valueOf(bookDto.getPrice()))
                        .param("numberOfTimesFavorited","0")
                        .with(user(getAdminUser()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("createbook"));
    }

    @Test
    void markAsFavorite_happyFlow() throws Exception {
        when(bookService.markBookAsFavorite(TEST_ID,TEST_ID)).thenReturn("Marked successfully");
        mockMvc.perform(post("/markAsFavorite")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("userId",TEST_ID)
                        .param("bookId",TEST_ID)
                        .with(user(getAdminUser()))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/?success=Marked successfully"));
    }

    @Test
    void DeleteBook_happyFlow() throws Exception {
        mockMvc.perform(post("/books/TEST_ID/delete")
                        .with(user(getAdminUser()))
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/?success=Book has been deleted succesfully."));
    }



    private SecurityUser getAdminUser() {
        ApplicationUser appplicationUser = getApplicationUser();
        appplicationUser.setEmail("admin");
        appplicationUser.setPassword("admin");
        appplicationUser.setRole(UserRole.ADMIN);
        return new SecurityUser(appplicationUser);
    }
}