package com.xero.interview.bankrecmatchmaker;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xero.interview.bankrecmatchmaker.core.common.CurrencyFormatter;
import com.xero.interview.bankrecmatchmaker.data.MockDataProvider;
import com.xero.interview.bankrecmatchmaker.databinding.ActivityFindMatchBinding;

import java.util.Arrays;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FindMatchActivity extends AppCompatActivity {

    public static final String TARGET_MATCH_VALUE = "com.xero.interview.target_match_value";
    private static final float DEFAULT_TARGET_VALUE = 10000f;

    private ActivityFindMatchBinding binding;
    private MatchAdapter adapter;
    private float remainingTotal;
    private CurrencyFormatter currencyFormatter;
    private List<MatchItem> matchItems;
    private Map<Float, MatchItem> totalToItemMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindMatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize CurrencyFormatter
        currencyFormatter = new CurrencyFormatter(Locale.getDefault(), Currency.getInstance(Locale.getDefault()));

        setupToolbar();
        setupMockData();
        setupMatchText();
        setupRecyclerView();
        selectMatchingItem();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_find_match);
        }
    }

    private void setupMockData() {
        MockDataProvider mockDataProvider = MockDataProvider.INSTANCE;
        matchItems = mockDataProvider.getMockMatchItems();
        totalToItemMap = mockDataProvider.getTotalToItemMap(matchItems);
    }

    private void setupMatchText() {
        remainingTotal = getIntent().getFloatExtra(TARGET_MATCH_VALUE, DEFAULT_TARGET_VALUE);
        updateRemainingTotal();
    }

    private void setupRecyclerView() {
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MatchAdapter(matchItems, this::handleItemCheck, currencyFormatter);
        binding.recyclerView.setAdapter(adapter);
    }

    private void handleItemCheck(MatchItem item, boolean isChecked) {
        if (isChecked) {
            remainingTotal -= item.total();
        } else {
            remainingTotal += item.total();
        }
        updateRemainingTotal();
    }

    private void updateRemainingTotal() {
        String formattedTotal = currencyFormatter.format(remainingTotal);
        binding.matchText.setText(getString(R.string.select_matches, formattedTotal));
    }

    private void selectMatchingItem() {
        MatchItem matchingItem = totalToItemMap.get(remainingTotal);
        if (matchingItem != null) {
            adapter.setItemSelected(matchingItem, true);
            handleItemCheck(matchingItem, true);
        }
    }

}