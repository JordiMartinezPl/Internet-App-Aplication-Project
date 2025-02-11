package com.labproject.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long albumId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "editor")
    private CollectionUser editor;

    @Column
    private boolean publicAvailability = true;

    private String title;


    private String description;

    private LocalDate beginDate;
    private LocalDate endingDate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "album")
    private List<AlbumSection> sections = null;

    @OneToOne
    @JoinColumn(name = "forum")
    private Forum forum = null;

    public Album() {
    }

    public Album(CollectionUser editor, String title, String description, Forum forum) {
        this.editor = editor;
        this.title = title;
        this.description = description;
        this.forum = forum;
        this.sections = new ArrayList<AlbumSection>();
    }

    public void addSection(AlbumSection section) {
        this.sections.add(section);
    }

    public void removeSection(AlbumSection section) {
        sections.remove(section);
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public void deleteForum() {
        this.forum = null;
    }

    public List<AlbumSection> getSections() {
        return sections;
    }

    public void setSections(List<AlbumSection> sections) {
        this.sections = sections;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CollectionUser getEditor() {
        return editor;
    }

    public void setEditor(CollectionUser owner) {
        this.editor = owner;
    }

    public boolean getPublicAvailability() {
        return publicAvailability;
    }

    public void setPublicAvailability(boolean publicAvailability) {
        this.publicAvailability = publicAvailability;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumID) {
        this.albumId = albumID;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(LocalDate endingDate) {
        this.endingDate = endingDate;
    }

}
