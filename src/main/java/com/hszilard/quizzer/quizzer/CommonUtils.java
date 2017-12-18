package main.java.com.hszilard.quizzer.quizzer;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.Optional;
import java.util.ResourceBundle;

class CommonUtils {

    static public void addStyle(Parent node, String stylesheet, String styleClass) {
        node.getStylesheets().add(stylesheet);
        node.getStyleClass().add(styleClass);
    }

    static public void iconify(Window window) {
        iconify((Stage)window);
    }

    static public void iconify(Stage stage) {
        stage.getIcons().add(new Image("/main/resources/com/hszilard/quizzer/quizzer/drawable/question.png"));
    }

    /* Confirmation dialog boilerplate */
    @SuppressWarnings("Duplicates")
    static Optional<ButtonType> showChangeAlert(String text, ResourceBundle resources) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resources.getString("alert_confirmation_title"));
        alert.setHeaderText(resources.getString("alert_sure-header"));
        alert.setContentText(text);

        ((Button)(alert.getDialogPane().lookupButton(ButtonType.OK))).setText(resources.getString("alert_ok-button"));
        ((Button)(alert.getDialogPane().lookupButton(ButtonType.CANCEL))).setText(resources.getString("alert_cancel-button"));

        return alert.showAndWait();
    }

    static void showPopup(String header, String content, ResourceBundle resources) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resources.getString("inform_title"));
        alert.setHeaderText(header);
        alert.setContentText(content);
        iconify(alert.getDialogPane().getScene().getWindow());
        alert.show();
    }

    static void showError(String text, ResourceBundle resources) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resources.getString("error_title"));
        alert.setHeaderText(resources.getString("error_header"));
        alert.setContentText(text);
        iconify(alert.getDialogPane().getScene().getWindow());
        alert.show();
    }

}
