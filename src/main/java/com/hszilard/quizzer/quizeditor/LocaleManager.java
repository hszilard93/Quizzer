package main.java.com.hszilard.quizzer.quizeditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * This class manages the locale settings for the supported languages.
 */
public class LocaleManager {
    private static final String LOCALE_KEY = "locale";
    private static final String DEFAULT_LOCALE_CODE = "en";
    /* When a new language is added to the strings resource bundle, this list must be updated! */
    private static final List<String> supportedLocales = new ArrayList<>(Arrays.asList("en", "hu"));

    private static Preferences preferences = Preferences.userNodeForPackage(LocaleManager.class);
    private static Locale preferredLocale;

    static Locale getPreferredLocale() {
        if (preferredLocale == null) {
            preferredLocale = new Locale(preferences.get(LOCALE_KEY, DEFAULT_LOCALE_CODE));
        }
        return preferredLocale;
    }

    static void setPreferredLocale(Locale locale) {
        if (supportedLocales.contains(locale.getLanguage())) {
            preferredLocale = locale;
            preferences.put(LOCALE_KEY, locale.getLanguage());
        }
    }
}
