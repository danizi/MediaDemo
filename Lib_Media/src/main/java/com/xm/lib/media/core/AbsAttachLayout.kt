package com.xm.lib.media.core

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.AbsVideoView
import com.xm.lib.media.core.MediaHelp
import com.xm.lib.media.core.constant.Constant

/**
 * 控制器
 */
open class AbsAttachLayout : FrameLayout, MediaHelp.HelpGestureListener, MediaHelp.HelpMediaListener, IActivityLife {
    var videoView: AbsVideoView? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /*=========================
     * activity生命周期回调
     */
    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onPause() {
    }

    override fun onResume() {
    }

    override fun onCreate() {
    }

    override fun onDestroy() {
    }

    override fun onRestart() {
    }

    /*=========================
     * 手势判断回调
     */
    override fun onDown() {
    }

    override fun onUp(gestureState: Constant.EnumGestureState?) {
    }

    override fun onVolume(slidePercent: Float) {
    }

    override fun onLight(slidePercent: Float) {
    }

    override fun onProgress(slidePercent: Float) {
    }

    override fun onClickListener() {
    }

    override fun onGesture(gestureState: Constant.EnumGestureState?, curPercent: Float?, player: AbsMediaCore?) {
    }

    /*=========================
     * 播放器回调
     */
    override fun onPrepared(mp: AbsMediaCore) {
    }

    override fun onError(mp: AbsMediaCore, what: Int?, extra: Int?): Boolean {
        return false
    }

    override fun onBufferingUpdate(mp: AbsMediaCore, percent: Int) {
    }

    override fun onCompletion(mp: AbsMediaCore) {
    }

    override fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
        return false
    }

    override fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int) {
    }

    override fun onTimedText(mp: AbsMediaCore, text: String) {
    }
}