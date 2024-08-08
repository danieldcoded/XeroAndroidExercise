package com.xero.interview.bankrecmatchmaker.data.accounting_records.repo

import com.xero.interview.bankrecmatchmaker.data.accounting_records.model.AccountingRecord
import com.xero.interview.bankrecmatchmaker.data.accounting_records.source.AccountingRecordsDataSource

class AccountingRecordsRepository(private val dataSource: AccountingRecordsDataSource) {

    suspend fun getAccountingRecords(): List<AccountingRecord> = dataSource.getAccountingRecords()

}