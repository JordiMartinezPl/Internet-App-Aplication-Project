package com.labproject.application.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class AlbumSectionDTO {
    @NotBlank
    @NotNull
    private String title;
    @NotBlank
    @NotNull
    private String description;
    @NotNull
    private List<StickerDTO> stickers;

    public AlbumSectionDTO() {
    }

    public AlbumSectionDTO(String title, List<StickerDTO> stickers) {
        this.title = title;
        this.stickers = stickers;
    }

    public AlbumSectionDTO(String title, String description, List<StickerDTO> sticker) {
        this.title = title;
        this.description = description;
        this.stickers = sticker;
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

    public List<StickerDTO> getStickers() {
        return stickers;
    }

    public void setStickers(List<StickerDTO> stickers) {
        this.stickers = stickers;
    }

    public void addSticker(StickerDTO stickerDto) {
        stickers.add(stickerDto);
    }

}
