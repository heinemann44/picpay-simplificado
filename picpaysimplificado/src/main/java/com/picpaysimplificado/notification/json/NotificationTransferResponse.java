package com.picpaysimplificado.notification.json;

public record NotificationTransferResponse(
    boolean message
) {

    public boolean isNotified() {
        return message;
      }
}
