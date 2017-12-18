package main.java.com.hszilard.quizzer.quizzer.game_model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import main.java.com.hszilard.quizzer.common.quiz_model.Quiz;
import main.java.com.hszilard.quizzer.quizzer.QuestionSceneController;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TurnsManager {
    private static final Logger LOGGER = Logger.getLogger(QuestionSceneController.class.getName());

    private Quiz quiz;
    private TeamsManager teamsManager;

    private IntegerProperty totalTurns = new SimpleIntegerProperty();
    private IntegerProperty currentTurn = new SimpleIntegerProperty(1);
    private IntegerProperty questionsPerTurn = new SimpleIntegerProperty();
    private IntegerProperty questionCounter = new SimpleIntegerProperty(1);

    public TurnsManager(Quiz quiz, TeamsManager teamsManager) {
        this.quiz = quiz;
        this.teamsManager = teamsManager;

        totalTurns.bind(Bindings.createIntegerBinding(() -> {
            if (quiz != null && teamsManager != null)
                if (teamsManager.teamsProperty().size() > 0)
                    return quiz.getQuestions().size() / teamsManager.teamsProperty().size();
            return 0;
        }, quiz.getQuestions(), teamsManager.teamsProperty()));

        totalTurns.addListener(((observable, oldValue, newValue) -> {
            LOGGER.log(Level.FINE, "Total turns: " + newValue);
        }));

        questionsPerTurn.bind(Bindings.createIntegerBinding(() -> {
            if (totalTurns.get() > 0)
                return quiz.getQuestions().size() / totalTurns.get();
            else
                return 0;
        }, quiz.getQuestions(), totalTurns));

        questionsPerTurn.addListener(((observable, oldValue, newValue) -> {
            System.out.println("Questions per turn: " + newValue);
        }));

        currentTurn.bind(Bindings.createIntegerBinding(() -> {
            if (questionsPerTurn.get() > 0)
                return questionCounter.get() / questionsPerTurn.get();
            else
                return 0;
        }, questionsPerTurn, questionCounter));

        currentTurn.addListener(((observable, oldValue, newValue) -> {
            System.out.println("Current turn: " + newValue);
        }));

    }

    public void nextTurn() {
        questionCounter.add(1);
    }

    public void reset() {
        currentTurn.set(0);
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
}
