package nanorstudios.ie.mathpractice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A Fragment to display the list of number choices for the user.
 */

public class NumberListFragment extends Fragment implements NumberListItemClickListener{

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    public static String TAG = "NumberListFragment";

    private NumberListRecyclerAdapter recyclerAdapter;
    private OperatorEnum mOperatorEnum;

    public static NumberListFragment newInstance(OperatorEnum operator) {
        NumberListFragment numberListFragment = new NumberListFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(Constants.CHOSEN_OPERATOR, operator);
        numberListFragment.setArguments(arguments);
        return numberListFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RequestCodes.QUIZ && resultCode == Constants.ResultCodes.QUIZ_FINISHED) {
            highlightItem(data.getIntExtra(Constants.CHOSEN_NUMBER, -1));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractArguments();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_number_list, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        return view;
    }

    private void extractArguments() {
        if (getArguments() != null && getArguments().containsKey(Constants.CHOSEN_OPERATOR)) {
            mOperatorEnum = (OperatorEnum) getArguments().getSerializable(Constants.CHOSEN_OPERATOR);
        }
    }

    private void setupRecyclerView() {
        recyclerAdapter = new NumberListRecyclerAdapter(getContext(), mOperatorEnum, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new BottomDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onListItemClicked(int pos) {
        Intent intent = new Intent(getActivity(), QuizActivity.class);
        intent.putExtra(Constants.CHOSEN_NUMBER, pos);
        intent.putExtra(Constants.CHOSEN_OPERATOR, mOperatorEnum);
        startActivityForResult(intent, Constants.RequestCodes.QUIZ);
    }

    public void highlightItem(int chosenNumber) {
        recyclerAdapter.highlightItem(chosenNumber);
    }
}