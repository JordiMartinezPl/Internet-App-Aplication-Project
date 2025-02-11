package com.labproject.application.dto.output;

import java.time.LocalDate;
import java.util.List;

public class VisualizeCollectionAlbumDTO {

    private long collectionAlbumId;

    private String userName;

    private String title;

    private String description;

    private LocalDate beginDate;
    private LocalDate endingDate;

    private List<VisualizeCollectionStickerDTO> collectedStickers;

    public VisualizeCollectionAlbumDTO() {
    }

    public VisualizeCollectionAlbumDTO(long collectionAlbumId, String userName, String title, String description, LocalDate beginDate, LocalDate endingDate, List<VisualizeCollectionStickerDTO> collectedStickers) {
        this.collectionAlbumId = collectionAlbumId;
        this.userName = userName;
        this.title = title;
        this.description = description;
        this.beginDate = beginDate;
        this.endingDate = endingDate;
        this.collectedStickers = collectedStickers;
    }

    public List<VisualizeCollectionStickerDTO> getCollectedStickers() {
        return collectedStickers;
    }

    public void setCollectedStickers(List<VisualizeCollectionStickerDTO> collectedStickers) {
        this.collectedStickers = collectedStickers;
    }

    public String getOwner() {
        return userName;
    }

    public void setOwner(String owner) {
        this.userName = owner;
    }

    public long getCollectionAlbumId() {
        return collectionAlbumId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getEndingDate() {
        return endingDate;
    }
}
