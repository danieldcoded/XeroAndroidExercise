package com.xero.interview.bankrecmatchmaker.data.accounting_records.repository

import com.xero.interview.bankrecmatchmaker.data.accounting_records.model.AccountingRecord
import com.xero.interview.bankrecmatchmaker.data.accounting_records.repo.AccountingRecordsRepository
import com.xero.interview.bankrecmatchmaker.data.accounting_records.source.AccountingRecordsDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class AccountingRecordsRepositoryTest {

    private lateinit var mockDataSource: AccountingRecordsDataSource
    private lateinit var repository: AccountingRecordsRepository

    @Before
    fun setup() {
        mockDataSource = mock(AccountingRecordsDataSource::class.java)
        repository = AccountingRecordsRepository(mockDataSource)
    }

    @Test
    fun getAccountingRecordsReturnsRecordsFromDataSource() {
        runBlocking {
            val expectedRecords = listOf(
                AccountingRecord("1", "Test Company", "2023-08-01", 100f, "Invoice"),
                AccountingRecord("2", "Another Company", "2023-08-02", 200f, "Bill")
            )
            `when`(mockDataSource.getAccountingRecords()).thenReturn(expectedRecords)

            val actualRecords = repository.getAccountingRecords()

            assertEquals(expectedRecords, actualRecords)
            verify(mockDataSource).getAccountingRecords()
        }
    }

}