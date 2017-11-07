package main.java.com.hszilard.quizzer.common.xml_converter;

import main.java.com.hszilard.quizzer.common.quiz_model.Answer;
import main.java.com.hszilard.quizzer.common.quiz_model.Question;
import main.java.com.hszilard.quizzer.common.quiz_model.Quiz;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Szil�rd Hompoth at https://github.com/hszilard93
 * This class is responsible for loading Quizez from XML files.
 */
public class SimpleQuizLoader implements QuizLoader{

    private static final Logger LOGGER = Logger.getLogger(SimpleQuizLoader.class.getName());

    /**
     * @param xmlPath the filepath of the .xml file to be loaded
     * @return Quiz object
     * @throws QuizLoadingException
     */
    public Quiz loadQuiz(String xmlPath) throws QuizLoadingException {
        LOGGER.log(Level.INFO, "loadQuiz invoked.");

        Quiz quiz;
        try {
            /* Load the .xml quiz into memory. */
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(xmlPath));
            document.getDocumentElement().normalize();

            /* Getting the root element, which is <quiz>. */
            Element quizElement = document.getDocumentElement();
            String created = quizElement.getAttribute(Values.CREATED_ATTR);
            String edited = quizElement.getAttribute(Values.EDITED_ATTR);
            String title = quizElement.getElementsByTagName(Values.TITLE_TAG).item(0).getTextContent();
            LOGGER.log(Level.FINE, "Loading quiz:\n title: " + title + " | created: " + created + " | edited: " + edited);
            quiz = new Quiz(title);
            quiz.setCreated(LocalDate.parse(created));
            quiz.setEdited(LocalDate.parse(edited));
            quiz.getQuestions().addAll(loadQuestions(quizElement));
            LOGGER.log(Level.INFO, "Quiz successfully loaded.");
        }
        catch (ParserConfigurationException | IOException | SAXException e) {
            throw new QuizLoadingException("Unable to load Quiz", e);
        }
        return quiz;
    }

    private List<Question> loadQuestions(Element quizElement) {
        List<Question> questionsList = new ArrayList<>();

        NodeList questionNodeList = quizElement.getElementsByTagName(Values.QUESTION_TAG);
        for (int i = 0; i < questionNodeList.getLength(); i++) {
            Element questionElement = (Element) questionNodeList.item(i);
            String questionText = questionElement.getElementsByTagName(Values.QUESTION_TEXT_TAG).item(0).getTextContent();
            Question question = new Question(questionText);
            // loading all the answers belinging to this question
            question.getAnswers().addAll(loadAnswers(questionElement));
            questionsList.add(question);
            LOGGER.log(Level.FINE, "Question # " + i + " loaded.\n" +
                    "question_text=" + questionText);
        }

        return questionsList;
    }

    private List<Answer> loadAnswers(Element questionElement) {
        List<Answer> answersList = new ArrayList<>();

        NodeList answersNodeList = questionElement.getElementsByTagName(Values.ANSWER_TAG);
        for (int i = 0; i < answersNodeList.getLength(); i++) {
            Element answerElement = (Element) answersNodeList.item(i);
            String answerText = answerElement.getElementsByTagName(Values.ANSWER_TEXT_TAG).item(0).getTextContent();
            boolean correct = Boolean.parseBoolean(answerElement.getAttribute(Values.CORRECT_ATTR));
            answersList.add(new Answer(answerText, correct));
            LOGGER.log(Level.FINE, "Answer #" + i + "loaded.\n" +
                    "answer_text=" + answerText + "| correct=" + correct);
        }

        return answersList;
    }

}