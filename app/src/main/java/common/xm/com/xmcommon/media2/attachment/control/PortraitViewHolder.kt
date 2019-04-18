package common.xm.com.xmcommon.media2.attachment.control

import android.app.Activity
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media2.base.XmMediaPlayer
import common.xm.com.xmcommon.media2.base.XmVideoView
import common.xm.com.xmcommon.media2.log.BKLog
import common.xm.com.xmcommon.media2.utils.ScreenUtil

@Deprecated("")
class PortraitViewHolder private constructor(var attachmentControl: AttachmentControl?, val rootView: View?, val clPortraitTop: ConstraintLayout, val ivBack: ImageView, val tvTitle: TextView, val ivListener: ImageView, val ivMiracast: ImageView, val ivShare: ImageView, val ivMore: ImageView, val clPortraitBottom: ConstraintLayout, val ivAction: ImageView, val seekBar: SeekBar, val tvTime: TextView, val ivScreenFull: ImageView, val clSeek: ConstraintLayout, val tvTime2: TextView, val pbLoading: ProgressBar) {

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
            val pbLoading = rootView.findViewById<View>(R.id.pb) as ProgressBar
            return PortraitViewHolder(attachmentControl, rootView, clPortraitTop, ivBack, tvTitle, ivListener, ivMiracast, ivShare, ivMore, clPortraitBottom, ivAction, seekBar, tvTime, ivScreenFull, clSeek, tvTime2, pbLoading)
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
    var progress = 0

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
                    this@PortraitViewHolder.progress = progress
                    BKLog.i(AttachmentControl.TAG, "触发滑动中... progress:$progress")
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                clSeek.visibility = View.VISIBLE
                progress = 0
                BKLog.d(AttachmentControl.TAG, "开始触发滑动 progress:$progress")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                clSeek.visibility = View.GONE
                val seekPos = xmVideoView?.mediaPlayer?.getDuration()!! * (progress.toFloat() / 100f)
                xmVideoView?.mediaPlayer?.seekTo(seekPos.toInt())
                BKLog.d(AttachmentControl.TAG, "结束触发滑动 progress:$progress")
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
        controlHelper?.updateProgress(seekBar, tvTime2, tvTime, slidePresent.toLong(), mediaPlayer?.getDuration()!!)
    }

    fun hideProgress() {
        /*隐藏手势播放进度*/
        clSeek.visibility = View.GONE
    }

    fun bind(xmVideoView: XmVideoView?) {
        mediaPlayer = xmVideoView?.mediaPlayer
        activity = attachmentControl?.context as Activity
        this.xmVideoView = xmVideoView
    }
}
