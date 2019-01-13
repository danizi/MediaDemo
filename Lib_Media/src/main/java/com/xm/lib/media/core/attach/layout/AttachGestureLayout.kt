package com.xm.lib.media.core.attach.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.xm.lib.media.R
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.AbsAttachLayout
import com.xm.lib.media.core.constant.Constant
import com.xm.lib.media.core.utils.Util

/**
 * 播放器附着手势页面
 */
class AttachGestureLayout : AbsAttachLayout {
    private var player: AbsMediaCore? = null
    private var gestureDetectorView: View? = null
    private var progressBar: ProgressBar? = null
    private var iv: ImageView? = null
    private var curPercent: Float? = null

    constructor(context: Context, layout: Int) : super(context) {
        gestureDetectorView = Util.getView(context, layout)
        progressBar = gestureDetectorView?.findViewById(R.id.progress)
        iv = gestureDetectorView?.findViewById(R.id.iv)

        gestureDetectorView?.visibility = View.GONE
        progressBar?.max = 100
        addView(gestureDetectorView)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onPrepared(mp: AbsMediaCore) {
        super.onPrepared(mp)
        player = mp
    }

    override fun onGesture(gestureState: Constant.EnumGestureState?, curPercent: Float?, player: AbsMediaCore?) {
        super.onGesture(gestureState, curPercent, player)
        this.curPercent = curPercent
    }

    override fun onDown() {
        super.onDown()
    }

    override fun onUp(gestureState: Constant.EnumGestureState?) {
        super.onUp(gestureState)
        Util.hide(gestureDetectorView)
        if (gestureState == Constant.EnumGestureState.PROGRESS) {
            player?.seekTo(((progressBar?.progress!! / 100f) * player?.getDuration()!!).toLong())
        }
    }

    override fun onVolume(slidePercent: Float) {
        super.onVolume(slidePercent)
        Util.showLog("onVolume$slidePercent")
        val percent = curPercent!! - slidePercent
        common(percent, R.mipmap.media_volume)
        Util.setVolume(context, percent)
    }

    override fun onLight(slidePercent: Float) {
        super.onLight(slidePercent)
        Util.showLog("onLight$slidePercent")
        val percent = curPercent!! - slidePercent
        common(percent, R.mipmap.media_light)
        Util.setSystemBrightness(context, percent)
    }

    override fun onProgress(slidePercent: Float) {
        super.onProgress(slidePercent)
        Util.showLog("onProgress$slidePercent")
        val percent = curPercent!! + slidePercent
        common(percent, R.mipmap.media_light)
        //用户触发up事件时调用设置播放进度操作
    }

    private fun common(percent: Float, resID: Int) {
        iv?.setImageResource(resID)
        Util.showLog("percent:$percent curProcess$curPercent")
        progressBar?.progress = (percent * 100).toInt()
        Util.show(gestureDetectorView)
    }
}

