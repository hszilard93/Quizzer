package main.java.com.hszilard.quizzer.quizeditor;

import main.java.com.hszilard.quizzer.common.quiz_model.Answer;
import main.java.com.hszilard.quizzer.common.quiz_model.Question;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * This window is displayed whenever a new Question needs to be added to a Quiz.
 */
public class NewQuestionEditController extends AbstractQuestionEditController {

    NewQuestionEditController (Callback callback, ResourceBundle resources) {
        this.question = new Question();
        this.callback = callback;
        this.resources = resources;
    }

    @Override
    protected void configureStage() {
        super.configureStage();
        stage.setTitle(resources.getString("edit_new-title"));
    }

    /* Automatically add two new possible answers to every new Question.*/
    @Override
    protected void configureAnswersVBox() throws IOException {
        question.getAnswers().add(new Answer("", true));
        question.getAnswers().add(new Answer("", false));
        super.configureAnswersVBox();
    }

    @Override
    protected void configureConfirmButton() {
        confirmButton.setText(resources.getString("edit_add-button"));
        super.configureConfirmButton();
    }

}
