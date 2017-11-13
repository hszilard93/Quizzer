package main.java.com.hszilard.quizzer.common.quiz_model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * Quiz object.
 * Its properties are its title; the dates it was created and last edited on; a list of Question objects.
 */
public class Quiz {
    private StringProperty title;
    private LocalDate created;
    private LocalDate edited;
    private ListProperty<Question> questions;

    /**
     * Default constructor for Quiz, creates a Quiz with an empty title and
     * initializes the list of possible questions, created and edited fields.
     */
    public Quiz() {
        this("");
    }

    /**
     * Creates a quiz with a title and initializes the list of possible questions,
     * created and edited fields.
     *
     * @param title the title of the new quiz
     */
    public Quiz(String title) {
        this.title = new SimpleStringProperty(title);
        this.created = LocalDate.now();
        this.edited = LocalDate.now();
        this.questions = new SimpleListProperty<>(FXCollections.observableArrayList());
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public ObservableList<Question> getQuestions() {
        return questions.get();
    }

    public ListProperty<Question> questionsProperty() {
        return questions;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getEdited() {
        return edited;
    }

    public void setEdited(LocalDate edited) {
        this.edited = edited;
    }

    /**
     * Convenience method for updating the edited field to the current date.
     */
    public void updateEdited() {
        setEdited(LocalDate.now());
    }

    @Override
    public int hashCode() {
        long questionsHash = 0;
        for (Question q : questions) {
            questionsHash += q.hashCode();
        }
        return Objects.hash(title, created, edited, questionsHash);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;
        /* ! JavaFX properties don't have their equals method overridden ! */
        return this.title.get().equals(((Quiz)other).title.get())
                && this.created.equals(((Quiz)other).created)
                && this.edited.equals(((Quiz)other).edited)
                && this.questions.equals(((Quiz)other).questions);
    }

}
