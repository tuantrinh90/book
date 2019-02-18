package com.dz.libraries.utilities

import com.dz.libraries.loggers.Logger
import java.text.DecimalFormat

class NumberUtility {
    companion object {
        private const val TAG = "NumberUtility"

        fun formatNumber(value: Int?, pattern: String): String? {
            try {
                if (OptionalUtility.isNullOrEmpty(value)) return ""

                val decimalFormat = DecimalFormat(pattern)
                return decimalFormat.format(value)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return ""
        }

        fun formatNumber(value: Long?, pattern: String): String? {
            try {
                if (OptionalUtility.isNullOrEmpty(value)) return ""

                val decimalFormat = DecimalFormat(pattern)
                return decimalFormat.format(value)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return ""
        }

        fun formatNumber(value: Float?, pattern: String): String? {
            try {
                if (OptionalUtility.isNullOrEmpty(value)) return ""

                val decimalFormat = DecimalFormat(pattern)
                return decimalFormat.format(value)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return ""
        }

        fun formatNumber(value: Double?, pattern: String): String? {
            try {
                if (OptionalUtility.isNullOrEmpty(value)) return ""

                val decimalFormat = DecimalFormat(pattern)
                return decimalFormat.format(value)
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return ""
        }

        fun convertStringToInt(value: String?): Int {
            try {
                if (OptionalUtility.isNullOrEmpty(value)) return 0

                return value!!.toInt()
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return 0
        }

        fun convertStringToLong(value: String?): Long {
            try {
                if (OptionalUtility.isNullOrEmpty(value)) return 0

                return value!!.toLong()
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return 0
        }

        fun convertStringToFloat(value: String?): Float {
            try {
                if (OptionalUtility.isNullOrEmpty(value)) return 0f

                return value!!.toFloat()
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return 0f
        }

        fun convertStringToDouble(value: String?): Double {
            try {
                if (OptionalUtility.isNullOrEmpty(value)) return 0.0

                return value!!.toDouble()
            } catch (e: Exception) {
                Logger.e(TAG, e)
            }

            return 0.0
        }
    }
}