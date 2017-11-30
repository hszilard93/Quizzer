package main.java.com.hszilard.quizzer.quizzer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.java.com.hszilard.quizzer.common.LocaleManager;

import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        ResourceBundle stringsBundle = ResourceBundle.getBundle("main.resources.com.hszilard.quizzer.quizzer.strings",
                LocaleManager.getPreferredLocale());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/com/hszilard/quizzer/quizzer/mainSceneLayout.fxml"), stringsBundle);
        Parent root = loader.load();
        Scene mainScene = new Scene(root);

        MainController mainController = loader.getController();

        primaryStage.setScene(mainScene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("Quizzer");
        primaryStage.getIcons().add(new Image("/main/resources/com/hszilard/quizzer/quizzer/drawable/question.png"));
        primaryStage.show();
    }
}
