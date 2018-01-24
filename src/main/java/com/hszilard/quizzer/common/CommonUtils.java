package main.java.com.hszilard.quizzer.common;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import main.java.com.hszilard.quizzer.common.quiz_model.Answer;
import main.java.com.hszilard.quizzer.common.quiz_model.Question;
import main.java.com.hszilard.quizzer.common.quiz_model.Quiz;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * Common methods for displaying popups and doing other repetitive tasks.
 */
public class CommonUtils {
    private static final Logger LOGGER = Logger.getLogger(CommonUtils.class.getName());
    private static final String QUIZZER_ICON_PATH = "/main/resources/com/hszilard/quizzer/quizzer/drawable/icon.png";
    private static final String EDITOR_ICON_PATH = "/main/resources/com/hszilard/quizzer/quizeditor/drawable/icon.png";
    private static final String VERSION = "0.93";

    private static String iconPath;
    private static ResourceBundle resources;

    /**
     * This method should be called at the start of each application. It helps configure the app-specific resources (e.g. the app icons)
     *
     * @param pack      The Quizzer or the QuizEditor package
     * @param resources The active string resources
     */
    public static void configure(Package pack, ResourceBundle resources) {
        CommonUtils.resources = resources;

        if (pack.getName().equals("main.java.com.hszilard.quizzer.quizzer"))
            iconPath = QUIZZER_ICON_PATH;
        else if (pack.getName().equals("main.java.com.hszilard.quizzer.quizeditor"))
            iconPath = EDITOR_ICON_PATH;
        else {
            LOGGER.log(Level.SEVERE, "Invalid package: " + pack);
        }
    }

    /* Adds given style to a parent object. */
    public static void addStyle(Parent node, String stylesheet, String styleClass) {
        node.getStylesheets().add(stylesheet);
        node.getStyleClass().add(styleClass);
    }

    /* Adds the app icon to the given window. */
    public static void iconify(Window window) {
        iconify((Stage) window);
    }

    /* /* Adds the app icon to the given stage. */
    public static void iconify(Stage stage) {
        LOGGER.log(Level.FINE, "Iconifying stage " + (stage.getTitle() == null ? stage : stage.getTitle()));
        if (iconPath != null)
            stage.getIcons().add(new Image(iconPath));
    }

    /* Confirmation alert boilerplate */
    public static Optional<ButtonType> showChangeAlert(String text) {
        LOGGER.log(Level.FINE, "Attempting to show change alert.");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resources.getString("alert_confirmation_title"));
        alert.setHeaderText(resources.getString("alert_sure-header"));
        alert.setContentText(text);
        iconify(alert.getDialogPane().getScene().getWindow());

        ((Button) (alert.getDialogPane().lookupButton(ButtonType.OK))).setText(resources.getString("alert_ok-button"));
        ((Button) (alert.getDialogPane().lookupButton(ButtonType.CANCEL)))
                .setText(resources.getString("alert_cancel-button"));

        return alert.showAndWait();
    }

    /* Information alert boilerplate */
    public static void showPopup(String header, String content) {
        LOGGER.log(Level.FINE, "Attempting to show popup. Header: " + header);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resources.getString("inform_title"));
        alert.setHeaderText(header);
        alert.setContentText(content);
        iconify(alert.getDialogPane().getScene().getWindow());
        alert.show();
    }

    public static void showAboutPopup() {
        showPopup(null, resources.getString("about_text")
                + "\n\n"
                + resources.getString("about_version") + " " + VERSION);
    }

    public static Optional<ButtonType> showInvalidQuizAlert() {
        LOGGER.log(Level.FINE, "Attempting to invalid quiz warning");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resources.getString("alert_invalid-title"));
        alert.setHeaderText(resources.getString("alert_invalid-header"));
        alert.setContentText(resources.getString("alert_invalid-text"));
        iconify(alert.getDialogPane().getScene().getWindow());

        ((Button) (alert.getDialogPane().lookupButton(ButtonType.OK))).setText(resources.getString("alert_ok-button"));
        ((Button) (alert.getDialogPane().lookupButton(ButtonType.CANCEL)))
                .setText(resources.getString("alert_cancel-button"));

        return alert.showAndWait();
    }

    /* Error alert boilerplate */
    public static void showError(String text) {
        LOGGER.log(Level.FINE, "Attempting to show error alert.");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resources.getString("error_title"));
        alert.setHeaderText(resources.getString("error_header"));
        alert.setContentText(text);
        iconify(alert.getDialogPane().getScene().getWindow());
        alert.show();
    }

    /* Checks for some mistakes that would make the quiz 'invalid' (e.g. not having at least 2 possible answers). */
    public static boolean isValidQuiz(Quiz quiz) {
        if (quiz.getQuestions().size() == 0)
            return false;
        for (Question q : quiz.getQuestions()) {
            if (q.getQuestionText().isEmpty() || q.getAnswers().size() < 2)
                return false;
            boolean hasCorrectAnswer = false;
            for (Answer a : q.getAnswers()) {
                if (a.getAnswerText().isEmpty())
                    return false;
                hasCorrectAnswer |= a.isCorrect();
            }
            if (!hasCorrectAnswer)
                return false;
        }
        return true;
    }
}
