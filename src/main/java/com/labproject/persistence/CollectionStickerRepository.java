package com.labproject.persistence;

import com.labproject.application.dto.output.HasStickerDTO;
import com.labproject.application.dto.output.VisualizeCollectionStickerDTO;
import com.labproject.domain.Album;
import com.labproject.domain.CollectionSticker;
import com.labproject.domain.CollectionUser;
import com.labproject.domain.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionStickerRepository extends JpaRepository<CollectionSticker, Long> {
    @Query("SELECT cs FROM CollectionAlbum ca JOIN ca.collectedStickers cs WHERE ca.owner = :user AND ca.album = :album AND cs.sticker = :sticker")
    Optional<CollectionSticker> findCollectionStickerByAlbumAndOwnerWithSpecificSticker(@Param("album") Album album, @Param("user") CollectionUser user, @Param("sticker") Sticker sticker);


    @Query("SELECT new com.labproject.application.dto.output.HasStickerDTO( " +
            "   ca.owner.collectionUserID, " +
            "   ca.owner.name, " +
            "   a.albumId, " +
            "   s.title, " +
            "   s.albumSectionID, " +
            "   stk.name, " +
            "   stk.numberInAlbum, " +
            "   stk.stickerID, " +
            "   COALESCE(cs.quantity, 0) ) " +
            "FROM Album a " +
            "JOIN a.sections s " +
            "JOIN s.stickers stk " +
            "LEFT JOIN CollectionAlbum ca ON ca.album = a AND ca.owner.collectionUserID = :collectionistID " +
            "LEFT JOIN ca.collectedStickers cs ON cs.sticker = stk " +
            "WHERE a.albumId = :albumID " +
            "ORDER BY s.albumSectionID, stk.numberInAlbum")
    List<HasStickerDTO> findAllStickersWithQuantities(@Param("albumID") Long albumID, @Param("collectionistID") Long collectionistID);


    @Query("SELECT new com.labproject.application.dto.output.HasStickerDTO( " +
            "   ca.owner.collectionUserID, " +
            "   ca.owner.name, " +
            "   a.albumId, " +
            "   s.title, " +
            "   s.albumSectionID, " +
            "   stk.name, " +
            "   stk.numberInAlbum, " +
            "   stk.stickerID ) " +
            "FROM Album a " +
            "JOIN a.sections s " +
            "JOIN s.stickers stk " +
            "LEFT JOIN CollectionAlbum ca ON ca.album.albumId = a.albumId " +
            "LEFT JOIN ca.collectedStickers cs ON cs.sticker.stickerID = stk.stickerID " +
            "WHERE a.albumId = :albumID " +
            "AND ca.owner.collectionUserID = :collectionistID " +
            "AND cs.collectionStickerId IS NULL " +
            "ORDER BY s.albumSectionID, stk.numberInAlbum")
    List<HasStickerDTO> findMissingStickersByAlbumAndUser(
            @Param("albumID") Long albumID,
            @Param("collectionistID") Long collectionistID);


    @Query("SELECT new com.labproject.application.dto.output.HasStickerDTO( " +
            "   c.collectionUserID, " +
            "   c.name, " +
            "   a.albumId, " +
            "   s.title, " +
            "   s.albumSectionID, " +
            "   stk.name, " +
            "   stk.numberInAlbum, " +
            "   stk.stickerID, " +
            "   cs.quantity ) " +
            "FROM CollectionAlbum ca " +
            "JOIN ca.collectedStickers cs " +
            "JOIN cs.sticker stk " +
            "JOIN ca.owner c " +
            "JOIN ca.album a " +
            "JOIN a.sections s " +
            "WHERE a.albumId = :albumID " +
            "AND c.collectionUserID = :collectionistID " +
            "AND stk MEMBER OF s.stickers " +
            "ORDER BY s.albumSectionID, stk.numberInAlbum")
    Optional<List<HasStickerDTO>> findAllByCollectionOrderBySectionAndNumberAsc(
            @Param("albumID") Long albumID,
            @Param("collectionistID") Long collectionistID);

    @Query("SELECT cs FROM CollectionAlbum ca JOIN ca.collectedStickers cs WHERE ca.owner.collectionUserID = :userId AND cs.sticker.stickerID = :stickerId")
    Optional<CollectionSticker> findCollectionStickerByUserIdAndStickerId(@Param("userId") Long userId, @Param("stickerId") Long stickerId);

    @Query("SELECT cs FROM CollectionAlbum ca JOIN ca.collectedStickers cs WHERE ca.owner.collectionUserID = :userId AND cs.sticker IN :stickerList")
    Optional<List<CollectionSticker>> findCollectionStickerListByOwnerAndStickerList(@Param("userId") Long userId, @Param("stickerList") List<Sticker> stickerList);

    @Query("""
                SELECT new com.labproject.application.dto.output.VisualizeCollectionStickerDTO(
                           cs.collectionStickerId,
                           cs.sticker,
                           cs.quantity
                       )
                FROM CollectionAlbum ca1
                JOIN ca1.collectedStickers cs
                WHERE ca1.owner.collectionUserID = :firstUserId
                  AND cs.quantity > 1
                  AND cs.sticker.stickerID NOT IN (
                      SELECT cs2.sticker.stickerID
                      FROM CollectionAlbum ca2
                      JOIN ca2.collectedStickers cs2
                      WHERE ca2.owner.collectionUserID = :secondUserId
                  )
            """)
    List<VisualizeCollectionStickerDTO> findStickersForFirstUserButNotSecond(
            @Param("firstUserId") Long firstUserId,
            @Param("secondUserId") Long secondUserId
    );

    @Query("""
                SELECT cs.sticker
                FROM CollectionAlbum ca1
                JOIN ca1.collectedStickers cs
                WHERE ca1.owner.collectionUserID = :firstUserId
                  AND cs.quantity > 1
                  AND cs.sticker.stickerID NOT IN (
                      SELECT cs2.sticker.stickerID
                      FROM CollectionAlbum ca2
                      JOIN ca2.collectedStickers cs2
                      WHERE ca2.owner.collectionUserID = :secondUserId
                  )
            """)
    List<Sticker> findAllStickersAvailableForFirstUserButNotSecond(
            @Param("firstUserId") Long firstUserId,
            @Param("secondUserId") Long secondUserId
    );

    @Query("""
                SELECT CASE WHEN COUNT(cs) > 0 THEN true ELSE false END
                FROM CollectionAlbum ca
                JOIN ca.collectedStickers cs
                WHERE ca.owner.collectionUserID = :userId
                  AND cs.sticker.stickerID = :stickerId
            """)
    boolean existsStickerForUser(@Param("userId") Long userId, @Param("stickerId") Long stickerId);

    @Query("""
                SELECT new com.labproject.application.dto.output.VisualizeCollectionStickerDTO(
                           cs.collectionStickerId,
                           cs.sticker,
                           cs.quantity
                       )
                FROM CollectionAlbum ca1
                JOIN ca1.collectedStickers cs
                WHERE ca1.owner.collectionUserID = :firstUserId
                  AND cs.quantity > 1
                  AND cs.sticker.stickerID NOT IN (
                      SELECT cs2.sticker.stickerID
                      FROM CollectionAlbum ca2
                      JOIN ca2.collectedStickers cs2
                      WHERE ca2.owner.collectionUserID = :secondUserId
                  )
                UNION

                SELECT new com.labproject.application.dto.output.VisualizeCollectionStickerDTO(
                           cs2.collectionStickerId,
                           cs2.sticker,
                           cs2.quantity
                       )
                FROM CollectionAlbum ca2
                JOIN ca2.collectedStickers cs2
                WHERE ca2.owner.collectionUserID = :secondUserId
                  AND cs2.quantity > 1
                  AND cs2.sticker.stickerID NOT IN (
                      SELECT cs1.sticker.stickerID
                      FROM CollectionAlbum ca1
                      JOIN ca1.collectedStickers cs1
                      WHERE ca1.owner.collectionUserID = :firstUserId
                  )
            """)
    List<VisualizeCollectionStickerDTO> findExchangeableStickersBetweenUsers(
            @Param("firstUserId") Long firstUserId,
            @Param("secondUserId") Long secondUserId
    );

    @Query("""
        SELECT st.sticker
        FROM CollectionAlbum ca
        JOIN ca.collectedStickers st
        JOIN ca.album al
        WHERE st.sticker IN (:stickersToCheck) AND (ca.publicAvailability IS true AND al.endingDate>CURRENT_TIMESTAMP)
""")
    Optional<List<Sticker>>findAvailableFromList(@Param("stickersToCheck") List<Sticker> stickersToCheck);
}
