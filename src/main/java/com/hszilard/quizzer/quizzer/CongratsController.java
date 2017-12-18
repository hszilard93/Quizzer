package main.java.com.hszilard.quizzer.quizzer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.com.hszilard.quizzer.quizzer.game_model.TeamsManager;
import main.java.com.hszilard.quizzer.quizzer.teams_model.Team;

import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class CongratsController {
    private static final String STYLESHEET = "/main/resources/com/hszilard/quizzer/quizzer/style/congrats_styles.css";

    @FXML private ResourceBundle resources;
    @FXML private VBox congratsVBox;
    @FXML private GridPane teamsGrid;

    private TeamsManager teamsManager;

    @FXML
    private void initialize() {

    }

    private void postInit() {
        List<Team> teamsInOrder = teamsManager.teamsProperty().sorted(Comparator.comparingInt(Team::getScore).reversed());
        Team previousTeam = teamsInOrder.get(0);
        int place = 1;

        for (int i = 0; i < teamsInOrder.size(); i++) {
            Team team = teamsInOrder.get(i);

            Label nameLabel = new Label(team.getName());
            Label scoreLabel = new Label(Integer.toString(team.getScore()));
            Label placeLabel = new Label();

            if (team.getScore() < previousTeam.getScore()){
                place += 1;
                previousTeam = team;
            }
            switch (place) {
                case 1: {
                    placeLabel.setText(place + resources.getString("congrats_first-place-suffix"));
                    CommonUtils.addStyle(nameLabel, STYLESHEET, "first-name-label");
                    CommonUtils.addStyle(scoreLabel, STYLESHEET, "first-score-label");
                    CommonUtils.addStyle(placeLabel, STYLESHEET, "first-place-label");
                    break;
                }
                case 2: {
                    placeLabel.setText(place + resources.getString("congrats_second-place-suffix"));
                    CommonUtils.addStyle(nameLabel, STYLESHEET, "second-name-label");
                    CommonUtils.addStyle(scoreLabel, STYLESHEET, "second-score-label");
                    CommonUtils.addStyle(placeLabel, STYLESHEET, "second-place-label");
                    break;
                }
                case 3: {
                    placeLabel.setText(place + resources.getString("congrats_third-place-suffix"));
                    CommonUtils.addStyle(nameLabel, STYLESHEET, "third-name-label");
                    CommonUtils.addStyle(scoreLabel, STYLESHEET, "third-score-label");
                    CommonUtils.addStyle(placeLabel, STYLESHEET, "third-place-label");
                    break;
                }
                default: {
                    placeLabel.setText(place + resources.getString("congrats_nth-place-suffix"));
                    CommonUtils.addStyle(nameLabel, STYLESHEET, "rest-name-label");
                    CommonUtils.addStyle(scoreLabel, STYLESHEET, "rest-score-label");
                    CommonUtils.addStyle(placeLabel, STYLESHEET, "rest-place-label");
                }
            }
            teamsGrid.add(placeLabel, 0, i);
            GridPane.setFillWidth(placeLabel, true);
            placeLabel.setMaxWidth(Double.MAX_VALUE);
            teamsGrid.add(nameLabel, 1, i);
            GridPane.setFillWidth(nameLabel, true);
            nameLabel.setMaxWidth(Double.MAX_VALUE);
            teamsGrid.add(scoreLabel, 2, i);
            GridPane.setFillWidth(scoreLabel, true);
            scoreLabel.setMaxWidth(Double.MAX_VALUE);
        }
    }

    public void setTeamsManager(TeamsManager teamsManager) {
        this.teamsManager = teamsManager;
        postInit();
    }

    @FXML
    private void okButtonClicked(ActionEvent actionEvent) {
        ((Stage)congratsVBox.getScene().getWindow()).close();
    }
}
