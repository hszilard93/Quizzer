package main.java.com.hszilard.quizzer.quizzer;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import main.java.com.hszilard.quizzer.common.quiz_model.Answer;
import main.java.com.hszilard.quizzer.common.quiz_model.Question;

import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static main.java.com.hszilard.quizzer.common.CommonUtils.addStyle;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * This window shows the question and its possible answers, along with buttons.
 */
public class QuestionSceneController {
    private static final Logger LOGGER = Logger.getLogger(QuestionSceneController.class.getName());
    private static final String STYLE_PATH = "/main/resources/com/hszilard/quizzer/quizzer/style/question_scene_styles.css";

    @FXML private ResourceBundle resources;
    @FXML private BorderPane root;
    @FXML private VBox topVBox;
    @FXML private Label questionTextLabel;
    @FXML private GridPane answersGrid;

    private Question question;
    private Callback callback;

    public interface Callback {
        void onCorrect();
        void onIncorrect();
    }

    void setup(final Question question, final Callback callback) {
        this.question = question;
        this.callback = callback;

        questionTextLabel.setText(question.getQuestionText());
        configureAnswersGrid(question.getAnswers());
        LOGGER.log(Level.FINE, "Scene ready.");
    }

    private void configureAnswersGrid(List<Answer> answers) {
        int answerSize = answers.size();
        for (int i = 0; i < answerSize; i++) {
            Answer answer = answers.get(i);

            int rowIndex = i / 2;
            int colIndex = i % 2 == 0 ? 0 : 1;

            Label answerLabel = new Label(answers.get(i).getAnswerText());
            addStyleClass(answerLabel, "answer-label");

            VBox cellVBox = new VBox(answerLabel);
            addStyleClass(cellVBox, "cell-vbox");
            cellVBox.setOnMouseClicked(e -> {
                LOGGER.log(Level.INFO, "Answer selected.");
                if (answer.isCorrect()) {
                    LOGGER.log(Level.FINE, "The answer was correct.");
                    onCorrectAnswer();
                }
                else {
                    LOGGER.log(Level.FINE, "The answer was incorrect.");
                    onIncorrectAnswer();
                }
            });

            answersGrid.add(cellVBox, colIndex, rowIndex);
            GridPane.setHgrow(cellVBox, Priority.ALWAYS);
        }
    }

    private void onCorrectAnswer() {
        Label correctAnswerLabel = new Label(resources.getString("question_correct-label"));
        addStyleClass(correctAnswerLabel, "correct-label");
        topVBox.getChildren().clear();
        topVBox.getChildren().add(correctAnswerLabel);

        VBox centerVBox = new VBox();
        addStyleClass(centerVBox, "center-vbox");
        Label answerLabel = new Label(question.getCorrectAnswer().getAnswerText());
        answerLabel.prefWidthProperty().bind(centerVBox.widthProperty());
        addStyleClass(answerLabel, "correct-answer-label");
        centerVBox.getChildren().add(answerLabel);
        root.setCenter(centerVBox);

        Button correctOkButton = new Button(resources.getString("question_positive-button"));
        correctOkButton.setDefaultButton(true);
        addStyleClass(correctOkButton, "correct-ok-button");
        correctOkButton.setOnAction(e -> {
            LOGGER.log(Level.INFO, "correctOkButton clicked");
            ((Stage)correctOkButton.getScene().getWindow()).close();
            callback.onCorrect();
        });

        HBox bottomHBox = new HBox(correctOkButton);
        addStyleClass(bottomHBox, "bottom-hbox");
        root.setBottom(bottomHBox);
    }

    private void onIncorrectAnswer() {
        Label inCorrectAnswerLabel = new Label(resources.getString("question_incorrect-label"));
        addStyleClass(inCorrectAnswerLabel, "incorrect-label");
        topVBox.getChildren().clear();
        topVBox.getChildren().add(inCorrectAnswerLabel);

        VBox centerVBox = new VBox();
        addStyleClass(centerVBox, "center-vbox");
        Label correctAnswerIsLabel1 = new Label(resources.getString("question_correct-is"));
        addStyleClass(correctAnswerIsLabel1, "correct-is-label1");
        Label correctAnswerIsLabel2 = new Label(question.getCorrectAnswer().getAnswerText());
        correctAnswerIsLabel2.prefWidthProperty().bind(centerVBox.widthProperty());
        addStyleClass(correctAnswerIsLabel2, "correct-is-label2");
        centerVBox.getChildren().addAll(correctAnswerIsLabel1, correctAnswerIsLabel2);
        root.setCenter(centerVBox);

        Button incorrectOkButton = new Button(resources.getString("question_negative-button"));
        incorrectOkButton.setDefaultButton(true);
        addStyleClass(incorrectOkButton, "incorrect-ok-button");
        incorrectOkButton.setOnAction(e -> {
            LOGGER.log(Level.INFO, "correctOkButton clicked");
            ((Stage)incorrectOkButton.getScene().getWindow()).close();
            callback.onIncorrect();
        });

        HBox bottomHBox = new HBox(incorrectOkButton);
        bottomHBox.getStyleClass().add(STYLE_PATH);
        addStyleClass(bottomHBox, "bottom-hbox");
        root.setBottom(bottomHBox);
    }

    private void addStyleClass(Parent node, String style) {
        addStyle(node, STYLE_PATH, style);
    }
}
