package common.xm.com.xmcommon.media2.attachment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media2.base.IXmMediaPlayer
import common.xm.com.xmcommon.media2.base.XmMediaPlayer
import common.xm.com.xmcommon.media2.base.XmVideoView
import common.xm.com.xmcommon.media2.event.PlayerObserver
import common.xm.com.xmcommon.media2.log.BKLog
import common.xm.com.xmcommon.media2.utils.ScreenUtil
import common.xm.com.xmcommon.media2.utils.TimeUtil.hhmmss
import common.xm.com.xmcommon.media2.utils.TimerHelper

class AttachmentControl(context: Context?) : BaseAttachmentView(context) {
    private var portraitViewHolder: PortraitViewHolder? = null
    private var landscapeViewHolder: LandscapeViewHolder? = null
    private var landscapeView: View? = null
    private var portraitView: View? = null
    private val controlHelper: ControlHelper? = null
    private var slidePresent = 0
    private val period: Int = 1000
    private val delay: Int = 5000

    companion object {
        const val TAG = "AttachmentControl"
        const val PORTRAIT = "portrait"
        const val LANDSCAPE = "landscape"
    }

    override fun onDownUp() {
        super.onDownUp()
        //手指滑动设置进度释放处理进度
        if (portraitViewHolder?.isHorizontalSlide!!) {
            portraitViewHolder?.hide()
            portraitViewHolder?.hideProgress()
            portraitViewHolder?.isHorizontalSlide = false
        }
        if (landscapeViewHolder?.isHorizontalSlide!!) {
            landscapeViewHolder?.hide()
            landscapeViewHolder?.hideProgress()
            landscapeViewHolder?.isHorizontalSlide = false
        }


        //定时隐藏控制器页面
        if (portraitViewHolder?.isClick!!) {
            controlHelper?.startDelayTimerHideControlView(portraitViewHolder, landscapeViewHolder, delay)
            portraitViewHolder?.isClick = false
        }
    }

    init {
        observer = object : PlayerObserver {
            override fun onScaleEnd(mediaPlayer: XmMediaPlayer?, scaleFactor: Float) {
                super.onScaleEnd(mediaPlayer, scaleFactor)
                if (context == null) {
                    BKLog.e(TAG, "context is null")
                    return
                }

                if (ScreenUtil.isLandscape(context)) {

                }
            }

            override fun onClick(mediaPlayer: XmMediaPlayer?) {
                super.onClick(mediaPlayer)
                portraitViewHolder?.isClick = true
                landscapeViewHolder?.isClick = true
                xmVideoView?.bringChildToFront(this@AttachmentControl)  //ps:可能会被其他控件置顶
                portraitViewHolder?.showAndHide()
                landscapeViewHolder?.showAndHide()
            }

            override fun onInfo(mp: IXmMediaPlayer, what: Int, extra: Int) {
                super.onInfo(mp, what, extra)
                if (what == IXmMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    portraitViewHolder?.ivAction?.setImageResource(R.mipmap.media_control_pause)
                    controlHelper?.progressTimerStart(xmVideoView, portraitViewHolder, landscapeViewHolder, period)
                }
            }

            override fun onBufferingUpdate(mp: IXmMediaPlayer, percent: Int) {
                super.onBufferingUpdate(mp, percent)
                portraitViewHolder?.seekBar?.secondaryProgress = percent
                landscapeViewHolder?.seekBar?.secondaryProgress = percent
            }

            @SuppressLint("SetTextI18n")
            override fun onHorizontal(mediaPlayer: XmMediaPlayer?, present: Int) {
                super.onHorizontal(mediaPlayer, present)

                //手指处于水平滑动中
                portraitViewHolder?.isHorizontalSlide = true
                landscapeViewHolder?.isHorizontalSlide = true

                //停止所有的计时器
                controlHelper?.stopDelayTimerHideControlView()
                controlHelper?.progressTimerStop()

                //显示控制器界面
                slidePresent = present * 1000//转化毫秒
                portraitViewHolder?.showProgress(slidePresent) //显示“竖屏”进度UI界面
                portraitViewHolder?.show() //显示“竖屏”控制界面

                landscapeViewHolder?.showProgress(slidePresent) //显示“横向”进度UI界面
                landscapeViewHolder?.show()//显示“横向”控制界面
            }
        }
    }

