package com.xero.interview.bankrecmatchmaker;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xero.interview.bankrecmatchmaker.databinding.ActivityFindMatchBinding;

/**
 * The main activity of the BankRecMatchmaker app.
 * It displays a list of match items and allows the user to select items to match a target total.
 * The activity uses data binding to interact with the layout and the FindMatchViewModel.
 */
public class FindMatchActivity extends AppCompatActivity {

    public static final String TARGET_MATCH_VALUE = "com.xero.interview.target_match_value";
    private static final float DEFAULT_TARGET_VALUE = 1000.00f;

    private ActivityFindMatchBinding binding;
    private MatchAdapter adapter;
    private FindMatchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_match);

        viewModel = new ViewModelProvider(this).get(FindMatchViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupToolbar();
        setupInitialTotal();
        setupRecyclerView();
        observeViewModel();
    }

    /**
     * Sets up the toolbar with the app title and a back button.
     */
    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_find_match);
        }
    }

    /**
     * Sets up the initial total to match based on the value passed through the intent extras.
     * If no value is provided, a default target value is used.
     */
    private void setupInitialTotal() {
        float initialTotal = getIntent().getFloatExtra(TARGET_MATCH_VALUE, DEFAULT_TARGET_VALUE);
        viewModel.setInitialTotal(initialTotal);
    }

    /**
     * Sets up the RecyclerView with the MatchAdapter and a LinearLayoutManager.
     * The adapter is responsible for displaying the list of match items and handling item selection.
     */
    private void setupRecyclerView() {
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MatchAdapter(this::handleItemCheck, viewModel);
        binding.recyclerView.setAdapter(adapter);
    }

    /**
     * Observes the LiveData properties of the FindMatchViewModel and updates the UI accordingly.
     * It observes the list of match items, selected items, and error messages.
     * It also triggers the automatic selection of matching items.
     */
    private void observeViewModel() {
        viewModel.getMatchItems().observe(this, matchItems ->
                adapter.submitList(matchItems));

        viewModel.getSelectedItems().observe(this, selectedItems -> {
            if (selectedItems != null && !selectedItems.isEmpty()) {
                for (MatchItem item : selectedItems) {
                    adapter.setItemSelected(item, true);
                    handleItemCheck(item, true);
                }
            }
        });

        viewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                showErrorMessage(errorMessage);
            }
        });

        viewModel.selectMatchingItems();
    }

    /**
     * Handles the checking/unchecking of an item.
     * It calls the corresponding method in the FindMatchViewModel to update the remaining total.
     *
     * @param item      The item that was checked/unchecked.
     * @param isChecked The checked state of the item.
     */
    private void handleItemCheck(MatchItem item, boolean isChecked) {
        viewModel.handleItemCheck(item, isChecked);
    }

    /**
     * Displays an error message to the user using a Toast.
     * TODO: Prevent the user from spamming error messages.
     *       Add an interval or keep track of the last error displayed
     *       to avoid displaying the same error multiple times.
     *
     * @param message The error message to display.
     */
    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}