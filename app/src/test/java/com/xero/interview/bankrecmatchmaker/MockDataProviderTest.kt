package com.xero.interview.bankrecmatchmaker;

import com.xero.interview.bankrecmatchmaker.data.MockDataProvider
import org.junit.Assert.*
import org.junit.Test

class MockDataProviderTest {

    @Test
    fun `test getMockMatchItems`() {
        val items = MockDataProvider.getMockMatchItems()
        assertEquals(10, items.size)
    }

    @Test
    fun `test getTotalToItemMap`() {
        val items = MockDataProvider.getMockMatchItems()
        val map = MockDataProvider.getTotalToItemMap(items)
        assertEquals(items.size, map.size)
        items.forEach { item ->
            assertEquals(item, map[item.total])
        }
    }

}