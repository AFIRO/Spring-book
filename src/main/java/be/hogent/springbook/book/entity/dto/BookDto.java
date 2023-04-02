package be.hogent.springbook.book.entity.dto;

import be.hogent.springbook.book.entity.Author;
import be.hogent.springbook.book.entity.Location;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

import java.util.List;

@Data
@Builder
public class BookDto {
    private String bookId;
    @NotNull
    @NotBlank
    private String title;
    @NotNull
    @NotEmpty
    private List<AuthorDto> authors;
    @ISBN
    private String isbn;
    @Positive
    @Max(100)
    private double price;
    @NotNull
    @NotEmpty
    private List<LocationDto> locations;
    @NotNull
    private int numberOfTimesFavorited;
}
