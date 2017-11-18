package main.java.com.hszilard.quizzer.quizzer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import main.java.com.hszilard.quizzer.common.quiz_model.Quiz;
import main.java.com.hszilard.quizzer.common.xml_converter.QuizLoader;
import main.java.com.hszilard.quizzer.common.xml_converter.QuizLoadingException;
import main.java.com.hszilard.quizzer.common.xml_converter.SimpleQuizLoader;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuController {
    private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());

    @FXML private MenuBar menuBar;
    @FXML private MenuItem fileOpenMenuItem;
    @FXML private MenuItem fileExitMenuItem;
    @FXML private MenuItem languageEnglishMenuItem;
    @FXML private MenuItem languageHungarianMenuItem;

    private MainController mainControllerInstance;

    void init(MainController mainControllerInstance) {
        this.mainControllerInstance = mainControllerInstance;
    }

    @FXML
    private void onOpenClicked(ActionEvent actionEvent) {
        mainControllerInstance.openQuiz();
    }

    @FXML
    private void onRestartButtonClicked(ActionEvent actionEvent) {

    }

    @FXML
    private void onExitButtonClicked(ActionEvent actionEvent) {
        // TODO: show popup exit dialog
        menuBar.getScene().getWindow();
    }

    @FXML
    private void onEnglishLanguageSelected(ActionEvent actionEvent) {
    }

    @FXML
    private void onHungarianLanguageSelected(ActionEvent actionEvent) {
    }
}
