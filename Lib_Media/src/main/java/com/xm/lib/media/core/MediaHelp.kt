package com.xm.lib.media.core

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v7.widget.AppCompatSeekBar
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.SeekBar
import com.xm.lib.media.R
import com.xm.lib.media.core.constant.Constant
import com.xm.lib.media.core.listener.MediaGestureListener
import com.xm.lib.media.core.listener.MediaListener
import com.xm.lib.media.core.utils.Util
import com.xm.lib.media.core.utils.Util.Companion.hide

/**
 * 播放器快速创建帮助类
 */
abstract class MediaHelp(val builder: Builder) : IMedia {
    var player: AbsMediaCore? = builder.player
    var surfaceView: SurfaceView? = null
    private var surfaceHolderCallback: SurfaceHolder.Callback? = null

    @Deprecated("播放实例,直接由外部传入 setupPlayer(...)")
    abstract fun createPlayer(): AbsMediaCore

    @SuppressLint("InflateParams")
    override fun setupPlayer(p: AbsMediaCore?) {
        //移除所有展示容器中的SurfaceView
        builder.mediaContain?.removeAllViews()
        //创建SurfaceView并赋值
        surfaceView = createSurfaceView(builder.context, builder.width!!, builder.height!!)
        //往画面展示容器中添加SurfaceView
        builder.mediaContain?.addView(surfaceView)
        //添加画布监听
        //surfaceViewAddCallback(surfaceView)

        //让播放器处于ide状态
        if (null != player) {
            player?.reset()
            player?.release()
            player = null
        }
        //重新创建播放器
        player = p
        builder.player = p

        //添加画布监听
        surfaceViewAddCallback(surfaceView)
        //添加播放器监听
        addMediaLisenter()
        //添加手势监听
        addGestureDetectorListener()
    }

    override fun setDisplaySize(width: Int?, height: Int?) {
        Util.setDisplaySize(builder.mediaContain, width!!, height!!)
        Util.setDisplaySize(surfaceView, width, height)
    }

