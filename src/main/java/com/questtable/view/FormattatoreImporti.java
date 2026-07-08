package com.questtable.view;

import java.util.Locale;

public final class FormattatoreImporti {
    private FormattatoreImporti() {
    }

    public static String formattaImporto(float importo) {
        return String.format(Locale.ITALY, "%.2f €", importo);
    }

    public static String formattaQuotaAPersona(float importo) {
        return formattaImporto(importo) + " a persona";
    }
}
