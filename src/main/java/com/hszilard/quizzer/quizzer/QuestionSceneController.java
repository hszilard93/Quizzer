package main.java.com.hszilard.quizzer.quizzer;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import main.java.com.hszilard.quizzer.common.quiz_model.Answer;
import main.java.com.hszilard.quizzer.common.quiz_model.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class QuestionSceneController {
    private static final Logger LOGGER = Logger.getLogger(QuestionSceneController.class.getName());
    private static String STYLE_PATH = "/main/resources/com/hszilard/quizzer/quizzer/style/question_scene_styles.css";

    @FXML private ResourceBundle resources;
    @FXML private BorderPane root;
    @FXML private Label questionTextLabel;
    @FXML private GridPane answersGrid;

    private Question question;
    private Callback callback;
    private Map<Label, Answer> answerLabelMap = new HashMap<>();

    public interface Callback {
        void onCorrect();
        void onIncorrect();
    }

    void setup(final Question question, final Callback callback) {
        this.question = question;
        this.callback = callback;

        questionTextLabel.setText(question.getQuestionText());
        configureAnswersGrid(question.getAnswers());
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
                if (answer.isCorrect()) {
                    onCorrectAnswer();
                }
                else {
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
        root.setTop(correctAnswerLabel);

        Button correctOkButton = new Button(resources.getString("question_positive-button"));
        addStyleClass(correctOkButton, "correct-ok-button");
        correctOkButton.setOnAction(e -> {
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
        root.setTop(inCorrectAnswerLabel);

        Label correctAnswerIsLabel1 = new Label(resources.getString("question_correct-is"));
        addStyleClass(correctAnswerIsLabel1, "correct-is-label1");
        Label correctAnswerIsLabel2 = new Label(question.getCorrectAnswer().getAnswerText());
        addStyleClass(correctAnswerIsLabel2, "correct-is-label2");
        VBox centerVBox = new VBox(correctAnswerIsLabel1, correctAnswerIsLabel2);
        addStyleClass(centerVBox, "center-vbox");
        root.setCenter(centerVBox);

        Button incorrectOkButton = new Button(resources.getString("question_negative-button"));
        addStyleClass(incorrectOkButton, "incorrect-ok-button");
        incorrectOkButton.setOnAction(e -> {
            ((Stage)incorrectOkButton.getScene().getWindow()).close();
            callback.onIncorrect();
        });

        HBox bottomHBox = new HBox(incorrectOkButton);
        bottomHBox.getStyleClass().add(STYLE_PATH);
        addStyleClass(bottomHBox, "bottom-hbox");
        root.setBottom(bottomHBox);
    }

    private void addStyleClass(Parent node, String style) {
        node.getStylesheets().add(STYLE_PATH);
        node.getStyleClass().add(style);
    }
}
