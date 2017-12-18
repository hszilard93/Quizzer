package main.java.com.hszilard.quizzer.quizzer.game_model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import main.java.com.hszilard.quizzer.common.quiz_model.Quiz;
import main.java.com.hszilard.quizzer.quizzer.QuestionSceneController;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.*;

public class TurnsManager {
    private static final Logger LOGGER = Logger.getLogger(QuestionSceneController.class.getName());

    private Quiz quiz;
    private TeamsManager teamsManager;

    private IntegerProperty totalTurns = new SimpleIntegerProperty();
    private IntegerProperty currentTurn = new SimpleIntegerProperty(1);
    private IntegerProperty questionsPerTurn = new SimpleIntegerProperty();
    private IntegerProperty questionCounter = new SimpleIntegerProperty(0);
    private BooleanProperty gameOver = new SimpleBooleanProperty();

    public TurnsManager(Quiz quiz, TeamsManager teamsManager) {
        this.quiz = quiz;
        this.teamsManager = teamsManager;

        totalTurns.bind(Bindings.createIntegerBinding(() -> {
            if (teamsManager.teamsProperty().size() > 0)
                return quiz.getQuestions().size() / teamsManager.teamsProperty().size();
            return 0;
        }, quiz.getQuestions(), teamsManager.teamsProperty()));

        questionsPerTurn.bind(Bindings.createIntegerBinding(() -> {
            if (totalTurns.get() > 0)
                return (quiz.getQuestions().size() - quiz.getQuestions().size() % teamsManager.teamsProperty().size()) / totalTurns.get();
            else
                return 0;
        }, quiz.getQuestions(), totalTurns));

        currentTurn.bind(Bindings.createIntegerBinding(() -> {
            if (questionsPerTurn.get() > 0)
                return questionCounter.get() / questionsPerTurn.get() + 1;
            else
                return 0;
        }, questionsPerTurn, questionCounter));

        gameOver.bind(Bindings.createBooleanBinding(() -> {
            if (totalTurns.greaterThan(0).get())
                return currentTurn.greaterThan(totalTurnsProperty()).get();
            else
                return false;
        }, currentTurn, totalTurns));

        totalTurns.addListener(((observable, oldValue, newValue) ->
                LOGGER.log(Level.FINE, "Total turns: " + newValue)));
        questionsPerTurn.addListener(((observable, oldValue, newValue) ->
                LOGGER.log(FINE, "Questions per turn: " + newValue)));
        currentTurn.addListener(((observable, oldValue, newValue) ->
                LOGGER.log(FINE, "Current turn: " + newValue)));
        questionCounter.addListener((observable, oldValue, newValue) ->
                LOGGER.log(FINE, "Question count: " + newValue));
        gameOver.addListener((observable, oldValue, newValue) ->
                LOGGER.log(INFO, "gameOver: " + newValue));
    }

    public void nextTurn() {
        questionCounter.set(questionCounter.get() + 1);
    }

    public void reset() {
        questionCounter.set(0);
    }

    public int getTotalTurns() {
        return totalTurns.get();
    }

    public IntegerProperty totalTurnsProperty() {
        return totalTurns;
    }

    public int getCurrentTurn() {
        return currentTurn.get();
    }

    public IntegerProperty currentTurnProperty() {
        return currentTurn;
    }

    public boolean isGameOver() {
        return gameOver.get();
    }

    public BooleanProperty gameOverProperty() {
        return gameOver;
    }
}
