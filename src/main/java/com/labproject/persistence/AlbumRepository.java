package com.labproject.persistence;

import com.labproject.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Query("SELECT o FROM Album o WHERE o.publicAvailability = true AND :actualDate BETWEEN o.beginDate AND o.endingDate")
    Optional<List<Album>> findByPublicAvailabilityAndDate(@Param("actualDate") LocalDate actualDate);

}
