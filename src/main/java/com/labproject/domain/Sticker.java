package com.labproject.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Sticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stickerID;

    private int numberInAlbum;

    private String name;

    private String description;

    private String imageURL;

    private String typeOfSticker;

    public Sticker() {
    }


    public long getStickerID() {
        return stickerID;
    }

    public void setStickerID(int sticker_id) {
        this.stickerID = sticker_id;
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

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberInAlbum() {
        return numberInAlbum;
    }

    public void setNumberInAlbum(int numberInSection) {
        this.numberInAlbum = numberInSection;
    }

}
