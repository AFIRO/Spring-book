package be.hogent.springbook.book.web;

import be.hogent.springbook.book.entity.Book;
import be.hogent.springbook.book.entity.dto.BookDto;
import be.hogent.springbook.book.mapper.BookMapper;
import be.hogent.springbook.book.service.BookService;
import be.hogent.springbook.security.entity.SecurityUser;
import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.entity.UserRole;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static be.hogent.springbook.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookRestController.class)
@AutoConfigureMockMvc
class BookRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    @MockBean
    private BookMapper bookMapper;

    @Test
    void getBooks_happyFlow() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Book> books = Arrays.asList(getBook());
        List<BookDto> bookDtos = Arrays.asList(getBookDto());
        when(bookService.getAll()).thenReturn(books);
        when(bookMapper.toDto(books.get(0))).thenReturn(bookDtos.get(0));
        MvcResult result = mockMvc.perform(get("/api/books").with(user(getAdminUser())))
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual.get(0)).isEqualTo(bookDtos.get(0));
        verify(bookService, times(1)).getAll();
    }

    @Test
    void getBooksByAuthor_happyFlow() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Book> books = Arrays.asList(getBook());
        List<BookDto> bookDtos = Arrays.asList(getBookDto());
        when(bookService.getAllByAuthor(books.get(0).getAuthors().get(0).getName())).thenReturn(books);
        when(bookMapper.toDto(books.get(0))).thenReturn(bookDtos.get(0));
        MvcResult result = mockMvc.perform(get("/api/books/author/" + books.get(0).getAuthors().get(0).getName()).with(user(getAdminUser())))
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual.get(0)).isEqualTo(bookDtos.get(0));
        verify(bookService, times(1)).getAllByAuthor(books.get(0).getAuthors().get(0).getName());
    }

    @Test
    void getByIsbn_happyFlow() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Book startData = getBook();
        BookDto expected = getBookDto();
        when(bookService.getByIsbn(startData.getIsbn())).thenReturn(startData);
        when(bookMapper.toDto(startData)).thenReturn(expected);
        MvcResult result = mockMvc.perform(get("/api/books/isbn/" + startData.getIsbn()).with(user(getAdminUser())))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual).isEqualTo(expected);
        verify(bookService, times(1)).getByIsbn(startData.getIsbn());
    }


    private SecurityUser getAdminUser() {
        ApplicationUser appplicationUser = getApplicationUser();
        appplicationUser.setEmail("admin");
        appplicationUser.setPassword("admin");
        appplicationUser.setRole(UserRole.ADMIN);
        return new SecurityUser(appplicationUser);
    }

}