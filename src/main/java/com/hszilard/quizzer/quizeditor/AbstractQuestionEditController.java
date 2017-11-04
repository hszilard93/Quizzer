package main.java.com.hszilard.quizzer.quizeditor;

import main.java.com.hszilard.quizzer.common.quiz_model.Answer;
import main.java.com.hszilard.quizzer.common.quiz_model.Question;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * This abstract class contains all common and default functionality of the {@link ExistingQuestionEditController}
 * and {@link NewQuestionEditController} classes.
 */
abstract class AbstractQuestionEditController {

    private static final String QEC_LAYOUT = "/main/resources/com/hszilard/quizzer/quizeditor/questionEditLayout.fxml";
    private static final String ANSWER_HBOX = "/main/resources/com/hszilard/quizzer/quizeditor/anwerHBox.fxml";
    protected static final Logger LOGGER = Logger.getLogger(AbstractQuestionEditController.class.getName());

    protected ResourceBundle resources;
    protected Stage stage;
    protected Parent root;
    protected TextField questionTextField;
    protected VBox answersVBox;
    protected Button addAnswerButton;
    protected Button removeAnswerButton;
    protected Button confirmButton;
    protected Button cancelButton;

    protected Question question;

    protected Callback callback;

    /**
     * This interface has one method signature for actions that should be invoked whenever an affirmative
     * route is chosen (ie. add question to quiz, edit existing question).
     * The interface should be implemented by callbacks in the AbstractQuestionEditController's subclasses.
     */
    protected interface Callback {
        void onConfirm(Question question);
    }

    /* This is the method that starts the logic and displays the new window. */
    protected void display() throws IOException {
        setUpLayout();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    protected void setUpLayout() throws IOException {
        stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(QEC_LAYOUT), resources);
        root = loader.load();
        /* This hack makes it possible to get access to the layout's objects. Abstract classes can't do this normally. */
        Map<String, Object> namespace = loader.getNamespace();

        answersVBox = (VBox) namespace.get("answersVBox");
        questionTextField = (TextField) namespace.get("questionTextField");
        addAnswerButton = (Button) namespace.get("addAnswerButton");
        removeAnswerButton = (Button) namespace.get("removeAnswerButton");
        confirmButton = (Button) namespace.get("confirmButton");
        cancelButton = (Button) namespace.get("cancelButton");

        configureStage();
        configureNodes();
    }

    protected void configureStage() {
        stage.setMinWidth(400);
        stage.setMinHeight(200);
    }

    protected void configureNodes() throws IOException {
        configureQuestionText();
        configureAnswers();
        configureAddAnswersButton();
        configureRemoveAnswersButton();
        configureConfirmButton();
        configureCancelButton();
    }

    protected void configureQuestionText() {
        questionTextField.setText(question.getQuestionText());
    }

    protected void configureAnswers() throws IOException {
        for (int i = 0; i < question.getAnswers().size(); i++) {
            answersVBox.getChildren().add(makeAnswerHBox(i + 1, question.getAnswers().get(i)));
        }
    }

    protected void configureAddAnswersButton() {
        LOGGER.log(Level.INFO, "addAnswerButton clicked.");
        addAnswerButton.setOnMouseClicked(e -> {
            try {
                Answer newAnswer = new Answer("", false);
                question.getAnswers().add(newAnswer);
                HBox newHBox = makeAnswerHBox(question.getAnswers().size(), newAnswer);
                answersVBox.getChildren().add(newHBox);
            }
            catch (IOException ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            }
        });
    }

    protected void configureRemoveAnswersButton() {
        LOGGER.log(Level.INFO, "removeAnswerButton clicked.");
        removeAnswerButton.disableProperty().bind(Bindings.or(question.answersProperty().isNull(), question.answersProperty().emptyProperty()));
        removeAnswerButton.setOnMouseClicked(e -> {
            int lastAnswerIndex = question.getAnswers().size() - 1;
            question.getAnswers().remove(lastAnswerIndex);
            answersVBox.getChildren().remove(lastAnswerIndex);
        });
    }

    protected void configureConfirmButton() {
        LOGGER.log(Level.INFO, "confirmButton clicked.");
        confirmButton.setOnMouseClicked(e -> {
            if (question.getAnswers().stream().filter(Answer::isCorrect).count() > 1) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(resources.getString("alert_warning_title"));
                alert.setHeaderText(resources.getString("alert_too-many-correct-header"));
                alert.setContentText(resources.getString("alert_too-many-correct-text"));
                alert.showAndWait();
            } else if (questionTextField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(resources.getString("alert_warning_title"));
                alert.setHeaderText(resources.getString("alert_empty-question-header"));
                alert.setContentText(resources.getString("alert_empty-question-text"));
                alert.showAndWait();
            } else {
                question.setQuestionText(questionTextField.getText());
                callback.onConfirm(question);
                stage.close();
            }
        });
    }

    protected void configureCancelButton() {
        LOGGER.log(Level.INFO, "cancelButton clicked.");
        cancelButton.setOnMouseClicked(e -> stage.close());
    }

    protected HBox makeAnswerHBox(int i, Answer answer) throws IOException {
        HBox answerHBox = new FXMLLoader(getClass().getResource(ANSWER_HBOX), resources).load();
        answerHBox.prefWidthProperty().bind(questionTextField.widthProperty());

        Label answerLabel = (Label) answerHBox.lookup("#answerLabel");
        answerLabel.setText(i + resources.getString("edit_answer_label"));

        TextField answerTextField = (TextField) answerHBox.lookup("#answerTextField");
        answerTextField.textProperty().bindBidirectional(answer.answerTextProperty());

        CheckBox correctCheckBox = (CheckBox) answerHBox.lookup("#correctCheckBox");
        correctCheckBox.selectedProperty().bindBidirectional(answer.correctProperty());

        return answerHBox;
    }

}
