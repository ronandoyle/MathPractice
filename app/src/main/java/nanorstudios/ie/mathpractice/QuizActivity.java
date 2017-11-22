package nanorstudios.ie.mathpractice;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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

    @BindView(R.id.tv_question) TextView mTvQuestion;
    @BindView(R.id.btn_opt1) Button mBtnOptOne;
    @BindView(R.id.btn_opt2) Button mBtnOptTwo;
    @BindView(R.id.btn_opt3) Button mBtnOptThree;
    @BindView(R.id.button_container) LinearLayout mBtnContainer;
    @BindView(R.id.correct_ans_animation_view) LottieAnimationView mCorrectAnimationView;
    @BindView(R.id.wrong_ans_animation_view) LottieAnimationView mWrongAnimationView;

    private int mChosenNumber;
    private OperatorEnum mOperator;
    private int mCorrectAnswer;

    private int[] mStdQuizNumbers = {0,1,2,3,4,5,6,7,8,9,10,11,12};
    private int[] mSubQuizNumbers = new int[13];
    private int[] mDivQuizNumbers = new int[13];


    private List<Integer> mUsedNumbers = new ArrayList<>(0);
    private List<Button> mBtnList;
    private Button mCorrectAnswerButton;

    // TODO: 14/09/2017 Removing for v1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);
        extractExtras();
        initArrays();
        setupAnimationListener();
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

    private void initArrays() {
        mSubQuizNumbers = new int[13];
        for (int i = 0; i < mStdQuizNumbers.length; i++) {
            mSubQuizNumbers[i] = mStdQuizNumbers[i] + mChosenNumber;
        }
    }

    private void setupUI() {
        setupQuestionTextView();
        setupButtons();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupQuestionTextView() {
        String operatorSymbol = "";
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

                if (endOfQuizReached(randomNumber)) {
                    endQuiz();
                    return;
                }

                operatorSymbol = getString(R.string.subtraction_symbol);
                if (randomNumber > mChosenNumber) {
                    mCorrectAnswer = randomNumber - mChosenNumber;
                } else if (randomNumber < mCorrectAnswer) {
                    mCorrectAnswer = mChosenNumber - randomNumber;
                } else {
                    mCorrectAnswer = mChosenNumber - randomNumber;
                }

                break;
            case MULTIPLICATION:

                randomNumber = getRandomNumberFromArray();

                if (endOfQuizReached(randomNumber)) {
                    endQuiz();
                    return;
                }

                operatorSymbol = getString(R.string.multiplication_symbol);
                mCorrectAnswer = randomNumber * mChosenNumber;
                break;
            case DIVISION:

                randomNumber = getRandomDivNumberFromArray(mChosenNumber);

                if (endOfQuizReached(randomNumber)) {
                    endQuiz();
                    return;
                }

                operatorSymbol = getString(R.string.division_symbol);
                if (randomNumber > mChosenNumber) {
                    mCorrectAnswer = randomNumber / mChosenNumber;
                } else if (randomNumber < mCorrectAnswer) {
                    mCorrectAnswer = mChosenNumber / randomNumber;
                } else {
                    mCorrectAnswer = mChosenNumber / randomNumber;
                }
                break;
        }

        if (randomNumber < mCorrectAnswer) {
            mTvQuestion.setText(
                    String.format(getString(R.string.question_text), String.valueOf(mChosenNumber),
                            operatorSymbol, String.valueOf(randomNumber)));
        } else {
            mTvQuestion.setText(
                    String.format(getString(R.string.question_text), String.valueOf(randomNumber),
                            operatorSymbol, String.valueOf(mChosenNumber)));
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
        mBtnList = new ArrayList<>(3);
        mBtnList.add(mBtnOptOne);
        mBtnList.add(mBtnOptTwo);
        mBtnList.add(mBtnOptThree);
    }

    private void setupButtonText() {
        int correctAnswerButton = new Random().nextInt(mBtnList.size());

        mCorrectAnswerButton = mBtnList.get(correctAnswerButton);
        mCorrectAnswerButton.setText(String.valueOf(mCorrectAnswer));

        List<Integer> randomWrongAnswerList = new ArrayList<>();
        for (Button button : mBtnList) {
            if (button != mBtnList.get(correctAnswerButton)) {
                int randomWrongAnswer = -1;
                randomWrongAnswer = getRandomWrongAnswer(randomWrongAnswerList, randomWrongAnswer);
                randomWrongAnswerList.add(randomWrongAnswer);
                button.setText(String.valueOf(randomWrongAnswer));
            }
        }
    }

    private int getRandomWrongAnswer(List<Integer> randomWrongAnswerList, int randomWrongAnswer) {
        while (randomWrongAnswer == -1) {
            randomWrongAnswer = new Random().nextInt(mStdQuizNumbers.length);
            if (randomWrongAnswerList.contains(randomWrongAnswer) || randomWrongAnswer == mCorrectAnswer) {
                randomWrongAnswer = -1;
            }
        }
        return randomWrongAnswer;
    }

    private int getRandomNumberFromArray() {
        if (mUsedNumbers.size() == mStdQuizNumbers.length) {
            return -1;
        }

        int randomNumber = new Random().nextInt(mStdQuizNumbers.length);
        if (mUsedNumbers.contains(randomNumber)) {
            return getRandomNumberFromArray();
        }

        mUsedNumbers.add(randomNumber);
        return randomNumber;
    }

    private int getRandomSubtractionNumberFromArray(int chosenNumber) {
        if (mUsedNumbers.size() == mSubQuizNumbers.length -1) {
            return -1;
        }

        int randomNumber = new Random().nextInt(mSubQuizNumbers[mSubQuizNumbers.length - 1] - chosenNumber) + chosenNumber;
        if (mUsedNumbers.contains(randomNumber)) {
            return getRandomSubtractionNumberFromArray(chosenNumber);
        }

        mUsedNumbers.add(randomNumber);
        return randomNumber;
    }

    private int getRandomDivNumberFromArray(int chosenNumber) {
        if (mUsedNumbers.size() == mDivQuizNumbers.length -1) {
            return -1;
        }

        int randomNumber = new Random().nextInt(mDivQuizNumbers[mDivQuizNumbers.length - 1] - chosenNumber) * chosenNumber;
        if (mUsedNumbers.contains(randomNumber)) {
            return getRandomDivNumberFromArray(chosenNumber);
        }

        mUsedNumbers.add(randomNumber);
        return randomNumber;
    }

    @OnClick({R.id.btn_opt1, R.id.btn_opt2, R.id.btn_opt3})
    public void onClickAnswer(Button clickedBtn) {
        handleBtnClick(clickedBtn);
    }

    private void handleBtnClick(Button button) {
        if (button == mCorrectAnswerButton) {
            showSuccessAnimation();
        } else {
            showFailureAnimation();
        }
    }

    private void setupAnimationListener() {
        setupCorrectAnimationView();
        setupWrongAnimationView();
    }

    private void setupCorrectAnimationView() {
        mCorrectAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                disableAnswerButtons();
                mCorrectAnimationView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                enableAnswerButtons();
                mCorrectAnimationView.setVisibility(View.GONE);
                mCorrectAnimationView.cancelAnimation();
                setupUI();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }

    private void setupWrongAnimationView() {
        mWrongAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                disableAnswerButtons();
                mWrongAnimationView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                enableAnswerButtons();
                mWrongAnimationView.setVisibility(View.GONE);
                mCorrectAnimationView.cancelAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        // Setting max progress as the wrong animation is to long and 0.5 matches the correct
        // animation length.
        mWrongAnimationView.setMaxProgress(0.5f);
    }

    private void showSuccessAnimation() {
        mCorrectAnimationView.playAnimation();
    }

    private void showFailureAnimation() {
        mWrongAnimationView.playAnimation();
    }

    private void endQuiz() {
        updateDatabase();
        EndOfQuizFragment fragment = EndOfQuizFragment.newInstance(mChosenNumber);
        getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(mBtnContainer, mBtnContainer.getTransitionName())
                .add(R.id.frame_container, fragment, NumberListFragment.TAG)
                .commit();
    }

    private void updateDatabase() {
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

    private void enableAnswerButtons() {
        mBtnOptOne.setEnabled(true);
        mBtnOptTwo.setEnabled(true);
        mBtnOptThree.setEnabled(true);
    }

    private void disableAnswerButtons() {
        mBtnOptOne.setEnabled(false);
        mBtnOptTwo.setEnabled(false);
        mBtnOptThree.setEnabled(false);
    }
}