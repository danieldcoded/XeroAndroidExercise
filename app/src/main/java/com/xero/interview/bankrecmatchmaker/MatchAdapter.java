package com.xero.interview.bankrecmatchmaker;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xero.interview.bankrecmatchmaker.core.common.CurrencyFormatter;
import com.xero.interview.bankrecmatchmaker.databinding.ListItemMatchBinding;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MatchItem> matchItems;
    private final OnItemCheckedListener onItemCheckedListener;
    private final CurrencyFormatter currencyFormatter;
    private final Set<MatchItem> selectedItems = new HashSet<>();

    public interface OnItemCheckedListener {
        void onItemChecked(MatchItem item, boolean isChecked);
    }

    public void setItemSelected(MatchItem item, boolean isSelected) {
        if (isSelected) {
            selectedItems.add(item);
        } else {
            selectedItems.remove(item);
        }
        notifyItemChanged(matchItems.indexOf(item));
    }

    public MatchAdapter(List<MatchItem> matchItems, OnItemCheckedListener listener, CurrencyFormatter currencyFormatter) {
        this.matchItems = matchItems;
        this.onItemCheckedListener = listener;
        this.currencyFormatter = currencyFormatter;
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
        ((ViewHolder) holder).bind(matchItems.get(position), selectedItems.contains(matchItems.get(position)));
    }

    @Override
    public int getItemCount() {
        return matchItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemMatchBinding binding;

        ViewHolder(@NonNull ListItemMatchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final MatchItem matchItem, boolean isSelected) {
            binding.textMain.setText(matchItem.paidTo());
            binding.textTotal.setText(currencyFormatter.format(matchItem.total()));
            binding.textSubLeft.setText(matchItem.transactionDate());
            binding.textSubRight.setText(matchItem.docType());

            binding.getRoot().setChecked(isSelected);
            binding.getRoot().setOnClickListener(v -> {
                boolean newCheckedState = !binding.getRoot().isChecked();
                setItemSelected(matchItem, newCheckedState);
                onItemCheckedListener.onItemChecked(matchItem, newCheckedState);
            });
        }
    }
}