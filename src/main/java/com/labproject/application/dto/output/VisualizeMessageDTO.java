package com.labproject.application.dto.output;

import java.time.LocalDateTime;

public class VisualizeMessageDTO {

    private long forumMessageID;

    private String body;

    private LocalDateTime sendDate;

    private long forumId;

    private String senderEmail;

    private Long replyTo;

    private boolean read;

    public VisualizeMessageDTO() {

    }

    public VisualizeMessageDTO(
            Long forumMessageID,
            String body,
            String senderEmail,
            LocalDateTime sendDate,
            Long forumID,
            Long replyToForumMessageID, boolean read
    ) {
        this.forumMessageID = forumMessageID;
        this.body = body;
        this.senderEmail = senderEmail;
        this.sendDate = sendDate;
        this.forumId = forumID;
        this.replyTo = replyToForumMessageID;
        this.read = read;
    }


    public long getForumMessageID() {
        return forumMessageID;
    }

    public void setForumMessageID(long forumMessageID) {
        this.forumMessageID = forumMessageID;
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

    public long getForumId() {
        return forumId;
    }

    public void setForumId(long forumId) {
        this.forumId = forumId;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public Long getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Long replyTo) {
        this.replyTo = replyTo;
    }

    public boolean getReadBy() {
        return read;
    }

    public void setReadBy(boolean read) {
        this.read = read;
    }
}
