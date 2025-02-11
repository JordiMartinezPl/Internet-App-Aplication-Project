package com.labproject.persistence;

import com.labproject.application.dto.output.VisualizeMessageDTO;
import com.labproject.domain.ForumMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ForumMessageRepository extends JpaRepository<ForumMessage, Long> {


    @Query("""
                SELECT new com.labproject.application.dto.output.VisualizeMessageDTO(
                    m.forumMessageID,
                    m.body,
                    m.sender.email,
                    m.sendDate,
                    m.forum.forumID,
                    m.replyTo.forumMessageID,
                    CASE WHEN :userId IN (
                        SELECT rb.collectionUserID FROM m.readBy rb
                    ) THEN true ELSE false END
                )
                FROM ForumMessage m
                LEFT JOIN m.readBy u
                WHERE m.forum.forumID = :forumId
                  AND m.sendDate >= :date
                GROUP BY m.forumMessageID, m.body, m.sender.email, m.sendDate, m.forum.forumID, m.replyTo.forumMessageID
                ORDER BY m.sendDate DESC
            """)
    List<VisualizeMessageDTO> findForumMessagesByForum(
            @Param("forumId") Long forumId,
            @Param("date") LocalDateTime date,
            @Param("userId") Long userId
    );


    @Query("""
                SELECT new com.labproject.application.dto.output.VisualizeMessageDTO(
                m.forumMessageID,
                    m.body,
                    m.sender.email,
                    m.sendDate,
                    m.forum.forumID,
                    m.replyTo.forumMessageID,
                    CASE WHEN :userId IN (
                        SELECT rb.collectionUserID FROM m.readBy rb
                    ) THEN true ELSE false END
                )
                FROM ForumMessage m
                WHERE m.sendDate >= :date
                ORDER BY m.sendDate DESC
            """)
    List<VisualizeMessageDTO> findMessageAfterDate(
            @Param("date") LocalDateTime date,
            @Param("userId") Long userId
    );


}
