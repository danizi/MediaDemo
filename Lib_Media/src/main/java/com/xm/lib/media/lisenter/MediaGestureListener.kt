package com.xm.lib.media.lisenter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import com.xm.lib.media.PolyvScreenUtils
import com.xm.lib.media.enum_.EnumGestureState

class MediaGestureListener(context: Context, gestureListener: GestureListener) : GestureDetector.SimpleOnGestureListener() {

    interface GestureListener {
        fun onVolume(offset: Float)
        fun onLight(offset: Float)
        fun onProgress(offset: Float)
        fun onClickListener()
    }

    val RESTRICT = 10
    var context: Context? = context
    var gestureState: EnumGestureState? = null
    var gestureListener: GestureListener? = gestureListener
    var w = PolyvScreenUtils.getNormalWH(context as Activity)[0]
    var h = PolyvScreenUtils.getNormalWH(context as Activity)[1]

    override fun onDown(e: MotionEvent?): Boolean {
        gestureState = EnumGestureState.NONE
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
            EnumGestureState.NONE -> {
                none(offsetX, offsetY, e1)
            }
            EnumGestureState.LIGHT -> {
                light(offsetY)
            }
            EnumGestureState.VOLUME -> {
                volume(offsetY)
            }
            EnumGestureState.PROGRESS -> {
                progress(offsetX)
            }
        }
        return super.onScroll(e1, e2, distanceX, distanceY)
    }

    private fun none(offsetX: Float?, offsetY: Float?, e1: MotionEvent?) {
        if (Math.abs(offsetX!!) > RESTRICT) {
            log("offsetX:" + offsetX + "gestureState:" + gestureState)
            gestureState = EnumGestureState.PROGRESS
        } else {
            if (e1?.x!! > w / 2) {
                gestureState = EnumGestureState.LIGHT
            } else if (e1.x < w / 2) {
                gestureState = EnumGestureState.VOLUME
            }
            log("offsetY:" + offsetY + "gestureState:" + gestureState)
        }
    }

    private fun volume(offsetY: Float?) {
        gestureListener?.onVolume(offsetY!!)
        log("onVolume offsetY:$offsetY")
    }

    private fun light(offsetY: Float?) {
        gestureListener?.onLight(offsetY!!)
        log("onLight offsetY:$offsetY")
    }

    private fun progress(offsetX: Float?) {
        gestureListener?.onProgress(offsetX!!)
        log("onProgress offsetX:$offsetX")
    }

    private fun log(msg: String) {
        Log.d("MediaGestureListener", msg)
    }
}