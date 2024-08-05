package com.xero.interview.bankrecmatchmaker

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Checkable
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatCheckBox

class CheckedListItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), Checkable {

    private val checkBox: AppCompatCheckBox
    private var onItemClickListener: OnItemClickListener? = null

    init {
        orientation = HORIZONTAL

        checkBox = (LayoutInflater.from(context)
            .inflate(R.layout.list_item_checkbox, this, false) as AppCompatCheckBox)
            .also { addView(it, 0) }

        setOnClickListener {
            toggle()
            onItemClickListener?.onItemClick(isChecked)
        }
    }

    override fun setChecked(checked: Boolean) {
        checkBox.isChecked = checked
    }

    override fun isChecked(): Boolean = checkBox.isChecked

    override fun toggle() {
        checkBox.toggle()
    }

    interface OnItemClickListener {
        fun onItemClick(isChecked: Boolean)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
}