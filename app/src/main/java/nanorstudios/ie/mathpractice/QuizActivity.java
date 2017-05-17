package nanorstudios.ie.mathpractice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This activity is used to quiz the user.
 */

public class QuizActivity extends AppCompatActivity implements EndOfQuizDialog.EndOfQuizCallbacks {

    @BindView(R.id.tv_question) TextView tvQuestion;
    @BindView(R.id.btn_opt1) Button btnOptOne;
    @BindView(R.id.btn_opt2) Button btnOptTwo;
    @BindView(R.id.btn_opt3) Button btnOptThree;
    @BindView(R.id.iv_feedback) ImageView ivFeedback;

    private int mChosenNumber;
    private OperatorEnum mOperator;
    private int mCorrectAnswer;

    private int[] quizNumbers = {0,1,2,3,4,5,6,7,8,9,10};
    private List<Integer> usedNumbers = new ArrayList<>(0);
    private List<Button> buttonList;
    private Button mCorrectAnswerButton;

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
        String operatorSymbol;

        int randomNumber = getRandomNumberFromArray();

        if (endOfQuizReached(randomNumber)) {
            endQuiz();
        }

        switch (mOperator) {
            case ADDITION:
                operatorSymbol = getString(R.string.addition_symbol);
                mCorrectAnswer = randomNumber + mChosenNumber;
                break;
            case SUBTRACTION:
                operatorSymbol = getString(R.string.subtraction_symbol);
                mCorrectAnswer = Math.abs(randomNumber - mChosenNumber);
                break;
            case MULTIPLICATION:
                operatorSymbol = getString(R.string.multiplication_symbol);
                mCorrectAnswer = randomNumber * mChosenNumber;
                break;
            case DIVISION:
                operatorSymbol = getString(R.string.division_symbol);
                mCorrectAnswer = randomNumber/mChosenNumber;
                break;
            default:
                operatorSymbol = getString(R.string.addition_symbol);
                mCorrectAnswer = randomNumber + mChosenNumber;
        }
        tvQuestion.setText(String.valueOf(randomNumber) + " " + operatorSymbol + String.valueOf(mChosenNumber) + " = ?" );
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
            randomWrongAnswer = new Random().nextInt(quizNumbers.length);
            if (randomWrongAnswerList.contains(randomWrongAnswer) || randomWrongAnswer == mCorrectAnswer) {
                randomWrongAnswer = -1;
            }
        }
        return randomWrongAnswer;
    }

    private int getRandomNumberFromArray() {
        if (usedNumbers.size() == quizNumbers.length) {
            return -1;
        }

        int randomNumber = new Random().nextInt(quizNumbers.length);
        if (usedNumbers.contains(randomNumber)) {
            return getRandomNumberFromArray();
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
        EndOfQuizDialog dialog = EndOfQuizDialog.newInstance(mChosenNumber);
        dialog.show(getSupportFragmentManager(), EndOfQuizDialog.TAG);
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
}