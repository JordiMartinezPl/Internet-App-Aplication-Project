package com.labproject.application.dto.output;

public class VisualizeForumDTO {

    String albumName;
    private long forumID;
    private long albumID;

    public VisualizeForumDTO() {
    }

    public VisualizeForumDTO(long forumID, long albumID, String albumName) {
        this.forumID = forumID;
        this.albumID = albumID;
        this.albumName = albumName;

    }

    public long getForumID() {
        return forumID;
    }

    public void setForumID(long forumID) {
        this.forumID = forumID;
    }

    public long getAlbumID() {
        return albumID;
    }

    public void setAlbumID(long albumID) {
        this.albumID = albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}
