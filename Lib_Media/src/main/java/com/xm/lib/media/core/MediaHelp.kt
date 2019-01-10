package com.xm.lib.media.core

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.AppCompatSeekBar
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.SeekBar
import com.xm.lib.media.core.constant.Constant
import com.xm.lib.media.core.listener.MediaGestureListener
import com.xm.lib.media.core.listener.MediaListener
import com.xm.lib.media.core.utils.Util
import com.xm.lib.media.core.utils.Util.Companion.hide

/**
 * 播放器快速创建帮助类
 */
abstract class MediaHelp {
    var mediaListener: HelpMediaListener? = null
    var gestureListener: HelpGestureListener? = null

    private var context: Context? = null
    private var mediaContain: FrameLayout? = null
    private var containeW: Int = LinearLayout.LayoutParams.MATCH_PARENT
    private var containeH: Int = 400
    private var gestureDetectorView: View? = null

    private var player: AbsMediaCore? = null
    private var surfaceView: SurfaceView? = null
    private var surfaceHolderCallback: SurfaceHolder.Callback? = null
    private var restart: Boolean? = false
    private var savaPosDefuat: Long? = -1
    private var savaPos: Long? = savaPosDefuat
    private var playWay: Constant.EnumPlayWay = Constant.EnumPlayWay.LIST_LOOP
    private var urls: ArrayList<String>? = ArrayList()
    private var urlIndex: Int = 0

    abstract fun createPlayer(): AbsMediaCore

    /**
     * 重置状态
     */
    @SuppressLint("InflateParams")
    open fun setupPlayer() {
        //重置附着在播放器上的控件状态 包括了定时器 进度条 ...
        resetState()
        //移除所有展示容器中的SurfaceView
        mediaContain?.removeAllViews()
        //创建SurfaceView并赋值
        surfaceView = createSurfaceView(context, containeW, containeH)
        //往画面展示容器中添加SurfaceView
        mediaContain?.addView(surfaceView)
        //添加画布监听
        surfaceViewAddCallback(surfaceView)

        //让播放器处于ide状态
        if (null != player) {
            player?.reset()
            player?.release()
            player = null
        }
        //重新创建播放器
        player = createPlayer()

        //添加播放器监听
        addMediaLisenter()

        //seekBar滑动监听
        addSeekBarChangeListener(null) //todo

        //添加手势布局和监听
        gestureDetectorView = Util.getView(context, 1)
        hide(gestureDetectorView)
        mediaContain?.addView(gestureDetectorView!!)
        addGestureDetectorListener()
    }

    /**
     * 上一集
     */
    fun pre() {
        urlIndex--
    }

    /**
     * 下一集
     */
    fun next() {
        urlIndex++
    }

    /**
     * 设置播放方式 ：列表循环 单集循环 播放暂停
     */
    fun setPlayWay(playWay: Constant.EnumPlayWay?) {
        if (playWay != null) {
            this.playWay = playWay
        }
    }

    /**
     * 设置定时播放后停止自定义 30 60 一集
     */
    fun setTimer() {

    }

    /**
     * 播放
     */
    fun start(url: String) {

    }

    /**
     * 暂停
     */
    fun pause() {

    }

    fun seek() {

    }

    fun release() {

    }

    private fun resetState() {

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
                if (!restart!!) {
                    savaPos = player?.getCurrentPosition()
                    restart = false
                }
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

    private fun addMediaLisenter() {
        player?.setOnPreparedListener(object : MediaListener.OnPreparedListener {
            override fun onPrepared(mp: AbsMediaCore) {
                mediaListener?.onPrepared(mp)
            }
        })
        player?.setOnErrorListener(object : MediaListener.OnErrorListener {
            override fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                return mediaListener?.onError(mp, what, extra)!!
            }
        })
        player?.setOnBufferingUpdateListener(object : MediaListener.OnBufferingUpdateListener {
            override fun onBufferingUpdate(mp: AbsMediaCore, percent: Int) {
                mediaListener?.onBufferingUpdate(mp, percent)
            }
        })
        player?.setOnCompletionListener(object : MediaListener.OnCompletionListener {
            override fun onCompletion(mp: AbsMediaCore) {
                mediaListener?.onCompletion(mp)
            }
        })
        player?.setOnInfoListener(object : MediaListener.OnInfoListener {
            override fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                return mediaListener?.onInfo(mp, what, extra)!!
            }
        })
        player?.setOnVideoSizeChangedListener(object : MediaListener.OnVideoSizeChangedListener {
            override fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int) {
                mediaListener?.onVideoSizeChanged(mp, width, height, sar_num, sar_den)
            }
        })
        player?.setOnTimedTextListener(object : MediaListener.OnTimedTextListener {
            override fun onTimedText(mp: AbsMediaCore, text: String) {
                mediaListener?.onTimedText(mp, text)
            }
        })
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

    @SuppressLint("ClickableViewAccessibility")
    private fun addGestureDetectorListener() {
        val listener: MediaGestureListener.GestureListener = object : MediaGestureListener.GestureListener() {

            var curPercent: Float? = 0f

            override fun onDown() {
                super.onDown()
                onDown()
            }

            override fun onGesture(gestureState: Constant.EnumGestureState?) {
                super.onGesture(gestureState)
                curPercent = when (gestureState) {
                    Constant.EnumGestureState.PROGRESS -> {
                        player?.getCurrentPosition()!! / player?.getDuration()?.toFloat()!!
                    }
                    Constant.EnumGestureState.LIGHT -> {
                        Util.getScreenBrightness(context)
                    }
                    Constant.EnumGestureState.VOLUME -> {
                        Util.getVolume(context)
                    }
                    else -> {
                        0f
                    }
                }
            }

            override fun onUp(gestureState: Constant.EnumGestureState?) {
                gestureListener?.onUp()
            }

            override fun onVolume(slidePercent: Float) {
                gestureListener?.onVolume()
            }

            override fun onLight(slidePercent: Float) {
                gestureListener?.onLight()
            }

            override fun onProgress(slidePercent: Float) {
                gestureListener?.onProgress()
            }

            override fun onClickListener() {
                gestureListener?.onClickListener()
            }
        }
        val gesture = MediaGestureListener(mediaContain, listener)
        val gestureDetector = GestureDetector(context, gesture)
        mediaContain?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                listener.onUp(gesture.gestureState)
            }
            gestureDetector.onTouchEvent(event)
        }
    }

    /**
     * 播放器监听
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
     * 手势相关监听
     */
    interface HelpGestureListener {
        fun onDown()
        fun onUp()
        fun onVolume()
        fun onLight()
        fun onProgress()
        fun onClickListener()
    }
}