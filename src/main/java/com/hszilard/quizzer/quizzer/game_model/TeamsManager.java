package main.java.com.hszilard.quizzer.quizzer.game_model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import main.java.com.hszilard.quizzer.quizzer.teams_model.Team;

public class TeamsManager {
    private ListProperty<Team> teams = new SimpleListProperty<>(FXCollections.observableArrayList());
    private IntegerProperty currentIndex = new SimpleIntegerProperty(0);
    private ObjectProperty<Team> currentTeam = new SimpleObjectProperty<>();

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
        teams.add(team);
    }

    public ObservableObjectValue<Team> currentTeamProperty() {
        return currentTeam;
    }

    public void nextTeam() {
        currentIndex.set(currentIndex.get() < teams.size()-1 ? currentIndex.get() + 1 : 0);
    }

    public void resetTeams() {
        for (Team team : teams) {
            team.setScore(0);
        }
        currentIndex.set(0);
    }

    public ListProperty<Team> teamsProperty() {
        return teams;
    }
}
