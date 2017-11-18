package main.java.com.hszilard.quizzer.quizzer;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.binding.IntegerExpression;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import main.java.com.hszilard.quizzer.common.quiz_model.Quiz;
import main.java.com.hszilard.quizzer.common.xml_converter.QuizLoader;
import main.java.com.hszilard.quizzer.common.xml_converter.QuizLoadingException;
import main.java.com.hszilard.quizzer.common.xml_converter.SimpleQuizLoader;
import main.java.com.hszilard.quizzer.quizzer.teams_model.Team;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

    @FXML private MenuBar menuBarLayout;
    @FXML private MenuController menuBarLayoutController;
    @FXML private GridPane questionsGrid;
    @FXML private VBox teamsVBox;
    @FXML private Line separatorLine;

    private Quiz quiz;
    private String quizPath;
    private ObservableList<Team> teams = new SimpleListProperty<>();

    @FXML
    private void initialize() {
        menuBarLayoutController.init(this);
        setup();
    }

    private void setup() {
        configureQuestionsGrid();
        configureTeamsVBox();
    }

    private void configureQuestionsGrid() {
        if (quiz == null) {
            Button openQuizButton = new Button("Open a quiz");
            openQuizButton.setOnAction(e -> openQuiz());
            questionsGrid.getChildren().clear();
            questionsGrid.getChildren().add(openQuizButton);
        } else {
            questionsGrid.getChildren().clear();
            int questionsSize = quiz.getQuestions().size();
            int rowSize = Double.valueOf(Math.ceil(Math.sqrt(questionsSize))).intValue();
            int colSize = Double.valueOf(Math.floor(Math.sqrt(questionsSize))).intValue();
            for (int i = 0; i < questionsSize; i++) {
                Button questionButton = new Button("Question" + (i + 1));
                questionButton.getStylesheets().add("/main/resources/com/hszilard/quizzer/quizzer/style/grid_cell_style.css");
                questionButton.getStyleClass().add("grid-cell-button");
//                questionButton.setGraphic(new ImageView("/main/resources/com/hszilard/quizzer/quizzer/drawable/safe-small.png"));
                NumberBinding size = Bindings.min(questionsGrid.widthProperty().divide(rowSize), questionsGrid.heightProperty().divide(colSize));
                questionButton.prefWidthProperty().bind(size);
                questionButton.prefHeightProperty().bind(size);

                int rowIndex = i / rowSize;
                int colIndex = i % rowSize;
                questionsGrid.add(questionButton, colIndex, rowIndex);
            }
        }
    }

    private void configureTeamsVBox() {
        Button addTeamButton = new Button("+");
        addTeamButton.getStylesheets().add("/main/resources/com/hszilard/quizzer/quizzer/style/teams_vbox_styles.css");
        addTeamButton.getStyleClass().add("add-team-button");
        addTeamButton.setOnAction(event -> {
            try {
                FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource("/main/resources/com/hszilard/quizzer/quizzer/AddTeamDialog.fxml"));
                Dialog<Team> addTeamDialog = new Dialog<>();
                addTeamDialog.setDialogPane(dialogLoader.load());
                TextField nameField = (TextField) dialogLoader.getNamespace().get("nameField");
                nameField.requestFocus();

                addTeamDialog.setTitle("Add team");
                addTeamDialog.setResultConverter(buttonType -> {
                    if (buttonType == ButtonType.APPLY) {
                        return new Team(nameField.getText(), 0);
                    }
                    return null;
                });

                Optional<Team> result = addTeamDialog.showAndWait();
                if (result.isPresent()) {
                    FXMLLoader teamBoxLoader = new FXMLLoader(getClass().getResource("/main/resources/com/hszilard/quizzer/quizzer/teamBox.fxml"));
                    AnchorPane teamBox = (AnchorPane) teamBoxLoader.load();
                    Label teamName = (Label) teamBoxLoader.getNamespace().get("nameLabel");
                    Label teamScore = (Label) teamBoxLoader.getNamespace().get("scoreLabel");

                    Team team = result.get();
                    teamName.textProperty().bind(team.nameProperty());
                    teamScore.textProperty().bind(team.scoreProperty().asString());

                    teamsVBox.getChildren().add(teamsVBox.getChildren().size() - 1, teamBox);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        teamsVBox.getChildren().add(addTeamButton);
    }

    void openQuiz() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a quiz");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Quiz XML files", "*.xml"));
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
                // TODO: show popup open dialog
            }
        }
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        LOGGER.log(Level.INFO, "Quiz  loaded");

        this.quiz = quiz;
        configureQuestionsGrid();
    }
}
