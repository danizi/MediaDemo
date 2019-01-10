package com.xm.lib.media.core.constant

/**
 * 存放一些常量和枚举类
 */
class Constant {

    /**
     * 附着在播放上的View的类型
     */
    enum class EnumMediaView {
        PRE,
        CONTROLLER,
        LOADING,
        GESTURE,
        COMPLETE,
        ERROR,
        TIP
    }

    /**
     * 播放器的生命状态
     */
    enum class MediaLifeState {
        Idle,             //reset 空闲状态
        Initialized,      //setDataSource 播放文件设置好了
        Prepared,         //prepareAsync方法触发OnPreparedListener.onPrepared监听进入资源准备状态
        Started,          //start 播放状态如果调用了seekTo还是处于播放
        Paused,           //pause 暂停状态,调用start方可恢复播放
        Stop,             //Started或者Paused状态下均可调用stop(),要想重新播放，需要通过prepareAsync()和prepare()回到先前的Prepared状态重新开始才可以
        PlaybackCompleted,//播放完毕，而又没有设置循环播放的话就进入该状态,播放完成触发onCompletion , 此时可以调用start重新从头播放，stop()停止MediaPlayer，也可以seekTo()来重新定位播放位置
        Error,            //播放器播放错误
        End               //release 资源释放状态
    }

    /**
     * 手势状态
     */
    enum class EnumGestureState {
        NONE,     //无状态
        VOLUME,   //滑动音量状态
        LIGHT,    //滑动亮度状态
        PROGRESS  //滑动进度状态
    }

    /**
     * 播放方式
     */
    enum class EnumPlayWay {
        LIST_LOOP,  //列表循环
        SIMPLE_LOOP,//单集循环
        PAUSE       //播放完成暂停
    }
}
