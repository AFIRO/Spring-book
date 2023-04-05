package be.hogent.springbook.book.entity.dto;


import be.hogent.springbook.book.validation.ISBNChecksum;
import be.hogent.springbook.book.validation.ISBNFormat;
import be.hogent.springbook.book.validation.LocationCode;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @NotNull(message = "At least one author is required")
    @Size(min = 1, max = 3, message = "You can have a maximum of 3 authors")
    private List<AuthorDto> authors;
    @ISBNChecksum
    @ISBNFormat
    private String isbn;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be higher than 0")
    @DecimalMax(value = "99.99", message = "Price must be lower than 100")
    private double price;
    @NotNull(message = "At least one location is required")
    @Size(min = 1, max = 3, message = "You can have a maximum of 3 locations")
    @LocationCode
    private List<LocationDto> locations;
    @NotNull
    private int numberOfTimesFavorited;
}
