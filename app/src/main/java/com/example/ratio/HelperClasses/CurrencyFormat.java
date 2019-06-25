package com.example.ratio.HelperClasses;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormat {
    private Locale locale = new Locale("en", "PH");
    private NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

    public String toPhp(double source) {
        return numberFormat.format(source);
    }
}
