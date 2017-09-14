package nanorstudios.ie.mathpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This activity is used to quiz the user.
 */

public class QuizActivity extends AppCompatActivity implements EndOfQuizFragment.EndOfQuizCallbacks {

    @BindView(R.id.tv_question) TextView tvQuestion;
    @BindView(R.id.btn_opt1) Button btnOptOne;
    @BindView(R.id.btn_opt2) Button btnOptTwo;
    @BindView(R.id.btn_opt3) Button btnOptThree;
    @BindView(R.id.btn_cheat) Button btnCheat;
    @BindView(R.id.iv_feedback) ImageView ivFeedback;
    @BindView(R.id.button_container) LinearLayout buttonContainer;

    private int mChosenNumber;
    private OperatorEnum mOperator;
    private int mCorrectAnswer;

    private int[] standardQuizNumbers = {0,1,2,3,4,5,6,7,8,9,10,11,12};

    private List<Integer> usedNumbers = new ArrayList<>(0);
    private List<Button> buttonList;
    private Button mCorrectAnswerButton;

    // TODO: 14/09/2017 Removing for v1
//    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);
        extractExtras();
        setupUI();
        setupToolbar();
    }

    private void extractExtras() {
        if (getIntent() == null) {
            return;
        }
        if (getIntent().hasExtra(Constants.CHOSEN_NUMBER)) {
            mChosenNumber = getIntent().getIntExtra(Constants.CHOSEN_NUMBER, -1);
        }
        if (getIntent().hasExtra(Constants.CHOSEN_OPERATOR)) {
            mOperator = (OperatorEnum) getIntent().getSerializableExtra(Constants.CHOSEN_OPERATOR);
        }
        verifyChosenNumber();
    }

    private void verifyChosenNumber() {
        if (mChosenNumber == -1) {
            Toast.makeText(this, getString(R.string.something_broke), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupUI() {
        setupQuestionTextView();
        setupButtons();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupQuestionTextView() {
        String operatorSymbol = "";

//        int randomNumber = getRandomNumberFromArray();
//
//        if (endOfQuizReached(randomNumber)) {
//            endQuiz();
//            return;
//        }

        int randomNumber = -1;


        switch (mOperator) {
            case ADDITION:

                randomNumber = getRandomNumberFromArray();

                if (endOfQuizReached(randomNumber)) {
                    endQuiz();
                    return;
                }

                operatorSymbol = getString(R.string.addition_symbol);
                mCorrectAnswer = randomNumber + mChosenNumber;
                break;
            case SUBTRACTION:

                randomNumber = getRandomSubtractionNumberFromArray(mChosenNumber);

                operatorSymbol = getString(R.string.subtraction_symbol);

                if (randomNumber > mChosenNumber) {
                    mCorrectAnswer = randomNumber - mChosenNumber;
                } else if (randomNumber < mCorrectAnswer) {
                    mCorrectAnswer = mChosenNumber - randomNumber;
                } else {
                    mCorrectAnswer = randomNumber;
                }

                break;
            case MULTIPLICATION:
                operatorSymbol = getString(R.string.multiplication_symbol);
                mCorrectAnswer = randomNumber * mChosenNumber;
                break;
            case DIVISION:
                operatorSymbol = getString(R.string.division_symbol);
                mCorrectAnswer = randomNumber/mChosenNumber;
                break;
        }

        if (randomNumber > mChosenNumber) {
            tvQuestion.setText(String.valueOf(randomNumber) + " " + operatorSymbol + " " + String.valueOf(mChosenNumber) + " = ?" );
        } else if (randomNumber < mCorrectAnswer) {
            tvQuestion.setText(String.valueOf(mChosenNumber) + " " + operatorSymbol + " " + String.valueOf(randomNumber) + " = ?" );
        }
    }

    private boolean endOfQuizReached(int randomNumber) {
        return randomNumber == -1;
    }

    private void setupButtons() {
        setupButtonList();
        setupButtonText();
    }

    private void setupButtonList() {
        buttonList = new ArrayList<>(3);
        buttonList.add(btnOptOne);
        buttonList.add(btnOptTwo);
        buttonList.add(btnOptThree);
    }

    private void setupButtonText() {
        int correctAnswerButton = new Random().nextInt(buttonList.size());

        mCorrectAnswerButton = buttonList.get(correctAnswerButton);
        mCorrectAnswerButton.setText(String.valueOf(mCorrectAnswer));

        List<Integer> randomWrongAnswerList = new ArrayList<>();
        for (Button button : buttonList) {
            if (button != buttonList.get(correctAnswerButton)) {
                int randomWrongAnswer = -1;
                randomWrongAnswer = getRandomWrongAnswer(randomWrongAnswerList, randomWrongAnswer);
                randomWrongAnswerList.add(randomWrongAnswer);
                button.setText(String.valueOf(randomWrongAnswer));
            }
        }
    }

    private int getRandomWrongAnswer(List<Integer> randomWrongAnswerList, int randomWrongAnswer) {
        while (randomWrongAnswer == -1) {
            randomWrongAnswer = new Random().nextInt(standardQuizNumbers.length);
            if (randomWrongAnswerList.contains(randomWrongAnswer) || randomWrongAnswer == mCorrectAnswer) {
                randomWrongAnswer = -1;
            }
        }
        return randomWrongAnswer;
    }

    private int getRandomNumberFromArray() {
        if (usedNumbers.size() == standardQuizNumbers.length) {
            return -1;
        }

        int randomNumber = new Random().nextInt(standardQuizNumbers.length);
        if (usedNumbers.contains(randomNumber)) {
            return getRandomNumberFromArray();
        }

        usedNumbers.add(randomNumber);
        return randomNumber;
    }

    private int getRandomSubtractionNumberFromArray(int chosenNumber) {
        int[] subtractionQuizNumbers = new int[13];
        for (int i = 0; i < standardQuizNumbers.length; i++) {
            subtractionQuizNumbers[i] = standardQuizNumbers[i] + chosenNumber;
        }

        int randomNumber = new Random().nextInt(subtractionQuizNumbers[subtractionQuizNumbers.length - 1] - chosenNumber) + chosenNumber;
        if (usedNumbers.contains(randomNumber)) {
            return getRandomSubtractionNumberFromArray(chosenNumber);
        }

        usedNumbers.add(randomNumber);
        return randomNumber;
    }

    @OnClick({R.id.btn_opt1, R.id.btn_opt2, R.id.btn_opt3})
    public void onClickOptOne(Button clickedBtn) {
        handleBtnClick(clickedBtn);
    }

    private void handleBtnClick(Button button) {
        if (button == mCorrectAnswerButton) {
            showSuccessAnimation();
            setupUI();
        } else {
            showFailureAnimation();
        }
    }

    private void showSuccessAnimation() {
    }

    private void showFailureAnimation() {
    }

    private void endQuiz() {
        btnCheat.setVisibility(View.GONE);
        updateDatabase();
        EndOfQuizFragment fragment = EndOfQuizFragment.newInstance(mChosenNumber);
        getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(buttonContainer, buttonContainer.getTransitionName())
                .add(R.id.frame_container, fragment, NumberListFragment.TAG)
                .commit();
    }

    private void updateDatabase() {
//        if (mRootRef == null) {
//            return;
//        }
        // TODO: 14/09/2017 Removing firebase from v1
//        DatabaseReference reference = mRootRef.child("completed");

//        CompletedQuizzesComponent quizzesComponent = ((MathPracticeApplication) getApplication()).getCompletedQuizzesComponent();


        SharedPreferences preferences = getSharedPreferences(Constants.Preferences.NAME, 0);

        String storedString = preferences.getString(Constants.Preferences.COMPLETED_QUIZZES, "");
        CompletedQuizzes completedQuizzes = new CompletedQuizzes();

        if (!TextUtils.isEmpty(storedString)) {
            completedQuizzes.setQuizzes((HashMap<String, ArrayList<String>>) new Gson().
                            fromJson(storedString, new TypeToken<HashMap<String, ArrayList<String>>>(){}.getType()));
        }

        switch (mOperator) {
            case ADDITION:
                completedQuizzes.getQuizzes().get(Constants.OperatorKeys.ADDITION).add(String.valueOf(mChosenNumber));
                break;
            case SUBTRACTION:
                completedQuizzes.getQuizzes().get(Constants.OperatorKeys.SUBTRACTION).add(String.valueOf(mChosenNumber));
                break;
            case MULTIPLICATION:
                completedQuizzes.getQuizzes().get(Constants.OperatorKeys.MULTIPLICATION).add(String.valueOf(mChosenNumber));
                break;
            case DIVISION:
                completedQuizzes.getQuizzes().get(Constants.OperatorKeys.DIVISION).add(String.valueOf(mChosenNumber));
                break;
        }
//        reference.setValue(quizzesComponent.provideCompletedQuizzes().getQuizzes());
        writeCompletedQuizzesToSharedPreferences(preferences.edit(), completedQuizzes);
    }

    private void writeCompletedQuizzesToSharedPreferences(SharedPreferences.Editor prefEditor, CompletedQuizzes completedQuizzes) {

        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.enableComplexMapKeySerialization().setPrettyPrinting().create();
        Type type = new TypeToken<HashMap<String, ArrayList<String>>>(){}.getType();
        String json = gson.toJson(completedQuizzes.getQuizzes(), type);

        prefEditor.putString(Constants.Preferences.COMPLETED_QUIZZES, json);
        prefEditor.apply();
    }

    @Override
    public void onBackPressed() {
        if (getParent() != null) {
            getParent().setResult(Constants.ResultCodes.QUIZ_QUIT);
        } else {
            setResult(Constants.ResultCodes.QUIZ_QUIT);
        }
        super.onBackPressed();
    }

    @Override
    public void closeActivity() {
        Intent data = new Intent();
        data.putExtra(Constants.CHOSEN_NUMBER, mChosenNumber);
        setResult(Constants.ResultCodes.QUIZ_FINISHED, data);
        finish();
    }

    @OnClick(R.id.btn_cheat)
    public void onClickCheat() {
        endQuiz();
    }
}