package com.labproject.persistence;

import com.labproject.application.dto.output.VisualizeCollectionStickerDTO;
import com.labproject.application.dto.output.VisualizeCollectionUserDTO;
import com.labproject.application.dto.output.VisualizeUserWithStickersDTO;
import com.labproject.domain.CollectionUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CollectionUser, Long> {


    @Query("""
               SELECT new com.labproject.application.dto.output.VisualizeCollectionUserDTO(
                   ca.owner.name,
                   ca.owner.secondName,
                   ca.owner.email,
                   ca.owner.dateOfBirth,
                   ca.owner.dateOfRegistration,
                   ca.owner.role
               )
               FROM CollectionAlbum ca
               WHERE ca.album.albumId = :albumId
               ORDER BY ca.owner.dateOfRegistration ASC
            """)
    Optional<List<VisualizeCollectionUserDTO>> findUsersCollectingAlbum(@Param("albumId") Long albumId);


    @Query("""
               SELECT new com.labproject.application.dto.output.VisualizeCollectionUserDTO(
                   cu.name,
                   cu.secondName,
                   cu.email,
                   cu.dateOfBirth,
                   cu.dateOfRegistration,
                   cu.role
               )
               FROM CollectionUser cu
               JOIN cu.subscribedForums f
               WHERE f.forumID = :forumId
               ORDER BY cu.dateOfRegistration ASC
            """)
    List<VisualizeCollectionUserDTO> findUsersSubscribedToForum(@Param("forumId") Long forumId);

    //---


    @Query("""
                SELECT new com.labproject.application.dto.output.VisualizeCollectionUserDTO(
                    u.name,
                    u.secondName,
                    u.email,
                    u.dateOfBirth,
                    u.dateOfRegistration,
                    u.role
                )
                FROM CollectionUser u
                ORDER BY u.secondName ASC
            """)
    Optional<List<VisualizeCollectionUserDTO>> findAllUsersOrderedBySecondName();

    @Query("""
                SELECT new com.labproject.application.dto.output.VisualizeCollectionUserDTO(
                    u.name,
                    u.secondName,
                    u.email,
                    u.dateOfBirth,
                    u.dateOfRegistration,
                    u.role
                )
                FROM CollectionUser u
                WHERE u.dateOfRegistration > :registrationDate
                ORDER BY u.dateOfRegistration ASC
            """)
    Optional<List<VisualizeCollectionUserDTO>> findUsersRegisteredAfterDate(@Param("registrationDate") LocalDate registrationDate);

    Optional<CollectionUser> findByEmail(String email);

    @Query("""
                SELECT new com.labproject.application.dto.output.VisualizeUserWithStickersDTO(
                    u.name,
                    u.secondName,
                    u.email
                )
                FROM CollectionAlbum ca
                JOIN ca.owner u
                JOIN ca.collectedStickers cs
                WHERE cs.sticker.stickerID = :stickerId
                  AND cs.quantity > 1
                  AND u.collectionUserID <> :excludedUserId
            """)
    List<VisualizeUserWithStickersDTO> findUsersWithDuplicateStickersExcludingUser(
            @Param("stickerId") Long stickerId,
            @Param("excludedUserId") Long excludedUserId
    );

    @Query("""
            SELECT CASE
                   WHEN EXISTS (
                       SELECT 1
                       FROM CollectionUser u1
                       JOIN u1.blockedUsers bu1
                       WHERE u1.collectionUserID = :firstUserId
                         AND bu1.collectionUserID = :secondUserId
                   )
                   OR EXISTS (
                       SELECT 1
                       FROM CollectionUser u2
                       JOIN u2.blockedUsers bu2
                       WHERE u2.collectionUserID = :secondUserId
                         AND bu2.collectionUserID = :firstUserId
                   )
                   THEN TRUE
                   ELSE FALSE
                   END
            """)
    boolean isUserBlockedByOther(@Param("firstUserId") Long firstUserId, @Param("secondUserId") Long secondUserId);

    @Query("""
                SELECT new com.labproject.application.dto.output.VisualizeCollectionUserDTO(
                    u.name,
                    u.secondName,
                    u.email,
                    u.dateOfBirth,
                    u.dateOfRegistration,
                    u.role
                )
                FROM CollectionUser u
                ORDER BY add_numbers(
                    (SELECT COUNT(*) FROM ForumMessage fm WHERE fm.sender.collectionUserID = u.collectionUserID),
                    (SELECT COUNT(*) FROM PrivateMessage pm WHERE pm.sender.collectionUserID = u.collectionUserID)
                ) DESC
            """)
    Optional<List<VisualizeCollectionUserDTO>> findAllUsersOrderedByMessageCount();

    @Query("""
                SELECT new com.labproject.application.dto.output.VisualizeUserWithStickersDTO(
                    u.name,
                    u.secondName,
                    u.email
                )
                FROM CollectionAlbum caOther
                JOIN caOther.owner u
                JOIN caOther.collectedStickers csOther
                JOIN CollectionAlbum caMine ON caMine.owner.collectionUserID = :currentUserId
                JOIN caMine.collectedStickers csMine
                WHERE u.collectionUserID <> :currentUserId
                  AND csOther.sticker NOT IN (
                      SELECT cs.sticker
                      FROM CollectionAlbum ca
                      JOIN ca.collectedStickers cs
                      WHERE ca.owner.collectionUserID = :currentUserId
                        AND cs.quantity > 0
                  )
                  AND csOther.quantity > 1
                  AND csMine.sticker NOT IN (
                      SELECT cs.sticker
                      FROM CollectionAlbum ca
                      JOIN ca.collectedStickers cs
                      WHERE ca.owner.collectionUserID = u.collectionUserID
                        AND cs.quantity > 0
                  )
                  AND csMine.quantity > 1
                GROUP BY u.collectionUserID, u.name, u.secondName, u.email
                ORDER BY add_numbers( COUNT(DISTINCT csOther.sticker), COUNT(DISTINCT csMine.sticker)) DESC
            """)
    List<VisualizeUserWithStickersDTO> findTop3UsersWithMostTotalExchangeable(Pageable pageable, @Param("currentUserId") Long currentUserId);


    @Query("""
                SELECT new com.labproject.application.dto.output.VisualizeUserWithStickersDTO(
                    u.name,
                    u.secondName,
                    u.email
                )
                FROM CollectionAlbum caOther
                JOIN caOther.owner u
                JOIN caOther.collectedStickers csOther
                JOIN CollectionAlbum caMine ON caMine.owner.collectionUserID = :currentUserId
                JOIN caMine.collectedStickers csMine
                WHERE u.collectionUserID <> :currentUserId
                  AND csOther.sticker NOT IN (
                      SELECT cs.sticker
                      FROM CollectionAlbum ca
                      JOIN ca.collectedStickers cs
                      WHERE ca.owner.collectionUserID = :currentUserId
                        AND cs.quantity > 0
                  )
                  AND csOther.quantity > 1
                  AND csMine.sticker NOT IN (
                      SELECT cs.sticker
                      FROM CollectionAlbum ca
                      JOIN ca.collectedStickers cs
                      WHERE ca.owner.collectionUserID = u.collectionUserID
                        AND cs.quantity > 0
                  )
                  AND csMine.quantity > 1
                GROUP BY u.collectionUserID, u.name, u.secondName, u.email
                ORDER BY add_numbers( COUNT(DISTINCT csOther.sticker), COUNT(DISTINCT csMine.sticker)) DESC
            """)
    List<VisualizeUserWithStickersDTO> findTop3CollectionStickers(Pageable pageable, @Param("currentUserId") Long currentUserId);


    @Query("""
                SELECT new com.labproject.application.dto.output.VisualizeCollectionStickerDTO(
                    csOther.collectionStickerId,
                    csOther.sticker,
                    csOther.quantity
                )
                FROM CollectionAlbum caOther
                JOIN caOther.owner u
                JOIN caOther.collectedStickers csOther
                JOIN CollectionAlbum caMine ON caMine.owner.collectionUserID = :currentUserId
                JOIN caMine.collectedStickers csMine
                WHERE u.collectionUserID <> :currentUserId
                  AND csOther.sticker NOT IN (
                      SELECT cs.sticker
                      FROM CollectionAlbum ca
                      JOIN ca.collectedStickers cs
                      WHERE ca.owner.collectionUserID = :currentUserId
                        AND cs.quantity > 0
                  )
                  AND csOther.quantity > 1
                  AND csMine.sticker NOT IN (
                      SELECT cs.sticker
                      FROM CollectionAlbum ca
                      JOIN ca.collectedStickers cs
                      WHERE ca.owner.collectionUserID = u.collectionUserID
                        AND cs.quantity > 0
                  )
                  AND csMine.quantity > 1
                GROUP BY caOther.owner, csOther.collectionStickerId
                ORDER BY add_numbers( COUNT(DISTINCT csOther.sticker), COUNT(DISTINCT csMine.sticker)) DESC
            """)
    List<VisualizeCollectionStickerDTO> findTopThreeCollectionStickers(Pageable pageable, @Param("currentUserId") Long currentUserId);

}