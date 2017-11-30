package main.java.com.hszilard.quizzer.quizzer;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import java.util.ResourceBundle;

class Util {

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

    static void showPopup(String text, ResourceBundle resources) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resources.getString("inform_title"));
        alert.setHeaderText(resources.getString("inform_header"));
        alert.setContentText(text);
        alert.show();
    }

    static void showError(String text, ResourceBundle resources) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resources.getString("error_title"));
        alert.setHeaderText(resources.getString("error_header"));
        alert.setContentText(text);
        alert.show();
    }

}
