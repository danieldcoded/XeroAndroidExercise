package com.xero.interview.bankrecmatchmaker.data.accounting_records.model

import org.junit.Assert.*
import org.junit.Test

class AccountingRecordTest {

    @Test
    fun `test MatchItem creation`() {
        val item = AccountingRecord(
            "1",
            "Test",
            "2023-08-01",
            100f,
            "Invoice"
        )
        assertEquals("Test", item.paidTo)
        assertEquals("2023-08-01", item.transactionDate)
        assertEquals(100f, item.total)
        assertEquals("Invoice", item.docType)
    }

    @Test
    fun `test MatchItem equality`() {
        val item1 = AccountingRecord(
            "1",
            "Test",
            "2023-08-01",
            100f,
            "Invoice"
        )
        val item2 = AccountingRecord(
            "1",
            "Test",
            "2023-08-01",
            100f,
            "Invoice"
        )
        val item3 = AccountingRecord(
            "2",
            "Test",
            "2023-08-01",
            100f,
            "Invoice"
        )
        val item4 = AccountingRecord(
            "3",
            "Different",
            "2023-08-01",
            100f,
            "Invoice"
        )
        assertEquals(item1, item2)
        assertNotEquals(item1, item3)
        assertNotEquals(item1, item4)
    }

}