package be.hogent.springbook.user.entity.dto;

import be.hogent.springbook.book.entity.dto.BookDto;
import be.hogent.springbook.user.entity.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApplicationUserDto {
    private String userId;
    private String email;
    private UserRole role;
    private List<BookDto> favoriteBooks;
    private int maxNumberOfFavorites;
}
