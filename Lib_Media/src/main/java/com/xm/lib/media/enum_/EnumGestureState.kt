package com.xm.lib.media.enum_

/**
 * 手势滑动状态,区分多重状态,用户滑动的过程只能出现一种视图
 */
enum class EnumGestureState {
    NONE,     //无状态
    VOLUME,   //滑动音量状态
    LIGHT,    //滑动亮度状态
    PROGRESS  //滑动进度状态
}