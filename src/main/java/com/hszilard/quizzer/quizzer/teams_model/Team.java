package main.java.com.hszilard.quizzer.quizzer.teams_model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;
import java.util.logging.Logger;

import static java.util.logging.Level.FINE;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 */
public class Team {
    private static final Logger LOGGER = Logger.getLogger(Team.class.getName());

    public Team() {
        this("", 0);
    }

    private final StringProperty name;
    private final IntegerProperty score;

    public Team(String name, int score) {
        this.name = new SimpleStringProperty(name);
        this.score = new SimpleIntegerProperty(score);
        LOGGER.log(FINE, "Team constructed. Name: " + this.name.get());
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        LOGGER.log(FINE, "New name being set. Old name: " + this.name + " New name: " + name);
        this.name.set(name);
    }

    public int getScore() {
        return score.get();
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void setScore(int score) {
        this.score.set(score);
        LOGGER.log(FINE, "New score set. Name: " + this.name + " Score: " + this.score);
    }

    public void addToScore(int addThis) throws IllegalArgumentException {
        if (addThis >= 0) {
            score.set(score.intValue() + addThis);
            LOGGER.log(FINE, "Score added. Name: " + this.name + " Score: " + this.score);
        }
        else
            throw new IllegalArgumentException("Team addToScore was passed a negative integer " + addThis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, score);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;

        return this.name.get().equals(((Team) other).name.get())
               && this.score.get() == ((Team) other).score.get();
    }
}

