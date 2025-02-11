package com.labproject.application.dto.output;

public class VisualizeSaleStickerDTO {


    private long stickerID;

    private String stickerName;

    private String sellerEmail;

    private long price;


    public VisualizeSaleStickerDTO() {
    }

    public VisualizeSaleStickerDTO(long StickerId, String stickerName, String sellerEmail, long price) {
        this.stickerID = StickerId;
        this.stickerName = stickerName;
        this.sellerEmail = sellerEmail;
        this.price = price;
    }

    public long getStickerID() {
        return stickerID;
    }

    public void setStickerID(long stickerID) {
        this.stickerID = stickerID;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }


    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getStickerName() {
        return stickerName;
    }

    public void setStickerName(String stickerName) {
        this.stickerName = stickerName;
    }
}
