package nanorstudios.ie.mathpractice;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * TODO Update this line
 */
@Module
public class AppModule {

    private MathPracticeApplication mApplication;

    public AppModule(MathPracticeApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    public Application providesApplication() {
        return mApplication;
    }
}
