package test.java.com.hszilard.quizzer.quizzer.game_model;

import main.java.com.hszilard.quizzer.quizzer.game_model.TeamsManager;
import main.java.com.hszilard.quizzer.quizzer.teams_model.Team;
import org.junit.jupiter.api.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Testing LocationManager")
class TeamsManagerTest {
    private TeamsManager teamsManager;

    @BeforeEach
    void setup() {
        teamsManager = new TeamsManager();
    }

    @Test
    void teamsPropertyHasAddedTeams() {
        Team a = new Team();
        Team b = new Team();
        teamsManager.addTeam(a);
        teamsManager.addTeam(b);

        assertThat(teamsManager.teamsProperty().size(), equalTo(2));
        assertThat(teamsManager.teamsProperty().get(0), equalTo(a));
        assertThat(teamsManager.teamsProperty().get(1), equalTo(b));
    }

    @Test
    void currentTeamPropertyValueIsNullWhenNoTeamsAdded() {
        assertThat(teamsManager.currentTeamProperty().get(), equalTo(null));
    }

    @Test
    void currentTeamPropertyValueIsFirstWhenNoNext() {
        Team a = new Team();
        Team b = new Team();
        teamsManager.addTeam(a);
        teamsManager.addTeam(b);

        assertThat(teamsManager.currentTeamProperty().get(), equalTo(a));
    }

    @Test
    void currentTeamPropertyValueIsCorrectWhenNoOverflow() {
        Team a = new Team();
        Team b = new Team();
        Team c = new Team();
        teamsManager.addTeam(a);
        teamsManager.addTeam(b);
        teamsManager.addTeam(c);

        for (int i = 0; i < 2; i++) {
            teamsManager.nextTeam();
        }

        assertThat(teamsManager.currentTeamProperty().get(), equalTo(c));
    }

    @Test
    void currentTeamPropertyValueIsCorrectWhenOverflow() {
        Team a = new Team();
        Team b = new Team();
        Team c = new Team();
        teamsManager.addTeam(a);
        teamsManager.addTeam(b);
        teamsManager.addTeam(c);

        for (int i = 0; i < 6; i++) {
            teamsManager.nextTeam();
        }

        assertThat(teamsManager.currentTeamProperty().get(), equalTo(a));
    }

    @Test
    void currentTeamIsFirstAndScoresAreZeroWhenReset() {
        Team a = new Team();
        a.setScore(100);
        Team b = new Team();
        b.setScore(100);
        teamsManager.addTeam(a);
        teamsManager.addTeam(b);
        teamsManager.nextTeam();

        teamsManager.resetTeams();

        assertThat(teamsManager.currentTeamProperty().get(), equalTo(a));
        assertThat(teamsManager.teamsProperty().get(0).getScore(), equalTo(0));
        assertThat(teamsManager.teamsProperty().get(1).getScore(), equalTo(0));
    }

}
