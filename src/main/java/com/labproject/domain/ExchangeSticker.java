package com.labproject.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ExchangeSticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "sticker_interested_id",
            joinColumns = @JoinColumn(name = "exchange_sticker_ID"),
            inverseJoinColumns = @JoinColumn(name = "stickers_from_owner"),
            uniqueConstraints = {}
    )
    @OnDelete(action = OnDeleteAction.CASCADE)

    private List<Sticker> interestingSticker;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private CollectionUser owner;

    @ManyToOne
    @JoinColumn(name = "interested_id", nullable = false)
    private CollectionUser interested;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "sticker_exchanging_to_owner",
            joinColumns = @JoinColumn(name = "exchange_sticker_ID"),
            inverseJoinColumns = @JoinColumn(name = "stickers_from_interested")
    )

    private List<Sticker> stickersForOwner;


    @Enumerated(EnumType.STRING)
    private ExchangeStatus status;

    private LocalDateTime exchangeDate;

    private LocalDateTime proposalInitiationDate;


    public ExchangeSticker() {
    }

    public ExchangeSticker(List<Sticker> interestingStickers, CollectionUser owner, CollectionUser interested) {
        this.interestingSticker = interestingStickers;
        this.owner = owner;
        this.interested = interested;
        status = ExchangeStatus.PENDING;
    }

    public ExchangeSticker(Sticker interestedSticker, CollectionUser owner, CollectionUser interested) {
        this.interestingSticker = new ArrayList<>();
        interestingSticker.add(interestedSticker);
        this.owner = owner;
        this.interested = interested;
        status = ExchangeStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sticker getInterestingSticker() {
        return interestingSticker.getFirst();
    }

    public void setInterestingSticker(List<Sticker> interestingSticker) {
        this.interestingSticker = interestingSticker;
    }

    public List<Sticker> getInterestingStickerList() {
        return interestingSticker;
    }

    public CollectionUser getOwner() {
        return owner;
    }

    public void setOwner(CollectionUser owner) {
        this.owner = owner;
    }

    public CollectionUser getInterested() {
        return interested;
    }

    public void setInterested(CollectionUser interested) {
        this.interested = interested;
    }

    public ExchangeStatus getStatus() {
        return status;
    }

    public void setStatus(ExchangeStatus status) {
        this.status = status;
    }

    public LocalDateTime getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(LocalDateTime exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public List<Sticker> getStickerListForOwner() {
        return stickersForOwner;
    }

    public void setStickerForOwner(List<Sticker> sticker) {
        this.stickersForOwner = sticker;
    }

    public void setStickerForOwner(Sticker ownerNewSticker) {
        this.stickersForOwner.add(ownerNewSticker);
    }

    public LocalDateTime getProposalInitiationDate() {
        return proposalInitiationDate;
    }

    public void setProposalInitiationDate(LocalDateTime proposalInitiationDate) {
        this.proposalInitiationDate = proposalInitiationDate;
    }
}