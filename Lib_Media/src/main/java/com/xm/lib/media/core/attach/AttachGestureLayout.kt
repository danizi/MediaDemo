package com.xm.lib.media.core.attach

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.attach.factory.MediaAttachLayoutFactory
import com.xm.lib.media.core.constant.Constant

/**
 * 播放器附着手势页面
 */
class AttachGestureLayout(context: Context?, layout: Int) : FrameLayout(context!!), MediaAttachLayoutFactory.IAttachLayout {
    private var gestureDetector: GestureDetector? = null

    init {
        val listener: MediaGestureListener.GestureListener = object : MediaGestureListener.GestureListener() {

            override fun onVolume(slidePercent: Float) {

            }

            override fun onLight(slidePercent: Float) {

            }

            override fun onProgress(slidePercent: Float) {

            }

            override fun onClickListener() {

            }
        }
        gestureDetector = GestureDetector(context, object : MediaGestureListener(null, listener) {})
    }

    override fun setPlayer(player: AbsMediaCore?) {

    }
}

/**
 * 手势监听
 */
open class MediaGestureListener( private val view: ViewGroup?, private var gestureListener: GestureListener?) : GestureDetector.SimpleOnGestureListener() {


    var gestureState:Constant.EnumGestureState? = null
    private val RESTRICT = 10
    private var h: Int? = 0
    private var w: Int? = 1

    override fun onDown(e: MotionEvent?): Boolean {
        gestureState = Constant.EnumGestureState.NONE
        h = view?.measuredHeight
        w = view?.measuredWidth
        log("w ->$w h:$h")
        gestureListener?.onDown()
        return true
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        gestureListener?.onClickListener()
        return super.onSingleTapUp(e)
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        val offsetX = e2?.x!! - e1?.x!!
        val offsetY = e2.y - e1.y
        when (gestureState) {
            Constant.EnumGestureState.NONE -> {
                none(offsetX, offsetY, e1)
            }
            Constant.EnumGestureState.LIGHT -> {
                light(offsetY)
            }
            Constant.EnumGestureState.VOLUME -> {
                volume(offsetY)
            }
            Constant.EnumGestureState.PROGRESS -> {
                progress(offsetX)
            }
        }
        return super.onScroll(e1, e2, distanceX, distanceY)
    }

    private fun none(offsetX: Float?, offsetY: Float?, e1: MotionEvent?) {
        if (Math.abs(offsetX!!) > RESTRICT) {
            gestureState = Constant.EnumGestureState.PROGRESS
            gestureListener?.onGesture(gestureState)
            return
        }

        if (isLight(e1)) {
            gestureState = Constant.EnumGestureState.LIGHT
            gestureListener?.onGesture(gestureState)
            return
        }

        if (isVolume(e1)) {
            gestureState = Constant.EnumGestureState.VOLUME
            gestureListener?.onGesture(gestureState)
            return
        }
    }

    private fun isLight(e1: MotionEvent?): Boolean {
        return e1?.x!! > w!! / 2
    }

    private fun isVolume(e1: MotionEvent?): Boolean {
        return e1?.x!! < w!! / 2
    }

    private fun volume(offsetY: Float?) {
        gestureListener?.onVolume(slidePercent = offsetY!! / h?.toFloat()!!)
    }

    private fun light(offsetY: Float?) {
        gestureListener?.onLight(slidePercent = offsetY!! / h?.toFloat()!!)
    }

    private fun progress(offsetX: Float?) {
        gestureListener?.onProgress(slidePercent = offsetX!! / w?.toFloat()!!)
    }

    private fun log(msg: String) {
        Log.d("MediaGestureListener", msg)
    }

    abstract class GestureListener {
        /**
         * 手势识别触发一次
         */
        open fun onGesture(gestureState:Constant.EnumGestureState?) {}

        /**
         * 触发按下事件
         */
        open fun onDown() {}

        /**
         * 触发释放事件
         */
        open fun onUp(gestureState: Constant.EnumGestureState?) {}

        /**
         * 音量手势监听(控件左半边部分)
         * @param slidePercent 滑动的百分比 = 滑动距离 / 播放器画面展示控件高 , 上滑 < 0
         */
        abstract fun onVolume(slidePercent: Float)

        /**
         * 亮度手势监听(控件右半边部分)
         * @param slidePercent 滑动的百分比 = 滑动距离 / 播放器画面展示控件高 ,上滑 < 0
         */
        abstract fun onLight(slidePercent: Float)

        /**
         * 播放进度监听
         * @param slidePercent 滑动的百分比 = 滑动距离 / 播放器画面展示控件宽,左滑< 0
         */
        abstract fun onProgress(slidePercent: Float)

        /**
         * 点击监听
         */
        abstract fun onClickListener()
    }


}

