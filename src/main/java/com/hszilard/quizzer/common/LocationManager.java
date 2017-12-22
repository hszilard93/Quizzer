package main.java.com.hszilard.quizzer.common;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.logging.Logger;
import java.util.prefs.Preferences;

import static java.util.logging.Level.FINE;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * This class stores the location of the last quiz opened or saved, but for each class!
 * Should be called from classes that handle IO for their applications.
 */
public class LocationManager {
    private static final Logger LOGGER = Logger.getLogger(LocationManager.class.getName());
    private static final String PREF_KEY = "location";

    private static Preferences preferences;
    private static String lastPath;

    @Nullable
    public static String getLastPath(@NotNull Class caller) {
        if (lastPath == null) {
            preferences = Preferences.userNodeForPackage(caller);
            lastPath = preferences.get(PREF_KEY, null);
            LOGGER.log(FINE, "Preferences loaded.");
        }
        LOGGER.log(FINE, "lastPath is: " + lastPath);
        return lastPath;
    }

    public static void setLastPath(@NotNull Class caller, String path) {
        lastPath = path;
        preferences = Preferences.userNodeForPackage(caller);
        preferences.put(PREF_KEY, lastPath);
        LOGGER.log(FINE, "lastPath set to: " + lastPath);
    }

}
