package com.xero.interview.bankrecmatchmaker.data.accounting_records.source

import com.xero.interview.bankrecmatchmaker.data.accounting_records.model.AccountingRecord

class MockAccountingRecordsDataSource : AccountingRecordsDataSource {
    override suspend fun getAccountingRecords(): List<AccountingRecord> {
        return listOf(
            AccountingRecord("1", "City Limousines", "30 Aug", 249.00f, "Sales Invoice"),
            AccountingRecord("2", "Ridgeway University", "12 Sep", 618.50f, "Sales Invoice"),
            AccountingRecord("3", "Cube Land", "22 Sep", 495.00f, "Sales Invoice"),
            AccountingRecord("4", "Bayside Club", "23 Sep", 234.00f, "Sales Invoice"),
            AccountingRecord("5", "SMART Agency", "12 Sep", 250f, "Sales Invoice"),
            AccountingRecord("6", "PowerDirect", "11 Sep", 108.60f, "Sales Invoice"),
            AccountingRecord("7", "PC Complete", "17 Sep", 216.99f, "Sales Invoice"),
            AccountingRecord("8", "Truxton Properties", "17 Sep", 181.25f, "Sales Invoice"),
            AccountingRecord("9", "MCO Cleaning Services", "17 Sep", 170.50f, "Sales Invoice"),
            AccountingRecord("10", "Gateway Motors", "18 Sep", 411.35f, "Sales Invoice")
        )
    }

}