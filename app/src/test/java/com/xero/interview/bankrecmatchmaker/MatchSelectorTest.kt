package com.xero.interview.bankrecmatchmaker

import com.xero.interview.bankrecmatchmaker.domain.MatchSelector
import com.xero.interview.bankrecmatchmaker.data.accounting_records.model.AccountingRecord
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MatchSelectorTest {

    private lateinit var matchSelector: MatchSelector

    @Before
    fun setup() {
        matchSelector = MatchSelector()
    }

    private fun createTestItems(): Map<Float, AccountingRecord> {
        return mapOf(
            100f to AccountingRecord("1", "Test1", "2023-08-01", 100f, "Invoice"),
            200f to AccountingRecord("2", "Test2", "2023-08-02", 200f, "Bill"),
            300f to AccountingRecord("3", "Test3", "2023-08-03", 300f, "Invoice")
        )
    }

    @Test
    fun `test exact match with all strategies`() {
        val items = createTestItems()
        MatchSelector.MatchingStrategy.values().forEach { strategy ->
            val result = matchSelector.selectMatchingItems(items, 200f, strategy)
            assertEquals("Strategy: $strategy", 1, result.size)
            assertEquals("Strategy: $strategy", 200f, result[0].total)
        }
    }

    @Test
    fun `test subset match with all strategies`() {
        val items = createTestItems()
        MatchSelector.MatchingStrategy.values().forEach { strategy ->
            val result = matchSelector.selectMatchingItems(items, 300f, strategy)
            assertEquals("Strategy: $strategy", 2, result.size)
            assertEquals(
                "Strategy: $strategy",
                300f,
                result.sumOf { it.total.toDouble() }.toFloat()
            )
        }
    }

    @Test
    fun `test no match with all strategies`() {
        val items = createTestItems()
        MatchSelector.MatchingStrategy.values().forEach { strategy ->
            val result = matchSelector.selectMatchingItems(items, 150f, strategy)
            assertTrue("Strategy: $strategy", result.isEmpty())
        }
    }

    @Test
    fun `test empty map with all strategies`() {
        val emptyMap = emptyMap<Float, AccountingRecord>()
        MatchSelector.MatchingStrategy.values().forEach { strategy ->
            val result = matchSelector.selectMatchingItems(emptyMap, 100f, strategy)
            assertTrue("Strategy: $strategy", result.isEmpty())
        }
    }

    @Test
    fun `test all items match with all strategies`() {
        val items = createTestItems()
        MatchSelector.MatchingStrategy.values().forEach { strategy ->
            val result = matchSelector.selectMatchingItems(items, 600f, strategy)
            assertEquals("Strategy: $strategy", 3, result.size)
            assertEquals(
                "Strategy: $strategy",
                600f,
                result.sumOf { it.total.toDouble() }.toFloat()
            )
        }
    }

    @Test
    fun `test multiple possible subsets, selects optimal with all strategies`() {
        val items = mapOf(
            100f to AccountingRecord("1", "Test1", "2023-08-01", 100f, "Invoice"),
            200f to AccountingRecord("2", "Test2", "2023-08-02", 200f, "Bill"),
            300f to AccountingRecord("3", "Test3", "2023-08-03", 300f, "Invoice"),
            400f to AccountingRecord("4", "Test4", "2023-08-04", 400f, "Bill")
        )
        MatchSelector.MatchingStrategy.values().forEach { strategy ->
            val result = matchSelector.selectMatchingItems(items, 500f, strategy)
            assertEquals("Strategy: $strategy", 2, result.size)
            assertTrue("Strategy: $strategy", result.any { it.total == 300f })
            assertTrue("Strategy: $strategy", result.any { it.total == 200f })
        }
    }

    @Test
    fun `test large number of items with all strategies`() {
        val items = (1..1000).associate {
            it.toFloat() to AccountingRecord(
                it.toString(),
                "Test$it",
                "2023-08-$it",
                it.toFloat(),
                "Invoice"
            )
        }
        MatchSelector.MatchingStrategy.values().forEach { strategy ->
            val result = matchSelector.selectMatchingItems(items, 500500f, strategy)
            assertFalse("Strategy: $strategy", result.isEmpty())
            assertEquals(
                "Strategy: $strategy",
                500500f,
                result.sumOf { it.total.toDouble() }.toFloat()
            )
        }
    }

    @Test
    fun `test performance comparison of all strategies`() {
        val items = (1..1000).associate {
            it.toFloat() to AccountingRecord(
                it.toString(),
                "Test$it",
                "2023-08-$it",
                it.toFloat(),
                "Invoice"
            )
        }
        val targetTotal = 500500f

        MatchSelector.MatchingStrategy.values().forEach { strategy ->
            val result = matchSelector.selectMatchingItems(items, targetTotal, strategy)
            assertFalse("Strategy: $strategy", result.isEmpty())
            assertEquals(
                "Strategy: $strategy",
                targetTotal,
                result.sumOf { it.total.toDouble() }.toFloat()
            )
            // Note: This test will also print the execution times for each strategy
        }
    }
}