package com.xero.interview.bankrecmatchmaker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xero.interview.bankrecmatchmaker.core.common.CurrencyFormatter
import com.xero.interview.bankrecmatchmaker.data.accounting_records.model.AccountingRecord
import java.util.Currency
import java.util.Locale

@Composable
fun AccountingRecordItem(
    accountingRecord: AccountingRecord,
    isSelected: Boolean,
    currencyFormatter: CurrencyFormatter,
    onItemClick: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(!isSelected) },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onItemClick(it) }
            )
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = accountingRecord.paidTo,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = currencyFormatter.format(accountingRecord.total),
                        style = MaterialTheme.typography.body1,
                    )
                }
                Row {
                    Text(
                        text = accountingRecord.transactionDate,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = accountingRecord.docType,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountingRecordItemPreview() {
    val sampleRecord = AccountingRecord(
        id = "1",
        paidTo = "Sample Company",
        transactionDate = "2023-08-01",
        total = 100.0f,
        docType = "Invoice"
    )
    val currencyFormatter = CurrencyFormatter(Locale.ENGLISH, Currency.getInstance("NZD"))

    AccountingRecordItem(
        accountingRecord = sampleRecord,
        isSelected = false,
        currencyFormatter = currencyFormatter,
        onItemClick = {}
    )
}