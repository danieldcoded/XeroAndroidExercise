package com.xero.interview.bankrecmatchmaker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xero.interview.bankrecmatchmaker.core.common.CurrencyFormatter
import com.xero.interview.bankrecmatchmaker.data.accounting_records.model.AccountingRecord
import com.xero.interview.bankrecmatchmaker.data.accounting_records.repo.AccountingRecordsRepository
import com.xero.interview.bankrecmatchmaker.domain.MatchSelector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindMatchViewModel @Inject constructor(
    private val repository: AccountingRecordsRepository,
    private val currencyFormatter: CurrencyFormatter,
    private val matchSelector: MatchSelector
) : ViewModel() {

    private val _accountingRecords = MutableLiveData<List<AccountingRecord>>(emptyList())
    val accountingRecords: LiveData<List<AccountingRecord>> = _accountingRecords

    private val _remainingTotal = MutableLiveData<Float>()
    val remainingTotal: LiveData<Float> = _remainingTotal

    private val _formattedTotal = MutableLiveData<String>()
    val formattedTotal: LiveData<String> = _formattedTotal

    private val _selectedItems = MutableLiveData<List<AccountingRecord>>()
    val selectedItems: LiveData<List<AccountingRecord>> = _selectedItems

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var matchingStrategy = MatchSelector.MatchingStrategy.DYNAMIC_PROGRAMMING

    init {
        loadAccountingRecords()
    }

    /**
     * Loads the mock data and updates the _matchItems LiveData.
     * If there are no items to match against, it gracefully handles the scenario.
     * The _matchItems LiveData is observed by the UI to display the list of items.
     */
    private fun loadAccountingRecords() {
        viewModelScope.launch {
            try {
                val records = repository.getAccountingRecords()
                _accountingRecords.value = records
                initialFilterOfData()
            } catch (e: Exception) {
                // TODO: Handle the case when there are no items to match against gracefully.
                //  Display an appropriate error message and navigate the user back.
                _error.value = "Failed to load accounting records: ${e.message}"
            }
        }
    }

    /**
     * Sets the initial total to match against and updates the _remainingTotal LiveData.
     * If the total is invalid (negative or zero), it gracefully handles the scenario.
     * It also triggers the initial filtering of data and updates the formatted total.
     * The _remainingTotal LiveData is observed by the UI to display the remaining total.
     */
    fun setInitialTotal(total: Float) {
        if (total <= 0) {
            // TODO: Handle the case when the total is negative or zero more gracefully.
            //  Display an appropriate error message and prevent the user from proceeding.
            _error.value = "Initial total cannot be negative"
            return
        }
        _remainingTotal.value = total
        updateFormattedTotal()
        initialFilterOfData()
    }

    /**
     * Filters out any invoices that exceed the total value to match,
     * since they won't be eligible for matching as the total cannot be negative.
     * It updates the _matchItems LiveData with the filtered list.
     * If the total to match is lower than any match item, it gracefully handles the scenario.
     */
    private fun initialFilterOfData() {
        val currentTotal = _remainingTotal.value ?: return
        // TODO: Handle the case when the total to match is lower than any match item gracefully.
        //  Display an appropriate error message and prevent the user from proceeding.
        _accountingRecords.value = _accountingRecords.value?.filter { it.total <= currentTotal }
    }

    /**
     * Handles the checking/unchecking of an item and updates the _remainingTotal LiveData accordingly.
     * If the remaining total becomes negative after checking an item, it displays an error message.
     * It also updates the formatted total.
     * This function is called from the UI when an item is checked/unchecked.
     */
    fun handleItemCheck(item: AccountingRecord, isChecked: Boolean) {
        val currentTotal = _remainingTotal.value ?: run {
            _error.value = "Remaining total is not initialized"
            return
        }
        val newTotal = if (isChecked) currentTotal - item.total else currentTotal + item.total
        if (newTotal < 0) {
            _error.value = "Insufficient remaining total.\nPlease unselect other items first."
            return
        }
        _remainingTotal.value = newTotal
        updateFormattedTotal()
    }

    /**
     * Checks if an item can be selected based on the current remaining total.
     * Returns true if the item can be selected, false otherwise.
     * This function is used by the UI to determine if an item should be selectable.
     */
    fun canSelectItem(item: AccountingRecord): Boolean {
        val currentTotal = _remainingTotal.value ?: return false
        return currentTotal - item.total >= 0
    }

    fun setMatchingStrategy(strategy: MatchSelector.MatchingStrategy) {
        matchingStrategy = strategy
    }

    /**
     * Selects the matching items based on the current total.
     * First, it tries to find an exact match using a HashMap lookup.
     * If no exact match is found, it attempts to find a subset of items that sum up to the target total.
     * If a matching item or subset is found, it updates the _selectedItems LiveData.
     * If no matching item or subset is found, it displays an error message.
     * This function is called from the UI to automatically select matching items.
     */
    fun selectMatchingItems() {
        val currentTotal = _remainingTotal.value ?: return
        val items = _accountingRecords.value ?: return

        // Filter out all items that exceed the current total sum,
        // as they are not relevant when trying to select an item or subset of items.
        // This avoids unnecessary computation.
        val totalToItemMap = items.associateBy { it.total }
        val filteredTotalToItemMap = totalToItemMap.filter { it.key <= currentTotal }

        val selectedItems = matchSelector.selectMatchingItems(filteredTotalToItemMap, currentTotal, matchingStrategy)
        if (selectedItems.isNotEmpty()) {
            selectItems(selectedItems)
        } else {
            _error.value = "No matching item or subset found"
        }
    }

    /**
     * Selects the provided items if they can be selected based on the remaining total.
     * It updates the _selectedItems LiveData with the list of selected items.
     * This function is called after finding an exact match or a subset of matching items.
     *
     * @param items The list of items to select.
     */
    private fun selectItems(items: List<AccountingRecord>) {
        val selectedItems = mutableListOf<AccountingRecord>()
        for (item in items) {
            if (canSelectItem(item)) {
                selectedItems.add(item)
            }
        }
        _selectedItems.value = selectedItems
    }

    /**
     * Updates the _formattedTotal LiveData based on the current remaining total.
     * It formats the remaining total using the CurrencyFormatter.
     * This function is called whenever the remaining total changes.
     * The _formattedTotal LiveData is observed by the UI to display the formatted remaining total.
     */
    private fun updateFormattedTotal() {
        _formattedTotal.value = currencyFormatter.format(_remainingTotal.value ?: 0f)
    }

}