package main.java.com.hszilard.quizzer.quizzer.teams_model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Team {

    private StringProperty name;
    private IntegerProperty score;

    public Team() {
        this("", 0);
    }

    public Team(String name, int score) {
        this.name = new SimpleStringProperty(name);
        this.score = new SimpleIntegerProperty(score);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
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
    }

    public void addToScore(int addThis) throws IllegalArgumentException {
        if (addThis >= 0) {
            score.set(score.intValue() + addThis);
        }
        else {
            throw new IllegalArgumentException("Team addToScore was passed the negative integer " + addThis);
        }
    }

    /*
    @Override
    public int hashCode() {
        return Objects.hash(name, score);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;

        return this.name.get().equals(((Team)other).name.get())
                && this.score.get() == ((Team)other).score.get();
    }
    */
}

