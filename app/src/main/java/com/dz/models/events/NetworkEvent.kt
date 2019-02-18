package com.dz.models.events

import com.dz.libraries.rxbus.IEvent
import com.dz.utilities.ErrorCodes

data class NetworkEvent(var errorCode: Int = 0, var message: String = "", var showMessage: Boolean = false) : IEvent {
    override fun toString(): String {
        return "ErrorCode: $errorCode - ${ErrorCodes.getErrorCode(errorCode)}, Message: $message, ShowMessage: $showMessage"
    }
}