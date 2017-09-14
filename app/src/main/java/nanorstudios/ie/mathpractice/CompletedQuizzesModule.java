package nanorstudios.ie.mathpractice;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * A DI module for the completed quizzes.
 */
@Module
public class CompletedQuizzesModule {
    private CompletedQuizzes sCompletedQuizzes;

    public CompletedQuizzesModule() {

    }

    public CompletedQuizzesModule(CompletedQuizzes completedQuizzes) {
        sCompletedQuizzes = completedQuizzes;
    }

    @Provides
    @Singleton
    CompletedQuizzes provideCompletedQuizzes() {
        if (sCompletedQuizzes == null) {
            sCompletedQuizzes = new CompletedQuizzes();
        }
        return sCompletedQuizzes;
    }
}
