package com.labproject.domain;

import com.labproject.application.dto.input.AlbumSectionDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class AlbumSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long albumSectionID;

    private String title;

    private String description;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "sectionID")
    private List<Sticker> stickers;

    public AlbumSection() {
    }

    public AlbumSection(AlbumSectionDTO albumSectionDTO) {
        this.title = albumSectionDTO.getTitle();
        this.description = albumSectionDTO.getDescription();
    }

    public void removeSticker(Sticker sticker) {
        stickers.remove(sticker);
    }

    public long getAlbumSectionID() {
        return albumSectionID;
    }

    public void setAlbumSectionID(long albumSectionID) {
        this.albumSectionID = albumSectionID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Sticker> getStickers() {
        return stickers;
    }

    public void setStickers(List<Sticker> stickers) {
        this.stickers = stickers;
    }

    public void addSticker(Sticker sticker) {
        stickers.add(sticker);
    }

    public String getSectionName() {
        return title;
    }

}
