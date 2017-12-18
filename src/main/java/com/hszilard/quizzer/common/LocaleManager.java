package main.java.com.hszilard.quizzer.common;

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
    private static final String PREF_KEY = "locale";
    /* When a new language is added to the strings resource bundle, this list must be updated! */
    private static final List<String> supportedLocales = new ArrayList<>(Arrays.asList("en", "hu"));

    private static Preferences preferences = Preferences.userNodeForPackage(LocaleManager.class);
    private static Locale preferredLocale;

    public static Locale getPreferredLocale() {
        if (preferredLocale == null) {
            String systemLang = System.getProperty("user.language");
            String defaultLang = supportedLocales.contains(systemLang) ? systemLang : "en";
            preferredLocale = new Locale(preferences.get(PREF_KEY, defaultLang));
        }
        return preferredLocale;
    }

    public static void setPreferredLocale(Locale locale) {
        if (supportedLocales.contains(locale.getLanguage())) {
            preferredLocale = locale;
            preferences.put(PREF_KEY, locale.getLanguage());
        }
    }
}
