package be.hogent.springbook.book.mapper;

import be.hogent.springbook.book.entity.Book;
import be.hogent.springbook.book.entity.dto.BookDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private LocationMapper locationMapper;
    private AuthorMapper authorMapper;

    public BookDto toDto(Book data){
        return BookDto.builder()
                .bookId(data.getBookId())
                .isbn(data.getIsbn())
                .title(data.getTitle())
                .numberOfTimesFavorited(data.getNumberOfTimesFavorited())
                .price(data.getPrice())
                .authors(data.getAuthors().stream().map(authorMapper::toDto).toList())
                .locations(data.getLocations().stream().map(locationMapper::toDto).toList())
                .build();
    }

    public Book toEntity(BookDto data){
        return Book.builder()
                .bookId(data.getBookId())
                .isbn(data.getIsbn())
                .title(data.getTitle())
                .numberOfTimesFavorited(data.getNumberOfTimesFavorited())
                .price(data.getPrice())
                .authors(data.getAuthors().stream().map(authorMapper::toEntity).toList())
                .locations(data.getLocations().stream().map(locationMapper::toEntity).toList())
                .build();
    }
}
