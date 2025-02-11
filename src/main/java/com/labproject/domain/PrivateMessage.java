package com.labproject.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
public class PrivateMessage {

    @ManyToOne
    @JoinColumn(name = "forumId", nullable = false)
    Forum forum;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long privateMessageId;
    @NotBlank
    private String body;
    @CreatedDate
    @NotNull
    private LocalDateTime sendDate;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "sender")
    private CollectionUser sender;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "receiver")
    private CollectionUser receiver;

    @ManyToOne
    @JoinColumn(name = "reply_to")
    private PrivateMessage replyTo = null;

    private boolean read = false;

    private boolean oneTimeMessage = false;

    public PrivateMessage() {
    }

    public PrivateMessage(Forum forum, String body, CollectionUser receiver, CollectionUser sender, boolean oneTimeMessage) {
        this.forum = forum;
        this.body = body;
        this.receiver = receiver;
        this.sender = sender;
        this.oneTimeMessage = oneTimeMessage;
        this.sendDate = LocalDateTime.now();
    }

    public PrivateMessage(Forum forum, String body, CollectionUser receiver, CollectionUser sender, boolean oneTimeMessage, PrivateMessage replyTo) {
        this.forum = forum;
        this.body = body;
        this.receiver = receiver;
        this.sender = sender;
        this.oneTimeMessage = oneTimeMessage;
        this.sendDate = LocalDateTime.now();
        this.replyTo = replyTo;
    }


    public long getPrivateMessageId() {
        return privateMessageId;
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

    public CollectionUser getSender() {
        return sender;
    }

    public void setSender(CollectionUser sender) {
        this.sender = sender;
    }

    public CollectionUser getReceiver() {
        return receiver;
    }

    public void setReceiver(CollectionUser receiver) {
        this.receiver = receiver;
    }

    public PrivateMessage getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(PrivateMessage replyTo) {
        this.replyTo = replyTo;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean readBy) {
        this.read = readBy;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public boolean isOneTimeMessage() {
        return oneTimeMessage;
    }


}