package com.labproject.application.dto.output;

import com.labproject.domain.Sticker;

public class VisualizeCollectionStickerDTO {

    private long collectionStickerId;

    private Sticker sticker;

    private int quantity;

    public VisualizeCollectionStickerDTO() {
    }

    public VisualizeCollectionStickerDTO(long collectionStickerId, Sticker sticker, int quantity) {
        this.collectionStickerId = collectionStickerId;
        this.sticker = sticker;
        this.quantity = quantity;
    }

    public long getCollectionStickerId() {
        return collectionStickerId;
    }

    public void setCollectionStickerId(long collectionStickerId) {
        this.collectionStickerId = collectionStickerId;
    }

    public Sticker getSticker() {
        return sticker;
    }

    public void setSticker(Sticker sticker) {
        this.sticker = sticker;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
