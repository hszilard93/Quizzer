package main.java.com.hszilard.quizzer.quizeditor;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import main.java.com.hszilard.quizzer.common.CommonUtils;
import main.java.com.hszilard.quizzer.common.LocaleManager;
import main.java.com.hszilard.quizzer.common.LocationManager;
import main.java.com.hszilard.quizzer.common.quiz_model.Answer;
import main.java.com.hszilard.quizzer.common.quiz_model.Question;
import main.java.com.hszilard.quizzer.common.quiz_model.Quiz;
import main.java.com.hszilard.quizzer.common.xml_converter.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Szil�rd Hompoth at https://github.com/hszilard93
 * The controller belonging to the mainLayout.fxml layout file, effective controller or the application.
 */
public class MainController {
    private static final Logger LOGGER = Logger.getLogger(SimpleXmlQuizLoader.class.getName());

    /*
     * Fields with the @FXML annotation are automatically injected.
     */
    @FXML private ResourceBundle resources;     // contains the internationalized strings.

    @FXML MenuItem fileNewMenuItem;
    @FXML MenuItem fileOpenMenuItem;
    @FXML MenuItem fileSaveMenuItem;
    @FXML MenuItem fileSaveAsMenuItem;
    @FXML MenuItem fileExitMenuItem;
    @FXML MenuItem languageEnglishMenuItem;
    @FXML MenuItem languageHungarianMenuItem;
    @FXML MenuItem aboutMenuItem;

    @FXML Button addButton;
    @FXML Button deleteButton;
    @FXML Button editButton;
    @FXML ListView<Question> listView;
    @FXML TextField titleField;
    @FXML Label numberOfQuestionLabel;

    private Quiz quiz;                          // main Quiz object of the application
    private String quizPath;
    private boolean justSaved = false;          // true if the quiz object hasn't been modified since created, opened or saved

    /* This Callback is used both when clicking on the Edit button and when double clicking on a ListView item (passed to the cell) */
    private final AbstractQuestionEditController.Callback editCallback = questionToSave -> {
        int selectedQuestionIndex = listView.getSelectionModel().getSelectedIndex();
        Question selectedQuestion = listView.getItems().get(selectedQuestionIndex);
        listView.getItems().remove(selectedQuestion);
        listView.getItems().add(selectedQuestionIndex, questionToSave);
        quiz.updateEdited();
        justSaved = false;
    };

