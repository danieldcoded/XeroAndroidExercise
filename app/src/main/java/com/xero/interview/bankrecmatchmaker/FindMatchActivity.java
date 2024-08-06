package com.xero.interview.bankrecmatchmaker;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xero.interview.bankrecmatchmaker.databinding.ActivityFindMatchBinding;

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

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_find_match);
        }
    }

    private void setupInitialTotal() {
        float initialTotal = getIntent().getFloatExtra(TARGET_MATCH_VALUE, DEFAULT_TARGET_VALUE);
        viewModel.setInitialTotal(initialTotal);
    }

    private void setupRecyclerView() {
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MatchAdapter(this::handleItemCheck, viewModel);
        binding.recyclerView.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getMatchItems().observe(this, matchItems ->
                adapter.submitList(matchItems));

        viewModel.getSelectedItem().observe(this, selectedItem -> {
            if (selectedItem != null) {
                adapter.setItemSelected(selectedItem, true);
                handleItemCheck(selectedItem, true);
            }
        });

        viewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                showErrorMessage(errorMessage);
            }
        });

        viewModel.selectMatchingItem();
    }

    private void handleItemCheck(MatchItem item, boolean isChecked) {
        viewModel.handleItemCheck(item, isChecked);
    }

    private void showErrorMessage(String message) {
        // TODO: PREVENT USER FROM BEING ABLE TO SPAM THIS
        //  Could either add an interval or just keep track of the last error display
        //  so the same error is not displayed twice.
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}