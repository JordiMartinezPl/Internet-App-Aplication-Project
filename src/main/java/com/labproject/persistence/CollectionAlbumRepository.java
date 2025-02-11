package com.labproject.persistence;

import com.labproject.application.dto.output.VisualizeCollectionAlbumDTO;
import com.labproject.domain.Album;
import com.labproject.domain.CollectionAlbum;
import com.labproject.domain.CollectionUser;
import com.labproject.domain.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionAlbumRepository extends JpaRepository<CollectionAlbum, Long> {

    Optional<CollectionAlbum> findByOwnerAndAlbum(CollectionUser user, Album album);

    @Query("SELECT CASE WHEN COUNT(ca) > 0 THEN true ELSE false END " +
            "FROM CollectionAlbum ca " +
            "WHERE ca.owner.collectionUserID = :ownerId AND ca.publicAvailability = true")
    boolean existsByOwnerAndPublicAvailabilityTrue(@Param("ownerId") Long ownerId);

    @Query("""
               SELECT new com.labproject.application.dto.output.VisualizeCollectionAlbumDTO(
                    ca.collectionAlbumId,
                    cu.name,
                    a.title,
                    a.description,
                    a.beginDate,
                    a.endingDate,
                    null
                )
                FROM CollectionAlbum ca
                JOIN ca.album a
                JOIN ca.owner cu
                WHERE cu.collectionUserID = :userId
                  AND a.endingDate < CURRENT_DATE
                ORDER BY a.endingDate ASC
            """)
    Optional<List<VisualizeCollectionAlbumDTO>> findEndedAlbumsByUserId(@Param("userId") Long userId);

    @Query("""
                SELECT ca 
                FROM CollectionAlbum ca 
                JOIN ca.owner u 
                WHERE u.collectionUserID = :userId 
                  AND ca.publicAvailability = true
                  AND :currentDate BETWEEN ca.album.beginDate AND ca.album.endingDate
            """)
    Optional<List<CollectionAlbum>> findByUserIdAndDateActive(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT ca FROM CollectionAlbum ca JOIN ca.collectedStickers cs WHERE ca.owner = :owner AND cs.sticker = :sticker")
    Optional<CollectionAlbum> findByUserAndSticker(@Param("owner") CollectionUser owner, @Param("sticker") Sticker sticker);


}
