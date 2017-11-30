package main.java.com.hszilard.quizzer.quizzer.teams_model;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TeamsManager {
    private ObservableList<Team> teams = new SimpleListProperty<>(FXCollections.observableArrayList());
    private int currentIndex = 0;

    public void addTeam(Team team) {
        teams.add(team);
    }

    public Team currentTeam() {
        return teams.size() > 0 ? teams.get(currentIndex) : null;
    }

    public void next() {
        currentIndex = currentIndex < teams.size() ? currentIndex + 1 : 0;
    }

    public void resetTeams() {
        for (Team team : teams) {
            team.setScore(0);
        }
        currentIndex = 0;
    }
}
