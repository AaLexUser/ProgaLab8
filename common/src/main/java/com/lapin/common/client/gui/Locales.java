package com.lapin.common.client.gui;

import java.util.Locale;

public enum Locales {
    EN_US_LOCALE(new Locale("en", "US"), "English"),
    RU_RU_LOCALE(new Locale("ru", "RU"), "Русский"),
    SK_SK_LOCALE(new Locale("sk", "SK"), "Slovenský"),
    SQ_SQ_LOCALE(new Locale("sq", "SQ"), "Shqiptare"),
    ES_MX_LOCALE(new Locale("es", "MX"), "Español");


    private final Locale locale;
    private final String representation;

    Locales(Locale locale, String representation) {
        this.locale = locale;
        this.representation = representation;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getRepresentation() {
        return representation;
    }
}
