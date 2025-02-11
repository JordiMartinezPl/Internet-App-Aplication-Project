package com.labproject.application.dto.output;

import java.util.List;

public class VisualizeUserWithStickersDTO {
    private String firstName;
    private String lastName;
    private String email;
    private List<VisualizeCollectionStickerDTO> visualizeCollectionSticker;

    public VisualizeUserWithStickersDTO() {
    }

    public VisualizeUserWithStickersDTO(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<VisualizeCollectionStickerDTO> getVisualizeCollectionSticker() {
        return visualizeCollectionSticker;
    }

    public void setVisualizeCollectionSticker(List<VisualizeCollectionStickerDTO> visualizeCollectionSticker) {
        this.visualizeCollectionSticker = visualizeCollectionSticker;
    }
}
