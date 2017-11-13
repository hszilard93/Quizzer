package main.java.com.hszilard.quizzer.common.quiz_model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Objects;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * Question object. A list of questions belong to a Quiz object.
 * Its properties are the question's text and a list of Answer objects.
 */
public class Question {

    private StringProperty questionText;
    private ListProperty<Answer> answers;

    /**
     * Default constructor for Question, creates a Question with an empty text and
     * initializes the list of possible answers.
     */
    public Question () {
        this("");
    }

    /**
     * Creates a question with text and initialized list of answers.
     * @param questionText
     */
    public Question(String questionText) {
        this.questionText = new SimpleStringProperty(questionText);
        this.answers = new SimpleListProperty<>(FXCollections.synchronizedObservableList(FXCollections.observableArrayList()));
    }

    public String getQuestionText() {
        return questionText.get();
    }

    public void setQuestionText(String questionText) {
        this.questionText.setValue(questionText);
    }

    public StringProperty questionTextProperty() {
        return questionText;
    }

    public ObservableList<Answer> getAnswers() {
        return answers.get();
    }

    public ListProperty<Answer> answersProperty() {
        return answers;
    }

    @Override
    public int hashCode() {
        long answersHash = 0;
        for (Answer a : answers) {
            answersHash += a.hashCode();
        }
        return Objects.hash(questionText, answersHash);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;

        return this.questionText.get().equals(((Question)other).questionText.get())
                && this.answers.equals(((Question)other).answers);
    }
}
