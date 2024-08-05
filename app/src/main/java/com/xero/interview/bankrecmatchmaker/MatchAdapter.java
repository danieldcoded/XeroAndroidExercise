package com.xero.interview.bankrecmatchmaker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private final List<MatchItem> matchItems;
    private final OnItemCheckedListener onItemCheckedListener;

    public interface OnItemCheckedListener {
        void onItemChecked(MatchItem item, boolean isChecked);
    }

    public MatchAdapter(List<MatchItem> matchItems, OnItemCheckedListener listener) {
        this.matchItems = matchItems;
        this.onItemCheckedListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CheckedListItem view = (CheckedListItem) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(matchItems.get(position));
    }

    @Override
    public int getItemCount() {
        return matchItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mainText;
        private final TextView total;
        private final TextView subtextLeft;
        private final TextView subtextRight;
        private final CheckedListItem itemView;

        public ViewHolder(@NonNull CheckedListItem itemView) {
            super(itemView);
            this.itemView = itemView;
            mainText = itemView.findViewById(R.id.text_main);
            total = itemView.findViewById(R.id.text_total);
            subtextLeft = itemView.findViewById(R.id.text_sub_left);
            subtextRight = itemView.findViewById(R.id.text_sub_right);
        }

        public void bind(final MatchItem matchItem) {
            mainText.setText(matchItem.paidTo());
            total.setText(String.format(Locale.getDefault(), "%.2f", matchItem.total()));
            subtextLeft.setText(matchItem.transactionDate());
            subtextRight.setText(matchItem.docType());

            itemView.setOnItemClickListener(isChecked ->
                    onItemCheckedListener.onItemChecked(matchItem, isChecked));
        }
    }
}