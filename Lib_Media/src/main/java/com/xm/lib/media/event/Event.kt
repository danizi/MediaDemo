package com.xm.lib.media.event

import com.xm.lib.media.enum_.EnumMediaEventType

/**
 * 事件描述类,携带参数说明
 */
class EventConstant {
    companion object {
        /*=======================================================================================
         * 事件产生来自于哪个类
         */
        val KEY_FROM: String = "KEY_FROM"
        val VALUE_FROM_MEDIACOMPONENT: String = "mediaComponent"
        val VALUE_FROM_PREVIEW: String = "preview"
        //val VALUE_FROM_CONTROLVIEW: String = "control"

        /*=======================================================================================
         * 播放器相关
         */
        val KEY_METHOD: String = "KEY_METHOD"                             //事件来自于于哪个方法
        /*调用播放器方法*/
        val VALUE_METHOD_SEEKTO: String = "seekto"                        //调用了seekto
        /*播放器回调的方法名称*/
        val VALUE_METHOD_CORE: String = "core"                            //播放器创建成功所在方法
        val VALUE_METHOD_SURFACECHANGED: String = "surfaceChanged"        //画布发生改变监听
        val VALUE_METHOD_SURFACEDESTROYED: String = "surfaceDestroyed"    //画布销毁监听
        val VALUE_METHOD_SURFACECREATED: String = "surfaceCreated"        //画布创建监听
        val VALUE_METHOD_ONPREPARED: String = "onPrepared"                //播放就绪监听
        val VALUE_METHOD_ONCOMPLETION: String = "onCompletion"            //播放完成监听
        val VALUE_METHOD_ONBUFFERINGUPDATE: String = "onBufferingUpdate"  //缓冲监听
        val VALUE_METHOD_ONSEEKCOMPLETE: String = "onSeekComplete"        //设置播放位置完成
        val VALUE_METHOD_ONVIDEOSIZECHANGED: String = "onVideoSizeChanged"//播放器组件发送改变
        val VALUE_METHOD_ONERROR: String = "onError"                      //播放错误
        val VALUE_METHOD_ONINFO: String = "onInfo"                        //播放信息
        val VALUE_METHOD_ONTIMEDTEXT: String = "onTimedText"              //定时显示文字
        /*播放容器当前的模式*/
        val KEY_SCREEN_MODE: String = "KEY_SCREEN_MODE"
        val VALUE_SCREEN_SMALL: String = "small"                          //小窗口
        val VALUE_SCREEN_FULL: String = "full"                            //全屏


        /*=======================================================================================
         * view相关方法
         */
        val VALUE_METHOD_ONTOUCHEVENT: String = "onTouchEvent"                      //点击
        val VALUE_METHOD_UP: String = "up"                                          //用户手指释放
        val VALUE_METHOD_CLICK: String = "CLICK"                                    //用户手指释放
        val VALUE_METHOD_ONVOLUME: String = "onVolume"                              //音量
        val VALUE_METHOD_ONLIGHT: String = "onLight"                                //亮度
        val VALUE_METHOD_ONPROGRESS: String = "onProgress"                          //进度
//        val VALUE_METHOD_ONPROGRESSCHANGED: String = "onProgressChanged"            //seekbar停止滑动
//        val VALUE_METHOD_ONSTARTTRACKINGTOUCH: String = "onStartTrackingTouch"      //seekbar停止滑动
//        val VALUE_METHOD_ONSTOPTRACKINGTOUCH: String = "onStoptrackingtouch"        //seekbar停止滑动

        /*=======================================================================================
         * 其他
         */
        val KEY_GESTURE_STATE: String = "gestureState"                              //手势
    }
}

/**
 * 事件描述类
 */
class Event {
    /**
     * 事件类型
     */
    var eventType: EnumMediaEventType? = null
    /**
     * 携带信息,具体请查看EventConstant类了解
     */
    var parameter: HashMap<String, Any>? = HashMap()

    fun setEventType(eventType: EnumMediaEventType?): Event {
        this.eventType = eventType
        return this
    }

    fun setParameter(key: String, value: Any): Event {
        this.parameter?.put(key, value)
        return this
    }
}