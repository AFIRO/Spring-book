package be.hogent.springbook.book.entity.dto;

import be.hogent.springbook.book.entity.Author;
import be.hogent.springbook.book.entity.Location;
import be.hogent.springbook.book.validation.ISBNChecksum;
import be.hogent.springbook.book.validation.ISBNFormat;
import be.hogent.springbook.book.validation.LocationCode;
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
    @Size(max = 3)
    private List<AuthorDto> authors;
    @ISBNChecksum
    @ISBNFormat
    private String isbn;
    @Positive
    @Max(100)
    private double price;
    @NotNull
    @NotEmpty
    @LocationCode
    private List<LocationDto> locations;
    @NotNull
    private int numberOfTimesFavorited;
}
