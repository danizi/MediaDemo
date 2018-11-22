package com.xm.lib.media.enum_

/**
 * 事件类型
 */
enum class EnumMediaEventType {
    MEDIA,// 播放器事件 ：各种播放的监听事件
    VIEW, // 触控事件(手势,点击,长按)
    OTHER // 外部事件   ：网络变化,横竖切屏
}