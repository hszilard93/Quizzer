package main.java.com.hszilard.quizzer.quizeditor;

import main.java.com.hszilard.quizzer.common.quiz_model.Question;

import java.util.ResourceBundle;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * This window is displayed whenever an existing Question needs to be edited.
 */
public class ExistingQuestionEditController extends AbstractQuestionEditController {

    ExistingQuestionEditController (Question question, Callback callback, ResourceBundle resources) {
        // Essentially cloning the existing question.
        this.question = new Question();
        this.question.setQuestionText(question.getQuestionText());
        this.question.answersProperty().set(question.getAnswers());
        this.question.difficultyObjectProperty().set(question.getDifficulty());

        this.callback = callback;
        this.resources = resources;
    }

    @Override
    protected void configureStage() {
        super.configureStage();
        stage.setTitle(resources.getString("edit_existing-title"));
    }

    @Override
    protected void configureConfirmButton() {
        confirmButton.setText(resources.getString("edit_confirm-changes-button"));
        super.configureConfirmButton();
    }

}
