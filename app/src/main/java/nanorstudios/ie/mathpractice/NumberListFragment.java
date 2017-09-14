package nanorstudios.ie.mathpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    // TODO: 14/09/2017 Removing for v1
//    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    public static NumberListFragment newInstance(OperatorEnum operator) {
        NumberListFragment numberListFragment = new NumberListFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(Constants.CHOSEN_OPERATOR, operator);
        numberListFragment.setArguments(arguments);
        return numberListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractArguments();
    }

    // TODO: 14/09/2017 Removing for v1
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        DatabaseReference completedRef = mRootRef.child("completed");
//        completedRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                HashMap<String, ArrayList<String>> completed = ((HashMap<String, ArrayList<String>>) dataSnapshot.getValue());
//                highlightItemsFromDatabase(completed);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        highlightItemsFromSharedPreferences();
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

    private void highlightItemsFromSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.Preferences.NAME, 0);

        String storedString = preferences.getString(Constants.Preferences.COMPLETED_QUIZZES, "");
        if (TextUtils.isEmpty(storedString)) {
            return;
        }

        CompletedQuizzes completedQuizzes = new CompletedQuizzes();
        completedQuizzes.setQuizzes(
                (HashMap<String, ArrayList<String>>) new Gson().fromJson(storedString, new TypeToken<HashMap<String, ArrayList<String>>>(){}.getType()));
        highlightItemsFromDatabase(completedQuizzes.getQuizzes());
    }

    public void highlightItemsFromDatabase(HashMap<String, ArrayList<String>> dbItems) {
        Iterator iterator = dbItems.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<String>> pair = (Map.Entry<String, ArrayList<String>>) iterator.next();
            switch (mOperatorEnum) {
                case ADDITION:
                    if (pair.getKey().equalsIgnoreCase(getString(R.string.addition))) {
                        extractValue(pair);
                    }
                    break;
                case SUBTRACTION:
                    if (pair.getKey().equalsIgnoreCase(getString(R.string.subtraction))) {
                        extractValue(pair);
                    }
                    break;
                case MULTIPLICATION:
                    if (pair.getKey().equalsIgnoreCase(getString(R.string.multiplication))) {
                        extractValue(pair);
                    }
                    break;
                case DIVISION:
                    if (pair.getKey().equalsIgnoreCase(getString(R.string.division))) {
                        extractValue(pair);
                    }
                    break;
            }
        }
    }

    private void extractValue(Map.Entry<String, ArrayList<String>> pair) {
        for (String value : pair.getValue()) {
            if (TextUtils.isEmpty(value.substring(0))) {
                continue;
            }
            highlightItem(Integer.valueOf(value.substring(0)));
        }
    }
}