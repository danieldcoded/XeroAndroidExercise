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

/**
 * An adapter for displaying a list of MatchItems in a RecyclerView.
 * It extends the ListAdapter class and uses DiffUtil for efficient list updates.
 * The adapter handles item selection and provides a listener interface for item click events.
 */
public class MatchAdapter extends ListAdapter<MatchItem, MatchAdapter.ViewHolder> {

    private final OnItemCheckedListener onItemCheckedListener;
    private final CurrencyFormatter currencyFormatter;
    private final Set<MatchItem> selectedItems = new HashSet<>();
    private final FindMatchViewModel viewModel;

    /**
     * An interface for handling item checked events.
     */
    public interface OnItemCheckedListener {
        void onItemChecked(MatchItem item, boolean isChecked);
    }

    /**
     * Constructs a new MatchAdapter with the given listener and view model.
     *
     * @param listener  The listener for item checked events.
     * @param viewModel The FindMatchViewModel instance.
     */
    public MatchAdapter(OnItemCheckedListener listener, FindMatchViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.onItemCheckedListener = listener;
        this.currencyFormatter = new CurrencyFormatter(Locale.getDefault(), java.util.Currency.getInstance(Locale.getDefault()));
        this.viewModel = viewModel;
    }

    /**
     * Sets the selected state of an item.
     * If the item cannot be selected based on the remaining total, the selection is ignored.
     *
     * @param item       The item to set the selected state for.
     * @param isSelected The selected state to set.
     */
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
        holder.bind(item, isSelected);
    }

    /**
     * A ViewHolder for displaying a MatchItem in the RecyclerView.
     * It binds the data to the views and handles item click events.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemMatchBinding binding;

        ViewHolder(@NonNull ListItemMatchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Binds the MatchItem data to the views and sets up the click listener.
         *
         * @param matchItem  The MatchItem to bind.
         * @param isSelected The selected state of the item.
         */
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

    /**
     * A DiffUtil.ItemCallback implementation for comparing MatchItems.
     * It defines how to determine if two items are the same and if their contents have changed.
     */
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