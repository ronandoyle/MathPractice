package nanorstudios.ie.mathpractice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Recycler View adapter for the number list.
 */

public class NumberListRecyclerAdapter extends RecyclerView.Adapter<NumberListRecyclerAdapter.ViewHolder>{

    private static OperatorEnum sOperatorEnum;
    private String[] mNumberList;
    private WeakReference<Context> mWeakRefContext;
    private NumberListItemClickListener listener;

    public NumberListRecyclerAdapter(Context context, OperatorEnum operatorEnum, NumberListItemClickListener listener) {
        mWeakRefContext = new WeakReference<>(context);
        sOperatorEnum = operatorEnum;
        this.listener = listener;
        mNumberList = mWeakRefContext.get().getResources().getStringArray(R.array.number_list_array);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.number_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String listItem = mNumberList[position];

        switch (sOperatorEnum) {
            case ADDITION:
                listItem = mWeakRefContext.get().getString(R.string.addition_symbol) + " " + listItem;
                break;
            case SUBTRACTION:
                listItem = mWeakRefContext.get().getString(R.string.subtraction_symbol) +  " " +listItem;
                break;
            case MULTIPLICATION:
                listItem = mWeakRefContext.get().getString(R.string.multiplication_symbol) +  " " +listItem;
                break;
            case DIVISION:
                listItem = mWeakRefContext.get().getString(R.string.division_symbol) +  " " +listItem;
                break;
        }

        holder.tvTitle.setText(listItem);
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onListItemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNumberList.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_list_item_title) TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
