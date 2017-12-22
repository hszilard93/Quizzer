package main.java.com.hszilard.quizzer.quizzer.game_model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import main.java.com.hszilard.quizzer.quizzer.teams_model.Team;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 */
public class TeamsManager {
    private static final Logger LOGGER = Logger.getLogger(TeamsManager.class.getName());

    private final ListProperty<Team> teams = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);
    private final ObjectProperty<Team> currentTeam = new SimpleObjectProperty<>();

    public TeamsManager() {
        ObservableObjectValue<Team> currentTeamValue = Bindings.createObjectBinding(() -> {
            if (teams.size() > 0)
                return teams.get(currentIndex.get());
            else
                return null;
        }, teams, currentIndex);
        currentTeam.bind(currentTeamValue);
    }

    public void addTeam(Team team) {
        LOGGER.log(INFO, "Adding team to manager.");
        teams.add(team);
    }

    public ObservableObjectValue<Team> currentTeamProperty() {
        return currentTeam;
    }

    public void nextTeam() {
        LOGGER.log(INFO, "Stepping to next team.");
        currentIndex.set(currentIndex.get() < teams.size()-1 ? currentIndex.get() + 1 : 0);
    }

    public void resetTeams() {
        LOGGER.log(INFO, "Resetting teams.");
        for (Team team : teams) {
            team.setScore(0);
        }
        currentIndex.set(0);
    }

    public ListProperty<Team> teamsProperty() {
        return teams;
    }
}
