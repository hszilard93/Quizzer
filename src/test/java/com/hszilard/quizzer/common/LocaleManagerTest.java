package test.java.com.hszilard.quizzer.common;

import main.java.com.hszilard.quizzer.common.LocaleManager;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 */
@DisplayName("Testing LocaleManager")
public class LocaleManagerTest {
    private static final String LOCALE_KEY = "locale";

    private static String oldUserLanguage;
    private static String oldPreferredLocale;

    @BeforeAll
    static void setupAll() {
        oldUserLanguage = System.getProperty("user.language");
        oldPreferredLocale = Preferences.userNodeForPackage(LocaleManager.class).get(LOCALE_KEY, "en");
    }

    @AfterAll
    static void cleanupAll() {
        System.setProperty("user.language", oldUserLanguage);
        Preferences.userNodeForPackage(LocaleManager.class).put(LOCALE_KEY, oldPreferredLocale);
    }

    @BeforeEach
    void setupEach() throws BackingStoreException, NoSuchFieldException, IllegalAccessException {
        Preferences.userNodeForPackage(LocaleManager.class).clear();
        /* preferredLocale should always be uninitialized for these tests */
        Field field = LocaleManager.class.getDeclaredField("preferredLocale");
        field.setAccessible(true);
        field.set(null, null);
    }

    @Test
    void getPreferredLocaleReturnsSystemLanguageAsDefaultWhenSupported() {
        /* Arrange that the user.language property is a supported language not "en" */
        System.setProperty("user.language", "hu");

        assertThat(LocaleManager.getPreferredLocale().getLanguage(),
                equalTo(new Locale("hu").getLanguage()));
    }

    @Test
    void getPreferredLocaleReturnsEnAsDefaultWhenSystemLanguageNotSupported() {
        /* Arrange that the user.language property is a non-supported language */
        System.setProperty("user.language", "mk");

        assertThat(LocaleManager.getPreferredLocale().getLanguage(),
                equalTo(new Locale("en").getLanguage()));
    }

    @Test
    void getPreferredLocaleReturnsUserPreferenceWhenSet() {
        Preferences preferences = Preferences.userNodeForPackage(LocaleManager.class);
        preferences.put(LOCALE_KEY, "hu");
        System.setProperty("user.language", "en");

        assertThat(LocaleManager.getPreferredLocale().getLanguage(),
                equalTo(new Locale("hu").getLanguage()));
    }

    @Test
    void setPreferredLocaleWorksCorrectly() {
        Preferences preferences = Preferences.userNodeForPackage(LocaleManager.class);
        preferences.put(LOCALE_KEY, "en");

        LocaleManager.setPreferredLocale(new Locale("hu"));

        assertThat(preferences.get(LOCALE_KEY, "en"), equalTo(new Locale("hu").getLanguage()));
    }

}