    override fun layouId(): Int {
        return R.layout.attachment_control
    }

    override fun unBind() {}

    override fun bind(xmVideoView: XmVideoView) {
        this.xmVideoView = xmVideoView

        portraitView = getView(R.layout.attachment_control_portrait)
        landscapeView = getView(R.layout.attachment_control_landscape)
        addView(portraitView, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        addView(landscapeView, LayoutParams(MATCH_PARENT, MATCH_PARENT))

        portraitView?.visibility = View.GONE
        landscapeView?.visibility = View.GONE

        portraitViewHolder = PortraitViewHolder.create(this, portraitView)
        landscapeViewHolder = LandscapeViewHolder.create(this, landscapeView)

        portraitViewHolder?.bind(xmVideoView)
        landscapeViewHolder?.bind(xmVideoView)
    }

    override fun initEvent() {
        portraitViewHolder?.initEvent()
        landscapeViewHolder?.initEvent()
    }

    private class ControlHelper {
        var isHorizontalSlide = false
        var isClick = false

        private var progressTimer: TimerHelper? = TimerHelper()
        private var controlViewHideTimer: TimerHelper? = TimerHelper()

        fun progressTimerStart(xmVideoView: XmVideoView?, portraitViewHolder: PortraitViewHolder?, landscapeViewHolder: LandscapeViewHolder?, period: Int) {
            /*定时更新进度*/
            progressTimer?.start(object : TimerHelper.OnPeriodListener {
                @SuppressLint("SetTextI18n")
                override fun onPeriod() {
                    val pos = xmVideoView?.mediaPlayer?.getCurrentPosition()!!
                    val duration = xmVideoView?.mediaPlayer?.getDuration()!!
                    if (pos > duration) {
                        progressTimer?.stop()
                        return
                    }
                    updateProgress(portraitViewHolder?.seekBar, portraitViewHolder?.tvTime, pos, duration)
                    updateProgress(landscapeViewHolder?.seekBar, landscapeViewHolder?.tvTime, pos, duration)
                }
            }, period.toLong())
        }

        fun progressTimerStop() {
            /*关闭定时更新进度*/
            progressTimer?.stop()
        }

        fun startDelayTimerHideControlView(portraitViewHolder: PortraitViewHolder?, landscapeViewHolder: LandscapeViewHolder?, delay: Int) {
            /*延时隐藏控制界面*/
            controlViewHideTimer?.start(object : TimerHelper.OnDelayTimerListener {
                override fun onDelayTimerFinish() {
                    portraitViewHolder?.hide()
                    landscapeViewHolder?.hide()
                }
            }, delay.toLong())

        }

        fun stopDelayTimerHideControlView() {
            /*停止延时隐藏控制界面*/
            controlViewHideTimer?.stop()
        }

        fun clickAction(playId: Int, pauseId: Int, action: ImageView?, xmMediaPlayer: XmMediaPlayer?) {
            /*暂停&播放*/
            try {
                if (xmMediaPlayer?.isPlaying() == true) {
                    action?.setImageResource(playId)
                    xmMediaPlayer.pause()
                } else {
                    action?.setImageResource(pauseId)
                    xmMediaPlayer?.start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun clickScreenSwitch(context: Context?, videoView: View? = null, l: Int = 0, t: Int = 0, r: Int = 0, b: Int = 0) {
            /*大小窗口切换*/
            if (context == null) {
                BKLog.e(TAG, "context is null")
                return
            }

            val activity = context as Activity

            if (ScreenUtil.isLandscape(context)) {
                BKLog.d(TAG, "Landscape -> Portrait")
                // 横屏高度 > 宽度
                //设置竖屏
                ScreenUtil.setPortrait(activity)
                //显示系统状态栏
                ScreenUtil.setDecorVisible(activity)
                //设置宽高
                videoView?.layout(l, t, r, b)
            } else {
                BKLog.d(TAG, "Portrait  -> Landscape")
                // 横屏高度 < 宽度
                //设置横屏
                ScreenUtil.setLandscape(activity)
                //隐藏系统状态栏
                ScreenUtil.hideStatusBar(activity)
                //设置宽高
                videoView?.layout(l, t, r, b)
            }
        }

        @SuppressLint("SetTextI18n")
        fun updateProgress(seekBar: SeekBar?, time: TextView?, pos: Long, duration: Long) {
            /*更新进度*/
            seekBar?.progress = (pos * 100f / duration.toFloat()).toInt()
            if (pos <= duration) {
                time?.text = hhmmss(pos) + ":" + hhmmss(duration)
            }
        }
    }

    private class PortraitViewHolder private constructor(var attachmentControl: AttachmentControl?, val rootView: View?, val clPortraitTop: ConstraintLayout, val ivBack: ImageView, val tvTitle: TextView, val ivListener: ImageView, val ivMiracast: ImageView, val ivShare: ImageView, val ivMore: ImageView, val clPortraitBottom: ConstraintLayout, val ivAction: ImageView, val seekBar: SeekBar, val tvTime: TextView, val ivScreenFull: ImageView, val clSeek: ConstraintLayout, val tvTime2: TextView) {

        companion object {
            fun create(attachmentControl: AttachmentControl?, rootView: View?): PortraitViewHolder {
                val clPortraitTop = rootView?.findViewById<View>(R.id.cl_portrait_top) as ConstraintLayout
                val ivBack = rootView.findViewById<View>(R.id.iv_back) as ImageView
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val ivListener = rootView.findViewById<View>(R.id.iv_listener) as ImageView
                val ivMiracast = rootView.findViewById<View>(R.id.iv_miracast) as ImageView
                val ivShare = rootView.findViewById<View>(R.id.iv_share) as ImageView
                val ivMore = rootView.findViewById<View>(R.id.iv_more) as ImageView
                val clPortraitBottom = rootView.findViewById<View>(R.id.cl_portrait_bottom) as ConstraintLayout
                val ivAction = rootView.findViewById<View>(R.id.iv_action) as ImageView
                val seekBar = rootView.findViewById<View>(R.id.seekBar) as SeekBar
                val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
                val ivScreenFull = rootView.findViewById<View>(R.id.iv_screen_full) as ImageView
                val clSeek = rootView.findViewById<View>(R.id.cl_seek) as ConstraintLayout
                val tvTime2 = rootView.findViewById<View>(R.id.tv_time2) as TextView
                return PortraitViewHolder(attachmentControl, rootView, clPortraitTop, ivBack, tvTitle, ivListener, ivMiracast, ivShare, ivMore, clPortraitBottom, ivAction, seekBar, tvTime, ivScreenFull, clSeek, tvTime2)
            }
        }

        private var activity: Activity? = null
        private var mediaPlayer: XmMediaPlayer? = null
        private var xmVideoView: XmVideoView? = null
        private var screenW = 0
        private var screenH = 0
        private var controlHelper: ControlHelper? = null
        var isHorizontalSlide = false
        var isClick = false

        init {
            controlHelper = attachmentControl?.controlHelper
            screenW = ScreenUtil.getNormalWH(activity)[0]
            screenH = ScreenUtil.getNormalWH(activity)[1]
        }

        fun initEvent() {
            ivAction.setOnClickListener {
                controlHelper?.clickAction(R.mipmap.media_control_play, R.mipmap.media_control_pause, ivAction, mediaPlayer)
            }
            ivScreenFull.setOnClickListener {
                controlHelper?.clickScreenSwitch(activity, xmVideoView, 0, 0, screenW, screenH)
                hide()
                attachmentControl?.landscapeViewHolder?.hide()
            }

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    //用户手指触控了屏幕
                    if (isHorizontalSlide) {
                        BKLog.i(TAG, "触发滑动 progress:$progress")
                        val seekPos = xmVideoView?.mediaPlayer?.getDuration()!! * (progress.toFloat() / 100f)
                        xmVideoView?.mediaPlayer?.seekTo(seekPos.toInt())
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    clSeek.visibility = View.VISIBLE
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    clSeek.visibility = View.GONE
                }
            })
        }

        fun showAndHide() {
            if (ScreenUtil.isPortrait(activity!!)) {
                if (clPortraitBottom.visibility == View.VISIBLE) {
                    hide()
                } else {
                    show()
                }
            }
        }

        fun show() {
            /*显示控制器*/
            clPortraitTop.visibility = View.VISIBLE
            clPortraitBottom.visibility = View.VISIBLE
            rootView?.visibility = View.VISIBLE
        }

        fun hide() {
            /*隐藏控制器*/
            clPortraitTop.visibility = View.GONE
            clPortraitBottom.visibility = View.GONE
            rootView?.visibility = View.GONE
        }

        fun showProgress(slidePresent: Int = 0) {
            /*显示手势播放进度*/
            clSeek.visibility = View.VISIBLE
            controlHelper?.updateProgress(seekBar, tvTime2, mediaPlayer?.getCurrentPosition()!! + slidePresent, mediaPlayer?.getDuration()!!)
        }

        fun hideProgress() {
            /*隐藏手势播放进度*/
            clSeek.visibility = View.GONE
        }

        fun bind(xmVideoView: XmVideoView?) {
            activity = attachmentControl?.context as Activity
            mediaPlayer = attachmentControl?.xmVideoView?.mediaPlayer
            this.xmVideoView = xmVideoView
        }
    }

    private class LandscapeViewHolder private constructor(val attachmentControl: AttachmentControl?, val clLandscapeTop: ConstraintLayout, val ivBack: ImageView, val tvTitle: TextView, val ivMore: ImageView, val clLandscapeBottom: ConstraintLayout, val seekBar: SeekBar, val ivAction: ImageView, val tvTime: TextView, val tvRatio: TextView) {
        companion object {

            fun create(attachmentControl: AttachmentControl?, rootView: View?): LandscapeViewHolder {
                val clLandscapeTop = rootView?.findViewById<View>(R.id.cl_landscape_top) as ConstraintLayout
                val ivBack = rootView.findViewById<View>(R.id.iv_back) as ImageView
                val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
                val ivMore = rootView.findViewById<View>(R.id.iv_more) as ImageView
                val clLandscapeBottom = rootView.findViewById<View>(R.id.cl_landscape_bottom) as ConstraintLayout
                val seekBar = rootView.findViewById<View>(R.id.seekBar) as SeekBar
                val ivAction = rootView.findViewById<View>(R.id.iv_action) as ImageView
                val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
                val tvRatio = rootView.findViewById<View>(R.id.tv_ratio) as TextView
                return LandscapeViewHolder(attachmentControl, clLandscapeTop, ivBack, tvTitle, ivMore, clLandscapeBottom, seekBar, ivAction, tvTime, tvRatio)
            }

        }


        private var activity: Activity? = null
        private var mediaPlayer: XmMediaPlayer? = null
        private var xmVideoView: XmVideoView? = null
        private var screenW = 0
        private var screenH = 0
        private var controlHelper: ControlHelper? = null
        var isHorizontalSlide = false
        var isClick = false

        init {
            controlHelper = attachmentControl?.controlHelper
            screenW = ScreenUtil.getNormalWH(activity)[0]
            screenH = ScreenUtil.getNormalWH(activity)[1]
        }

        fun initEvent() {
            ivBack.setOnClickListener {
                controlHelper?.clickScreenSwitch(activity, xmVideoView)
                hide()
                attachmentControl?.portraitViewHolder?.hide()
            }
        }

        fun showAndHide() {
            if (ScreenUtil.isLandscape(activity!!)) {
                if (clLandscapeBottom.visibility == View.VISIBLE) {
                    show()
                } else {
                    hide()
                }
            }
        }

        fun show() {
            clLandscapeTop.visibility = View.VISIBLE
            clLandscapeBottom.visibility = View.VISIBLE
        }

        fun hide() {
            clLandscapeTop.visibility = View.GONE
            clLandscapeBottom.visibility = View.GONE
        }

        fun bind(xmVideoView: XmVideoView) {
            activity = attachmentControl?.context as Activity
            mediaPlayer = attachmentControl.xmVideoView?.mediaPlayer
            this.xmVideoView = xmVideoView
        }

        fun showProgress(slidePresent: Int) {
        }

        fun hideProgress() {

        }
    }
}