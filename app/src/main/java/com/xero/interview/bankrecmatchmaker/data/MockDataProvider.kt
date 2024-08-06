package com.xero.interview.bankrecmatchmaker.data

import com.xero.interview.bankrecmatchmaker.MatchItem

/**
 * Provides mock data for testing and development purposes.
 * This object contains methods to generate sample MatchItems and related data structures.
 */
object MockDataProvider {

    /**
     * Generates a list of mock MatchItems.
     *
     * This function creates a fixed set of MatchItems with predefined values.
     * It's useful for testing the UI and functionality without requiring a back-end connection.
     *
     * @return A List of MatchItem objects representing different invoices.
     */
    fun getMockMatchItems(): List<MatchItem> = listOf(
        MatchItem("City Limousines", "30 Aug", 249.00f, "Sales Invoice"),
        MatchItem("Ridgeway University", "12 Sep", 618.50f, "Sales Invoice"),
        MatchItem("Cube Land", "22 Sep", 495.00f, "Sales Invoice"),
        MatchItem("Bayside Club", "23 Sep", 234.00f, "Sales Invoice"),
        MatchItem("SMART Agency", "12 Sep", 250f, "Sales Invoice"),
        MatchItem("PowerDirect", "11 Sep", 108.60f, "Sales Invoice"),
        MatchItem("PC Complete", "17 Sep", 216.99f, "Sales Invoice"),
        MatchItem("Truxton Properties", "17 Sep", 181.25f, "Sales Invoice"),
        MatchItem("MCO Cleaning Services", "17 Sep", 170.50f, "Sales Invoice"),
        MatchItem("Gateway Motors", "18 Sep", 411.35f, "Sales Invoice")
    )

    /**
     * Creates a map of total amounts to MatchItems.
     *
     * This function takes a list of MatchItems and creates a map where
     * the keys are the total amounts and the values are the corresponding MatchItems.
     * This is useful for quickly finding a MatchItem given a specific total amount.
     *
     * @param matchItems The list of MatchItems to process.
     * @return A Map with Float keys (total amounts) and MatchItem values.
     */
    fun getTotalToItemMap(matchItems: List<MatchItem>): Map<Float, MatchItem> =
        matchItems.associateBy { it.total }

}