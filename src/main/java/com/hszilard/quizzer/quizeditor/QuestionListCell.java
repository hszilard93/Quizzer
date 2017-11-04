package main.java.com.hszilard.quizzer.quizeditor;

import main.java.com.hszilard.quizzer.common.quiz_model.Answer;
import main.java.com.hszilard.quizzer.common.quiz_model.Question;
import main.java.com.hszilard.quizzer.common.xml_converter.QuizLoader;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * Customized ListView cell to display a Questions text and possible answers.
 */
class QuestionListCell extends ListCell<Question> {
    private static final String QUESTION_LIST_CELL_FXML = "/main/resources/com/hszilard/quizzer/quizeditor/questionListCell.fxml";
    private static final String STYLES = "/main/resources/com/hszilard/quizzer/quizeditor/style/main_scene_styles.css";
    private static final Logger LOGGER = Logger.getLogger(QuizLoader.class.getName());

    @FXML private HBox cellBox;
    @FXML private Label title;
    @FXML private GridPane answersGrid;

    private FXMLLoader loader;
    private ResourceBundle resources;

    private Question question;
    private AbstractQuestionEditController.Callback callback;

    QuestionListCell(AbstractQuestionEditController.Callback callback, ResourceBundle resources) {
        this.resources = resources;
        this.callback = callback;
    }

    /* This method is invoked whenever the corresponding Question changes, or when the cell is created */
    @Override
    protected void updateItem(Question item, boolean empty) {
        super.updateItem(item, empty);
        question = item;
        if (empty || question == null) {
            clearContent();
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource(QUESTION_LIST_CELL_FXML));
                loader.setController(this);             // set this class as the layout's controller
                try {
                    loader.load();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
            addContent();
        }
    }

    private void clearContent() {
        setText(null);
        setGraphic(null);
    }

    /* Cell customization logic */
    private void addContent() {
        title.textProperty().bind(question.questionTextProperty());

        configureGrid();

        final NumberBinding cellBoxWidth = Bindings.subtract(getListView().widthProperty(), 36);
        cellBox.prefWidthProperty().bind(cellBoxWidth);
        answersGrid.prefWidthProperty().bind(cellBoxWidth);         // make the grid grow with the cell

        cellBox.setOnMouseClicked(event -> {
            /*
            * If the left mouse button is double clicked on a cell,
            * display an ExistingQuestionEditController for the corresponding question.
            */
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                LOGGER.log(Level.FINE, "A QuestionListCell has been double clicked.");
                try {
                    ExistingQuestionEditController existingQuestionEditController =
                            new ExistingQuestionEditController(question, callback, resources);
                    existingQuestionEditController.display();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });
        setGraphic(cellBox);
    }

    /* This grid contains all the possible answers in 2 columns */
    private void configureGrid() {
        answersGrid.getChildren().clear();                  // if this isn't done, we get weird overdrawing issues when deleting ListView items
        int answersSize = question.getAnswers().size();
        for (int i = 0; i < answersSize; i++) {
            int rowIndex = i / 2;
            int colIndex = i % 2 == 0 ? 0 : 1;

            Answer answer = question.getAnswers().get(i);
            Label answerLabel = new Label();
            answerLabel.textProperty().bind(answer.answerTextProperty());
            answerLabel.prefWidthProperty().bind(Bindings.divide(answersGrid.widthProperty(), 2));
            answerLabel.getStylesheets().add(STYLES);
            answerLabel.getStyleClass().add("answer-label");
            if (answer.isCorrect()) {
                answerLabel.getStyleClass().add("correct-answer-label");
            }
            answersGrid.add(answerLabel, colIndex, rowIndex);
        }
    }

}
