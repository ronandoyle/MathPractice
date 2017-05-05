package nanorstudios.ie.mathpractice;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private NumberListFragment mNumberListFragment;
    private OperatorEnum mChosenOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        if (mNumberListFragment != null) {
            removeFragment();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.addition)
    public void onClickAddition() {
        mChosenOperator = OperatorEnum.ADDITION;
        initNumberListFragment(mChosenOperator);
    }

    @OnClick(R.id.subtraction)
    public void onClickSubtraction() {
        mChosenOperator = OperatorEnum.SUBTRACTION;
        initNumberListFragment(mChosenOperator);
    }

    @OnClick(R.id.multiplication)
    public void onClickMultiplication() {
        mChosenOperator = OperatorEnum.MULTIPLICATION;
        initNumberListFragment(mChosenOperator);
    }

    @OnClick(R.id.division)
    public void onClickDivision() {
        mChosenOperator = OperatorEnum.DIVISION;
        initNumberListFragment(mChosenOperator);
    }

    private void initNumberListFragment(OperatorEnum operatorEnum) {
        mNumberListFragment = NumberListFragment.newInstance(operatorEnum);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame_container, mNumberListFragment, NumberListFragment.TAG);
        ft.commit();
    }

    private void removeFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(mNumberListFragment);
        ft.commit();
        mNumberListFragment = null;
    }
}