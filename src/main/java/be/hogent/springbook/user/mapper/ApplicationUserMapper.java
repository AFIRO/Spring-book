package be.hogent.springbook.user.mapper;

import be.hogent.springbook.book.mapper.BookMapper;
import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.entity.dto.ApplicationUserDto;
import be.hogent.springbook.user.entity.dto.RegisterDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
@Component
@AllArgsConstructor
public class ApplicationUserMapper {
    private final BookMapper bookMapper;


    public ApplicationUser toEntity(RegisterDto dto) {
        return ApplicationUser.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();
    }

    public ApplicationUserDto toDto(ApplicationUser applicationUser) {
        return ApplicationUserDto.builder()
                .userId(applicationUser.getUserId())
                .email(applicationUser.getEmail())
                .role(applicationUser.getRole())
                .maxNumberOfFavorites(applicationUser.getMaxNumberOfFavorites())
                .favoriteBooks(applicationUser.getFavoriteBooks().stream().map(bookMapper::toDto).toList())
                .build();
    }
}
