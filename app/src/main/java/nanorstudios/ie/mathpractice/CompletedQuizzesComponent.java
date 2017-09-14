package nanorstudios.ie.mathpractice;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A DI component interface for the completed quizzes.
 */
@Singleton
@Component(modules = {AppModule.class, CompletedQuizzesModule.class})
public interface CompletedQuizzesComponent {
    CompletedQuizzes provideCompletedQuizzes();
}
