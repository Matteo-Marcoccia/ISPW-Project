package com.questtable.model;

public final class RegolaPuntiFedelta {
    private static final int PUNTI_PER_EURO = 10;

    private RegolaPuntiFedelta() {
    }

    public static int calcolaPuntiPer(float importo) {
        return Math.round(importo * PUNTI_PER_EURO);
    }
}
