package com.labproject.persistence;

import com.labproject.application.dto.output.VisualizeForumDTO;
import com.labproject.domain.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {
    @Query("SELECT new com.labproject.application.dto.output.VisualizeForumDTO(f.forumID, a.albumId, a.title) " +
            "FROM CollectionUser u " +
            "JOIN u.subscribedForums f " +
            "JOIN Album a ON a.forum.forumID = f.forumID " +
            "WHERE u.collectionUserID = :userID")
    Optional<List<VisualizeForumDTO>> findForumsByUserId(@Param("userID") Long userID);

    @Query("SELECT new com.labproject.application.dto.output.VisualizeForumDTO(f.forumID, a.albumId, a.title) " +
            "FROM Album a " +
            "JOIN a.forum f " +
            "LEFT JOIN ForumMessage fm ON fm.forum.forumID = f.forumID " +
            "GROUP BY f.forumID, a.albumId, a.title " +
            "ORDER BY COUNT(fm.forumMessageID) DESC")
    Optional<List<VisualizeForumDTO>> findAllForumsOrderedByMessageCount();


}
