package nanorstudios.ie.mathpractice;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MathPracticeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
    }

    private void setupDatabase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}