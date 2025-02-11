package com.labproject.application.dto.input;

import com.labproject.domain.CollectionUser;
import com.labproject.domain.Forum;
import com.labproject.domain.ForumMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class ForumMessageDTO {

    @NotNull
    @NotBlank
    private String body;

    private LocalDateTime sendDate;

    private Forum forum;

    private CollectionUser sender;

    private ForumMessage replyTo;

    private List<CollectionUser> readBy;

    public ForumMessageDTO() {
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public CollectionUser getSender() {
        return sender;
    }

    public void setSender(CollectionUser sender) {
        this.sender = sender;
    }

    public ForumMessage getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(ForumMessage replyTo) {
        this.replyTo = replyTo;
    }

    public List<CollectionUser> getReadBy() {
        return readBy;
    }

    public void setReadBy(List<CollectionUser> readBy) {
        this.readBy = readBy;
    }
}
