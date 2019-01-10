package common.xm.com.xmcommon.media

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatSeekBar
import android.view.*
import android.widget.*
import com.xm.lib.ijkplayer.IJKPlayer
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.constant.Constant
import com.xm.lib.media.core.listener.MediaGestureListener
import com.xm.lib.media.core.listener.MediaListener
import com.xm.lib.media.core.utils.TimerUtil
import com.xm.lib.media.core.utils.Util
import com.xm.lib.media.core.utils.Util.Companion.hhmmss
import common.xm.com.xmcommon.R

class ActivityIjkplayer : AppCompatActivity() {
    /*---------------------------------
     * 播放器
     */
    private var player: AbsMediaCore? = IJKPlayer()
    private var surfaceView: SurfaceView? = null
    private var surfaceHolderCallback: SurfaceHolder.Callback? = null
    private var restart: Boolean? = false
    private var savaPosDefuat: Long? = -1
    private var savaPos: Long? = savaPosDefuat
    private var playWay: Constant.EnumPlayWay = Constant.EnumPlayWay.LIST_LOOP
    private var urls: ArrayList<String>? = ArrayList()
    private var urlIndex: Int = 0

    /*---------------------------------
     * 展示播放器信息的控件
     */
    private var mediaContain: FrameLayout? = null
    private var seekBar: AppCompatSeekBar? = null
    private var tvPos: TextView? = null
    private var tvDuration: TextView? = null
    private var tvTcp: TextView? = null
    private var containeW: Int = LinearLayout.LayoutParams.MATCH_PARENT
    private var containeH: Int = 400

