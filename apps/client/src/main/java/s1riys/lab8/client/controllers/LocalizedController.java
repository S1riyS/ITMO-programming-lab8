package s1riys.lab8.client.controllers;

import s1riys.lab8.client.utils.Localizator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

abstract public class LocalizedController {
    protected Localizator localizator;
    protected final HashMap<String, Locale> localeMap = new LinkedHashMap<>() {{
        put("Русский", new Locale("ru", "RU"));
        put("Slovenski", new Locale("sl", "Sl"));
        put("Ελληνικά", new Locale("el", "GR"));
        put("Español", new Locale("es", "PA"));
    }};

    public void setLocalizator(Localizator localizator) {
        this.localizator = localizator;
    }

    abstract public void changeLanguage();
}
