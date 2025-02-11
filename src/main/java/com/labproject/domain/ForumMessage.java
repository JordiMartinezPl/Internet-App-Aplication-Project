package com.labproject.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class ForumMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long forumMessageID;

    private String body;

    @CreatedDate
    private LocalDateTime sendDate;

    @ManyToOne
    @JoinColumn(name = "forum")
    private Forum forum;

    @ManyToOne
    @JoinColumn(name = "sender")
    private CollectionUser sender;

    @ManyToOne
    @JoinColumn(name = "replyTo")
    private ForumMessage replyTo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "forum_message_read_by", joinColumns = @JoinColumn(name = "ReadForumMessageID"), inverseJoinColumns = @JoinColumn(name = "ReadCollectionUserID"))
    private List<CollectionUser> readBy;

    public ForumMessage() {
    }

    public ForumMessage(Forum forum, CollectionUser sender, String body) {
        this.forum = forum;
        this.sender = sender;
        this.body = body;
    }

    public ForumMessage(String body, CollectionUser user, Forum forum) {
        this.body = body;
        this.sender = user;
        this.forum = forum;
        this.sendDate = LocalDateTime.now();
    }

    public void addRead(CollectionUser user) {
        readBy.add(user);
    }

    public Long getForumMessageID() {
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

    public CollectionUser getSender() {
        return sender;
    }

    public void setSender(CollectionUser user) {
        this.sender = user;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
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