    /*---------------------------------
     * 手势控制亮度 进度 音量的控件
     */
    private var gestureDetectorView: View? = null
    private var progressBar: ProgressBar? = null
    private var iv: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ijkplayer)
        findViews()
        initData()
    }

    private fun initData() {
        urls?.add("http://hls.videocc.net/26de49f8c2/9/26de49f8c273bbc8f6812d1422a11b39_2.m3u8")
        urls?.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
        initPlayer()
        player?.setDataSource(urls!![urlIndex])
    }

    private fun findViews() {
        mediaContain = findViewById(R.id.media_contain)
        seekBar = findViewById(R.id.seekbar)
        tvPos = findViewById(R.id.tv_pos)
        tvDuration = findViewById(R.id.tv_duration)
        tvTcp = findViewById(R.id.tv_tcp)
        gestureDetectorView = getGestureDetectorView()
        gestureDetectorView?.visibility = View.GONE
        iv = gestureDetectorView?.findViewById(R.id.iv)
        progressBar = gestureDetectorView?.findViewById(R.id.progress)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_media_action, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_replay -> {
                restart()
            }
            R.id.menu_prepareAsync -> {
                player?.prepareAsync()
            }
            R.id.menu_start -> {
                player?.start()
            }
            R.id.menu_pause -> {
                player?.pause()
            }
            R.id.menu_stop -> {
                player?.stop()
            }
            R.id.menu_state -> {
                Util.showToast(this, "当前播放器播放状态:" + player?.mediaLifeState)
            }
            R.id.menu_full -> {
                if (Util.isPortrait(this)) {
                    Util.hideStatusBar(this)
                    Util.setLandscape(this)
                    setDisplaySize(surfaceView, mediaContain, Util.getNormalWH(this)[0], Util.getNormalWH(this)[1])
                }
            }
            R.id.menu_small -> {
                if (Util.isLandscape(this)) {
                    Util.hideStatusBar(this)
                    Util.setPortrait(this)
                    setDisplaySize(surfaceView, mediaContain, Util.getNormalWH(this)[0], containeH)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRestart() {
        super.onRestart()
        restart()
    }

    private fun restart() {
        restart = true
        initPlayer()
        player?.setDataSource(urls!![urlIndex])
        player?.prepareAsync()
    }

    @SuppressLint("InflateParams")
    private fun initPlayer() {
        //重置附着在播放器上的控件状态 包括了定时器 进度条 ...
        resetState()
        //移除所有展示容器中的SurfaceView
        removeAllSurfaceView(mediaContain)
        //创建SurfaceView并赋值
        surfaceView = createSurfaceView(this, containeW, containeH)
        //往画面展示容器中添加SurfaceView
        addSurfaceView(mediaContain, surfaceView)
        //添加画布监听
        surfaceViewAddCallback(surfaceView)

        //让播放器处于ide状态
        if (null != player) {
            player?.reset()
            player?.release()
            player = null
        }
        //重新创建播放器
        player = IJKPlayer()

        //添加播放器监听
        addMediaLisenter()
        //seekBar滑动监听
        addSeekBarChangeListener()

        //添加手势布局和监听
        addGestureDetectorLayout(mediaContain, gestureDetectorView!!)
        addGestureDetectorListener()
    }

    @SuppressLint("InflateParams")
    private fun getGestureDetectorView(): View? {
        return LayoutInflater.from(this).inflate(R.layout.media_gesture_common, null, false)
    }

    private fun addGestureDetectorLayout(mediaContain: ViewGroup?, gestureDetectorView: View) {
        mediaContain?.addView(gestureDetectorView)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addGestureDetectorListener() {
        val listener: MediaGestureListener.GestureListener = object : MediaGestureListener.GestureListener() {

            var curPercent: Float? = 0f

            override fun onDown() {
                super.onDown()
                progressBar?.max = 100
            }

            override fun onGesture(gestureState: Constant.EnumGestureState?) {
                super.onGesture(gestureState)
                curPercent = when (gestureState) {
                    Constant.EnumGestureState.PROGRESS -> {
                        player?.getCurrentPosition()!! / player?.getDuration()?.toFloat()!!
                    }
                    Constant.EnumGestureState.LIGHT -> {
                        Util.getScreenBrightness(baseContext)
                    }
                    Constant.EnumGestureState.VOLUME -> {
                        Util.getVolume(baseContext)
                    }
                    else -> {
                        0f
                    }
                }
            }

            override fun onUp(gestureState: Constant.EnumGestureState?) {
                hide(gestureDetectorView)
                if (gestureState == Constant.EnumGestureState.PROGRESS) {
                    player?.seekTo(((progressBar?.progress!! / 100f) * player?.getDuration()!!).toLong())
                }
            }

            override fun onVolume(slidePercent: Float) {
                Util.showLog("onVolume$slidePercent")
                val percent = curPercent!! - slidePercent
                common(percent, R.mipmap.media_volume)
                Util.setVolume(baseContext, percent)
            }

            override fun onLight(slidePercent: Float) {

                Util.showLog("onLight$slidePercent")
                val percent = curPercent!! - slidePercent
                common(percent, R.mipmap.media_light)
                Util.setSystemBrightness(baseContext, percent)
            }

            override fun onProgress(slidePercent: Float) {
                Util.showLog("onProgress$slidePercent")
                val percent = curPercent!! + slidePercent
                common(percent, R.mipmap.media_light)
                //用户触发up事件时调用设置播放进度操作
            }

            private fun common(percent: Float, resID: Int) {
                iv?.setImageResource(resID)
                Util.showLog("percent:$percent curProcess$curPercent")
                progressBar?.progress = (percent * 100).toInt()
                show(gestureDetectorView)
            }

            override fun onClickListener() {
                Util.showLog("onClickListener")
            }
        }
        val gesture = MediaGestureListener(mediaContain, listener)
        val gestureDetector = GestureDetector(this, gesture)
        mediaContain?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                listener.onUp(gesture.gestureState)
            }
            gestureDetector.onTouchEvent(event)
        }
    }

    private fun show(view: View?) {
        if (view?.visibility == View.GONE) {
            view.visibility = View.VISIBLE
        }
    }

    private fun hide(view: View?) {
        if (view?.visibility == View.VISIBLE) {
            view.visibility = View.GONE
        }
    }

    private fun addSeekBarChangeListener() {
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

    private fun resetState() {
        seekBar?.progress = 0
        tvPos?.text = "0"
        //移除计时器重新
        TimerUtil.getInstance().stop()
        //启动计时器
        TimerUtil.getInstance().start(object : TimerUtil.PeriodListenner {
            override fun onPeriodListenner() {
                period()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun period() {
        if (null != player) {
            val pos: Int = player?.getCurrentPosition()?.toInt()!!
            tvPos?.text = "播放进度:${hhmmss(pos.toLong())}"
            seekBar?.progress = pos
            tvTcp?.text = "资源下行速度:" + player?.getTcpSpeed()
        }
    }

    private fun setDisplaySize(surfaceView: SurfaceView?, rlSurfaceViewContainer: ViewGroup?, width: Int, height: Int) {
        surfaceView?.layoutParams?.width = width
        surfaceView?.layoutParams?.height = height
        rlSurfaceViewContainer?.layoutParams?.width = width
        rlSurfaceViewContainer?.layoutParams?.height = height
    }

    private fun createSurfaceView(context: Context?, width: Int, height: Int): SurfaceView? {
        val surfaceView = SurfaceView(context)
        surfaceView.holder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        surfaceView.layoutParams = ViewGroup.LayoutParams(width, height)
        return surfaceView
    }

    private fun removeAllSurfaceView(viewGroup: ViewGroup?) {
        viewGroup?.removeAllViews()
    }

    private fun addSurfaceView(viewGroup: ViewGroup?, surfaceView: SurfaceView?) {
        viewGroup?.addView(surfaceView)
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
                prepared(mp)
            }
        })
        player?.setOnErrorListener(object : MediaListener.OnErrorListener {
            override fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                error(mp, what, extra)
                return false
            }
        })
        player?.setOnBufferingUpdateListener(object : MediaListener.OnBufferingUpdateListener {
            override fun onBufferingUpdate(mp: AbsMediaCore, percent: Int) {
                bufferingUpdate(mp, percent)
            }
        })
        player?.setOnCompletionListener(object : MediaListener.OnCompletionListener {
            override fun onCompletion(mp: AbsMediaCore) {
                completion(mp)
            }
        })
        player?.setOnInfoListener(object : MediaListener.OnInfoListener {
            override fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                return info(mp, what, extra)
            }
        })
        player?.setOnVideoSizeChangedListener(object : MediaListener.OnVideoSizeChangedListener {
            override fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int) {
                videoSizeChanged(mp, width, height, sar_num, sar_den)
            }
        })
        player?.setOnTimedTextListener(object : MediaListener.OnTimedTextListener {
            override fun onTimedText(mp: AbsMediaCore, text: String) {
                timedText(mp, text)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun prepared(mp: AbsMediaCore?) {
        player?.start()
        if (savaPos!! >= 0 && savaPos!! < player?.getDuration()!!) {
            player?.seekTo(savaPos)
            savaPos = savaPosDefuat
        }
        seekBar?.max = player?.getDuration()?.toInt()!!
        tvDuration?.text = "视频总时长:" + Util.hhmmss(player?.getDuration())
        //开启定时器
        TimerUtil.getInstance().start(object : TimerUtil.PeriodListenner {
            override fun onPeriodListenner() {
                period()
            }
        })
    }

    private fun error(mp: AbsMediaCore, what: Int, extra: Int) {
        Util.showToast(this, "onError what:" + what + "extra:" + extra)
    }

    private fun bufferingUpdate(mp: AbsMediaCore?, percent: Int) {
        seekBar?.secondaryProgress = ((percent.toFloat() / 100f) * player?.getDuration()?.toFloat()!!).toInt()
    }

    private fun completion(mp: AbsMediaCore) {
        Util.showToast(this, "播放完成")
        if (playWay == Constant.EnumPlayWay.LIST_LOOP) {
            if (++urlIndex < urls?.size!!) {
                restart()
            }
        }
    }

    private fun info(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
        return false
    }

    private fun videoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int) {

    }

    private fun timedText(mp: AbsMediaCore, text: String) {

    }
}




