package com.xero.interview.bankrecmatchmaker

import org.junit.Assert.*
import org.junit.Test

class MatchItemTest {

    @Test
    fun `test MatchItem creation`() {
        val item = MatchItem("Test", "2023-08-01", 100f, "Invoice")
        assertEquals("Test", item.paidTo)
        assertEquals("2023-08-01", item.transactionDate)
        assertEquals(100f, item.total)
        assertEquals("Invoice", item.docType)
    }

    @Test
    fun `test MatchItem equality`() {
        val item1 = MatchItem("Test", "2023-08-01", 100f, "Invoice")
        val item2 = MatchItem("Test", "2023-08-01", 100f, "Invoice")
        val item3 = MatchItem("Different", "2023-08-01", 100f, "Invoice")
        assertEquals(item1, item2)
        assertNotEquals(item1, item3)
    }

}