package main.java.com.hszilard.quizzer.quizzer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

import static java.util.ResourceBundle.getBundle;
import static main.java.com.hszilard.quizzer.common.LocaleManager.getPreferredLocale;
import static main.java.com.hszilard.quizzer.quizzer.CommonUtils.iconify;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        ResourceBundle stringsBundle = getBundle("main.resources.com.hszilard.quizzer.quizzer.strings",
                getPreferredLocale());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/com/hszilard/quizzer/quizzer/mainLayout.fxml"), stringsBundle);
        Parent root = loader.load();
        Scene mainScene = new Scene(root);

        MainController mainController = loader.getController();

        primaryStage.setScene(mainScene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("Quizzer");
        iconify(primaryStage);
        primaryStage.show();
    }
}
