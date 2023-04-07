package be.hogent.springbook.user.entity;

import be.hogent.springbook.book.entity.Book;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class ApplicationUser {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String userId;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> favoriteBooks;
    private int maxNumberOfFavorites;
}