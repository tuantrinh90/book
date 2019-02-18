package com.dz.models.events

import com.dz.libraries.rxbus.IEvent
import com.dz.models.responses.MemberResponse

data class AddFriendEvent(var items: ArrayList<MemberResponse?>?) : IEvent