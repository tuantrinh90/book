package com.dz.libraries.views.datetimes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.dz.libraries.R
import com.dz.libraries.fragments.ExtBaseBottomDialogFragment
import com.dz.libraries.views.textviews.ExtTextView
import java.util.*

class ExtDatePickerDialogFragment : ExtBaseBottomDialogFragment() {
    companion object {
        private const val TAG = "ExtDatePickerDialogFragment"

        fun newInstance(): ExtDatePickerDialogFragment {
            return ExtDatePickerDialogFragment()
        }
    }

    // view
    private lateinit var tvCancel: ExtTextView
    private lateinit var tvSave: ExtTextView
    private lateinit var dpDatePicker: DatePicker

    // consumer
    private var calendarConsumer: ((calendar: Calendar?) -> Unit)? = null
    private var conditionConsumer: ((calendar: Calendar) -> Boolean)? = null

    // variable
    private var calendar: Calendar? = null
    private var minDate: Calendar? = null
    private var maxDate: Calendar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.date_picker_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // null point exception value
        if (minDate == null || maxDate == null || calendar == null
                || calendarConsumer == null || conditionConsumer == null) throw NullPointerException()

        // cancel
        tvCancel = view.findViewById(R.id.tvCancel)
        tvCancel.setOnClickListener { onClickCancel() }

        // save
        tvSave = view.findViewById(R.id.tvSave)
        tvSave.setOnClickListener { onClickSave() }

        dpDatePicker = view.findViewById(R.id.dpDatePicker)
        dpDatePicker.init(calendar!!.get(Calendar.YEAR), calendar!!.get(Calendar.MONTH), calendar!!.get(Calendar.DAY_OF_MONTH))
        { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            run {
                calendar!!.set(Calendar.YEAR, year)
                calendar!!.set(Calendar.MONTH, monthOfYear)
                calendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                setDisplayButtonDone(conditionConsumer!!.invoke(calendar!!))
            }
        }

        // set min, max date
        dpDatePicker.minDate = minDate!!.timeInMillis
        dpDatePicker.maxDate = maxDate!!.timeInMillis

        // display button done
        setDisplayButtonDone(conditionConsumer!!.invoke(calendar!!))
    }

    private fun setDisplayButtonDone(isVisible: Boolean) {
        tvSave.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun onClickCancel() {
        calendarConsumer?.invoke(null)
        dismiss()
    }

    private fun onClickSave() {
        calendarConsumer?.invoke(calendar)
        dismiss()
    }

    fun setValue(calendar: Calendar): ExtDatePickerDialogFragment {
        this.calendar = calendar.clone() as Calendar
        return this
    }

    fun setMinDate(minDate: Calendar): ExtDatePickerDialogFragment {
        this.minDate = minDate
        return this
    }

    fun setMaxDate(maxDate: Calendar): ExtDatePickerDialogFragment {
        this.maxDate = maxDate
        return this
    }

    fun setCalendarConsumer(calendarConsumer: ((calendar: Calendar?) -> Unit)? = null): ExtDatePickerDialogFragment {
        this.calendarConsumer = calendarConsumer
        return this
    }

    fun setConditionConsumer(conditionConsumer: ((calendar: Calendar) -> Boolean)? = null): ExtDatePickerDialogFragment {
        this.conditionConsumer = conditionConsumer
        return this
    }
}
