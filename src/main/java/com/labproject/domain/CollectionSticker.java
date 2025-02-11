package com.labproject.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class CollectionSticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long collectionStickerId;

    @ManyToOne
    @JoinColumn(name = "sticker", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Sticker sticker;

    @Column
    private int quantity;

    @Column(columnDefinition = "int Default 0")
    private int blockedCopies = 0;

    public CollectionSticker() {
    }

    public CollectionSticker(Sticker sticker) {
        this.sticker = sticker;
        this.quantity = 1;
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

    public void incrementQuantity() {
        this.quantity++;
    }

    public void reduceQuantity() {
        this.quantity--;
    }

    public int getBlockedCopies() {
        return blockedCopies;
    }

    public void increaseBlockedCopies() {
        this.quantity -= 1;
        this.blockedCopies += 1;
    }

    public void reduceBlockedCopies() {
        this.quantity += 1;
        this.blockedCopies -= 1;


    }

}
