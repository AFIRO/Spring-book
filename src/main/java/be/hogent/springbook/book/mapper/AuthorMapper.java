package be.hogent.springbook.book.mapper;

import be.hogent.springbook.book.entity.Author;
import be.hogent.springbook.book.entity.dto.AuthorDto;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public AuthorDto toDto(Author data){
        return AuthorDto.builder()
                .authorId(data.getAuthorId())
                .name(data.getName())
                .build();
    }

    public Author toEntity(AuthorDto data){
        return Author.builder()
                .authorId(data.getAuthorId())
                .name(data.getName())
                .build();
    }
}
