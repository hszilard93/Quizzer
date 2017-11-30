package main.java.com.hszilard.quizzer.quizzer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import main.java.com.hszilard.quizzer.common.LocaleManager;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuController {
    private static final Logger LOGGER = Logger.getLogger(MenuController.class.getName());

    @FXML private ResourceBundle resources;                 // contains the internationalized strings.
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
    private void onResetButtonClicked(ActionEvent actionEvent) {
        LOGGER.log(Level.FINE, "Reset menu item clicked.");

        String key = resources.getString("alert_sure-reset-text");
        Optional<ButtonType> result = Util.showChangeAlert(key, resources);
        if (result.isPresent() && result.get() != ButtonType.CANCEL) {
            mainControllerInstance.restart();
        }
    }

    @FXML
    private void onExitButtonClicked(ActionEvent actionEvent) {
        LOGGER.log(Level.FINE, "Exit menu item clicked.");

        Optional<ButtonType> result = Util.showChangeAlert(resources.getString("alert_sure-exit-text"), resources);
        if (result.isPresent() && result.get() != ButtonType.CANCEL) {
            ((Stage) menuBar.getScene().getWindow()).close();
        }
    }

    @FXML
    private void onEnglishLanguageSelected(ActionEvent actionEvent) {
        LOGGER.log(Level.FINE, "English language menu item clicked.");
        changeLanguage(new Locale("en"));
    }

    @FXML
    private void onHungarianLanguageSelected(ActionEvent actionEvent) {
        LOGGER.log(Level.FINE, "English language menu item clicked.");
        changeLanguage(new Locale("hu"));
    }

    private void changeLanguage(Locale locale) {
        if (!LocaleManager.getPreferredLocale().equals(locale)) {
            LocaleManager.setPreferredLocale(locale);
        } else {
            return;  // don't do anything if selected language is already in use
        }

        /* Show confirmation dialog */
        Optional<ButtonType> returnType = Util.showChangeAlert(ResourceBundle.getBundle("main.resources.com.hszilard.quizzer.quizzer.strings",
                locale).getString("alert_language-change")
                + "\n"
                + resources.getString("alert_language-change"), resources);
        if (!returnType.isPresent() || returnType.get() == ButtonType.CANCEL) {
            return;
        }

        LOGGER.log(Level.INFO, "Attempting to restart application because of language change.");
        ((Stage) menuBar.getScene().getWindow()).close();
        /*
         * We must restart the application for the language change to take place.
         * The preferred locale is already changed, so if the user cancels here, he will still get the
         * selected language on next start.
         */
        Platform.runLater(() -> {
            try {
                new Main().start(new Stage());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Restarting application failed/n" + e.getMessage(), e);
            }
        });
    }

}
