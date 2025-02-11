package com.labproject.application.dto.output;

import com.labproject.domain.Sticker;

public class VisualizeStickerDTO {

    private int number;

    private String stickerName;

    private String description;

    private String imageURL;

    private String typeOfSticker;

    public VisualizeStickerDTO() {
    }

    public VisualizeStickerDTO(int number, String stickerName, String description, String imageURL, String type) {
        this.stickerName = stickerName;
        this.description = description;
        this.imageURL = imageURL;
        this.typeOfSticker = type;
        this.number = number;
    }

    public VisualizeStickerDTO(Sticker sticker) {
        this.stickerName = sticker.getName();
        this.description = sticker.getDescription();
        this.imageURL = sticker.getImageURL();
        this.typeOfSticker = sticker.getTypeOfSticker();

    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStickerName() {
        return stickerName;
    }

    public void setStickerName(String stickerName) {
        this.stickerName = stickerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTypeOfSticker() {
        return typeOfSticker;
    }

    public void setTypeOfSticker(String typeOfSticker) {
        this.typeOfSticker = typeOfSticker;
    }
}
