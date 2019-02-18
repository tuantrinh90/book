package com.dz.libraries.views.datetimes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dz.libraries.R
import com.dz.libraries.fragments.ExtBaseBottomDialogFragment
import com.dz.libraries.utilities.DateTimeUtility
import com.dz.libraries.views.numberpickers.ExtNumberPicker
import com.dz.libraries.views.textviews.ExtTextView
import java.util.*

class ExtDayMonthYearDialogFragment : ExtBaseBottomDialogFragment() {
    companion object {
        // const
        private const val MIN_INDEX_MONTH = 0
        private const val MAX_INDEX_MONTH = 11
        private const val MIN_INDEX_DAY = 1
        private const val MAX_INDEX_DAY_31 = 31
        private const val MAX_INDEX_DAY_30 = 30
        private const val MAX_INDEX_DAY_29 = 29
        private const val MAX_INDEX_DAY_28 = 28

        // instance
        fun newInstance(): ExtDayMonthYearDialogFragment {
            return ExtDayMonthYearDialogFragment()
        }
    }

    // view
    private lateinit var tvCancel: ExtTextView
    private lateinit var tvSave: ExtTextView
    private lateinit var numPickerDay: ExtNumberPicker
    private lateinit var numPickerMonth: ExtNumberPicker
    private lateinit var numPickerYear: ExtNumberPicker

    // calendar
    private var minDate: Calendar? = null
    private var maxDate: Calendar? = null
    private var valueDate: Calendar? = null
    private var dayOfMonth = MAX_INDEX_DAY_31

    // consumer
    private var calendarConsumer: ((calendar: Calendar?) -> Unit)? = null
    private var conditionConsumer: ((calendar: Calendar) -> Boolean)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.day_month_year_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews(view)
        initDatePicker()
    }

    private fun setUpViews(view: View) {
        // null point exception value
        if (minDate == null || maxDate == null || valueDate == null
                || calendarConsumer == null || conditionConsumer == null) throw NullPointerException()

        // cancel
        tvCancel = view.findViewById(R.id.tvCancel)
        tvCancel.setOnClickListener { onCancelClick() }

        // save
        tvSave = view.findViewById(R.id.tvSave)
        tvSave.setOnClickListener { onSaveClick() }

        // number picker
        numPickerDay = view.findViewById(R.id.numPickerDay)
        numPickerMonth = view.findViewById(R.id.numPickerMonth)
        numPickerYear = view.findViewById(R.id.numPickerYear)
    }

    private fun initDatePicker() {
        // wheel
        numPickerDay.wrapSelectorWheel = false
        numPickerMonth.wrapSelectorWheel = false
        numPickerYear.wrapSelectorWheel = false

        //  max min day
        numPickerDay.minValue = MIN_INDEX_DAY
        numPickerDay.maxValue = MAX_INDEX_DAY_31

        // day value display
        numPickerDay.displayedValues = arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
                "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31")

        // max min month
        numPickerMonth.minValue = MIN_INDEX_MONTH
        numPickerMonth.maxValue = MAX_INDEX_MONTH

        // month value display
        numPickerMonth.displayedValues = arrayOf(getString(R.string.month_one), getString(R.string.month_two), getString(R.string.month_three),
                getString(R.string.month_four), getString(R.string.month_five), getString(R.string.month_six), getString(R.string.month_seven),
                getString(R.string.month_eight), getString(R.string.month_nice), getString(R.string.month_ten), getString(R.string.month_eleven),
                getString(R.string.month_twelfth))

        // max, min year by params
        numPickerYear.minValue = minDate!!.get(Calendar.YEAR)
        numPickerYear.maxValue = maxDate!!.get(Calendar.YEAR)

        // listener year
        numPickerYear.setOnValueChangedListener { _, _, newVal ->
            run {
                valueDate!!.set(Calendar.YEAR, newVal)
                setDisplayButtonDone(conditionConsumer!!.invoke(valueDate!!))
            }
        }

        // month
        numPickerMonth.setOnValueChangedListener { _, _, newVal ->
            run {
                valueDate!!.set(Calendar.MONTH, newVal)
                setDisplayButtonDone(conditionConsumer!!.invoke(valueDate!!))
            }
        }

        // day
        numPickerDay.setOnValueChangedListener { _, _, newVal ->
            run {
                valueDate!!.set(Calendar.DAY_OF_MONTH, newVal)
                setDisplayButtonDone(conditionConsumer!!.invoke(valueDate!!))
            }
        }

        // Set default value
        setValue()
    }

    private fun setValue() {
        numPickerDay.value = valueDate!!.get(Calendar.DAY_OF_MONTH)
        numPickerMonth.value = valueDate!!.get(Calendar.MONTH)
        numPickerYear.value = valueDate!!.get(Calendar.YEAR)
        setDisplayButtonDone(conditionConsumer!!.invoke(valueDate!!))
    }

    private fun setDisplayButtonDone(isVisible: Boolean) {
        // check month
        when (valueDate!!.get(Calendar.MONTH)) {
            0, 2, 4, 6, 7, 9, 11 -> dayOfMonth = MAX_INDEX_DAY_31
            1 -> dayOfMonth = if (DateTimeUtility.isLeapYear(valueDate!!.get(Calendar.YEAR))) MAX_INDEX_DAY_29 else MAX_INDEX_DAY_28
            3, 5, 8, 10 -> dayOfMonth = MAX_INDEX_DAY_30
        }

        // set max day
        numPickerDay.maxValue = dayOfMonth
        if (valueDate!!.get(Calendar.DAY_OF_MONTH) > dayOfMonth) {
            numPickerDay.value = dayOfMonth
        }

        tvSave.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun onSaveClick() {
        calendarConsumer?.invoke(valueDate!!)
        dismiss()
    }

    private fun onCancelClick() {
        calendarConsumer?.invoke(null)
        dismiss()
    }

    fun setMinDate(minDate: Calendar): ExtDayMonthYearDialogFragment {
        this.minDate = minDate
        return this
    }

    fun setMaxDate(maxDate: Calendar): ExtDayMonthYearDialogFragment {
        this.maxDate = maxDate
        return this
    }

    fun setValueDate(valueDate: Calendar): ExtDayMonthYearDialogFragment {
        this.valueDate = valueDate.clone() as Calendar
        return this
    }

    fun setCalendarConsumer(calendarConsumer: ((calendar: Calendar?) -> Unit)? = null): ExtDayMonthYearDialogFragment {
        this.calendarConsumer = calendarConsumer
        return this
    }

    fun setConditionConsumer(conditionConsumer: ((calendar: Calendar) -> Boolean)? = null): ExtDayMonthYearDialogFragment {
        this.conditionConsumer = conditionConsumer
        return this
    }
}
