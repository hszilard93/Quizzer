package main.java.com.hszilard.quizzer.common.quiz_model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * Difficulty is a property of a question.
 */
public class Difficulty {
    public static final Difficulty EASY = new Difficulty(6);
    public static final Difficulty DEFAULT = new Difficulty(8);
    public static final Difficulty HARD = new Difficulty(10);

    final private IntegerProperty value = new SimpleIntegerProperty();

    public Difficulty(int value) {
        setValue(value);
    }

    public int getValue() {
        return value.get();
    }

    public void setValue(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Illegal value: " + value);
        }
        this.value.setValue(value);
    }

    public IntegerProperty valueProperty() {
        return value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value.get());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;
        /* ! JavaFX properties don't have their equals method overridden ! */
        return this.valueProperty().get() == ((Difficulty) other).valueProperty().get();
    }

}
