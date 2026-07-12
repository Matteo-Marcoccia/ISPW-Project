package com.questtable.exception;

import java.io.Serial;

public class PostiNonDisponibiliException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = 1L;

    public PostiNonDisponibiliException(String messaggio) {
        super(messaggio);
    }
}
