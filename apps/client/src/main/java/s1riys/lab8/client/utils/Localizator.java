package s1riys.lab8.client.utils;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.ResourceBundle;

public class Localizator {
    private ResourceBundle bundle;

    public Localizator(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String getKeyString(String key) {
        return bundle.getString(key);
    }

    public String getDate(Date date) {
        if (date == null) return "null";
        LocalDate localDate = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(bundle.getLocale());
        return localDate.format(formatter);
    }

    public String getNumber(Double number) {
        if (number == null) return "";
        NumberFormat formatter = NumberFormat.getInstance(bundle.getLocale());
        return formatter.format(number);
    }
}
