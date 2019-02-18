package com.dz.utilities

class ErrorCodes {
    companion object {
        // app
        const val UNKNOWN = 0
        const val NO_INTERNET = 1
        const val AVAILABLE_INTERNET = 5
        const val TIME_OUT = 2
        const val UPDATING = 3
        const val APP_ERROR = 4

        // network
        const val UNAUTHORIZED = 401
        const val FORBIDDEN = 403
        const val NOT_FOUND = 404
        const val SERVER_ERROR = 500
        const val BAD_GATEWAY = 502
        const val MAX_ERROR = 600

        fun getErrorCode(code: Int): String {
            return when (code) {
                UNKNOWN -> "Unknown"
                NO_INTERNET -> "No Internet"
                AVAILABLE_INTERNET -> "Available"
                TIME_OUT -> "Time Out"
                UPDATING -> "Updating"
                APP_ERROR -> "App Error"
                UNAUTHORIZED -> "Unauthorized"
                FORBIDDEN -> "Forbidden"
                NOT_FOUND -> "Not Found"
                SERVER_ERROR -> "Server Error"
                BAD_GATEWAY -> "Bad Gateway"
                else -> "Unknown"
            }
        }
    }
}