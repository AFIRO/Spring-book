package be.hogent.springbook.book.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.ISBN;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Book {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String bookId;
    private String title;
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Author> authors;
    @Column(unique = true)
    private String isbn;
    private double price;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Location> locations;
    private int numberOfTimesFavorited;

}
