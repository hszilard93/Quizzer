package main.java.com.hszilard.quizzer.common.xml_converter;

public class QuizExportingException extends Exception {

    public QuizExportingException() { }

    public QuizExportingException(String msg) {
        super(msg);
    }

    public QuizExportingException(Throwable cause) {
        super(cause);
    }

    public QuizExportingException(String msg, Throwable cause) {
        super(msg, cause);
    }

}