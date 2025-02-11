package com.labproject.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class SaleSticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long saleStickerID;

    @OneToOne
    @JoinColumn(name = "collection_sticker_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CollectionSticker collectionSticker;

    @Column(nullable = false)
    private boolean sold;

    @ManyToOne
    @JoinColumn(name = "seller_collection_userid", nullable = false)
    private CollectionUser seller;

    private long price;

    public SaleSticker() {
    }

    public SaleSticker(CollectionUser seller, long price, CollectionSticker collectionSticker) {
        this.seller = seller;
        this.price = price;
        this.collectionSticker = collectionSticker;
    }


    public Long getSaleStickerID() {
        return saleStickerID;
    }

    public void setSaleStickerID(Long saleStickerID) {
        this.saleStickerID = saleStickerID;
    }

    public CollectionSticker getCollectionSticker() {
        return collectionSticker;
    }

    public void setCollectionSticker(CollectionSticker collectionSticker) {
        this.collectionSticker = collectionSticker;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }


    public CollectionUser getSeller() {
        return seller;
    }

    public void setSeller(CollectionUser seller) {
        this.seller = seller;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }
}
