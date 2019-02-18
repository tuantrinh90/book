package com.dz.libraries.utilities

import com.dz.libraries.loggers.Logger
import com.google.common.io.BaseEncoding
import java.nio.charset.Charset

class Base64Utility {
    companion object {
        private const val TAG = "Base64Utility"

        /**
         * @param data
         * @return a string is encoded to base64
         */
        fun encode(data: String?): String? {
            try {
                if (StringUtility.isNullOrEmpty(data))return ""

                return BaseEncoding.base64().encode(data!!.toByteArray())
            } catch (ex: Exception) {
                Logger.e(TAG, ex)
            }

            return ""
        }

        /**
         * @param data
         * @return a string is decoded from base64
         */
        fun decode(data: String?): String? {
            try {
                if (StringUtility.isNullOrEmpty(data))return ""

                val contentInBytes = BaseEncoding.base64().decode(data!!)
                return String(contentInBytes,  Charset.forName("UTF-8"))
            } catch (ex: Exception) {
                Logger.e(TAG, ex)
            }

            return ""
        }
    }
}