    override fun addMediaLisenter() {
        player?.setOnPreparedListener(object : MediaListener.OnPreparedListener {
            override fun onPrepared(mp: AbsMediaCore) {
                builder.mediaListener?.onPrepared(mp)
            }
        })
        player?.setOnErrorListener(object : MediaListener.OnErrorListener {
            override fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                return builder.mediaListener?.onError(mp, what, extra)!!
            }
        })
        player?.setOnBufferingUpdateListener(object : MediaListener.OnBufferingUpdateListener {
            override fun onBufferingUpdate(mp: AbsMediaCore, percent: Int) {
                builder.mediaListener?.onBufferingUpdate(mp, percent)
            }
        })
        player?.setOnCompletionListener(object : MediaListener.OnCompletionListener {
            override fun onCompletion(mp: AbsMediaCore) {
                builder.mediaListener?.onCompletion(mp)
            }
        })
        player?.setOnInfoListener(object : MediaListener.OnInfoListener {
            override fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                return builder.mediaListener?.onInfo(mp, what, extra)!!
            }
        })
        player?.setOnVideoSizeChangedListener(object : MediaListener.OnVideoSizeChangedListener {
            override fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int) {
                builder.mediaListener?.onVideoSizeChanged(mp, width, height, sar_num, sar_den)
            }
        })
        player?.setOnTimedTextListener(object : MediaListener.OnTimedTextListener {
            override fun onTimedText(mp: AbsMediaCore, text: String) {
                builder.mediaListener?.onTimedText(mp, text)
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun addGestureDetectorListener() {
        val listener: MediaGestureListener.GestureListener = object : MediaGestureListener.GestureListener() {

            var curPercent: Float? = 0f

            override fun onDown() {
                super.onDown()
                builder.gestureListener?.onDown()
            }

            override fun onGesture(gestureState: Constant.EnumGestureState?) {
                super.onGesture(gestureState)
                curPercent = when (gestureState) {
                    Constant.EnumGestureState.PROGRESS -> {
                        player?.getCurrentPosition()!! / player?.getDuration()?.toFloat()!!
                    }
                    Constant.EnumGestureState.LIGHT -> {
                        Util.getScreenBrightness(builder.context)
                    }
                    Constant.EnumGestureState.VOLUME -> {
                        Util.getVolume(builder.context)
                    }
                    else -> {
                        0f
                    }
                }
                builder.gestureListener?.onGesture(gestureState, curPercent, player)
            }

            override fun onUp(gestureState: Constant.EnumGestureState?) {
                builder.gestureListener?.onUp(gestureState)
            }

            override fun onVolume(slidePercent: Float) {
                builder.gestureListener?.onVolume(slidePercent)
            }

            override fun onLight(slidePercent: Float) {
                builder.gestureListener?.onLight(slidePercent)
            }

            override fun onProgress(slidePercent: Float) {
                builder.gestureListener?.onProgress(slidePercent)
            }

            override fun onClickListener() {
                builder.gestureListener?.onClickListener()
            }
        }
        val gesture = MediaGestureListener(builder.mediaContain, listener)
        val gestureDetector = GestureDetector(builder.context, gesture)
        builder.mediaContain?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                listener.onUp(gesture.gestureState)
            }
            gestureDetector.onTouchEvent(event)
        }
    }

    override fun choiceScreenMode() {
        if (Util.isPortrait(builder.context!!)) {
            Util.setDisplaySize(builder.mediaContain, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            Util.setDisplaySize(surfaceView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            Util.setLandscape(builder.context as Activity)
        } else if (Util.isLandscape(builder.context!!)) {
            Util.setDisplaySize(builder.mediaContain, builder.width!!, builder.height!!)
            Util.setDisplaySize(surfaceView, builder.width!!, builder.height!!)
            Util.setPortrait(builder.context as Activity)
        }
    }

    private fun resetState() {

    }

    private fun addSeekBarChangeListener(seekBar: AppCompatSeekBar?) {
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            var progress: Int? = null
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                this.progress = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                player?.seekTo(progress?.toLong())
            }
        })
    }

    private fun createSurfaceView(context: Context?, width: Int, height: Int): SurfaceView? {
        val surfaceView = SurfaceView(context)
        surfaceView.holder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        surfaceView.layoutParams = ViewGroup.LayoutParams(width, height)
        return surfaceView
    }

    private fun surfaceViewAddCallback(surfaceView: SurfaceView?) {
        if (null != surfaceHolderCallback) {
            surfaceView?.holder?.removeCallback(surfaceHolderCallback)
        }
        surfaceHolderCallback = object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                Util.showLog("surfaceChanged")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                Util.showLog("surfaceDestroyed")
//                if (!restart!!) {
//                    savaPos = player?.getCurrentPosition()
//                    restart = false
//                }
                //在调用remove画布的时候，这里会触发
                if (surfaceView?.holder == holder) {
                    player?.release()
                    player = null
                }
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                Util.showLog("surfaceCreated")
                player?.setDisplay(holder)  //todo 必须等待画布创建完成设置,不然会出现有声音没画面的现象
            }
        }
        surfaceView?.holder?.addCallback(surfaceHolderCallback)
    }


    /**
     * 播放器帮助建造类
     */
    class Builder {
        var infos: ArrayList<MediaInfo> = ArrayList()
        var player: AbsMediaCore? = null
        var mediaListener: HelpMediaListener? = null
        var gestureListener: HelpGestureListener? = null
        var context: Context? = null
        var mediaContain: FrameLayout? = null
        var width: Int? = 0
        var height: Int? = 0
        var index: Int? = 0

        fun index(index: Int): Builder {
            this.index = index
            return this
        }

        fun infos(infos: ArrayList<MediaInfo>?): Builder {
            this.infos = infos!!
            return this
        }

        fun context(context: Context?): Builder {
            this.context = context
            return this
        }

        fun player(player: AbsMediaCore?): Builder {
            this.player = player
            return this
        }

        fun mediaListener(mediaListener: HelpMediaListener?): Builder {
            this.mediaListener = mediaListener
            return this
        }

        fun mediaContain(mediaContain: FrameLayout?): Builder {
            this.mediaContain = mediaContain
            return this
        }

        fun gestureListener(gestureListener: HelpGestureListener?): Builder {
            this.gestureListener = gestureListener
            return this
        }

        fun width(width: Int?): Builder {
            this.width = width
            return this
        }

        fun height(height: Int?): Builder {
            this.height = height
            return this
        }

        fun build(): MediaHelp {
            return object : MediaHelp(this) {
                override fun setDisplaySize(width: Int?, height: Int?) {
                }

                override fun next() {
                }

                override fun pre() {
                }

                override fun seek(mesc: Long) {
                }

                override fun release() {
                }

                override fun setPlayWay(playWay: Constant.EnumPlayWay?) {
                }

                override fun createPlayer(): AbsMediaCore {
                    return player!!
                }
            }
        }
    }

    /**
     * 播放器监听 详情请查看{link@MediaListener}
     */
    interface HelpMediaListener {
        fun onPrepared(mp: AbsMediaCore)
        fun onError(mp: AbsMediaCore, what: Int?, extra: Int?): Boolean
        fun onBufferingUpdate(mp: AbsMediaCore, percent: Int)
        fun onCompletion(mp: AbsMediaCore)
        fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean
        fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int)
        fun onTimedText(mp: AbsMediaCore, text: String)
    }

    /**
     * 手势相关监听 详情请查看{link@MediaGestureListener}
     */
    interface HelpGestureListener {
        fun onDown()
        fun onUp(gestureState: Constant.EnumGestureState?)
        fun onVolume(slidePercent: Float)
        fun onLight(slidePercent: Float)
        fun onProgress(slidePercent: Float)
        fun onClickListener()
        fun onGesture(gestureState: Constant.EnumGestureState?, curPercent: Float?, player: AbsMediaCore?)
    }
}

/**
 * 播放实体类
 */
class MediaInfo {
    var top: String? = ""
    var url: String? = ""
}

/**
 * 帮助类方法 提供一些常用方法
 */
interface IMedia {
    /**
     * 重置播放器
     */
    fun setupPlayer(player: AbsMediaCore?)

    /**
     * 播放下一集
     */
    fun next()

    /**
     * 播放上一集
     */
    fun pre()

    /**
     * 设置进度
     */
    fun seek(mesc: Long)

    /**
     * 选择屏幕模式
     */
    fun choiceScreenMode()

    /**
     * 释放播放器
     */
    fun release()

    /**
     * 设置播放方式
     */
    fun setPlayWay(playWay: Constant.EnumPlayWay?)

    /**
     * 设置展示的大小
     */
    fun setDisplaySize(width: Int?, height: Int?)

    /**
     * 添加播放器所有监听
     */
    fun addMediaLisenter()

    /**
     * 添加手势监听
     */
    fun addGestureDetectorListener()

}