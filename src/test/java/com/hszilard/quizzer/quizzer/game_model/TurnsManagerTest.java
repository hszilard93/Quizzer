package test.java.com.hszilard.quizzer.quizzer.game_model;

import main.java.com.hszilard.quizzer.common.quiz_model.Question;
import main.java.com.hszilard.quizzer.common.quiz_model.Quiz;
import main.java.com.hszilard.quizzer.quizzer.game_model.TeamsManager;
import main.java.com.hszilard.quizzer.quizzer.game_model.TurnsManager;
import main.java.com.hszilard.quizzer.quizzer.teams_model.Team;
import org.junit.jupiter.api.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Testing TurnsManager")
public class TurnsManagerTest {

    private TurnsManager turnsManager;
    private TeamsManager teamsManager;
    private Quiz quiz;

    @BeforeEach
    void setup() {
        quiz = new Quiz("");
        teamsManager = new TeamsManager();
        turnsManager = new TurnsManager(quiz, teamsManager);
    }

    @Test
    void getTotalTurnsReturnsCorrectAmountWhenNoRemainder() {
        /* For 6 questions and 2 teams.. */
        for (int i = 0; i < 6; i++)
            quiz.getQuestions().add(new Question());
        for (int i = 0; i < 2; i++)
            teamsManager.teamsProperty().add(new Team());

        /* ..the answer must be 3 */
        assertThat(turnsManager.getTotalTurns(), equalTo(3));
    }

    @Test
    void getTotalTurnsReturnsCorrectAmountWhenRemainder() {
        /* For 11 questions and 3 teams.. */
        for (int i = 0; i < 11; i++)
            quiz.getQuestions().add(new Question());
        for (int i = 0; i < 3; i++)
            teamsManager.teamsProperty().add(new Team());

        /* ..the answer must be 3 */
        assertThat(turnsManager.getTotalTurns(), equalTo(3));
    }

    @Test
    void getTotalTurnsReturnsZeroWhenNoQuestions() {
        /* For 0 questions and 2 teams.. */
        for (int i = 0; i < 0; i++)
            quiz.getQuestions().add(new Question());
        for (int i = 0; i < 2; i++)
            teamsManager.teamsProperty().add(new Team());

        /* ..the answer must be 0 */
        assertThat(turnsManager.getTotalTurns(), equalTo(0));
    }

    @Test
    void getTotalTurnsReturnsZeroWhenNoTeams() {
        /* For 6 questions and 0 teams.. */
        for (int i = 0; i < 7; i++)
            quiz.getQuestions().add(new Question());
        for (int i = 0; i < 0; i++)
            teamsManager.teamsProperty().add(new Team());

        /* ..the answer must be 0 */
        assertThat(turnsManager.getTotalTurns(), equalTo(0));
    }

    @Test
    void getCurrentTurnReturnsCorrectValueWhenProgress() {
        /* For 10 questions and 2 teams.. */
        for (int i = 0; i < 10; i++)
            quiz.getQuestions().add(new Question());
        for (int i = 0; i < 2; i++)
            teamsManager.teamsProperty().add(new Team());

        /* ..the number of total turns is now 5. Let's skip 5 questions.*/
        for (int i = 0; i < 5; i++)
            turnsManager.nextQuestion();

        /* The current turn should be 5/2+1 = 3. */
        assertThat(turnsManager.getCurrentTurn(), equalTo(3));
    }

    @Test
    void getCurrentTurnReturnsZeroWhenNoTeams() {
        for (int i = 0; i < 10; i++)
            quiz.getQuestions().add(new Question());
        for (int i = 0; i < 0; i++)
            teamsManager.teamsProperty().add(new Team());

        for (int i = 0; i < 5; i++)
            turnsManager.nextQuestion();

        assertThat(turnsManager.getCurrentTurn(), equalTo(0));
    }

    @Test
    void getCurrentTurnReturnsZeroWhenNoQuestions() {
        for (int i = 0; i < 0; i++)
            quiz.getQuestions().add(new Question());
        for (int i = 0; i < 5; i++)
            teamsManager.teamsProperty().add(new Team());

        for (int i = 0; i < 5; i++)
            turnsManager.nextQuestion();

        assertThat(turnsManager.getCurrentTurn(), equalTo(0));
    }

    @Test
    void getCurrentTurnReturnsOneWhenProgressAndReset() {
        for (int i = 0; i < 10; i++)
            quiz.getQuestions().add(new Question());
        for (int i = 0; i < 2; i++)
            teamsManager.teamsProperty().add(new Team());

        /* ..the number of total turns is now 5. Let's skip 5 questions.*/
        for (int i = 0; i < 5; i++)
            turnsManager.nextQuestion();
        /* Now let's reset the questions */
        turnsManager.reset();

        assertThat(turnsManager.getCurrentTurn(), equalTo(1));
    }

    @Test
    void isGameOverReturnsFalseWhenTotalTurnsZero() {
        for (int i = 0; i < 0; i++)
            quiz.getQuestions().add(new Question());
        for (int i = 0; i < 0; i++)
            teamsManager.teamsProperty().add(new Team());

        assertThat(turnsManager.isGameOver(), equalTo(false));
    }

    @Test
    void isGameOverReturnsFalseWhenNoProgress() {
        for (int i = 0; i < 10; i++)
            quiz.getQuestions().add(new Question());
        for (int i = 0; i < 2; i++)
            teamsManager.teamsProperty().add(new Team());

        assertThat(turnsManager.isGameOver(), equalTo(false));
    }

    @Test
    void isGameOverReturnsFalseWhenCurrentTurnLessThanTotal() {
        for (int i = 0; i < 10; i++)
            quiz.getQuestions().add(new Question());
        for (int i = 0; i < 2; i++)
            teamsManager.teamsProperty().add(new Team());

        for (int i = 0; i < 9; i++)
            turnsManager.nextQuestion();

        assertThat(turnsManager.isGameOver(), equalTo(false));
    }

    @Test
    void isGameOverReturnsTrueWhenCurrentTurnEqualsTotal() {
        for (int i = 0; i < 10; i++)
            quiz.getQuestions().add(new Question());
        for (int i = 0; i < 2; i++)
            teamsManager.teamsProperty().add(new Team());

        for (int i = 0; i < 10; i++)
            turnsManager.nextQuestion();

        assertThat(turnsManager.isGameOver(), equalTo(true));
    }

}
