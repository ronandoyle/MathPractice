package nanorstudios.ie.mathpractice;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private NumberListFragment mNumberListFragment;
    private OperatorEnum mChosenOperator;

    @BindView(R.id.version) TextView mVersionTV;
    // TODO: 14/09/2017 Removing this for v1
//    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    // TODO: 14/09/2017 Removing this for v1
//    private CompletedQuizzesComponent sCompletedQuizzesComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        // TODO: 14/09/2017 Removing this for v1
//        sCompletedQuizzesComponent = ((MathPracticeApplication) getApplication()).getCompletedQuizzesComponent();
        ButterKnife.bind(this);
        setupToolbar();
        populateVersionNumber();
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    private void populateVersionNumber() {
        try {
            mVersionTV.setText(String.format(getString(R.string.version),
                    getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    // TODO: 14/09/2017 Removing this for v1
//    protected CompletedQuizzesComponent getCompletedQuizzesComponent() {
//        return sCompletedQuizzesComponent;
//    }

    // TODO: 14/09/2017 Removing this for v1
//    private void setupDatabase() {
//        DatabaseReference completedRef = mRootRef.child("completed");
//        completedRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
                // TODO: 14/09/2017 Removing this for v1
//                sCompletedQuizzesComponent.provideCompletedQuizzes().setQuizzes(((HashMap<String, ArrayList<String>>) dataSnapshot.getValue()));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}