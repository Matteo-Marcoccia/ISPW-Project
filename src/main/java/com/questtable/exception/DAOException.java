package com.questtable.exception;

import java.io.Serial;

public class DAOException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public DAOException(String messaggio, Throwable causa) {
        super(messaggio, causa);
    }
}
