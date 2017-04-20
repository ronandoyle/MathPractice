package nanorstudios.ie.mathpractice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A Fragment to display the list of number choices for the user.
 */

public class NumberListFragment extends Fragment {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    public static String TAG = "NumberListFragment";
    private static String OPERATOR_KEY = "OPERATOR_KEY";

    private NumberListRecyclerAdapter recyclerAdapter;
    private OperatorEnum mOperatorEnum;

    public static NumberListFragment newInstance(OperatorEnum operator) {
        NumberListFragment numberListFragment = new NumberListFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(OPERATOR_KEY, operator);
        numberListFragment.setArguments(arguments);
        return numberListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractArguments();
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
        if (getArguments() != null && getArguments().containsKey(OPERATOR_KEY)) {
            mOperatorEnum = (OperatorEnum) getArguments().getSerializable(OPERATOR_KEY);
        }
    }

    private void setupRecyclerView() {
        recyclerAdapter = new NumberListRecyclerAdapter(getContext(), mOperatorEnum);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new BottomDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(recyclerAdapter);
    }
}