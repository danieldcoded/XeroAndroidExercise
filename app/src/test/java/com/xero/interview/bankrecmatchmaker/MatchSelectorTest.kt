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
            400f to AccountingRecord("3", "Test3", "2023-08-03", 400f, "Invoice")
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
            val result = matchSelector.selectMatchingItems(items, 700f, strategy)
            assertEquals("Strategy: $strategy", 3, result.size)
            assertEquals(
                "Strategy: $strategy",
                700f,
                result.sumOf { it.total.toDouble() }.toFloat()
            )
        }
    }

    @Test
    fun `test performance comparison of all strategies`() {
        val items = (1..15).associate {
            it.toFloat() to AccountingRecord(
                it.toString(),
                "Test$it",
                "2023-08-$it",
                it.toFloat(),
                "Invoice"
            )
        }
        val targetTotal = 40f  // Sum of 8, 9, 10, 13 is exactly 40

        MatchSelector.MatchingStrategy.values().forEach { strategy ->
            val startTime = System.nanoTime()
            val result = matchSelector.selectMatchingItems(items, targetTotal, strategy)
            val endTime = System.nanoTime()
            val executionTime = (endTime - startTime) / 1_000_000.0  // Convert to milliseconds

            assertFalse("Strategy: $strategy", result.isEmpty())
            assertEquals("Strategy: $strategy", targetTotal, result.sumOf { it.total.toDouble() }.toFloat())
            println("${strategy.name} execution time: $executionTime ms")
        }
    }

}