package com.xero.interview.bankrecmatchmaker;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xero.interview.bankrecmatchmaker.databinding.ActivityFindMatchBinding;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FindMatchActivity extends AppCompatActivity {

    public static final String TARGET_MATCH_VALUE = "com.xero.interview.target_match_value";
    private static final float DEFAULT_TARGET_VALUE = 10000f;

    private ActivityFindMatchBinding binding;
    private MatchAdapter adapter;
    private float remainingTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindMatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupMatchText();
        setupRecyclerView();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_find_match);
        }
    }

    private void setupMatchText() {
        remainingTotal = getIntent().getFloatExtra(TARGET_MATCH_VALUE, DEFAULT_TARGET_VALUE);
        updateRemainingTotal();
    }

    private void setupRecyclerView() {
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MatchAdapter(buildMockData(), (matchItem, isChecked) -> {
            if (isChecked) {
                remainingTotal -= matchItem.total();
            } else {
                remainingTotal += matchItem.total();
            }
            updateRemainingTotal();
        });
        binding.recyclerView.setAdapter(adapter);
    }

    private void updateRemainingTotal() {
        binding.matchText.setText(String.format(Locale.getDefault(), getString(R.string.select_matches), remainingTotal));
    }

    private List<MatchItem> buildMockData() {
        return Arrays.asList(
                new MatchItem("City Limousines", "30 Aug", 249.00f, "Sales Invoice"),
                new MatchItem("Ridgeway University", "12 Sep", 618.50f, "Sales Invoice"),
                new MatchItem("Cube Land", "22 Sep", 495.00f, "Sales Invoice"),
                new MatchItem("Bayside Club", "23 Sep", 234.00f, "Sales Invoice"),
                new MatchItem("SMART Agency", "12 Sep", 250f, "Sales Invoice"),
                new MatchItem("PowerDirect", "11 Sep", 108.60f, "Sales Invoice"),
                new MatchItem("PC Complete", "17 Sep", 216.99f, "Sales Invoice"),
                new MatchItem("Truxton Properties", "17 Sep", 181.25f, "Sales Invoice"),
                new MatchItem("MCO Cleaning Services", "17 Sep", 170.50f, "Sales Invoice"),
                new MatchItem("Gateway Motors", "18 Sep", 411.35f, "Sales Invoice")
        );
    }
}