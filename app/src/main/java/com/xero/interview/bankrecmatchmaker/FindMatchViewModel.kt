package com.xero.interview.bankrecmatchmaker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xero.interview.bankrecmatchmaker.core.common.CurrencyFormatter
import com.xero.interview.bankrecmatchmaker.data.MockDataProvider
import java.util.*

class FindMatchViewModel : ViewModel() {

    private val currencyFormatter =
        CurrencyFormatter(Locale.getDefault(), Currency.getInstance(Locale.getDefault()))
    private val mockDataProvider = MockDataProvider

    private val _matchItems = MutableLiveData<List<MatchItem>>()
    val matchItems: LiveData<List<MatchItem>> = _matchItems

    private val _remainingTotal = MutableLiveData<Float>()
    val remainingTotal: LiveData<Float> = _remainingTotal

    private val _formattedTotal = MutableLiveData<String>()
    val formattedTotal: LiveData<String> = _formattedTotal

    private val _selectedItem = MutableLiveData<MatchItem?>()
    val selectedItem: LiveData<MatchItem?> = _selectedItem

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadMockData()
    }

    private fun loadMockData() {
        _matchItems.value = mockDataProvider.getMockMatchItems()
    }

    fun setInitialTotal(total: Float) {
        if (total < 0) {
            _error.value = "Initial total cannot be negative"
            return
        }
        _remainingTotal.value = total
        updateFormattedTotal()
    }

    fun handleItemCheck(item: MatchItem, isChecked: Boolean) {
        val currentTotal = _remainingTotal.value ?: run {
            _error.value = "Remaining total is not initialized"
            return
        }
        val newTotal = if (isChecked) currentTotal - item.total() else currentTotal + item.total()
        if (newTotal < 0) {
            _error.value = "Insufficient remaining total. Please unselect other items first."
            return
        }
        _remainingTotal.value = newTotal
        updateFormattedTotal()
    }

    fun canSelectItem(item: MatchItem): Boolean {
        val currentTotal = _remainingTotal.value ?: return false
        return currentTotal - item.total() >= 0
    }

    fun selectMatchingItem() {
        val matchingItem = mockDataProvider.getTotalToItemMap(
            _matchItems.value ?: emptyList()
        )[_remainingTotal.value]
        if (matchingItem == null) {
            _error.value = "No matching item found"
        } else {
            _selectedItem.value = matchingItem
        }
    }

    private fun updateFormattedTotal() {
        _formattedTotal.value = currencyFormatter.format(_remainingTotal.value ?: 0f)
    }

}