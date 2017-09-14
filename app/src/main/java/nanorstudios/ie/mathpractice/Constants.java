package nanorstudios.ie.mathpractice;

/**
 * Contains the static constants of the app.
 */

public class Constants {
    public static final String CHOSEN_NUMBER = "ChosenNum";
    public static final String CHOSEN_OPERATOR = "OperatorKey";

    public static class RequestCodes {
        public static int QUIZ = 100;
    }

    public static class ResultCodes {
        public static int QUIZ_FINISHED = 200;
        public static int QUIZ_QUIT = 200;
    }

    public static class OperatorKeys {
        public static String ADDITION = "addition";
        public static String DIVISION = "division";
        public static String MULTIPLICATION = "multiplaction";
        public static String SUBTRACTION = "subtraction";
    }

    public static class Preferences {
        public static String NAME = "mathPractice";
        public static String COMPLETED_QUIZZES = "completedQuizzes";
    }
}
