package com.lapin.common.client.gui;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Localization {
    private ResourceBundle resourceBundle;
    private Locales locale;
    private final static Localization LOCALIZATION = new Localization();
    private Localization(){
        try {
            Locale currentLocale = Locale.getDefault();
            resourceBundle = ResourceBundle.getBundle("bundles.lang");
            locale = defineLocales(currentLocale);
        }catch (MissingResourceException e) {
            resourceBundle = ResourceBundle.getBundle("bundles.lang", Locales.EN_US_LOCALE.getLocale());
            locale = Locales.EN_US_LOCALE;
        }
    }
    public static Localization getInstance() {
        return LOCALIZATION;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public Locales getLocales() {
        return locale;
    }

    public void setResourceBundle(Locales locale) {
        this.locale = locale;
        resourceBundle = ResourceBundle.getBundle("bundles.lang", locale.getLocale());
    }
    public Locales defineLocales(Locale locale) {
        for (Locales locales : Locales.values()) {
            if (locales.getLocale().equals(locale)) {
                return locales;
            }
        }
        return null;
    }

}
