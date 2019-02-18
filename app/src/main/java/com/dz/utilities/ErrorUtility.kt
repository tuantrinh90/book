package com.dz.utilities

import android.content.Context
import com.dz.libraries.rxbus.IEvent
import com.dz.libraries.rxbus.RxBus
import com.dz.libraries.utilities.StringUtility
import com.dz.models.ErrorBody
import com.dz.models.events.NetworkEvent
import com.dz.ui.R
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

class ErrorUtility {
    companion object {
        private const val TAG = "ErrorUtility"

        /**
         * @param bus
         * @param errorBody from restful service
         * @return error message from http code as no internet, timeout, forbidden...
         */
        fun getBaseErrorText(context: Context, bus: RxBus<IEvent>, errorBody: ErrorBody): String? {
            val stringRes: String?

            when (errorBody.status) {
                in ErrorCodes.SERVER_ERROR..ErrorCodes.MAX_ERROR -> {
                    bus.send(NetworkEvent(ErrorCodes.SERVER_ERROR))
                    stringRes = context.getString(R.string.some_thing_wrong)
                }
                ErrorCodes.NO_INTERNET, ErrorCodes.BAD_GATEWAY -> {
                    bus.send(NetworkEvent(ErrorCodes.NO_INTERNET))
                    stringRes = context.getString(R.string.no_internet)
                }
                ErrorCodes.TIME_OUT -> {
                    bus.send(NetworkEvent(ErrorCodes.TIME_OUT))
                    stringRes = context.getString(R.string.server_not_responding)
                }
                ErrorCodes.FORBIDDEN -> {
                    bus.send(NetworkEvent(ErrorCodes.FORBIDDEN))
                    stringRes = context.getString(R.string.forbidden)
                }
                ErrorCodes.APP_ERROR -> {
                    bus.send(NetworkEvent(ErrorCodes.APP_ERROR))
                    val messageId = context.resources.getIdentifier(errorBody.message, "string", context.packageName)
                    stringRes = if (messageId > 0) {
                        context.getString(messageId)
                    } else {
                        errorBody.message
                    }
                }
                ErrorCodes.UNAUTHORIZED -> {
                    bus.send(NetworkEvent(ErrorCodes.UNAUTHORIZED))
                    stringRes = context.getString(R.string.unauthorized)
                }
                ErrorCodes.NOT_FOUND -> {
                    bus.send(NetworkEvent(ErrorCodes.NOT_FOUND))
                    stringRes = context.getString(R.string.server_not_found)
                }
                else -> {
                    bus.send(NetworkEvent(ErrorCodes.UNKNOWN))
                    stringRes = context.getString(R.string.unknown_error)
                }
            }

            return stringRes
        }

        /**
         * @param e
         * @return error message include code, message from HttpException
         */
        fun createErrorBody(e: Throwable): ErrorBody {
            with(ErrorBody()) {
                when (e) {
                    is HttpException -> {
                        status = e.code()
                        message = e.message()
                        StringUtility.with(e.response().errorBody()?.string()).doIfPresent { message = it }
                    }
                    is IOException -> {
                        status = if (e is UnknownHostException) ErrorCodes.NO_INTERNET else ErrorCodes.TIME_OUT
                        message = e.message
                    }
                    else -> status = ErrorCodes.UNKNOWN
                }

                return this
            }
        }

        /**
         * @param messageCode
         * @return error message which is defined from the application
         */
        fun getErrorBodyApp(messageCode: String?): ErrorBody = ErrorBody(ErrorCodes.APP_ERROR, messageCode)
    }
}