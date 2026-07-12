package com.questtable.exception;

import java.io.Serial;

public class PaymentException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = 1L;

    public PaymentException(String messaggio) {
        super(messaggio);
    }
}
