package main.java.com.hszilard.quizzer.quizzer;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import main.java.com.hszilard.quizzer.quizzer.teams_model.Team;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static main.java.com.hszilard.quizzer.common.CommonUtils.iconify;
import static main.java.com.hszilard.quizzer.common.CommonUtils.showPopup;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * This is a factory that can make two kinds of Team Dialogs for editing new and existing teams.
 */
class TeamDialogFactory {
    private static final Logger LOGGER = Logger.getLogger(TeamDialogFactory.class.getName());

    private final ResourceBundle resources;

    TeamDialogFactory(ResourceBundle resources) {
        this.resources = resources;
    }

    Dialog<Team> createAddTeamDialog() throws IOException {
        return configureDialog(resources.getString("team_new-team-label"), "");
    }

    Dialog<Team> createEditTeamDialog(Team team) throws IOException {
        return configureDialog(resources.getString("team_edit-team-label"), team.getName());
    }

    private Dialog<Team> configureDialog(String header, String teamName) throws IOException {
        FXMLLoader dialogLoader =
                new FXMLLoader(getClass().getResource("/main/resources/com/hszilard/quizzer/quizzer/teamDialog.fxml"),
                        resources);
        Dialog<Team> teamDialog = new Dialog<>();
        teamDialog.setDialogPane(dialogLoader.load());

        Window window = teamDialog.getDialogPane().getScene().getWindow();
        iconify(window);

        Label headerLabel = (Label) dialogLoader.getNamespace().get("headerLabel");
        headerLabel.setText(header);
        Label nameLabel = (Label) dialogLoader.getNamespace().get("nameLabel");
        nameLabel.setText(resources.getString("team_team-name-label"));
        TextField nameField = (TextField) dialogLoader.getNamespace().get("nameField");
        nameField.setText(teamName);
        nameField.requestFocus();

        Button applyButton = (Button) dialogLoader.getNamespace().get("applyButton");
        Button cancelButton = (Button) dialogLoader.getNamespace().get("cancelButton");

        applyButton.setOnAction(action -> {
            if (nameField.getText().isEmpty())
                showPopup(resources.getString("inform_header-uh-oh"), resources.getString("inform_name_empty"));
            else {
                ((Stage) window).close();
                teamDialog.setResult(new Team(nameField.getText(), 0));
            }
            LOGGER.log(Level.INFO, "TeamDialog applied and closed.");
        });
        cancelButton.setOnAction(action -> {
            ((Stage) window).close();
            LOGGER.log(Level.INFO, "TeamDialog canceled and closed.");
        });
        window.setOnCloseRequest(event -> {
            ((Stage) window).close();
            LOGGER.log(Level.INFO, "TeamDialog cancelled and closed.");
        });

        return teamDialog;
    }

}
