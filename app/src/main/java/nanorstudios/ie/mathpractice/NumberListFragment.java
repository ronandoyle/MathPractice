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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

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
            extractExtraFromResult(data);
        }
    }

    private void extractExtraFromResult(Intent data) {
        if (data != null && data.hasExtra(Constants.CHOSEN_NUMBER)) {
            highlightItem(data.getIntExtra(Constants.CHOSEN_NUMBER, -1));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractArguments();
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference completedReg = mRootRef.child("completed");
        completedReg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, ArrayList<String>> completed = ((HashMap<String, ArrayList<String>>) dataSnapshot.getValue());
                highlightItemsFromDatabase(completed);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
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

    public void highlightItemsFromDatabase(HashMap<String, ArrayList<String>> dbItems) {
        Iterator iterator = dbItems.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<String>> pair = (Map.Entry<String, ArrayList<String>>) iterator.next();
            switch (mOperatorEnum) {
                case ADDITION:
                    if (pair.getKey().equals("addition")) {
                        for (String value : pair.getValue()) {
                            highlightItem(Integer.valueOf(value.substring(1)));
                        }
                    }
            }
        }
    }
}