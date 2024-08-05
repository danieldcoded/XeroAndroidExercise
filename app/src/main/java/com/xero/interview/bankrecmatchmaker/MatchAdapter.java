package com.xero.interview.bankrecmatchmaker;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xero.interview.bankrecmatchmaker.databinding.ListItemMatchBinding;

import java.util.List;
import java.util.Locale;

public class MatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemMatchBinding binding = ListItemMatchBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bind(matchItems.get(position));
    }

    @Override
    public int getItemCount() {
        return matchItems.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemMatchBinding binding;

        public ViewHolder(@NonNull ListItemMatchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final MatchItem matchItem) {
            binding.textMain.setText(matchItem.paidTo());
            binding.textTotal.setText(String.format(Locale.getDefault(), "%.2f", matchItem.total()));
            binding.textSubLeft.setText(matchItem.transactionDate());
            binding.textSubRight.setText(matchItem.docType());

            binding.getRoot().setOnItemClickListener(isChecked ->
                    onItemCheckedListener.onItemChecked(matchItem, isChecked));
        }
    }
}