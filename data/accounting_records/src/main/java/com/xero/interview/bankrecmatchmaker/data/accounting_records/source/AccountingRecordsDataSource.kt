package com.xero.interview.bankrecmatchmaker.data.accounting_records.source

import com.xero.interview.bankrecmatchmaker.data.accounting_records.model.AccountingRecord

interface AccountingRecordsDataSource {
    suspend fun getAccountingRecords(): List<AccountingRecord>
}