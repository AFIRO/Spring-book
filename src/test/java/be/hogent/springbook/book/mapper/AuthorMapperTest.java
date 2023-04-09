package be.hogent.springbook.book.mapper;

import be.hogent.springbook.book.entity.Author;
import be.hogent.springbook.book.entity.dto.AuthorDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static be.hogent.springbook.TestData.getAuthor;
import static be.hogent.springbook.TestData.getAuthorDto;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class AuthorMapperTest {
    @InjectMocks
    private AuthorMapper authorMapper;

    @Test
    void toDto() {
        AuthorDto expected = getAuthorDto();
        AuthorDto actual = authorMapper.toDto(getAuthor());
        assertThat(actual.getAuthorId()).isEqualTo(expected.getAuthorId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    @Test
    void toEntity() {
        Author expected = getAuthor();
        Author actual = authorMapper.toEntity(getAuthorDto());
        assertThat(actual.getAuthorId()).isEqualTo(expected.getAuthorId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }
}