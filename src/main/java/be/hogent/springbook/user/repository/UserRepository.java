package be.hogent.springbook.user.repository;

import be.hogent.springbook.user.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, String> {
    Optional<ApplicationUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
