package com.dz.libraries.utilities

class StringUtility private constructor(v: String?) {
    companion object {
        private const val TAG = "StringUtility"

        fun with(value: String?): StringUtility = StringUtility(value)

        fun isNullOrEmpty(value: String?) = value == null || value.isNullOrEmpty() || value.isNullOrBlank()
    }

    val value: String? = v

    fun doIfEmpty(consumer: () -> Unit): StringUtility {
        if (isNullOrEmpty(value)) {
            consumer()
        }

        return this
    }

    fun doIfPresent(consumer: (v: String) -> Unit): StringUtility {
        if (!isNullOrEmpty(value)) {
            consumer(value!!)
        }

        return this
    }

    fun capitalize(): String? {
        if (isNullOrEmpty(value)) return ""

        val stringBuilder = StringBuilder()

        val arr: CharArray = value!!.toCharArray()
        var capitalizeNext = true

        for (v: Char in arr) {
            if (capitalizeNext && Character.isLetter(v)) {
                stringBuilder.append(Character.toUpperCase(v))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(v)) {
                capitalizeNext = true
            }

            stringBuilder.append(v)
        }

        return stringBuilder.toString()
    }
}