package com.dz.models.responses

import com.dz.libraries.models.IModel
import com.dz.libraries.utilities.DateTimeUtility
import com.dz.utilities.Constant
import com.google.gson.annotations.SerializedName
import java.util.*

data class ContestResponse(@SerializedName("numberOfParticipants")
                           var numberOfParticipants: Int? = 0,
                           @SerializedName("endDate")
                           var endDate: String? = "",
                           @SerializedName("filePath")
                           var filePath: String? = "",
                           @SerializedName("description")
                           var description: String? = "",
                           @SerializedName("created_at")
                           var createdAt: String? = "",
                           @SerializedName("type")
                           var type: Int? = 0,
                           @SerializedName("deleted_at")
                           var deletedAt: String? = null,
                           @SerializedName("updated_at")
                           var updatedAt: String? = "",
                           @SerializedName("name")
                           var name: String? = "",
                           @SerializedName("id")
                           var id: Int? = 0,
                           @SerializedName("fileType")
                           var fileType: Int? = 0,
                           @SerializedName("startDate")
                           var startDate: String? = "",
                           @SerializedName("status")
                           var status: Int? = 0) : IModel {
    val startDateCalendar: Calendar?
        get() = DateTimeUtility.convertStringToCalendar(startDate, Constant.FORMAT_DATE_SERVER_RETURN)

    val startDateText: String?
        get() = DateTimeUtility.convertCalendarToString(startDateCalendar, Constant.FORMAT_YEAR_MONTH_DAY)

    val endDateCalendar: Calendar?
        get() = DateTimeUtility.convertStringToCalendar(endDate, Constant.FORMAT_DATE_SERVER_RETURN)

    val endDateText: String?
        get() = DateTimeUtility.convertCalendarToString(endDateCalendar, Constant.FORMAT_YEAR_MONTH_DAY)
}