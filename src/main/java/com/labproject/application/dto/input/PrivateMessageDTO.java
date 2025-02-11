package com.labproject.application.dto.input;

import jakarta.validation.constraints.NotBlank;

public class PrivateMessageDTO {

    @NotBlank
    private String body;

    private String receiverEmail;


    public PrivateMessageDTO() {
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }
}
