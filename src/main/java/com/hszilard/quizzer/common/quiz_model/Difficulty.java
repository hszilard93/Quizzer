package main.java.com.hszilard.quizzer.common.quiz_model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.HashMap;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * Difficulty is a property of a question.
 */
public class Difficulty {
    public static final Difficulty EASY = new Difficulty(6);
    public static final Difficulty DEFAULT = new Difficulty(8);
    public static final Difficulty HARD = new Difficulty(10);

    private static final HashMap<Integer, Difficulty> difficulties = new HashMap<>();

    private final int value;

    static {
        difficulties.put(EASY.value, EASY);
        difficulties.put(DEFAULT.value, DEFAULT);
        difficulties.put(HARD.value, HARD);
    }

    private Difficulty(int value) {
        this.value = value;
    }

    /* Static factory method (see Effective Java, Chapter 1).
     * Only one Difficulty of the same value exists. */
    public static Difficulty difficulty(int value) {
        if (value <= 0)
            throw new IllegalArgumentException("Illegal value: " + value);

        if (difficulties.containsKey(value))
            return difficulties.get(value);
        else {
            Difficulty newDifficulty = new Difficulty(value);
            difficulties.put(value, newDifficulty);
            return newDifficulty;
        }
    }

    public int value() {
        return value;
    }
}
