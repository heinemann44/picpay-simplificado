package com.picpaysimplificado.transaction.json;

import java.math.BigDecimal;

public record TransactionRequest(
    BigDecimal value,
    Long payer,
    Long payee
) {
    public TransactionRequest {
        value = value.setScale(2);
    }
}
