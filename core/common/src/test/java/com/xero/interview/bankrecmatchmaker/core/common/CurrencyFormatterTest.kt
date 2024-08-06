package com.xero.interview.bankrecmatchmaker.core.common

import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class CurrencyFormatterTest {

    @Test
    fun `format with default locale and currency`() {
        val formatter = CurrencyFormatter()
        val result = formatter.format(1000.50)
        // Note: This test assumes the default locale. Adjust the expected result if necessary.
        assertTrue(result.contains("1,000.50"))
    }

    @Test
    fun `format with US locale`() {
        val formatter = CurrencyFormatter(Locale.US)
        val result = formatter.format(1000.50)
        assertEquals("$1,000.50", result)
    }

    @Test
    fun `format with UK locale`() {
        val formatter = CurrencyFormatter(Locale.UK)
        val result = formatter.format(1000.50)
        assertEquals("£1,000.50", result)
    }

    @Test
    fun `format with custom fraction digits`() {
        val formatter = CurrencyFormatter(Locale.US)
        val result =
            formatter.format(1000.56789, minimumFractionDigits = 3, maximumFractionDigits = 4)
        assertEquals("$1,000.5679", result)
    }

    @Test
    fun `format large amount`() {
        val formatter = CurrencyFormatter(Locale.US)
        val largeAmount = BigDecimal("1000000000000.50")
        val result = formatter.formatLargeAmount(largeAmount)
        assertEquals("$1,000,000,000,000.50", result)
    }

    @Test
    fun `get currency symbol`() {
        val formatter = CurrencyFormatter(Locale.US)
        assertEquals("$", formatter.getCurrencySymbol())
    }

    @Test
    fun `format with specific currency`() {
        val formatter = CurrencyFormatter.forCurrency("EUR")
        val result = formatter.format(1000.50)
        assertTrue(result.contains("€") &&
                (result.contains("1.000,50") || result.contains("1,000.50")))
    }

    @Test
    fun `format zero amount`() {
        val formatter = CurrencyFormatter(Locale.US)
        val result = formatter.format(0)
        assertEquals("$0.00", result)
    }

    @Test
    fun `format negative amount`() {
        val formatter = CurrencyFormatter(Locale.US)
        val result = formatter.format(-1000.50)
        assertEquals("-$1,000.50", result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `format with invalid currency code throws exception`() {
        CurrencyFormatter.forCurrency("INVALID")
    }

    @Test
    fun `format with Japanese locale`() {
        val formatter = CurrencyFormatter(Locale.JAPAN)
        val result = formatter.format(1000.50)
        // Note: Yen doesn't display decimal points
        assertTrue(result.contains("￥") && result.contains("1,000") || result.contains("1,001"))
    }

    @Test
    fun `format very small amount`() {
        val formatter = CurrencyFormatter(Locale.US)
        val result = formatter.format(0.01)
        assertEquals("$0.01", result)
    }
}