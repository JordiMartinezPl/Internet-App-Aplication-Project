package com.labproject.application.dto.output;

public class HasStickerDTO {
    private Long collectionUserID;
    private String name;
    private Long albumId;
    private String sectionTitle;
    private Long albumSectionID;
    private String stickerName;
    private int numberInSection;
    private Long stickerID;
    private Integer quantity;

    public HasStickerDTO() {
    }

    public HasStickerDTO(Long collectionUserID, String name, Long albumId, String sectionTitle,
                         Long albumSectionID, String stickerName, int numberInSection,
                         Long stickerID, Integer quantity) {
        this.collectionUserID = collectionUserID;
        this.name = name;
        this.albumId = albumId;
        this.sectionTitle = sectionTitle;
        this.albumSectionID = albumSectionID;
        this.stickerName = stickerName;
        this.numberInSection = numberInSection;
        this.stickerID = stickerID;
        this.quantity = quantity;
    }

    public HasStickerDTO(Long collectionUserID, String name, Long albumId, String sectionTitle,
                         Long albumSectionID, String stickerName, int numberInSection,
                         Long stickerID) {
        this.collectionUserID = collectionUserID;
        this.name = name;
        this.albumId = albumId;
        this.sectionTitle = sectionTitle;
        this.albumSectionID = albumSectionID;
        this.stickerName = stickerName;
        this.numberInSection = numberInSection;
        this.stickerID = stickerID;
        this.quantity = 0;
    }


    public Long getCollectionUserID() {
        return collectionUserID;
    }

    public void setCollectionUserID(Long collectionUserID) {
        this.collectionUserID = collectionUserID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getStickerID() {
        return stickerID;
    }

    public void setStickerID(Long stickerID) {
        this.stickerID = stickerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public Long getAlbumSectionID() {
        return albumSectionID;
    }

    public void setAlbumSectionID(Long albumSectionID) {
        this.albumSectionID = albumSectionID;
    }

    public String getStickerName() {
        return stickerName;
    }

    public void setStickerName(String stickerName) {
        this.stickerName = stickerName;
    }

    public int getNumberInSection() {
        return numberInSection;
    }

    public void setNumberInSection(int numberInSection) {
        this.numberInSection = numberInSection;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }
}


