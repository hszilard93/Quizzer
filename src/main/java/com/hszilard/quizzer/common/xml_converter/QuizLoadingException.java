package main.java.com.hszilard.quizzer.common.xml_converter;

public class QuizLoadingException extends Exception {

    public QuizLoadingException() { }

    public QuizLoadingException(String msg) {
        super(msg);
    }

    public QuizLoadingException(Throwable cause) {
        super(cause);
    }

    public QuizLoadingException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
