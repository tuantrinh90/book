package com.dz.libraries.utilities

import android.annotation.SuppressLint
import com.dz.libraries.loggers.Logger
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtility {
    companion object {
        private const val TAG = "DateTimeUtility"

        /**
         * @param date
         * @param pattern
         * @return
         */
        @SuppressLint("SimpleDateFormat")
        fun convertStringToCalendar(date: String?, pattern: String): Calendar? {
            try {
                if (StringUtility.isNullOrEmpty(date)) return null

                val simpleDateFormat = SimpleDateFormat(pattern)
                val calendar = Calendar.getInstance()
                calendar.time = simpleDateFormat.parse(date)
                return calendar
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return null
        }

        /**
         * convert calendar to string
         *
         * @param calendar
         * @param pattern
         * @return string has formatted with pattern
         */
        fun convertCalendarToString(calendar: Calendar?, pattern: String): String? = convertDateToString(calendar?.time, pattern)

        /**
         * @param date
         * @param pattern
         * @return a string has converted from date with pattern
         */
        fun convertDateToString(date: Date?, pattern: String): String? {
            try {
                if (OptionalUtility.isNullOrEmpty(date)) return ""

                @SuppressLint("SimpleDateFormat")
                val simpleDateFormat = SimpleDateFormat(pattern)
                return simpleDateFormat.format(date)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return ""
        }

        /**
         * @param year
         * @return true if yearn is leap
         */
        fun isLeapYear(year: Int): Boolean {
            try {
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return false
        }

        fun formatTime(timestamp: String, formatType: String): String {
            if (StringUtility.isNullOrEmpty(timestamp)) return ""

            val formatDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            formatDateTime.timeZone = TimeZone.getTimeZone("GMT")

            val formatReturn = SimpleDateFormat(formatType, Locale.getDefault())
            try {
                val newDate = formatDateTime.parse(timestamp)
                return formatReturn.format(newDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return ""
        }
    }
}