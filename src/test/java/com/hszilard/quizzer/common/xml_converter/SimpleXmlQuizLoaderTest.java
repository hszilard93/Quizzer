package test.java.com.hszilard.quizzer.common.xml_converter;

import main.java.com.hszilard.quizzer.common.xml_converter.QuizLoader;
import main.java.com.hszilard.quizzer.common.xml_converter.QuizLoaderException;
import main.java.com.hszilard.quizzer.common.xml_converter.SimpleXmlQuizLoader;
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
@DisplayName("Testing the SimpleXmlQuizLoader class for expected exceptions")
class SimpleXmlQuizLoaderTest {
    private static final String TEST_XML_PATH = "test-resources/test-quiz.xml";
    private QuizLoader quizLoader;

    @BeforeEach
    void setup() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_XML_PATH));
        quizLoader = new SimpleXmlQuizLoader();
    }

    @Test
    void loadingNonExistingQuizThrowsException() {
        Executable codeToTest = () -> quizLoader.loadQuiz(TEST_XML_PATH);

        assertThrows(QuizLoaderException.class, codeToTest);
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_XML_PATH));
    }

}
