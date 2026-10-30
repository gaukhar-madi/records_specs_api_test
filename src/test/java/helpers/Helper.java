package helpers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Helper {

    public static String getStars(int n) {
        switch (n) {
            case 1:
                return "★☆☆☆☆";
            case 2:
                return "★★☆☆☆";
            case 3:
                return "★★★☆☆";
            case 4:
                return "★★★★☆";
            case 5:
                return "★★★★★";
        }
        return "";
    }

    public static String getNowDate() {
        LocalDate date = LocalDate.now();
        return formatDate(date);
    }

    public static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("d MMMM yyyy 'г.'")
                .withLocale(new Locale("ru"));
        return date.format(formatter);
    }
}