package nanorstudios.ie.mathpractice;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MathPracticeApplication extends Application {
    // TODO: 14/09/2017 Removing for v1
//    private CompletedQuizzesComponent sCompletedQuizzesComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
        // TODO: 14/09/2017 Removing for v1
//        sCompletedQuizzesComponent = initDagger(this);

    }
    // TODO: 14/09/2017 Removing for v1
//    public CompletedQuizzesComponent getCompletedQuizzesComponent() {
//        return sCompletedQuizzesComponent;
//    }
// TODO: 14/09/2017 Removing for v1
//    protected CompletedQuizzesComponent initDagger(MathPracticeApplication application) {
//        return DaggerCompletedQuizzesComponent.builder()
//                .completedQuizzesModule(new CompletedQuizzesModule())
//                .build();
//    }

    private void setupDatabase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}