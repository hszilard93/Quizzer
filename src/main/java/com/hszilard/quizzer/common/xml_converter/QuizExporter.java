package main.java.com.hszilard.quizzer.common.xml_converter;

import main.java.com.hszilard.quizzer.common.quiz_model.Quiz;

public interface QuizExporter {
    void exportQuiz(final Quiz quiz, final String xmlPath) throws QuizExporterException;
}
