package main.java.com.hszilard.quizzer.quizzer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/main/resources/com/hszilard/quizzer/quizzer/mainSceneLayout.fxml"));
        Scene mainScene = new Scene(root);
        primaryStage.setScene(mainScene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
//        primaryStage.getIcons().add(new Image("/main/resources/com/hszilard/quizzer/quizeditor/drawable/question-class-note-symbol_color.png"));
        primaryStage.show();
    }
}
