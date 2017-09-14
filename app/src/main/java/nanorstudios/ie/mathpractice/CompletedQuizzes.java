package nanorstudios.ie.mathpractice;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Entity for completed quizzes.
 */
@Singleton
public class CompletedQuizzes implements Parcelable {
    private HashMap<String, ArrayList<String>> mQuizzes;

    public CompletedQuizzes() {
        init();
    }

    @Inject
    public CompletedQuizzes(HashMap<String, ArrayList<String>> quizzes) {
        mQuizzes = quizzes;
    }

    protected CompletedQuizzes(Parcel in) {
        setQuizzes((HashMap<String, ArrayList<String>>) in.readSerializable());
    }

    public HashMap<String, ArrayList<String>> getQuizzes() {
        return mQuizzes;
    }

    public void setQuizzes(HashMap<String, ArrayList<String>> quizzes) {
        mQuizzes = quizzes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(getQuizzes());
    }

    public static final Parcelable.Creator<CompletedQuizzes> CREATOR = new Parcelable.Creator<CompletedQuizzes>() {
        @Override
        public CompletedQuizzes createFromParcel(Parcel source) {
            return new CompletedQuizzes(source);
        }

        @Override
        public CompletedQuizzes[] newArray(int size) {
            return new CompletedQuizzes[size];
        }
    };


    private void init() {
        mQuizzes = new HashMap<>();
        mQuizzes.put(Constants.OperatorKeys.SUBTRACTION, new ArrayList<String>());
        mQuizzes.put(Constants.OperatorKeys.ADDITION, new ArrayList<String>());
        mQuizzes.put(Constants.OperatorKeys.MULTIPLICATION, new ArrayList<String>());
        mQuizzes.put(Constants.OperatorKeys.DIVISION, new ArrayList<String>());
    }
}
