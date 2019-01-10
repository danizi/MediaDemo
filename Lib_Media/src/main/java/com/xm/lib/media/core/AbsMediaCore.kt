package com.xm.lib.media.core

import android.content.Context
import android.view.SurfaceHolder
import com.xm.lib.media.core.constant.Constant
import com.xm.lib.media.core.listener.MediaListener

/**
 * 播放器引擎抽象接口
 */
abstract class AbsMediaCore {
    var context: Context? = null
    var mediaLifeState: Constant.MediaLifeState? = Constant.MediaLifeState.Idle

    init {
        init()
    }

    open fun init() {
        //初始化
    }

    open fun start() {
        //播放
//        if (canStart()) {
//            mediaLifeState = Constant.MediaLifeState.Started
//        }
    }

    open fun isPlaying(): Boolean {
        return mediaLifeState == Constant.MediaLifeState.Started
    }

    open fun canStart(): Boolean {
        return (mediaLifeState == Constant.MediaLifeState.Prepared || mediaLifeState == Constant.MediaLifeState.Paused)
    }

    open fun canPause(): Boolean {
        return mediaLifeState != Constant.MediaLifeState.Stop && mediaLifeState != Constant.MediaLifeState.Idle
    }

    open fun canStop(): Boolean {
        return (mediaLifeState == Constant.MediaLifeState.Started || mediaLifeState == Constant.MediaLifeState.Paused)
    }

    open fun canSeekTo(msec: Long?, duration: Long): Boolean {
        return (msec!! > 0 && msec!! < duration) && canSeekTo()
    }

    open fun canSeekTo(): Boolean {
        return canStart() || canPause()
    }

    open fun prepareAsync() {
        //异步准备播放资源
    }

    open fun pause() {
        //暂停播放
//        if (canPause()) {
//            mediaLifeState = Constant.MediaLifeState.Paused
//        }
    }

    open fun stop() {
        //停止播放
//        if (canStop()) {
//            mediaLifeState = Constant.MediaLifeState.Stop
//        }
    }

    open fun getDuration(): Long? {
        //获取当前视频时长
        return 0
    }

    open fun getCurrentPosition(): Long? {
        //获取当前播放时间
        return 0
    }

    open fun seekTo(msec: Long?) {
        //设置播放进度
    }

    open fun release() {
        //释放资源
//        mediaLifeState = Constant.MediaLifeState.End
    }

    open fun reset() {
        //重置状态
//        mediaLifeState = Constant.MediaLifeState.Idle
    }

    open fun getVideoHeight(): Int? {
        //视频控件的长度
        return 0
    }

    open fun getVideoWidth(): Int? {
        //视频控件的宽度
        return 0
    }

    open fun setAudioStreamType() {
        //设置类型
    }

    open fun setVolume() {
        //设置音量&声道
    }

    open fun setSpeed(speed: Float) {
        //设置播放速度
    }

    open fun getSpeed(): Float? {
        //获取播放速度
        return 0f
    }

    open fun setDisplay(sh: SurfaceHolder?) {
        //设置SurfaceHolder显示多媒体
    }

    open fun setDataSource(path: String) {
        //设置播放资源
//        mediaLifeState = Constant.MediaLifeState.Initialized
    }

    open fun getTcpSpeed(): Long? {
        //获取当前网速
        return 0
    }

    open fun setOnPreparedListener(listener: MediaListener.OnPreparedListener) {
        //资源准备完成监听
//        mediaLifeState = Constant.MediaLifeState.Prepared
    }

    open fun setOnCompletionListener(listener: MediaListener.OnCompletionListener) {
        //播放完成监听
//        mediaLifeState = Constant.MediaLifeState.PlaybackCompleted
    }

    open fun setOnBufferingUpdateListener(listener: MediaListener.OnBufferingUpdateListener) {
        //资源更新进度监听
    }

    open fun setOnSeekCompleteListener(listener: MediaListener.OnSeekCompleteListener) {
        //设置进度完成监听
    }

    open fun setOnVideoSizeChangedListener(listener: MediaListener.OnVideoSizeChangedListener) {
        //播放器窗口变化监听
    }

    open fun setOnErrorListener(listener: MediaListener.OnErrorListener) {
        //播放错误监听
//        mediaLifeState = Constant.MediaLifeState.Error
    }

    open fun setOnInfoListener(listener: MediaListener.OnInfoListener) {
        //播放信息
    }

    open fun setOnTimedTextListener(listener: MediaListener.OnTimedTextListener) {
        //定时显示文件监听
    }
}

