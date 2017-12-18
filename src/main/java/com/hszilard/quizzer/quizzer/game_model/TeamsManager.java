package main.java.com.hszilard.quizzer.quizzer.teams_model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TeamsManager {
    private ObservableList<Team> teams = new SimpleListProperty<>(FXCollections.observableArrayList());
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

    public ObservableObjectValue<Team> currentTeam() {
        return currentTeam;
    }

    public void next() {
        currentIndex.set(currentIndex.get() < teams.size()-1 ? currentIndex.get() + 1 : 0);
    }

    public void resetTeams() {
        for (Team team : teams) {
            team.setScore(0);
        }
        currentIndex.set(0);
    }

    public ObservableList<Team> teamsProperty() {
        return teams;
    }
}
