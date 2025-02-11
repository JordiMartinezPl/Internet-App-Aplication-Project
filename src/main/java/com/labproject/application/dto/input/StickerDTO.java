package com.labproject.application.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StickerDTO {

    @NotBlank
    private long numberInSection;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String description;

    @NotBlank
    @NotNull
    private String imageURL;

    @NotNull
    @NotBlank
    private String typeOfSticker;

    public StickerDTO() {
    }


    public StickerDTO(String name, String description, String imageURL, int quantity, String typeOfStickere) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.typeOfSticker = typeOfStickere;

    }

    public long getStickerID() {
        return numberInSection;
    }

    public void setStickerID(long numberInSection) {
        this.numberInSection = numberInSection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
