package com.xm.lib.media.core

/**
 * 播放器添加View的类型
 */
enum class EnumViewType {
    CONTROLLER,      //控制
    PREVIEW,         //预览
    LOADING,         //缓冲
    COMPLE,          //播放完成
    ERROR,           //错误
    GESTURE_LIGHT,   //手势亮度
    GESTURE_VOLUME,  //手势音量
    GESTURE_PROGRESS //手势滑动进度
}