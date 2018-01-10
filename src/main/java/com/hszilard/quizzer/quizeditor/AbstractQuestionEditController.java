package main.java.com.hszilard.quizzer.quizeditor;

import javafx.scene.image.Image;
import javafx.stage.Modality;
import main.java.com.hszilard.quizzer.common.quiz_model.Answer;
import main.java.com.hszilard.quizzer.common.quiz_model.Difficulty;
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
    protected RadioButton easyRadioButton;
    protected RadioButton mediumRadioButton;
    protected RadioButton hardRadioButton;
    protected RadioButton customRadioButton;
    protected TextField difficultyTextField;

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
        easyRadioButton = (RadioButton) namespace.get("easyRadioButton");
        mediumRadioButton = (RadioButton) namespace.get("mediumRadioButton");
        hardRadioButton = (RadioButton) namespace.get("hardRadioButton");
        customRadioButton = (RadioButton) namespace.get("customRadioButton");
        difficultyTextField = (TextField) namespace.get("difficultyTextField");

        configureStage();
        configureNodes();
    }

    protected void configureStage() {
        stage.setMinWidth(500);
        stage.setMinHeight(350);
        stage.getIcons().add(new Image(
                "/main/resources/com/hszilard/quizzer/quizeditor/drawable/icon.png"
        ));
        /* Making sure the 'owner' stage cannot be interacted with. */
        stage.initOwner(Main.getStage());
        stage.initModality(Modality.WINDOW_MODAL);
    }

    protected void configureNodes() throws IOException {
        configureQuestionText();
        configureDifficultiesHBox();
        configureAnswersVBox();
        configureAddAnswersButton();
        configureRemoveAnswersButton();
        configureConfirmButton();
        configureCancelButton();
    }

    protected void configureQuestionText() {
        questionTextField.setText(question.getQuestionText());
    }

    protected void configureDifficultiesHBox() {
        Difficulty difficulty = question.getDifficulty();

        ToggleGroup toggleGroup = new ToggleGroup();
        easyRadioButton.setToggleGroup(toggleGroup);
        mediumRadioButton.setToggleGroup(toggleGroup);
        hardRadioButton.setToggleGroup(toggleGroup);
        customRadioButton.setToggleGroup(toggleGroup);

        difficultyTextField.setEditable(false);
        difficultyTextField.setOnMouseClicked(e -> customRadioButton.setSelected(true));

        easyRadioButton.setOnAction(e -> difficultyTextField.setText(Integer.toString(Difficulty.EASY.getValue())));
        mediumRadioButton.setOnAction(e -> difficultyTextField.setText(Integer.toString(Difficulty.DEFAULT.getValue())));
        hardRadioButton.setOnAction(e -> difficultyTextField.setText(Integer.toString(Difficulty.HARD.getValue())));

        if (difficulty.equals(Difficulty.EASY))
            easyRadioButton.setSelected(true);
        else if (difficulty.equals(Difficulty.DEFAULT))
            mediumRadioButton.setSelected(true);
        else if (difficulty.equals(Difficulty.HARD))
            hardRadioButton.setSelected(true);
        else {
            customRadioButton.setSelected(true);
            difficultyTextField.setEditable(true);
        }
        difficultyTextField.setText(Integer.toString(difficulty.getValue()));

        toggleGroup.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> {
            newToggle.setSelected(true);
            if (newToggle == customRadioButton) {
                difficultyTextField.setEditable(true);
                difficultyTextField.requestFocus();
            }
        });
    }

    protected void configureAnswersVBox() throws IOException {
        for (int i = 0; i < question.getAnswers().size(); i++)
            answersVBox.getChildren().add(makeAnswerHBox(i + 1, question.getAnswers().get(i)));
    }

    protected void configureAddAnswersButton() {
        addAnswerButton.setOnMouseClicked(e -> {
            LOGGER.log(Level.INFO, "addAnswerButton clicked.");
            try {
                Answer newAnswer = new Answer("", false);
                question.getAnswers().add(newAnswer);
                HBox newHBox = makeAnswerHBox(question.getAnswers().size(), newAnswer);
                answersVBox.getChildren().add(newHBox);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            }
        });
    }

    protected void configureRemoveAnswersButton() {
        removeAnswerButton.disableProperty().bind(Bindings.or(question.answersProperty().isNull(), question.answersProperty().emptyProperty()));
        removeAnswerButton.setOnMouseClicked(e -> {
            LOGGER.log(Level.INFO, "removeAnswerButton clicked.");
            int lastAnswerIndex = question.getAnswers().size() - 1;
            question.getAnswers().remove(lastAnswerIndex);
            answersVBox.getChildren().remove(lastAnswerIndex);
        });
    }

    protected void configureConfirmButton() {
        confirmButton.setOnMouseClicked(e -> {
            LOGGER.log(Level.INFO, "confirmButton clicked.");
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
                if (easyRadioButton.isSelected())
                    question.setDifficulty(Difficulty.EASY);
                else if (mediumRadioButton.isSelected())
                    question.setDifficulty(Difficulty.DEFAULT);
                else if (hardRadioButton.isSelected())
                    question.setDifficulty(Difficulty.HARD);
                else if (customRadioButton.isSelected()) {
                    try {
                        int difficultyValue = Integer.parseInt(difficultyTextField.getText());
                        if (difficultyValue <= 0)
                            throw new NumberFormatException();
                        else if (difficultyValue == Difficulty.EASY.getValue())
                            question.setDifficulty(Difficulty.EASY);
                        else if (difficultyValue == Difficulty.DEFAULT.getValue())
                            question.setDifficulty(Difficulty.DEFAULT);
                        else if (difficultyValue == Difficulty.HARD.getValue())
                            question.setDifficulty(Difficulty.HARD);
                        else
                            question.setDifficulty(new Difficulty(difficultyValue));
                    } catch (NumberFormatException nfe) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle(resources.getString("alert_warning_title"));
                        alert.setHeaderText(resources.getString("alert_difficulty-number-header"));
                        alert.setContentText(resources.getString("alert_difficulty-number-text"));
                        alert.showAndWait();
                        return;
                    }
                }
                callback.onConfirm(question);
                stage.close();
            }
        });
    }

    protected void configureCancelButton() {
        cancelButton.setOnMouseClicked(e -> {
            LOGGER.log(Level.INFO, "cancelButton clicked.");
            stage.close();
        });
    }

    protected HBox makeAnswerHBox(int i, Answer answer) throws IOException {
        HBox answerHBox = new FXMLLoader(getClass().getResource(ANSWER_HBOX), resources).load();
        answerHBox.prefWidthProperty().bind(questionTextField.widthProperty());

        Label answerLabel = (Label) answerHBox.lookup("#answerLabel");
        answerLabel.setText(String.format(resources.getString("edit_answer_label"), i));

        TextField answerTextField = (TextField) answerHBox.lookup("#answerTextField");
        answerTextField.textProperty().bindBidirectional(answer.answerTextProperty());

        CheckBox correctCheckBox = (CheckBox) answerHBox.lookup("#correctCheckBox");
        correctCheckBox.selectedProperty().bindBidirectional(answer.correctProperty());

        return answerHBox;
    }

}
