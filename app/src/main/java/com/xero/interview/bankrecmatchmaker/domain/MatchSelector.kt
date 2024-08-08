package com.xero.interview.bankrecmatchmaker.domain

import com.xero.interview.bankrecmatchmaker.data.accounting_records.model.AccountingRecord
import kotlin.system.measureTimeMillis

class MatchSelector {

    enum class MatchingStrategy {
        DYNAMIC_PROGRAMMING,
        GREEDY,
        BACKTRACKING
    }

    /**
     * Selects matching items based on the current total using the specified strategy.
     * First, it tries to find an exact match using a HashMap lookup.
     * If no exact match is found, it attempts to find a subset of items that sum up to the target total
     * using the specified strategy.
     *
     * @param items The list of AccountingRecord to select from
     * @param targetTotal The target total to match
     * @param strategy The strategy to use for finding a subset match
     * @return List of selected AccountingRecord that match the target total
     */
    fun selectMatchingItems(
        items: Map<Float, AccountingRecord>,
        targetTotal: Float,
        strategy: MatchingStrategy
    ): List<AccountingRecord> {
        // First, try to find an exact match using the HashMap.
        // Using HashMap lookup is more efficient than a linear search on the list.
        // Time complexity for HashMap lookup: O(1) average case.
        // Time complexity for List.find(): O(n) where n is the number of items.
        val exactMatch = findExactMatch(items, targetTotal)
        if (exactMatch != null) {
            return listOf(exactMatch)
        }

        var result: List<AccountingRecord>
        val time = measureTimeMillis {
            result = when (strategy) {
                MatchingStrategy.DYNAMIC_PROGRAMMING -> findSubsetUsingDP(items, targetTotal)
                MatchingStrategy.GREEDY -> findSubsetUsingGreedy(items, targetTotal)
                MatchingStrategy.BACKTRACKING -> findSubsetUsingBacktracking(items, targetTotal)
            }
        }

        println("${strategy.name} approach time: $time ms")
        return result
    }

    private fun findExactMatch(
        items: Map<Float, AccountingRecord>,
        targetTotal: Float
    ): AccountingRecord? {
        return items[targetTotal]
    }

    /**
     * Dynamic Programming (DP):
     * This method is for moderate-sized problems where the total isn't too large.
     * The basic idea is to use a boolean DP table to keep track of possible sums up to the target.
     * - Steps:
     *  1. Create a DP table dp where dp[i] is True if a subset with sum i can be formed, otherwise False
     *  2. Initialize dp[0] as True (a sum of 0 can always be achieved with an empty subset).
     *  3. For each item price, update the DP table in reverse (to avoid using the same item more than once in this iteration).
     *  4. If dp[target] is True, then a subset with the desired sum exists.
     * - Complexity: O(n * T), where n is the number of items and T is the target total.
     */
    private fun findSubsetUsingDP(
        items: Map<Float, AccountingRecord>,
        targetTotal: Float
    ): List<AccountingRecord> {
        // TODO: Implement Dynamic Programming approach
        // Steps:
        // 1. Create a DP table
        // 2. Fill the table
        // 3. Backtrack to find the actual subset
        return emptyList()
    }

    /**
     * Backtracking:
     * This method tries all possible subsets and is suitable for smaller lists or when exact solutions are needed.
     * - Steps:
     *  1. Recursively explore each item, either including it in the current subset or not.
     *  2. Check if the current subset sums up to the target.
     *  3. If it does, return the subset; otherwise, backtrack and try other combinations.
     * - Complexity: O(2^n), where n is the number of items. This is exponential, so it's only practical for small lists.
     */
    private fun findSubsetUsingBacktracking(
        items: Map<Float, AccountingRecord>,
        targetTotal: Float
    ): List<AccountingRecord> {
        // TODO: Implement Backtracking approach
        // Steps:
        // 1. Define a recursive function that tries including/excluding each item
        // 2. If current sum equals target, return the current subset
        // 3. If current sum exceeds target or all items exhausted, backtrack
        return emptyList()
    }

    /**
     * Meet-in-the-Middle:
     * This technique is useful for larger lists where a direct DP approach is infeasible.
     * It involves splitting the list into two halves, finding all subset sums for each half,
     * and then checking if there are combinations from the two halves that sum up to the target.
     * - Steps:
     *  1. Divide the list into two halves.
     *  2. Compute all possible subset sums for each half.
     *  3. Use a hash set to check if there are any pairs of sums (one from each half) that add up to the target.
     * - Complexity: O(2^(n/2)) for generating subsets and checking pairs, which is more manageable than O(2^n).
     */
    private fun findSubsetUsingGreedy(
        items: Map<Float, AccountingRecord>,
        targetTotal: Float
    ): List<AccountingRecord> {
        // TODO: Implement Greedy approach
        // Steps:
        // 1. Sort items by total (descending)
        // 2. Iterate through items, adding to result if it doesn't exceed target
        // 3. Return result if exact match, otherwise empty list
        return emptyList()
    }

}