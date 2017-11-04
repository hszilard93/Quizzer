package main.java.com.hszilard.quizzer.common.xml_converter;

import main.java.com.hszilard.quizzer.common.quiz_model.Answer;
import main.java.com.hszilard.quizzer.common.quiz_model.Question;
import main.java.com.hszilard.quizzer.common.quiz_model.Quiz;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Szilárd Hompoth at https://github.com/hszilard93
 * Contains static methods for exporting Quiz objects in .xml format.
 */
public class QuizExporter {

    private static final Logger LOGGER = Logger.getLogger(QuizLoader.class.getName());

    /**
     * @param quiz Quiz object to be exported
     * @param xmlPath the filepath of the .xml document that will be created (or overwritten)
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static void export(final Quiz quiz, final String xmlPath) throws ParserConfigurationException, TransformerException {
        /* Building document */
        LOGGER.log(Level.FINE, "Attempting to export quiz as: " + xmlPath);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        /* Add root <quiz> element to document */
        Element quizElement = document.createElement(Values.QUIZ_TAG);
        document.appendChild(quizElement);

        quizElement.setAttribute(Values.CREATED_ATTR, quiz.getCreated().toString());
        quizElement.setAttribute(Values.EDITED_ATTR, quiz.getEdited().toString());
        quizElement.appendChild(getTextElement(document, Values.TITLE_TAG, quiz.getTitle()));

        List<Question> questionsList = quiz.getQuestions();
        /* Add the questions to the document */
        for (Question question : questionsList) {
            quizElement.appendChild(getQuestionElement(document, question));
        }

        writeQuiz(document, xmlPath);
        LOGGER.log(Level.INFO, "Quiz successfully exported as: " + xmlPath);

    }

    private static void writeQuiz(Document document, String xmlPath) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        /* Format XML output */
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(document);
        StreamResult file = new StreamResult(new File(xmlPath));

        transformer.transform(source, file);
    }

    private static Node getQuestionElement(Document doc, Question question) {
        Element questionElement = doc.createElement(Values.QUESTION_TAG);
        questionElement.appendChild(getTextElement(doc, Values.QUESTION_TEXT_TAG, question.getQuestionText()));

        for (Answer answer : question.getAnswers()) {
            questionElement.appendChild(getAnswerElement(doc, answer));
        }
        return questionElement;
    }


    private static Node getAnswerElement(Document doc, Answer answer) {
        Element answerElement = doc.createElement(Values.ANSWER_TAG);
        answerElement.setAttribute(Values.CORRECT_ATTR, Boolean.toString(answer.isCorrect()));
        answerElement.appendChild(getTextElement(doc, Values.ANSWER_TEXT_TAG, answer.getAnswerText()));
        return answerElement;
    }

    private static Node getTextElement(Document doc, String name, String value) {
        Element textElement = doc.createElement(name);
        textElement.appendChild(doc.createTextNode(value));
        return textElement;
    }


}
