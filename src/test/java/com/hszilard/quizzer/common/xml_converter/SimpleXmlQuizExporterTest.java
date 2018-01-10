package test.java.com.hszilard.quizzer.common.xml_converter;

import main.java.com.hszilard.quizzer.common.quiz_model.Quiz;
import main.java.com.hszilard.quizzer.common.xml_converter.*;
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
@DisplayName("Testing the SimpleXmlQuizExporter class for expected exceptions")
class SimpleXmlQuizExporterTest {
    private static final String TEST_XML_PATH = "test-resources/test-quiz.xml";
    private QuizExporter quizExporterInstance;

    @BeforeEach
    void setup() {
        quizExporterInstance = new SimpleXmlQuizExporter();
    }

    @Test
    void savingNullQuizThrowsException() {
        Executable codeToTest = () -> quizExporterInstance.exportQuiz(null, TEST_XML_PATH);

        assertThrows(QuizExporterException.class, codeToTest);
    }

    @Test
    void savingToNullPathThrowsException() {
        Executable codeToTest = () -> quizExporterInstance.exportQuiz(new Quiz(), null);

        assertThrows(QuizExporterException.class, codeToTest);
    }

    @AfterAll
    static void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_XML_PATH));
    }
}
