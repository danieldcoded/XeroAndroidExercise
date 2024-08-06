package com.xero.interview.bankrecmatchmaker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Checkable
import android.widget.LinearLayout
import com.xero.interview.bankrecmatchmaker.databinding.ListItemCheckboxBinding

/**
 * A custom LinearLayout that implements the Checkable interface.
 * It represents a list item with a checkbox and handles the checked state.
 * The checked state can be toggled by clicking on the item.
 * It also provides a listener interface to notify when the checked state changes.
 */
class CheckedListItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Checkable {

    private val binding: ListItemCheckboxBinding
    private var onItemClickListener: OnItemClickListener? = null

    init {
        orientation = HORIZONTAL

        binding = ListItemCheckboxBinding.inflate(LayoutInflater.from(context), this, true)

        setOnClickListener {
            toggle()
            onItemClickListener?.onItemClick(isChecked)
        }
    }

    override fun setChecked(checked: Boolean) {
        binding.checkbox.isChecked = checked
    }

    override fun isChecked(): Boolean = binding.checkbox.isChecked

    override fun toggle() {
        binding.checkbox.toggle()
    }

    /**
     * An interface to notify when the checked state of the item changes.
     */
    interface OnItemClickListener {
        fun onItemClick(isChecked: Boolean)
    }

}