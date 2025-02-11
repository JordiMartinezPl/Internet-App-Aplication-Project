package com.labproject.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
public class CollectionAlbum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long collectionAlbumId;

    @ManyToOne
    @JoinColumn(name = "owner")
    private CollectionUser owner;

    @ManyToOne
    @JoinColumn(name = "album", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Album album;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "collection_album_id")
    private List<CollectionSticker> collectedStickers;

    private boolean publicAvailability = true;

    public CollectionAlbum() {
    }

    public CollectionAlbum(CollectionUser owner, Album album, Boolean publicAvailability) {
        this.album = album;
        this.owner = owner;
        this.publicAvailability = publicAvailability;
    }

    public long getCollectionAlbumId() {
        return collectionAlbumId;
    }

    public void setCollectionAlbumId(long collectionAlbumId) {
        this.collectionAlbumId = collectionAlbumId;
    }

    public @NotNull CollectionUser getOwner() {
        return owner;
    }

    public void setOwner(@NotNull CollectionUser owner) {
        this.owner = owner;
    }

    public @NotNull Album getAlbum() {
        return album;
    }

    public void setAlbum(@NotNull Album album) {
        this.album = album;
    }

    public List<CollectionSticker> getCollectedStickers() {
        return collectedStickers;
    }

    public void setCollectedStickers(List<CollectionSticker> collectedStickers) {
        this.collectedStickers = collectedStickers;
    }

    public void addSticker(CollectionSticker collectionSticker) {
        collectedStickers.add(collectionSticker);
    }

    public boolean getPublicAvailability() {
        return publicAvailability;
    }

    public void setPublicAvailability(boolean publicAvailability) {
        this.publicAvailability = publicAvailability;
    }

    public void removeSticker(CollectionSticker collectionSticker) {
        collectedStickers.remove(collectionSticker);
    }
}
