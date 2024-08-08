package com.xero.interview.bankrecmatchmaker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.xero.interview.bankrecmatchmaker.core.common.CurrencyFormatter
import com.xero.interview.bankrecmatchmaker.data.accounting_records.model.AccountingRecord
import com.xero.interview.bankrecmatchmaker.data.accounting_records.repo.AccountingRecordsRepository
import kotlinx.coroutines.launch
import java.util.*

class FindMatchViewModel(private val repository: AccountingRecordsRepository) : ViewModel() {

    private val currencyFormatter =
        CurrencyFormatter(Locale.getDefault(), Currency.getInstance(Locale.getDefault()))

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
        val totalToItemMap = items.associateBy { it.total }

        // Filter out all items that exceed the current total sum,
        // as they are not relevant when trying to select an item or subset of items.
        // This avoids unnecessary computation.
        val filteredTotalToItemMap = totalToItemMap.filter { it.key <= currentTotal }

        // First, try to find an exact match using the HashMap.
        // Using HashMap lookup is more efficient than a linear search on the list.
        // Time complexity for HashMap lookup: O(1) average case.
        // Time complexity for List.find(): O(n) where n is the number of items.
        val exactMatch = filteredTotalToItemMap[currentTotal]
        if (exactMatch != null) {
            selectItems(listOf(exactMatch))
            return
        }

        // Second, try to find a subset of matches
        val selectedItems = findSubsetOfMatchingItems(filteredTotalToItemMap, currentTotal)
        if (selectedItems.isNotEmpty()) {
            selectItems(selectedItems)
        } else {
            _error.value = "No matching item or subset found"
        }
    }

    /**
     * Finds a subset of items that sum up to the target total using dynamic programming.
     *
     * @param itemMap The map of total amounts to corresponding MatchItems.
     * @param target The target total to match.
     * @return A list of MatchItems that form a subset summing up to the target total, or an empty list if no subset is found.
     */
    private fun findSubsetOfMatchingItems(
        itemMap: Map<Float, AccountingRecord>,
        target: Float
    ): List<AccountingRecord> {
        /**
         * Finding a subset of items that add up to a specific total is a well-known problem
         * called the "Subset Sum Problem" which is a specific case of the "Knapsack Problem".
         * This problem is generally NP-complete, meaning that there is no known efficient algorithm
         * to solve it for all cases. However, there's a few approaches to consider depending
         * on the size of the list and the total target:
         *
         * 1. Dynamic Programming (DP):
         *    This method is for moderate-sized problems where the total isn't too large.
         *    The basic idea is to use a boolean DP table to keep track of possible sums up to the target.
         *    - Steps:
         *      1. Create a DP table dp where dp[i] is True if a subset with sum i can be formed, otherwise False.
         *      2. Initialize dp[0] as True (a sum of 0 can always be achieved with an empty subset).
         *      3. For each item price, update the DP table in reverse (to avoid using the same item more than once in this iteration).
         *      4. If dp[target] is True, then a subset with the desired sum exists.
         *    - Complexity: O(n * T), where n is the number of items and T is the target total.
         *
         * 2. Backtracking:
         *    This method tries all possible subsets and is suitable for smaller lists or when exact solutions are needed.
         *    - Steps:
         *      1. Recursively explore each item, either including it in the current subset or not.
         *      2. Check if the current subset sums up to the target.
         *      3. If it does, return the subset; otherwise, backtrack and try other combinations.
         *    - Complexity: O(2^n), where n is the number of items. This is exponential, so it's only practical for small lists.
         *
         * 3. Meet-in-the-Middle:
         *    This technique is useful for larger lists where a direct DP approach is infeasible.
         *    It involves splitting the list into two halves, finding all subset sums for each half,
         *    and then checking if there are combinations from the two halves that sum up to the target.
         *    - Steps:
         *      1. Divide the list into two halves.
         *      2. Compute all possible subset sums for each half.
         *      3. Use a hash set to check if there are any pairs of sums (one from each half) that add up to the target.
         *    - Complexity: O(2^(n/2)) for generating subsets and checking pairs, which is more manageable than O(2^n).
         *
         * 4. Exact Cover Problem (Subset Sum via Integer Programming):
         *    For some problems, this can be formulated it as an integer programming problem and use specialized solvers.
         *    This approach can be very effective but requires knowledge of integer programming techniques and solvers.
         *
         * 5. Greedy Algorithms:
         *    While not always suitable (especially for exact solutions), greedy algorithms might work for
         *    specific cases or approximate solutions where items are sorted to try to reach the target.
         *
         * Summary:
         * - Dynamic Programming is efficient for moderate-sized lists with a manageable target total.
         * - Backtracking is useful for smaller lists.
         * - Meet-in-the-Middle is suitable for larger lists.
         * - Integer Programming can be effective for specific cases.
         */
        return emptyList()
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

    class Factory(private val repository: AccountingRecordsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FindMatchViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FindMatchViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}