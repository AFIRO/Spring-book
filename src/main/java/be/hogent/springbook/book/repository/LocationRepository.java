package be.hogent.springbook.book.repository;

import be.hogent.springbook.book.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {
    boolean existsByLocationCode1AndLocationCode2AndLocationName(Integer locationCode1, Integer locationCode2, String locationName);
}
