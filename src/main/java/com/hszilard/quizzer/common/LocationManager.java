package main.java.com.hszilard.quizzer.common;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * This class stores the location of the last quiz opened.
 */
public class LocationManager {
    private static final Logger LOGGER = Logger.getLogger(LocationManager.class.getName());
    private static final String PREF_KEY = "location";

    private static Preferences preferences;
    private static String lastPath;

    public static String getLastPath(Class caller) {
        if (lastPath == null)
            if (caller != null)
                preferences = Preferences.userNodeForPackage(caller);
            else {
                LOGGER.log(Level.SEVERE, "caller was null");
                return null;
            }
            lastPath = preferences.get(PREF_KEY, null);
        return lastPath;
    }

    public static void setLastPath(Class caller, String path) {
        lastPath = path;
        if (caller != null) {
            preferences = Preferences.userNodeForPackage(caller);
            preferences.put(PREF_KEY, lastPath);
        }
        else
            LOGGER.log(Level.SEVERE, "caller was null");
    }

}
