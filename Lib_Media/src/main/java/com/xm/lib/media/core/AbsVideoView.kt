package com.xm.lib.media.core

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.xm.lib.media.core.constant.Constant
import com.xm.lib.media.core.listener.MediaListener

abstract class AbsVideoView : FrameLayout, IActivityLife {

    protected var attachLayouts: HashMap<Constant.EnumMediaView, AbsAttachLayout>? = HashMap()
    protected var mediaHelp: MediaHelp? = null
    var auto: Boolean? = false
    protected var path: String? = null
    protected var preUrl: String? = null
    protected var width: Int? = 0
    protected var height: Int? = 0


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun addAttachLayouts() {
        for (view in attachLayouts?.entries!!) {
            view.value.videoView = this
            addView(view.value)
        }
    }

    open fun prepareAsync() {

    }

    open fun reStart() {

    }

    open fun choiceScreenMode() {
        mediaHelp?.choiceScreenMode()
    }

    override fun onRestart() {
        for (attach in attachLayouts?.entries!!) {
            attach.value.onRestart()
        }
    }

    override fun onStart() {
        for (attach in attachLayouts?.entries!!) {
            attach.value.onStart()
        }
    }

    override fun onStop() {
        for (attach in attachLayouts?.entries!!) {
            attach.value.onStop()
        }
    }

    override fun onPause() {
        for (attach in attachLayouts?.entries!!) {
            attach.value.onPause()
        }
    }

    override fun onResume() {
        for (attach in attachLayouts?.entries!!) {
            attach.value.onResume()
        }
    }

    override fun onCreate() {
        for (attach in attachLayouts?.entries!!) {
            attach.value.onCreate()
        }
    }

    override fun onDestroy() {
        for (attach in attachLayouts?.entries!!) {
            attach.value.onDestroy()
        }
    }

    @Deprecated("去掉了哟")
    abstract class IVideoView {
        open fun isPlaying(): Boolean? {
            //是否正在播放
            return false
        }

        open fun canPause(): Boolean? {
            //是否暂停
            return false
        }

        open fun canSeekBackward(): Boolean? {
            //进度能够后退
            return false
        }

        open fun canSeekForward(): Boolean? {
            //进度能否前进
            return false
        }

        open fun getCurrentPosition(): Long? {
            //总时长
            return 0
        }

        open fun pause() {
            //暂停
        }

        open fun resume() {
            //恢复播放
        }

        open fun seekTo(msec: Int) {
            //设置进度
        }

        open fun start() {
            //播放
        }

        open fun realse() {
            //释放资源
        }

        open fun setVideoPath(path: String?) {
            //设置播放资源
        }

        open fun setAttachLayout(type: Constant.EnumMediaView?, attachLayout: AbsAttachLayout?) {
            //添加控制器
        }

        open fun setOnCompletionListener(l: MediaListener.OnCompletionListener) {
            //播放完成监听
        }

        open fun setOnErrorListener(l: MediaListener.OnErrorListener) {
            //播放错误监听
        }

        open fun setOnInfoListener(l: MediaListener.OnInfoListener) {
            //播放信息监听
        }

        open fun setOnPreparedListener(l: MediaListener.OnPreparedListener) {
            //播放资源完成监听
        }
    }
}

/**
 * activity的生命周期
 */
interface IActivityLife {
    fun onStart()
    fun onStop()
    fun onPause()
    fun onResume()
    fun onCreate()
    fun onDestroy()
    fun onRestart()
}