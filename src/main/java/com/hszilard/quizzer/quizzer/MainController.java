package main.java.com.hszilard.quizzer.quizzer;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.com.hszilard.quizzer.common.quiz_model.Question;
import main.java.com.hszilard.quizzer.common.quiz_model.Quiz;
import main.java.com.hszilard.quizzer.common.xml_converter.QuizLoader;
import main.java.com.hszilard.quizzer.common.xml_converter.QuizLoadingException;
import main.java.com.hszilard.quizzer.common.xml_converter.SimpleQuizLoader;
import main.java.com.hszilard.quizzer.quizzer.teams_model.Team;
import main.java.com.hszilard.quizzer.quizzer.teams_model.TeamsManager;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("Duplicates")
public class MainController {
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());
    private static final int DEFAULT_SCORE = 10;

    @FXML private ResourceBundle resources;                 // contains the internationalized strings.
    @FXML private MenuBar menuBarLayout;
    @FXML private MenuController menuBarLayoutController;
    @FXML private GridPane questionsGrid;
    @FXML private VBox teamsVBox;
    private Button addTeamButton;

    private Quiz quiz;
    private String quizPath;
    private TeamsManager teamsManager;

    @FXML
    private void initialize() {
        teamsManager = new TeamsManager();
        setupViews();
    }

    void restart() {
        teamsManager.resetTeams();
        setupViews();
    }

    void openQuiz() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resources.getString("open_title"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(resources.getString("open_format"), "*.xml"));
        if (quizPath == null) {
            /* The default path of the FileChooser is the user's home directory */
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        } else {
            /* If we already have a path to a quiz file, we get its directory */
            String quizDirectory = quizPath.substring(0, quizPath.lastIndexOf(File.separator));
            fileChooser.setInitialDirectory(new File(quizDirectory));
        }

        File file = fileChooser.showOpenDialog(menuBarLayout.getScene().getWindow());
        if (file != null) {
            try {
                Quiz quiz;
                QuizLoader quizLoader = new SimpleQuizLoader();
                quiz = quizLoader.loadQuiz(file.getPath());
                setQuiz(quiz);
                quizPath = file.getPath();
            } catch (QuizLoadingException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                /* Show generic error message*/
                Util.showError(resources.getString("error_open-message"), resources);
            }
        }
    }

    private void setupViews() {
//        ((Stage)menuBarLayout.getScene().getWindow()).setTitle(Bindings.concat(resources.getString("main_window-title"), " - ", quiz.getTitle()).toString());
        menuBarLayoutController.init(this);
        configureQuestionsGrid();
        configureTeamsVBox();
    }

    private void configureQuestionsGrid() {
        if (quiz == null) {
            Button openQuizButton = new Button(resources.getString("main_open-quiz-button"));
            openQuizButton.setOnAction(e -> openQuiz());
            questionsGrid.getChildren().clear();
            questionsGrid.getChildren().add(openQuizButton);
        } else {
            questionsGrid.getChildren().clear();
            int questionsSize = quiz.getQuestions().size();
            int rowSize = Double.valueOf(Math.ceil(Math.sqrt(questionsSize))).intValue();
            int colSize = Double.valueOf(Math.floor(Math.sqrt(questionsSize))).intValue();

            NumberBinding buttonSize = Bindings.min(questionsGrid.widthProperty().divide(rowSize), questionsGrid.heightProperty().divide(colSize)).subtract(8);

            for (int i = 0; i < questionsSize; i++) {
                Button questionButton = new Button("" + (i + 1));
                questionButton.getStylesheets().add("/main/resources/com/hszilard/quizzer/quizzer/style/grid_cell_style.css");
                questionButton.getStyleClass().add("question-button");
                questionButton.prefWidthProperty().bind(buttonSize);
                questionButton.prefHeightProperty().bind(buttonSize);
                final Question question = quiz.getQuestions().get(i);
                questionButton.setOnAction(e -> {
                    if (teamsManager.currentTeam() != null) {                // if null, there are no teams yet
                        showQuestionDialog(question, questionButton);
                    }
                    else {
                        Util.showPopup(resources.getString("inform_teams-first"), resources);
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
            addTeamButton.getStylesheets().add("/main/resources/com/hszilard/quizzer/quizzer/style/teams_vbox_styles.css");
            addTeamButton.getStyleClass().add("add-team-button");
            addTeamButton.setOnAction(event -> {
                try {
                    FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource("/main/resources/com/hszilard/quizzer/quizzer/AddTeamDialog.fxml"), resources);
                    Dialog<Team> addTeamDialog = new Dialog<>();
                    addTeamDialog.setDialogPane(dialogLoader.load());
                    TextField nameField = (TextField) dialogLoader.getNamespace().get("nameField");
                    nameField.requestFocus();

                    addTeamDialog.setTitle(resources.getString("add_title"));
                    addTeamDialog.setResultConverter(buttonType -> {
                        if (buttonType == ButtonType.APPLY) {
                            return new Team(nameField.getText(), 0);
                        }
                        return null;
                    });

                    Optional<Team> result = addTeamDialog.showAndWait();
                    if (result.isPresent()) {
                        Team team = result.get();

                        FXMLLoader teamBoxLoader = new FXMLLoader(getClass().getResource("/main/resources/com/hszilard/quizzer/quizzer/teamBox.fxml"), resources);
                        Parent teamBox = teamBoxLoader.load();
                        teamBox.styleProperty().bind(Bindings.when(Bindings.createBooleanBinding(() -> team.equals(teamsManager.currentTeam())))
                                .then("-fx-background-color: firebrick;")
                                .otherwise("-fx-background-color: gold;"));

                        Label teamName = (Label) teamBoxLoader.getNamespace().get("nameLabel");
                        Label teamScore = (Label) teamBoxLoader.getNamespace().get("scoreLabel");
                        teamName.textProperty().bind(team.nameProperty());
                        teamScore.textProperty().bind(team.scoreProperty().asString());

                        teamsManager.addTeam(team);
                        teamsVBox.getChildren().add(teamsVBox.getChildren().size() - 1, teamBox);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            teamsVBox.getChildren().add(addTeamButton);
        }
    }

    private void showQuestionDialog(final Question question, final Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/main/resources/com/hszilard/quizzer/quizzer/QuestionScene.fxml"), resources);
            Parent root = loader.load();
            Scene questionScene = new Scene(root);
            QuestionSceneController questionSceneController = loader.getController();
            questionSceneController.setup(question, new QuestionSceneController.Callback() {
                @Override
                public void onCorrect() {
                    if (teamsManager.currentTeam() != null) {
                        teamsManager.currentTeam().addToScore(DEFAULT_SCORE);
                        button.setText("" + DEFAULT_SCORE);
                    }
                    teamsManager.next();

                    button.getStyleClass().clear();
                    button.getStyleClass().add("coin-button");
                }

                @Override
                public void onIncorrect() {
                    teamsManager.next();

                    button.getStyleClass().clear();
                    button.setText("");
                    button.getStyleClass().add("empty-button");
                }
            });
            Stage questionStage = new Stage();
            questionStage.setScene(questionScene);
            questionStage.setTitle(resources.getString("question_title"));
            questionStage.initOwner(questionsGrid.getScene().getWindow());          // setting up the stage-hierarchy
            questionStage.initModality(Modality.WINDOW_MODAL);                      // making the owner stage impossible to interact with until the 'top' stage is active

            questionStage.setMinHeight(200);
            questionStage.setMinWidth(400);
            questionStage.show();
        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            Util.showError(resources.getString("error_generic-message"), resources);
        }
    }

    private void setQuiz(Quiz quiz) {
        LOGGER.log(Level.INFO, "Quiz  loaded");
        this.quiz = quiz;

        Collections.shuffle(quiz.getQuestions());
        configureQuestionsGrid();
        ((Stage)(questionsGrid.getScene().getWindow())).setTitle("Quizzer - " + quiz.getTitle());
    }
}
