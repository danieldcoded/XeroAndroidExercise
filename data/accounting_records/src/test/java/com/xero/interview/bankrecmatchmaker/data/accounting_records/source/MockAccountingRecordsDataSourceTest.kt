package com.xero.interview.bankrecmatchmaker.data.accounting_records.source

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class MockAccountingRecordsDataSourceTest {

    private lateinit var dataSource: MockAccountingRecordsDataSource

    @Before
    fun setup() {
        dataSource = MockAccountingRecordsDataSource()
    }

    @Test
    fun `getAccountingRecords returns non-empty list`() = runBlocking {
        val records = dataSource.getAccountingRecords()

        assertFalse(records.isEmpty())
    }

    @Test
    fun `getAccountingRecords returns expected number of records`() = runBlocking {
        val records = dataSource.getAccountingRecords()

        // Assuming MockAccountingRecordsDataSource returns 11 records
        assertEquals(11, records.size)
    }

}