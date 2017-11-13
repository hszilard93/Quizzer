package test.java.com.hszilard.quizzer.common.xml_converter;

import main.java.com.hszilard.quizzer.common.xml_converter.QuizLoader;
import main.java.com.hszilard.quizzer.common.xml_converter.QuizLoadingException;
import main.java.com.hszilard.quizzer.common.xml_converter.SimpleQuizLoader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 */
@DisplayName("Testing the SimpleQuizLoader class for expected exceptions")
public class SimpleQuizLoaderTest {
    private static final String TEST_XML_PATH = "test-resources/test-quiz.xml";
    private QuizLoader quizLoaderInstance;

    @BeforeEach
    void setup() throws IOException{
        Files.deleteIfExists(Paths.get(TEST_XML_PATH));
        quizLoaderInstance = new SimpleQuizLoader();
    }

    @Test
    void loadingNonExistingQuizThrowsException() {
        Executable codeToTest = () -> quizLoaderInstance.loadQuiz(TEST_XML_PATH);

        assertThrows(QuizLoadingException.class, codeToTest);
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_XML_PATH));
    }

}
