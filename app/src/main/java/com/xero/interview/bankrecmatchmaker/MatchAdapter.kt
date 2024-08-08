package com.xero.interview.bankrecmatchmaker

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.xero.interview.bankrecmatchmaker.core.common.CurrencyFormatter
import com.xero.interview.bankrecmatchmaker.data.accounting_records.model.AccountingRecord
import com.xero.interview.bankrecmatchmaker.databinding.ListItemMatchBinding
import java.util.Currency

import java.util.Locale

class MatchAdapter(
    private val onItemCheckedListener: (AccountingRecord, Boolean) -> Unit,
    private val canSelectItem: (AccountingRecord) -> Boolean
) : ListAdapter<AccountingRecord, MatchAdapter.ViewHolder>(DIFF_CALLBACK) {

    private val currencyFormatter =
        CurrencyFormatter(Locale.getDefault(), Currency.getInstance(Locale.getDefault()))
    private val selectedItems = mutableSetOf<AccountingRecord>()

    fun setItemSelected(item: AccountingRecord, isSelected: Boolean) {
        if (isSelected && !canSelectItem(item)) {
            return
        }
        if (isSelected) {
            selectedItems.add(item)
        } else {
            selectedItems.remove(item)
        }
        notifyItemChanged(currentList.indexOf(item))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val isSelected = selectedItems.contains(item)
        holder.bind(item, isSelected)
    }

    inner class ViewHolder(private val binding: ListItemMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(accountingRecord: AccountingRecord, isSelected: Boolean) {
            binding.textMain.text = accountingRecord.paidTo
            binding.textTotal.text = currencyFormatter.format(accountingRecord.total)
            binding.textSubLeft.text = accountingRecord.transactionDate
            binding.textSubRight.text = accountingRecord.docType

            binding.root.isChecked = isSelected
            binding.root.setOnClickListener {
                val newCheckedState = !binding.root.isChecked
                setItemSelected(accountingRecord, newCheckedState)
                onItemCheckedListener(accountingRecord, newCheckedState)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AccountingRecord>() {
            override fun areItemsTheSame(
                oldItem: AccountingRecord,
                newItem: AccountingRecord
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AccountingRecord,
                newItem: AccountingRecord
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}