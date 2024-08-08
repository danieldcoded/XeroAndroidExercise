package com.xero.interview.bankrecmatchmaker.ui.screens.findmatch.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.xero.interview.bankrecmatchmaker.core.common.CurrencyFormatter
import com.xero.interview.bankrecmatchmaker.data.accounting_records.model.AccountingRecord
import com.xero.interview.bankrecmatchmaker.ui.components.AccountingRecordItem
import java.util.*

@Composable
fun MatchList(
    accountingRecords: List<AccountingRecord>,
    selectedRecords: Set<AccountingRecord>,
    onItemCheckedListener: (AccountingRecord, Boolean) -> Unit,
    canSelectItem: (AccountingRecord) -> Boolean
) {
    val currencyFormatter = remember {
        CurrencyFormatter(Locale.getDefault(), Currency.getInstance(Locale.getDefault()))
    }

    LazyColumn {
        items(accountingRecords) { record ->
            AccountingRecordItem(
                accountingRecord = record,
                isSelected = selectedRecords.contains(record),
                currencyFormatter = currencyFormatter,
                onItemClick = { isChecked ->
                    if (isChecked && !canSelectItem(record)) return@AccountingRecordItem
                    onItemCheckedListener(record, isChecked)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MatchListPreview() {
    val sampleRecords = listOf(
        AccountingRecord("1", "Company A", "2023-08-01", 100f, "Invoice"),
        AccountingRecord("2", "Company B", "2023-08-02", 200f, "Bill"),
        AccountingRecord("3", "Company C", "2023-08-03", 300f, "Invoice")
    )

    MatchList(
        accountingRecords = sampleRecords,
        selectedRecords = setOf(sampleRecords[0]),
        onItemCheckedListener = { _, _ -> },
        canSelectItem = { true }
    )
}