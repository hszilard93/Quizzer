package main.java.com.hszilard.quizzer.quizzer;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 */
class CommonUtils {
    private static final Logger LOGGER = Logger.getLogger(CommonUtils.class.getName());

    /* Adds given style to a parent object. */
    static public void addStyle(Parent node, String stylesheet, String styleClass) {
        node.getStylesheets().add(stylesheet);
        node.getStyleClass().add(styleClass);
    }

    /* Adds the app icon to the given window. */
    static public void iconify(Window window) {
        iconify((Stage) window);
    }

    /* /* Adds the app icon to the given stage. */
    static public void iconify(Stage stage) {
        LOGGER.log(INFO, "Iconifying stage " + (stage.getTitle() == null ? stage : stage.getTitle()));
        stage.getIcons().add(new Image("/main/resources/com/hszilard/quizzer/quizzer/drawable/icon.png"));
    }

    /* Confirmation alert boilerplate */
    @SuppressWarnings("Duplicates")
    static Optional<ButtonType> showChangeAlert(String text, ResourceBundle resources) {
        LOGGER.log(Level.INFO, "Attempting to show change alert.");

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
    static void showPopup(String header, String content, ResourceBundle resources) {
        LOGGER.log(Level.INFO, "Attempting to show popup. Header: " + header);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resources.getString("inform_title"));
        alert.setHeaderText(header);
        alert.setContentText(content);
        iconify(alert.getDialogPane().getScene().getWindow());
        alert.show();
    }

    /* Error alert boilerplate */
    static void showError(String text, ResourceBundle resources) {
        LOGGER.log(Level.INFO, "Attempting to show error alert.");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resources.getString("error_title"));
        alert.setHeaderText(resources.getString("error_header"));
        alert.setContentText(text);
        iconify(alert.getDialogPane().getScene().getWindow());
        alert.show();
    }
}
