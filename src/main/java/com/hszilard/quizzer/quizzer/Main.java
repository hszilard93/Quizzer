package main.java.com.hszilard.quizzer.quizzer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;
import java.util.logging.Logger;

import static java.util.ResourceBundle.getBundle;
import static java.util.logging.Level.INFO;
import static main.java.com.hszilard.quizzer.common.LocaleManager.getPreferredLocale;
import static main.java.com.hszilard.quizzer.quizzer.CommonUtils.iconify;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 */
public class Main extends Application {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.log(INFO, "Starting application.");

        ResourceBundle stringsBundle = getBundle("main.resources.com.hszilard.quizzer.quizzer.strings",
                getPreferredLocale());
        LOGGER.log(INFO, "The preferred language is: " + stringsBundle.getLocale().getLanguage());

        Parent root = FXMLLoader.load(getClass()
                .getResource("/main/resources/com/hszilard/quizzer/quizzer/mainLayout.fxml"),
                stringsBundle);
        Scene mainScene = new Scene(root);

        primaryStage.setScene(mainScene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("Quizzer");
        iconify(primaryStage);
        primaryStage.show();
    }
}
