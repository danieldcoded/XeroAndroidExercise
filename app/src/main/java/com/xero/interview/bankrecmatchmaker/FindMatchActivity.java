package com.xero.interview.bankrecmatchmaker;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xero.interview.bankrecmatchmaker.databinding.ActivityFindMatchBinding;

public class FindMatchActivity extends AppCompatActivity {

    public static final String TARGET_MATCH_VALUE = "com.xero.interview.target_match_value";
    private static final float DEFAULT_TARGET_VALUE = 249.00f;

    private ActivityFindMatchBinding binding;
    private MatchAdapter adapter;
    private FindMatchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindMatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(FindMatchViewModel.class);

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
        adapter = new MatchAdapter(this::handleItemCheck);
        binding.recyclerView.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getMatchItems().observe(this, matchItems ->
                adapter.submitList(matchItems));

        viewModel.getFormattedTotal().observe(this, formattedTotal ->
                binding.matchText.setText(getString(R.string.select_matches, formattedTotal)));

        viewModel.getSelectedItem().observe(this, selectedItem -> {
            if (selectedItem != null) {
                adapter.setItemSelected(selectedItem, true);
                handleItemCheck(selectedItem, true);
            }
        });

        viewModel.selectMatchingItem();
    }

    private void handleItemCheck(MatchItem item, boolean isChecked) {
        viewModel.handleItemCheck(item, isChecked);
    }

}