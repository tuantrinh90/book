package com.dz.libraries.utilities

import com.dz.libraries.loggers.Logger
import java.util.regex.Pattern

class EmailUtility {
    companion object {
        private const val TAG = "EmailUtility"

        // const
        const val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

        /**
         * Validate hex with regular expression
         *
         * @return true valid hex, false invalid email
         */
        fun isValidate(email: String?): Boolean {
            try {
                if (StringUtility.isNullOrEmpty(email)) return false

                val pattern = Pattern.compile(EMAIL_PATTERN)
                val matcher = pattern.matcher(email)
                return matcher.matches()
            } catch (ex: Exception) {
                Logger.e(TAG, ex)
            }

            return false
        }

        /**
         * @param email
         * @param emailPattern "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
         * "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
         * @return true valid hex, false invalid email
         */
        fun isValidate(email: String?, emailPattern: String): Boolean {
            try {
                if (StringUtility.isNullOrEmpty(email)) return false

                val pattern = Pattern.compile(emailPattern)
                val matcher = pattern.matcher(email)
                return matcher.matches()
            } catch (ex: Exception) {
                Logger.e(TAG, ex)
            }

            return false
        }
    }
}