package main.java.com.hszilard.quizzer.quizzer;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.java.com.hszilard.quizzer.common.LocationManager;
import main.java.com.hszilard.quizzer.common.quiz_model.Question;
import main.java.com.hszilard.quizzer.common.quiz_model.Quiz;
import main.java.com.hszilard.quizzer.common.xml_converter.QuizLoader;
import main.java.com.hszilard.quizzer.common.xml_converter.QuizLoadingException;
import main.java.com.hszilard.quizzer.common.xml_converter.SimpleQuizLoader;
import main.java.com.hszilard.quizzer.quizzer.game_model.TeamsManager;
import main.java.com.hszilard.quizzer.quizzer.game_model.TurnsManager;
import main.java.com.hszilard.quizzer.quizzer.teams_model.Team;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.beans.binding.Bindings.equal;
import static javafx.beans.binding.Bindings.when;
import static javafx.stage.Modality.WINDOW_MODAL;
import static main.java.com.hszilard.quizzer.quizzer.CommonUtils.*;
import static main.java.com.hszilard.quizzer.quizzer.QuestionSceneController.Callback;

@SuppressWarnings("Duplicates")
public class MainController {
    private static final String GRID_STYLESHEET = "/main/resources/com/hszilard/quizzer/quizzer/style/grid_styles.css";
    private static final String RIGHT_STYLESHEET = "/main/resources/com/hszilard/quizzer/quizzer/style/right_vbox_styles.css";
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());
    private static final int DEFAULT_SCORE = 10;

    @FXML private ResourceBundle resources;                 // contains the internationalized strings.
    @FXML private MenuBar menuBarLayout;
    @FXML private MenuController menuBarLayoutController;
    @FXML private GridPane questionsGrid;
    @FXML private VBox teamsVBox;
    @FXML private Label turnsLabel;
    private Button addTeamButton;

    private Quiz quiz;
    private TeamsManager teamsManager;
    private TurnsManager turnsManager;

    @FXML
    private void initialize() {
        teamsManager = new TeamsManager();
        setupViews();
    }

    void restart() {
        teamsManager.resetTeams();
        if (turnsManager != null)
            turnsManager.reset();
        setupViews();
    }

    void openQuiz() {
        File file = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resources.getString("open_title"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(resources.getString("open_format"),"*.xml"));

        String quizPath = LocationManager.getLastPath(this.getClass());
        try {
            String quizDirectory = quizPath;
            fileChooser.setInitialDirectory(new File(quizDirectory));
            file = fileChooser.showOpenDialog(menuBarLayout.getScene().getWindow());
        }
        /* quizPath is null or folder does not exist (eg. portable drive removed) */
        catch (NullPointerException | IllegalArgumentException e) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            try {
                file = fileChooser.showOpenDialog(menuBarLayout.getScene().getWindow());
            }
            /* Now something very bad must have happened */
            catch (Exception e2) {
                LOGGER.log(Level.SEVERE, "Could not open location! This should not happen!");
                CommonUtils.showError(resources.getString("error_generic-message"), resources);
            }
        }

        if (file != null) {
            try {
                Quiz quiz;
                QuizLoader quizLoader = new SimpleQuizLoader();
                quiz = quizLoader.loadQuiz(file.getPath());
                postOpenQuiz(quiz);
                quizPath = file.getPath();
                LocationManager.setLastPath(this.getClass(), quizPath.substring(0, quizPath.lastIndexOf(File.separator)));
            } catch (QuizLoadingException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                /* Show generic error message*/
                CommonUtils.showError(resources.getString("error_open-message"), resources);
            }
        }
    }

    private void postOpenQuiz(Quiz quiz) {
        LOGGER.log(Level.INFO, "Quiz  loaded");
        this.quiz = quiz;
        restart();

        Collections.shuffle(quiz.getQuestions());
        turnsManager = new TurnsManager(quiz, teamsManager);
        configureQuestionsGrid();
        ((Stage) (questionsGrid.getScene().getWindow())).setTitle("Quizzer - " + quiz.getTitle());

        configureTurnsLabel();

        /*
        What happens when there are no more turns left.
         */
        turnsManager.gameOverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == true) {
                for (Node node : questionsGrid.getChildren())
                    node.setDisable(true);
                turnsLabel.textProperty().unbind();
                turnsLabel.setText(resources.getString("main_turns-over"));
                showCongratsPopup();
            }
        });
    }

    private void setupViews() {
        menuBarLayoutController.init(this);
        configureQuestionsGrid();
        configureTeamsVBox();
        configureTurnsLabel();
    }

    private void configureQuestionsGrid() {
        if (quiz == null) {
            Button openQuizButton = new Button(resources.getString("main_open-quiz-button"));
            openQuizButton.setOnAction(e -> openQuiz());
            addStyle(openQuizButton, GRID_STYLESHEET, "open-quiz-button");
            questionsGrid.getChildren().clear();
            questionsGrid.getChildren().add(openQuizButton);
        } else {
            questionsGrid.getChildren().clear();
            int questionsSize = quiz.getQuestions().size();
            int rowSize = Double.valueOf(Math.ceil(Math.sqrt(questionsSize))).intValue();
            int colSize = Double.valueOf(Math.floor(Math.sqrt(questionsSize))).intValue();

            NumberBinding buttonSize = Bindings.min(questionsGrid.widthProperty().divide(rowSize),
                    questionsGrid.heightProperty().divide(colSize)).subtract(8);

            for (int i = 0; i < questionsSize; i++) {
                Button questionButton = new Button("" + (i + 1));
                addStyle(questionButton, GRID_STYLESHEET, "question-button");
                questionButton.prefWidthProperty().bind(buttonSize);
                questionButton.prefHeightProperty().bind(buttonSize);
                final Question question = quiz.getQuestions().get(i);
                questionButton.setOnAction(e -> {
                    if (teamsManager.currentTeamProperty().get() != null) {                // if null, there are no teams yet
                        showQuestionDialog(question, questionButton);
                    } else {
                        showPopup(resources.getString("inform_header-uh-oh"), resources.getString("inform_teams-first"), resources);
                    }
                });

                int rowIndex = i / rowSize;
                int colIndex = i % rowSize;
                questionsGrid.add(questionButton, colIndex, rowIndex);
            }
        }
    }

    private void configureTeamsVBox() {
        if (!teamsVBox.getChildren().contains(addTeamButton)) {
            addTeamButton = new Button("+");
            addStyle(addTeamButton, RIGHT_STYLESHEET, "add-team-button");
            addTeamButton.setOnAction(event -> {
                try {
                    TeamDialogFactory teamDialogFactory = new TeamDialogFactory(resources);
                    Dialog<Team> addTeamDialog = teamDialogFactory.createAddTeamDialog();

                    Optional<Team> result = addTeamDialog.showAndWait();
                    if (result.isPresent()) {
                        Team team = result.get();
                        teamsManager.addTeam(team);

                        FXMLLoader teamBoxLoader = new FXMLLoader(getClass()
                                .getResource("/main/resources/com/hszilard/quizzer/quizzer/teamBox.fxml"), resources);
                        Parent teamBox = teamBoxLoader.load();
                        teamBox.styleProperty().bind(when(equal(team, teamsManager.currentTeamProperty()))
                                .then("-fx-background-color: crimson;")
                                .otherwise("-fx-background-color: #78909C;"));

                        Label teamName = (Label) teamBoxLoader.getNamespace().get("nameLabel");
                        Label teamScore = (Label) teamBoxLoader.getNamespace().get("scoreLabel");
                        teamName.textProperty().bind(team.nameProperty());
                        teamName.maxWidthProperty().bind(teamsVBox.widthProperty()
                                .subtract(teamScore.widthProperty()).subtract(4));
                        teamScore.textProperty().bind(team.scoreProperty().asString());

                        teamBox.setOnMouseClicked(teamBoxEvent -> {
                            if (teamBoxEvent.getClickCount() == 2) {
                                try {
                                    Dialog<Team> editTeamDialog = teamDialogFactory.createEditTeamDialog(team);
                                    Optional<Team> editResult = editTeamDialog.showAndWait();
                                    if (editResult.isPresent())
                                        team.setName(editResult.get().getName());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        teamsVBox.getChildren().add(teamsVBox.getChildren().size() - 1, teamBox);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            teamsVBox.getChildren().add(addTeamButton);
        }
    }

    private void configureTurnsLabel() {
        if (turnsManager != null) {
            turnsLabel.textProperty().bind(new SimpleStringProperty(resources.getString("main_turns"))
                    .concat(turnsManager.currentTurnProperty().asString()
                            .concat("/").concat(turnsManager.totalTurnsProperty().asString())));
        } else {
            turnsLabel.setText(resources.getString("main_turns") + "-/-");
        }
    }

    private void showQuestionDialog(final Question question, final Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass()
                    .getResource("/main/resources/com/hszilard/quizzer/quizzer/questionScene.fxml"), resources);
            Parent root = loader.load();
            Scene questionScene = new Scene(root);
            QuestionSceneController questionSceneController = loader.getController();
            questionSceneController.setup(question, new Callback() {
                @Override
                public void onCorrect() {
                    if (teamsManager.currentTeamProperty() != null) {
                        teamsManager.currentTeamProperty().get().addToScore(DEFAULT_SCORE);
                        button.setText("" + DEFAULT_SCORE);
                    }
                    teamsManager.nextTeam();
                    turnsManager.nextQuestion();

                    button.getStyleClass().clear();
                    button.getStyleClass().add("coin-button");
                    button.setDisable(true);
                }

                @Override
                public void onIncorrect() {
                    teamsManager.nextTeam();
                    turnsManager.nextQuestion();

                    button.getStyleClass().clear();
                    button.setText("");
                    button.getStyleClass().add("empty-button");
                    button.setDisable(true);
                }
            });
            Stage questionStage = new Stage();
            questionStage.setScene(questionScene);
            questionStage.setTitle(resources.getString("question_title"));
            iconify(questionStage);
            questionStage.initOwner(questionsGrid.getScene().getWindow());          // setting up the stage-hierarchy
            questionStage.initModality(WINDOW_MODAL);                      // making the owner stage impossible to interact with until the 'top' stage is active

            questionStage.setMinHeight(200);
            questionStage.setMinWidth(400);
            questionStage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            CommonUtils.showError(resources.getString("error_generic-message"), resources);
        }
    }

    private void showCongratsPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass()
                    .getResource("/main/resources/com/hszilard/quizzer/quizzer/congratsLayout.fxml"), resources);
            Parent root = loader.load();
            ((CongratsController)loader.getController()).setTeamsManager(teamsManager);
            Scene congratsScene = new Scene(root);
            Stage congratsStage = new Stage();
            congratsStage.setScene(congratsScene);
            congratsStage.setResizable(false);
            iconify(congratsStage);
            congratsStage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "congratsLayout can't be shown!", e.getMessage());
        }
    }

}
