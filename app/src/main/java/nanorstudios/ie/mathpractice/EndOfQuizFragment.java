package nanorstudios.ie.mathpractice;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Dialog Fragment to show the end of the quiz to the user.
 */

public class EndOfQuizFragment extends Fragment {

    public static final String TAG = "EndOfQuizDialog";
    private EndOfQuizCallbacks mCallbacks;
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.tv_subtitle) TextView tvSubTitle;

    private Unbinder mUnbinder;

    public static EndOfQuizFragment newInstance(int chosenNumber) {
        EndOfQuizFragment dialogFrag = new EndOfQuizFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.CHOSEN_NUMBER, chosenNumber);
        dialogFrag.setArguments(args);
        return dialogFrag;
    }

    @Override
    public void onAttach(Context context) {
        mCallbacks = (EndOfQuizCallbacks) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.explode));
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_end_of_quiz, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: 18/05/2017 Improve this
        tvSubTitle.setText("Well done, you've beaten " + getArguments().getInt(Constants.CHOSEN_NUMBER) + ", keep up the great work!");
    }

    @OnClick(R.id.btn_done)
    public void onDoneClicked() {
        mCallbacks.closeActivity();
    }

    public interface EndOfQuizCallbacks {
        void closeActivity();
    }
}