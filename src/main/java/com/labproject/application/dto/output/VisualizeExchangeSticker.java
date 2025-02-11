package com.labproject.application.dto.output;

import com.labproject.domain.Sticker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VisualizeExchangeSticker {

    private long exchangeId;
    private String proposerName;
    private LocalDateTime requestCreationTime;
    private List<VisualizeStickerDTO> stickersOfOwner, stickersOfProposer;

    public VisualizeExchangeSticker(Long exchangeId, List<Sticker> stickersFromOwner, List<Sticker> stickersFromProposer, String proposerName, LocalDateTime requestCreationTime) {
        this.exchangeId = exchangeId;
        this.proposerName = proposerName;
        this.requestCreationTime = requestCreationTime;
        stickersOfOwner = new ArrayList<>();
        stickersOfProposer = new ArrayList<>();
        for (Sticker sticker : stickersFromOwner) {
            this.stickersOfOwner.add(new VisualizeStickerDTO(sticker));
        }
        for (Sticker sticker : stickersFromProposer) {
            this.stickersOfProposer.add(new VisualizeStickerDTO(sticker));
        }
    }

    public long getExchangeId() {
        return exchangeId;
    }

    public String getProposerName() {
        return proposerName;
    }

    public void setProposerName(String proposerName) {
        this.proposerName = proposerName;
    }

    public LocalDateTime getRequestCreationTime() {
        return requestCreationTime;
    }

    public void setRequestCreationTime(LocalDateTime requestCreationTime) {
        this.requestCreationTime = requestCreationTime;
    }

    public List<VisualizeStickerDTO> getStickersOfOwner() {
        return stickersOfOwner;
    }

    public void setStickersOfOwner(List<VisualizeStickerDTO> stickersOfOwner) {
        this.stickersOfOwner = stickersOfOwner;
    }

    public List<VisualizeStickerDTO> getStickersOfProposer() {
        return stickersOfProposer;
    }

    public void setStickersOfProposer(List<VisualizeStickerDTO> stickersOfProposer) {
        this.stickersOfProposer = stickersOfProposer;
    }
}
