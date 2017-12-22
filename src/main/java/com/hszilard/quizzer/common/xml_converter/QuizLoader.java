package main.java.com.hszilard.quizzer.common.xml_converter;

import main.java.com.hszilard.quizzer.common.quiz_model.Quiz;

public interface QuizLoader {
    Quiz loadQuiz (String path) throws QuizLoaderException;
}
