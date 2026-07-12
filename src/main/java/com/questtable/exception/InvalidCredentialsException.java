package com.questtable.exception;

import java.io.Serial;

public class InvalidCredentialsException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidCredentialsException(String messaggio) {
        super(messaggio);
    }
}
