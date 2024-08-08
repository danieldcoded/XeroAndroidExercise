package com.xero.interview.bankrecmatchmaker.data.accounting_records.model

data class AccountingRecord(
    val id: String,
    val paidTo: String,
    val transactionDate: String,
    val total: Float,
    val docType: String
)
