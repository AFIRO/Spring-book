package be.hogent.springbook.book.entity.dto;


import be.hogent.springbook.book.validation.ISBNChecksum;
import be.hogent.springbook.book.validation.ISBNFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private String bookId;
    @NotNull
    @NotBlank
    private String title;
    @NotNull(message = "{author.required}")
    @Size(min = 1, max = 3, message = "{max.three.author}")
    @Valid
    private List<AuthorDto> authors;
    @ISBNChecksum
    @ISBNFormat
    private String isbn;
    @DecimalMin(value = "0.01", message = "{price.too.low}")
    @DecimalMax(value = "99.99", message = "{price.too.high}")
    private double price;
    @NotNull(message = "{location.required}")
    @Size(min = 1, max = 3, message = "{max.three.location}")
    @Valid
    private List<LocationDto> locations;
    @NotNull
    private int numberOfTimesFavorited;

    public static BookDto  generateDefault(){
        return BookDto.builder()
                .bookId(null)
                .isbn(null)
                .authors(new ArrayList<>())
                .locations(new ArrayList<>())
                .numberOfTimesFavorited(0)
                .price(0.01)
                .build();
    }
}
