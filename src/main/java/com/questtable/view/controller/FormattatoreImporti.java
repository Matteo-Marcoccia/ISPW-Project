package com.questtable.view.controller;

import java.util.Locale;

final class FormattatoreImporti {
    private FormattatoreImporti() {
    }

    static String formattaImporto(float importo) {
        return String.format(Locale.ITALY, "%.2f€", importo);
    }

    static String formattaQuotaAPersona(float importo) {
        return formattaImporto(importo) + " a persona";
    }
}
