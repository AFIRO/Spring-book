package be.hogent.springbook.user.mapper;

import be.hogent.springbook.book.mapper.BookMapper;
import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.entity.dto.ApplicationUserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static be.hogent.springbook.user.UserTestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ApplicationUserMapperTest {
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private ApplicationUserMapper applicationUserMapper;

    @Test
    void toEntity() {
        ApplicationUser expected = getApplicationUserNoId();
        ApplicationUser actual = applicationUserMapper.toEntity(getRegisterDto());

        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getRole()).isEqualTo(expected.getRole());
        assertThat(actual.getUserId()).isEqualTo(actual.getUserId());
        assertThat(actual.getMaxNumberOfFavorites()).isEqualTo(3);
        assertThat(actual.getFavoriteBooks()).isEqualTo(expected.getFavoriteBooks());
    }

    @Test
    void toDto() {
        ApplicationUserDto expected = getApplicationUserDto();
        ApplicationUserDto actual = applicationUserMapper.toDto(getApplicationUser());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getRole()).isEqualTo(expected.getRole());
        assertThat(actual.getUserId()).isEqualTo(actual.getUserId());
        assertThat(actual.getMaxNumberOfFavorites()).isEqualTo(expected.getMaxNumberOfFavorites());
        assertThat(actual.getFavoriteBooks()).isEqualTo(expected.getFavoriteBooks());

    }

}