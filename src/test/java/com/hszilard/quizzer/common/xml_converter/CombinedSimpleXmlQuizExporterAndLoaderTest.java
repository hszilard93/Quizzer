package test.java.com.hszilard.quizzer.common.xml_converter;

import com.sun.istack.internal.logging.Logger;
import main.java.com.hszilard.quizzer.common.quiz_model.Answer;
import main.java.com.hszilard.quizzer.common.quiz_model.Difficulty;
import main.java.com.hszilard.quizzer.common.quiz_model.Question;
import main.java.com.hszilard.quizzer.common.quiz_model.Quiz;
import main.java.com.hszilard.quizzer.common.xml_converter.*;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.Level;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 */
@DisplayName("Combined testing of the SimpleXmlQuizExporter and SimpleXmlQuizLoader classes' functionality")
public class CombinedSimpleXmlQuizExporterAndLoaderTest {
    private static final String TEST_XML_PATH = "test-resources/test-quiz.xml";

    private QuizExporter quizExporterInstance;
    private QuizLoader quizLoaderInstance;

    @AfterAll
    static void cleanupAll() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_XML_PATH));
    }

    @BeforeEach
    void setupEach() {
        quizExporterInstance = new SimpleXmlQuizExporter();
        quizLoaderInstance = new SimpleXmlQuizLoader();
    }

    @Test
    void savingAndLoadingEmptyQuizEqualsOriginal() throws QuizExporterException, QuizLoaderException {
        Quiz originalQuiz = new Quiz();

        quizExporterInstance.exportQuiz(originalQuiz, TEST_XML_PATH);
        Quiz recreatedQuiz = quizLoaderInstance.loadQuiz(TEST_XML_PATH);

        assertThat(originalQuiz, equalTo(recreatedQuiz));
    }

    @Test
    void savingAndLoadingSimpleQuizEqualsOriginal() throws QuizExporterException, QuizLoaderException {
        Quiz originalQuiz = new Quiz("Simple Quiz");

        Question question1 = new Question("Question1 some text");
        Answer answer1ToQuestion1 = new Answer("Some answer 1", true);
        Answer answer2ToQuestion1 = new Answer("Some answer 2", false);
        Answer answer3ToQuestion1 = new Answer("Some answer 3", false);
        question1.answersProperty().addAll(answer1ToQuestion1, answer2ToQuestion1, answer3ToQuestion1);
        question1.setDifficulty(Difficulty.EASY);

        Question question2 = new Question("Question2 some other text");
        Answer answer1ToQuestion2 = new Answer("Some answer 4", false);
        Answer answer2ToQuestion2 = new Answer("Some answer 5", false);
        Answer answer3ToQuestion2 = new Answer("Some answer 6", true);
        Answer answer4ToQuestion2 = new Answer("Some answer 7", false);
        question2.answersProperty().addAll(answer1ToQuestion2, answer2ToQuestion2, answer3ToQuestion2, answer4ToQuestion2);
        question2.setDifficulty(new Difficulty(5));             // custom difficulty

        originalQuiz.getQuestions().addAll(question1, question2);


        quizExporterInstance.exportQuiz(originalQuiz, TEST_XML_PATH);
        Quiz recreatedQuiz = quizLoaderInstance.loadQuiz(TEST_XML_PATH);


        assertThat(originalQuiz, equalTo(recreatedQuiz));
    }

    @Test
    void savingAndLoadingRandomQuizEqualsOriginal() throws QuizLoaderException, QuizExporterException {
        /* Generate and log a seed for random */
        final int seed = (int)System.currentTimeMillis();
        Logger.getLogger(this.getClass()).log(Level.INFO, "Random seed is: " + seed);

        Random random = new Random(seed);
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder().
                withinRange('0', 'Z').
                usingRandom(random::nextInt).build();
        /* Make quiz with random title, random number of questions */
        String quizTitle = randomStringGenerator.generate(random.nextInt(256));
        Quiz originalQuiz = new Quiz(quizTitle);

        int numberOfQuestions = random.nextInt(256);
        /* Make questions with random texts, random number of answers */
        for (int i = 0; i < numberOfQuestions; i++) {
            String questionText = randomStringGenerator.generate(random.nextInt(256));
            Question question = new Question(questionText);
            Difficulty difficulty = new Difficulty(random.nextInt(Integer.MAX_VALUE) + 1);
            question.setDifficulty(difficulty);

            int numberOfAnswers = random.nextInt(64);
            /* Make answers with random texts and 'correctness' */
            for (int j = 0; j < numberOfAnswers; j++) {
                String answerText = randomStringGenerator.generate(random.nextInt(256));
                boolean correct = random.nextBoolean();
                Answer answer = new Answer(answerText, correct);
                question.getAnswers().add(answer);
            }
            originalQuiz.getQuestions().add(question);
        }


        quizExporterInstance.exportQuiz(originalQuiz, TEST_XML_PATH);
        Quiz recreatedQuiz = quizLoaderInstance.loadQuiz(TEST_XML_PATH);


        assertThat(originalQuiz, equalTo(recreatedQuiz));
    }

}
