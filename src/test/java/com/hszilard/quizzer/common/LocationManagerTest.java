package test.java.com.hszilard.quizzer.common;

import main.java.com.hszilard.quizzer.common.LocationManager;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Testing LocationManager")
public class LocationManagerTest {

    @BeforeEach
    void setupEach() throws BackingStoreException, NoSuchFieldException, IllegalAccessException {
        Preferences.userNodeForPackage(this.getClass()).clear();
        /* lastPath should always be uninitialized for these tests */
        Field field = LocationManager.class.getDeclaredField("lastPath");
        field.setAccessible(true);
        field.set(null, null);
    }

    @Test
    void getLastPathReturnsPathWhenSet() {
        final String testPath = "C://Random Folder//Other Folder";
        LocationManager.setLastPath(this.getClass(), testPath);

        assertThat(LocationManager.getLastPath(this.getClass()), equalTo(testPath));
    }

    @Test
    void getLastPathReturnsLastPathWhenMultipleSet() {
        final String testPath1 = "C://Random Folder//Other Folder";
        final String testPath2 = "C://Random Folder//Other Folder 2";
        LocationManager.setLastPath(this.getClass(), testPath1);
        LocationManager.setLastPath(this.getClass(), testPath2);

        assertThat(LocationManager.getLastPath(this.getClass()), equalTo(testPath2));
    }

    @Test
    void getLastPathReturnsNullWhenPathNotSet() throws BackingStoreException {
        assertThat(LocationManager.getLastPath(this.getClass()), equalTo(null));
    }

}
