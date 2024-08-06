package com.xero.interview.bankrecmatchmaker.core.common

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * CurrencyFormatter provides locale-aware currency formatting capabilities.
 *
 * IMPORTANT: This class is intended for demonstration purposes only and is not production-ready.
 * In a real-world scenario, currency formatting should be handled by the backend to ensure
 * consistency across different app versions and devices.
 *
 * Reasons for back-end handling:
 * - Avoids discrepancies between app versions
 * - Easier to manage and update without requiring app releases
 * - Ensures users see consistent formatting regardless of their app version or device settings
 * - Allows for centralized control over currency display rules
 *
 * This class is designed to showcase skills in locale-aware formatting and follows the
 * Single Responsibility Principle by focusing solely on currency formatting tasks.
 *
 * Features demonstrated:
 * - Locale-aware formatting
 * - Support for different currencies
 * - Configurable decimal places
 * - Handling of large numbers
 *
 * Usage example (for demonstration purposes only):
 * val formatter = CurrencyFormatter()
 * val formattedAmount = formatter.format(1000.50) // Uses default 2 decimal places
 * val customFormatted = formatter.format(1000.50, 3, 3) // Uses 3 decimal places
 *
 * In a production environment, consider:
 * - Receiving pre-formatted currency strings from the back-end
 * - Using server-side configuration for currency display rules
 * - Implementing a more robust error handling and fallback mechanism
 *
 * @property locale The locale to use for formatting. Defaults to the system's default locale.
 * @property currency The currency to use for formatting. Defaults to the locale's default currency.
 */
class CurrencyFormatter(
    private val locale: Locale = Locale.getDefault(),
    private val currency: Currency = Currency.getInstance(locale)
) {
    private val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(locale).apply {
        this.currency = this@CurrencyFormatter.currency
    }

    /**
     * Formats the given amount according to the specified locale and currency.
     *
     * @param amount The amount to format.
     * @param minimumFractionDigits The minimum number of digits to show after the decimal point.
     * @param maximumFractionDigits The maximum number of digits to show after the decimal point.
     * @return A formatted string representation of the amount.
     */
    @JvmOverloads
    fun format(
        amount: Number,
        minimumFractionDigits: Int = DEFAULT_FRACTION_DIGITS,
        maximumFractionDigits: Int = DEFAULT_FRACTION_DIGITS
    ): String {
        numberFormat.minimumFractionDigits = minimumFractionDigits
        numberFormat.maximumFractionDigits = maximumFractionDigits
        return numberFormat.format(amount)
    }

    /**
     * Formats a large amount (BigDecimal) with grouping.
     *
     * This method is useful for displaying very large currency amounts
     * that exceed the precision of standard floating-point types.
     *
     * @param amount The large amount to format.
     * @return A formatted string representation of the large amount.
     */
    fun formatLargeAmount(amount: BigDecimal): String {
        return numberFormat.format(amount)
    }

    /**
     * Returns the currency symbol for the current currency.
     *
     * @return The currency symbol as a string.
     */
    fun getCurrencySymbol(): String = currency.getSymbol(locale)

    companion object {

        const val DEFAULT_FRACTION_DIGITS = 2

        /**
         * Creates a CurrencyFormatter for a specific currency code.
         *
         * @param currencyCode The ISO 4217 currency code (e.g., "USD", "EUR").
         * @return A CurrencyFormatter instance for the specified currency.
         * @throws IllegalArgumentException if the currency code is invalid.
         */
        fun forCurrency(currencyCode: String): CurrencyFormatter {
            val currency = Currency.getInstance(currencyCode)
            val locale = when (currencyCode) {
                "EUR" -> Locale.GERMANY
                "JPY" -> Locale.JAPAN
                "GBP" -> Locale.UK
                else -> Locale.getDefault(Locale.Category.FORMAT)
            }
            return CurrencyFormatter(currency = currency, locale = locale)
        }
    }
}