    /* This method is invoked automatically after the resources have been loaded; starts all configuration logic */
    @FXML
    private void initialize() {
        LOGGER.log(Level.FINE, "MainController initialization.");
        if (quiz == null) {
            quiz = new Quiz(resources.getString("main_unnamed"));
            quiz.setCreated(LocalDate.now());
            quiz.setEdited(LocalDate.now());
            justSaved = true;
        }
        configureQuestionsListView();
        configureNumberOfQuestionsLabel();
        configureTitleTextField();
        configureButtons();
        configureStage();
        configureMenuItems();
        justSaved = true;
    }
    @FXML
    private void onNewClicked() {
        LOGGER.log(Level.INFO, "New menu item clicked.");
        if (!justSaved) {
            Optional<ButtonType> result = CommonUtils.showChangeAlert(resources.getString("alert_sure-reset-text"));
            /* Don't do anything if the cancel option was chosen */
            if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                return;
            }
        }
        /* Create new quiz, start initialization logic */
        quiz = null;
        quizPath = null;
        initialize();
    }

    @FXML
    private void onOpenClicked() {
        LOGGER.log(Level.INFO, "Open menu item clicked.");
        if (!justSaved) {
            Optional<ButtonType> result = CommonUtils.showChangeAlert(resources.getString("alert_sure-text"));
            if (result.isPresent() && result.get() == ButtonType.CANCEL)
                return;
        }

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, resources.getString("chooser_open"));

        File file = fileChooser.showOpenDialog(Main.getStage());
        if (file != null) {
            try {
                QuizLoader quizLoader = new SimpleXmlQuizLoader();
                quiz = quizLoader.loadQuiz(file.getPath());
                quizPath = file.getPath();
                LocationManager.setLastPath(this.getClass(), quizPath.substring(0, quizPath.lastIndexOf(File.separator)));
                initialize();
            }
            catch (QuizLoaderException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                /* Show generic error message*/
                CommonUtils.showError(resources.getString("error_not-legal"));
            }
        }
    }

    @FXML
    private void onSaveClicked() {
        LOGGER.log(Level.INFO, "Save menu item clicked.");
        /* If we know the filepath, just save */
        if (quizPath != null) {
            if (!CommonUtils.isValidQuiz(quiz)) {
                /* If quiz is invalid, warn user before saving. */
                Optional<ButtonType> result = CommonUtils.showInvalidQuizAlert();
                if (result.isPresent() && result.get() == ButtonType.CANCEL)
                    return;
            }
            try {
                QuizExporter exporter = new SimpleXmlQuizExporter();
                exporter.exportQuiz(quiz, quizPath);
            }
            catch (QuizExporterException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                CommonUtils.showError(resources.getString("error_no-save"));
            }
            justSaved = true;
        }
        /* Else Save As */
        else {
            onSaveAsClicked();
        }
    }

    @FXML
    private void onSaveAsClicked() {
        if (!CommonUtils.isValidQuiz(quiz)) {
            /* If quiz is invalid, warn user before saving. */
            Optional<ButtonType> result = CommonUtils.showInvalidQuizAlert();
            if (result.isPresent() && result.get() == ButtonType.CANCEL)
                return;
        }
        LOGGER.log(Level.INFO, "Save As quiz menu item clicked clicked or method invoked.");
        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser, resources.getString("file_save"));
        File file = fileChooser.showSaveDialog(Main.getStage());
        if (file != null) {
            try {
                QuizExporter exporter = new SimpleXmlQuizExporter();
                exporter.exportQuiz(quiz, file.getPath());
            }
            catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                CommonUtils.showError(resources.getString("error_no-save"));
            }
            quizPath = file.getPath();
            LocationManager.setLastPath(this.getClass(), quizPath.substring(0, quizPath.lastIndexOf(File.separator)));
            justSaved = true;
        }
    }

    @FXML
    private void onExitButtonClicked() {
        LOGGER.log(Level.INFO, "Exit menu item clicked.");
        if (!justSaved) {
            Optional<ButtonType> result = CommonUtils.showChangeAlert((resources.getString("alert_sure-text")));
            if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                return;
            }
        }
        Main.getStage().close();
    }

    @FXML
    private void onEnglishLanguageSelected() {
        LOGGER.log(Level.INFO, "English language menu item clicked.");
        changeLanguage(new Locale("en"));
    }

    @FXML
    private void onHungarianLanguageSelected() {
        LOGGER.log(Level.INFO, "English language menu item clicked.");
        changeLanguage(new Locale("hu"));
    }

    @FXML
    private void onAboutClicked(ActionEvent actionEvent) {
        LOGGER.log(Level.INFO, "About menu item clicked.");
        CommonUtils.showAboutPopup();
    }

    /* Add a new question to the quiz */
    @FXML
    private void onAddButtonClicked() {
        LOGGER.log(Level.INFO, "addButton clicked.");
        NewQuestionEditController newQuestionEditController = new NewQuestionEditController(question -> {
            quiz.getQuestions().add(question);
            quiz.updateEdited();
            justSaved = false;
        }, resources);
        try {
            newQuestionEditController.display();
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            CommonUtils.showError(resources.getString("error_unexpected-error"));
        }
    }

    @FXML
    private void onDeleteButtonClicked() {
        LOGGER.log(Level.INFO, "deleteButton clicked.");

        int index = listView.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            quiz.getQuestions().remove(index);
            quiz.updateEdited();
        }

        justSaved = false;
    }

    @FXML
    void onEditButtonClicked() {
        LOGGER.log(Level.INFO, "editButton clicked.");
        ExistingQuestionEditController existingQuestionEditController =
                new ExistingQuestionEditController(listView.getSelectionModel().getSelectedItem(), editCallback,
                        resources);
        try {
            existingQuestionEditController.display();
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            CommonUtils.showError(resources.getString("error_unexpected-error"));
        }
    }

    private void changeLanguage(Locale locale) {
        if (!LocaleManager.getPreferredLocale().equals(locale))
            LocaleManager.setPreferredLocale(locale);
        else
            return;     // don't do anything if selected language is already in use

        /* Show confirmation dialog */
        Optional<ButtonType> returnType =
                CommonUtils.showChangeAlert(ResourceBundle.getBundle("main.resources.com.hszilard.quizzer.quizeditor.strings",
                        locale).getString("alert_language-change")
                                + "\n"
                                + resources.getString("alert_language-change"));
        if (!returnType.isPresent() || returnType.get() == ButtonType.CANCEL)
            return;

        LOGGER.log(Level.INFO, "Attempting to restart application because of language change.");
        Main.getStage().close();
        /*
         * We must restart the application for the language change to take place.
         * The preferred locale is already change, so if the user cancels here, he will still get the
         * selected language on nextTeam start.
         */
        Platform.runLater(() -> {
            try {
                new Main().start(new Stage());
            }
            catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Restarting application failed/n" + e.getMessage(), e);
            }
        });
    }

    private void configureQuestionsListView() {
        /* Setting up custom cells, passing a callback and the language resources */
        listView.setCellFactory(listView -> new QuestionListCell(editCallback, resources));
        /* Making use of the JavaFX bindings to bind the ListView elements to a list of questions */
        listView.itemsProperty().bindBidirectional(quiz.questionsProperty());
    }

    private void configureNumberOfQuestionsLabel() {
        StringBinding text = (StringBinding) Bindings.concat(resources.getString("main_number-of-questions"),
                quiz.questionsProperty().sizeProperty());
        numberOfQuestionLabel.textProperty().bind(text);
    }

    private void configureTitleTextField() {
        titleField.textProperty().bindBidirectional(quiz.titleProperty());
        /* Whenever the title changes, we register it as a modification of the quiz */
        titleField.textProperty().addListener(e -> justSaved = false);
    }

    private void configureButtons() {
        editButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(listView.getSelectionModel().selectedItemProperty().isNull());
    }

    private void configureFileChooser(FileChooser fileChooser, String title) {
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter(resources.getString("chooser_xml"), "*.xml"));
        if (quizPath != null) {
            /* If we already have a path to a quiz file, we get its directory */
            String directoryPath = quizPath.substring(0, quizPath.lastIndexOf(File.separator));
            fileChooser.setInitialDirectory(new File(directoryPath));
        }
        else {
            String lastPath = LocationManager.getLastPath(this.getClass());
            if (lastPath != null) {
                File directory = new File(lastPath);
                fileChooser.setInitialDirectory(
                        directory.exists() ? directory : new File(System.getProperty("user.home"))
                                               );
            }
            else {
                fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            }
        }
    }

    private void configureStage() {
        Main.getStage().titleProperty()
                .bind(Bindings.concat(resources.getString("main_window-title"), quiz.titleProperty()));
        /* Make the window X button behave as the Exit menu item */
        Main.getStage().setOnCloseRequest(e -> {
            onExitButtonClicked();
            e.consume();                // stop the event from propagating
        });
    }

    private void configureMenuItems() {
        /* Keyboard shortcuts for menu actions */
        fileNewMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        fileOpenMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        fileSaveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        fileExitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
    }

}
