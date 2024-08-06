package com.xero.interview.bankrecmatchmaker;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.xero.interview.bankrecmatchmaker.core.common.CurrencyFormatter;
import com.xero.interview.bankrecmatchmaker.databinding.ListItemMatchBinding;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class MatchAdapter extends ListAdapter<MatchItem, MatchAdapter.ViewHolder> {

    private final OnItemCheckedListener onItemCheckedListener;
    private final CurrencyFormatter currencyFormatter;
    private final Set<MatchItem> selectedItems = new HashSet<>();
    private final FindMatchViewModel viewModel;

    public interface OnItemCheckedListener {
        void onItemChecked(MatchItem item, boolean isChecked);
    }

    public MatchAdapter(OnItemCheckedListener listener, FindMatchViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.onItemCheckedListener = listener;
        this.currencyFormatter = new CurrencyFormatter(Locale.getDefault(), java.util.Currency.getInstance(Locale.getDefault()));
        this.viewModel = viewModel;
    }

    public void setItemSelected(MatchItem item, boolean isSelected) {
        if (isSelected && !viewModel.canSelectItem(item)) {
            return;
        }
        if (isSelected) {
            selectedItems.add(item);
        } else {
            selectedItems.remove(item);
        }
        notifyItemChanged(getCurrentList().indexOf(item));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemMatchBinding binding = ListItemMatchBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchItem item = getItem(position);
        boolean isSelected = selectedItems.contains(item);
        boolean canSelect = viewModel.canSelectItem(item);
        holder.bind(item, isSelected, canSelect);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemMatchBinding binding;

        ViewHolder(@NonNull ListItemMatchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final MatchItem matchItem, boolean isSelected, boolean canSelect) {
            binding.textMain.setText(matchItem.paidTo());
            binding.textTotal.setText(currencyFormatter.format(matchItem.total()));
            binding.textSubLeft.setText(matchItem.transactionDate());
            binding.textSubRight.setText(matchItem.docType());

            binding.getRoot().setEnabled(canSelect || isSelected);
            binding.getRoot().setChecked(isSelected);
            binding.getRoot().setOnClickListener(v -> {
                if (isSelected || canSelect) {
                    boolean newCheckedState = !binding.getRoot().isChecked();
                    setItemSelected(matchItem, newCheckedState);
                    onItemCheckedListener.onItemChecked(matchItem, newCheckedState);
                }
            });
        }
    }

    private static final DiffUtil.ItemCallback<MatchItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<MatchItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull MatchItem oldItem, @NonNull MatchItem newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull MatchItem oldItem, @NonNull MatchItem newItem) {
            return oldItem.equals(newItem);
        }
    };

}