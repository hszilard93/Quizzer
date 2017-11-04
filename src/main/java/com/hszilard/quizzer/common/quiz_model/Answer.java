package main.java.com.hszilard.quizzer.common.quiz_model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * Answer object. A list of answers belong to a Question object.
 * Its properties are the answer's text and whether it is correct.
 */
public class Answer {

    private StringProperty answerText;
    private BooleanProperty correct;

    /**
     * @param answerText the text of the answer
     * @param correct true if this is a correct answer
     */
    public Answer(String answerText, boolean correct) {
        this.answerText = new SimpleStringProperty(answerText);
        this.correct = new SimpleBooleanProperty(correct);
    }

    public String getAnswerText() {
        return answerText.get();
    }

    public StringProperty answerTextProperty() {
        return answerText;
    }

    public boolean isCorrect() {
        return correct.get();
    }

    public BooleanProperty correctProperty() {
        return correct;
    }

}
