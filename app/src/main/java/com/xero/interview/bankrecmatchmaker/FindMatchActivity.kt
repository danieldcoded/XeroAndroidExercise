package com.xero.interview.bankrecmatchmaker

import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.xero.interview.bankrecmatchmaker.data.accounting_records.model.AccountingRecord
import com.xero.interview.bankrecmatchmaker.data.accounting_records.repo.AccountingRecordsRepository
import com.xero.interview.bankrecmatchmaker.data.accounting_records.source.MockAccountingRecordsDataSource
import com.xero.interview.bankrecmatchmaker.databinding.ActivityFindMatchBinding

/**
 * The main activity of the BankRecMatchmaker app.
 * It displays a list of match items and allows the user to select items to match a target total.
 * The activity uses data binding to interact with the layout and the FindMatchViewModel.
 */
class FindMatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindMatchBinding
    private lateinit var adapter: MatchAdapter
    private lateinit var viewModel: FindMatchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AccountingRecordsRepository(MockAccountingRecordsDataSource())
        val viewModelFactory = FindMatchViewModel.Factory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[FindMatchViewModel::class.java]

        // Set the ViewModel for data binding
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupToolbar()
        setupInitialTotal()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.title_find_match)
        }
    }

    private fun setupInitialTotal() {
        val initialTotal = intent.getFloatExtra(TARGET_MATCH_VALUE, DEFAULT_TARGET_VALUE)
        viewModel.setInitialTotal(initialTotal)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MatchAdapter(::handleItemCheck, viewModel)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.accountingRecords.observe(this) { records ->
            adapter.submitList(records)
        }

        viewModel.selectedItems.observe(this) { selectedItems ->
            selectedItems.forEach { item ->
                adapter.setItemSelected(item, true)
                handleItemCheck(item, true)
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            showErrorMessage(errorMessage)
        }

        viewModel.selectMatchingItems()
    }

    private fun handleItemCheck(item: AccountingRecord, isChecked: Boolean) {
        viewModel.handleItemCheck(item, isChecked)
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TARGET_MATCH_VALUE = "com.xero.interview.target_match_value"
        const val DEFAULT_TARGET_VALUE = 1000.00f
    }
}