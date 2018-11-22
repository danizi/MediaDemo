package com.xm.lib.media.event

import com.xm.lib.media.enum_.EnumMediaEventType

class EventConstant {
    companion object {
        val KEY_METHOD: String = "key_method"
        val VALUE_METHOD_ONERROR: String = "onError"
        val VALUE_METHOD_ONPREPARED: String = "onPrepared"
        val VALUE_METHOD_ONCOMPLETION: String = "onCompletion"
        val VALUE_METHOD_ONTOUCHEVENT: String = "onTouchEvent"

        val KEY_SCREEN_MODE: String = "key_method"
        val VALUE_SCREEN_SMALL: String = "small"
        val VALUE_SCREEN_FULL: String = "full"
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