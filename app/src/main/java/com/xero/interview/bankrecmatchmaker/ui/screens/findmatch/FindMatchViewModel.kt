package com.xero.interview.bankrecmatchmaker.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xero.interview.bankrecmatchmaker.core.common.CurrencyFormatter
import com.xero.interview.bankrecmatchmaker.data.accounting_records.model.AccountingRecord
import com.xero.interview.bankrecmatchmaker.data.accounting_records.repo.AccountingRecordsRepository
import com.xero.interview.bankrecmatchmaker.domain.MatchSelector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindMatchViewModel @Inject constructor(
    private val repository: AccountingRecordsRepository,
    private val currencyFormatter: CurrencyFormatter,
    private val matchSelector: MatchSelector
) : ViewModel() {
    private val _state = MutableStateFlow(FindMatchState())
    val state: StateFlow<FindMatchState> = _state

    init {
        loadAccountingRecords()
    }

    private fun loadAccountingRecords() {
        viewModelScope.launch {
            try {
                val records = repository.getAccountingRecords()
                _state.update { it.copy(accountingRecords = records) }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Failed to load accounting records: ${e.message}") }
            }
        }
    }

    fun setInitialTotal(total: Float) {
        if (total <= 0) {
            _state.update { it.copy(error = "Initial total cannot be negative or zero") }
            return
        }
        _state.update {
            it.copy(
                initialTotal = total,
                remainingTotal = total,
                formattedTotal = currencyFormatter.format(total)
            )
        }
        initialFilterOfData()
    }

    private fun initialFilterOfData() {
        val currentTotal = _state.value.initialTotal
        _state.update {
            it.copy(accountingRecords = it.accountingRecords.filter { record -> record.total <= currentTotal })
        }
    }

    fun handleItemCheck(item: AccountingRecord, isChecked: Boolean) {
        val currentTotal = _state.value.remainingTotal
        val newTotal = if (isChecked) currentTotal - item.total else currentTotal + item.total
        if (newTotal < 0) {
            _state.update { it.copy(error = "Insufficient remaining total.\nPlease unselect other items first.") }
            return
        }
        _state.update {
            it.copy(
                remainingTotal = newTotal,
                formattedTotal = currencyFormatter.format(newTotal),
                selectedRecords = if (isChecked) it.selectedRecords + item else it.selectedRecords - item
            )
        }
    }

    fun canSelectItem(item: AccountingRecord): Boolean {
        val canSelect = _state.value.remainingTotal - item.total >= 0
        if (!canSelect) {
            _state.update { it.copy(error = "Cannot select this item.\nInsufficient remaining total.") }
        }
        return canSelect
    }

    fun selectMatchingItems() {
        val currentTotal = _state.value.remainingTotal
        val items = _state.value.accountingRecords.associateBy { it.total }
        val selectedItems = matchSelector.selectMatchingItems(
            items,
            currentTotal,
            MatchSelector.MatchingStrategy.DYNAMIC_PROGRAMMING
        )
        if (selectedItems.isNotEmpty()) {
            _state.update {
                it.copy(
                    selectedRecords = selectedItems.toSet(),
                    remainingTotal = currentTotal - selectedItems.sumOf { item -> item.total.toDouble() }
                        .toFloat(),
                    formattedTotal = currencyFormatter.format(currentTotal - selectedItems.sumOf { item -> item.total.toDouble() }
                        .toFloat())
                )
            }
        } else {
            _state.update { it.copy(error = "No matching item or subset found") }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}

data class FindMatchState(
    val accountingRecords: List<AccountingRecord> = emptyList(),
    val selectedRecords: Set<AccountingRecord> = emptySet(),
    val initialTotal: Float = 0f,
    val remainingTotal: Float = 0f,
    val formattedTotal: String = "",
    val error: String? = null
)