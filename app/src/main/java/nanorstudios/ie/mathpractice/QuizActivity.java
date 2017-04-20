package nanorstudios.ie.mathpractice;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This activity is used to quiz the user.
 */

public class QuizActivity extends AppCompatActivity {

    @BindView(R.id.tv_question) TextView tvQuestion;

    private int mChosenNumber;
    private int mOptionOne;
    private int mOptionTwo;
    private int mOptionThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);
        extractExtras();
        setupUI();
    }

    private void extractExtras() {
        if (getIntent() != null && getIntent().hasExtra(Constants.CHOSEN_NUMBER)) {
            mChosenNumber = getIntent().getIntExtra(Constants.CHOSEN_NUMBER, -1);
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

    }

    @OnClick(R.id.btn_opt1)
    public void onClickOptOne() {

    }

    @OnClick(R.id.btn_opt2)
    public void onClickOptTwo() {

    }

    @OnClick(R.id.btn_opt3)
    public void onClickOptThree() {

    }
}