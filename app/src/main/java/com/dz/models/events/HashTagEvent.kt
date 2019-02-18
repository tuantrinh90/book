package com.dz.models.events

import com.dz.libraries.rxbus.IEvent
import com.dz.models.responses.HashTagResponse

data class HashTagEvent(var items: ArrayList<HashTagResponse?>?) : IEvent