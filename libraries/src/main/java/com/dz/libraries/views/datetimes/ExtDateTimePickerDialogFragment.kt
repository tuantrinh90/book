package com.dz.libraries.views.datetimes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import com.dz.libraries.R
import com.dz.libraries.fragments.ExtBaseBottomDialogFragment
import com.dz.libraries.views.textviews.ExtTextView
import java.util.*

class ExtDateTimePickerDialogFragment : ExtBaseBottomDialogFragment() {
    companion object {
        // instance
        fun newInstance(): ExtDateTimePickerDialogFragment {
            return ExtDateTimePickerDialogFragment()
        }
    }

    // view
    private lateinit var tvCancel: ExtTextView
    private lateinit var tvSave: ExtTextView
    private lateinit var dpDatePicker: DatePicker
    private lateinit var tpTimePicker: TimePicker

    // variable
    private var calendarConsumer: ((calendar: Calendar?) -> Unit)? = null
    private var conditionConsumer: ((calendar: Calendar) -> Boolean)? = null

    private var calendar: Calendar? = null
    private var minDate: Calendar? = null
    private var maxDate: Calendar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.datetime_picker_dialog_fragment, container, false)
    }

    @Suppress("DEPRECATION")
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

        tpTimePicker = view.findViewById(R.id.tpTimePicker)
        tpTimePicker.currentHour = calendar!!.get(Calendar.HOUR_OF_DAY)
        tpTimePicker.currentMinute = calendar!!.get(Calendar.MINUTE)
        tpTimePicker.setIs24HourView(true)

        tpTimePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            run {
                calendar!!.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar!!.set(Calendar.MINUTE, minute)
                setDisplayButtonDone(conditionConsumer!!.invoke(calendar!!))
            }
        }

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

    fun setValue(calendar: Calendar): ExtDateTimePickerDialogFragment {
        this.calendar = calendar.clone() as Calendar
        return this
    }

    fun setMinDate(minDate: Calendar): ExtDateTimePickerDialogFragment {
        this.minDate = minDate
        return this
    }

    fun setMaxDate(maxDate: Calendar): ExtDateTimePickerDialogFragment {
        this.maxDate = maxDate
        return this
    }

    fun setCalendarConsumer(calendarConsumer: ((calendar: Calendar?) -> Unit)? = null): ExtDateTimePickerDialogFragment {
        this.calendarConsumer = calendarConsumer
        return this
    }

    fun setConditionConsumer(conditionConsumer: ((calendar: Calendar) -> Boolean)? = null): ExtDateTimePickerDialogFragment {
        this.conditionConsumer = conditionConsumer
        return this
    }
}
