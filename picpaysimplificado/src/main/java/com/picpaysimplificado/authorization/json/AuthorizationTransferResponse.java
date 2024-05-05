package com.picpaysimplificado.authorization.json;

public record AuthorizationTransferResponse(
    String message
) {

    public boolean isAuthorized() {
        return message.equals("Autorizado");
    }
}
