package be.hogent.springbook.book.mapper;

import be.hogent.springbook.book.entity.Book;
import be.hogent.springbook.book.entity.dto.BookDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static be.hogent.springbook.TestData.getBook;
import static be.hogent.springbook.TestData.getBookDto;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookMapperTest {
    @Mock
    private AuthorMapper authorMapper;
    @Mock
    private LocationMapper locationMapper;
    @InjectMocks
    private BookMapper bookMapper;

    @Test
    void toDto() {
        Book startData = getBook();
        BookDto expected = getBookDto();
        when(authorMapper.toDto(startData.getAuthors().get(0))).thenReturn(expected.getAuthors().get(0));
        when(locationMapper.toDto(startData.getLocations().get(0))).thenReturn(expected.getLocations().get(0));
        BookDto actual = bookMapper.toDto(startData);

        assertThat(actual.getBookId()).isEqualTo(expected.getBookId());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        assertThat(actual.getNumberOfTimesFavorited()).isEqualTo(expected.getNumberOfTimesFavorited());
        assertThat(actual.getLocations()).isEqualTo(expected.getLocations());
        assertThat(actual.getAuthors()).isEqualTo(expected.getAuthors());
        verify(authorMapper, times(1)).toDto(startData.getAuthors().get(0));
        verify(locationMapper, times(1)).toDto(startData.getLocations().get(0));
    }

    @Test
    void toEntity() {
        BookDto startData = getBookDto();
        Book expected = getBook();
        when(authorMapper.toEntity(startData.getAuthors().get(0))).thenReturn(expected.getAuthors().get(0));
        when(locationMapper.toEntity(startData.getLocations().get(0))).thenReturn(expected.getLocations().get(0));
        Book actual = bookMapper.toEntity(startData);

        assertThat(actual.getBookId()).isEqualTo(expected.getBookId());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        assertThat(actual.getNumberOfTimesFavorited()).isEqualTo(expected.getNumberOfTimesFavorited());
        assertThat(actual.getLocations()).isEqualTo(expected.getLocations());
        assertThat(actual.getAuthors()).isEqualTo(expected.getAuthors());
        verify(authorMapper, times(1)).toEntity(startData.getAuthors().get(0));
        verify(locationMapper, times(1)).toEntity(startData.getLocations().get(0));

    }


}