package main.java.com.hszilard.quizzer.quizeditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.java.com.hszilard.quizzer.common.LocaleManager;

import java.util.ResourceBundle;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * The Quiz Editor's Application object.
 */
public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        /* This bundle contains all the internationalized strings. */
        ResourceBundle stringsBundle = ResourceBundle.getBundle("main.resources.com.hszilard.quizzer.quizeditor.strings",
                LocaleManager.getPreferredLocale());
        /* Loads the root object of the mainScene and initializes its controller (MainController.java) */
        Parent root = FXMLLoader.load(getClass().getResource("/main/resources/com/hszilard/quizzer/quizeditor/mainSceneLayout.fxml"), stringsBundle);
        Scene mainScene = new Scene(root, 800, 600);
        primaryStage.setScene(mainScene);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.getIcons().add(new Image("/main/resources/com/hszilard/quizzer/quizeditor/drawable/question-class-note-symbol_color.png"));
        primaryStage.show();
    }

    /**
     *  The entry point of the application.
     */
    public static void main(String[] args) {
        launch(args);
    }

    static Stage getPrimaryStage() {
        return primaryStage;
    }

}
