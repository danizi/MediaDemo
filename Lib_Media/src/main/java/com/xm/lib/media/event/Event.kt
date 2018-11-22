package com.xm.lib.media.event

import com.xm.lib.media.enum_.EnumMediaEventType

class EventConstant {
    companion object {
        val METHOD: String = "METHOD"
    }
}

class Event {
    var eventType: EnumMediaEventType? = null
    var parameter: HashMap<String, Any>? = null

    constructor() {
        parameter = HashMap()
    }

    fun setEventType(eventType: EnumMediaEventType?): Event {
        this.eventType = eventType
        return this
    }

    fun setParameter(key: String, value: Any): Event {
        this.parameter?.put(key, value)
        return this
    }
}