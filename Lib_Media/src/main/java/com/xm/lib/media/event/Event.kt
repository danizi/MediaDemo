package com.xm.lib.media.event

import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.enum_.EnumViewType

class EventConstant {
    companion object {
        val KEY_FROM: String = "KEY_FROM"                        //事件产生来自于哪里
        val VALUE_FROM_MEDIACOMPONENT: String = "mediaComponent"

        // 播放器回调的方法名称
        val KEY_METHOD: String = "KEY_METHOD"
        val VALUE_METHOD_CORE: String = "core"                   //播放器创建成功所在方法
        val VALUE_METHOD_ONERROR: String = "onError"             //播放错误
        val VALUE_METHOD_ONPREPARED: String = "onPrepared"       //播放就绪
        val VALUE_METHOD_ONCOMPLETION: String = "onCompletion"   //播放完成
        val VALUE_METHOD_ONTOUCHEVENT: String = "onTouchEvent"   //点击
        val VALUE_METHOD_UP: String = "up"                       //用户手指释放
        val VALUE_METHOD_CLICK: String = "CLICK"                 //用户手指释放
        val VALUE_METHOD_ONVOLUME: String = "onVolume"           //音量
        val VALUE_METHOD_ONLIGHT: String = "onLight"             //亮度
        val VALUE_METHOD_ONPROGRESS: String = "onProgress"       //进度


        // 播放容器当前的模式
        val KEY_SCREEN_MODE: String = "KEY_SCREEN_MODE"
        val VALUE_SCREEN_SMALL: String = "small"                 //小窗口
        val VALUE_SCREEN_FULL: String = "full"                   //全屏
    }
}

/**
 * 互相传递的消息
 */
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