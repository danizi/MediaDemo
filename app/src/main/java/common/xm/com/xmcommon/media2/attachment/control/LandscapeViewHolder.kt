package common.xm.com.xmcommon.media2.attachment.control

import android.annotation.SuppressLint
import android.app.Activity
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media2.log.BKLog
import common.xm.com.xmcommon.media2.utils.ScreenUtil
import common.xm.com.xmcommon.media2.utils.TimeUtil

/**
 * 横屏界面
 */
class LandscapeViewHolder : ControlViewHolder {

    companion object {
        fun create(rootView: View?): LandscapeViewHolder {
            val clLandscapeTop = rootView?.findViewById<View>(R.id.cl_landscape_top) as ConstraintLayout
            val ivBack = rootView.findViewById<View>(R.id.iv_back) as ImageView
            val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
            val ivMore = rootView.findViewById<View>(R.id.iv_more) as ImageView
            val clLandscapeBottom = rootView.findViewById<View>(R.id.cl_landscape_bottom) as ConstraintLayout
            val seekBar = rootView.findViewById<View>(R.id.seekBar) as SeekBar
            val ivAction = rootView.findViewById<View>(R.id.iv_action) as ImageView
            val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
            val tvRatio = rootView.findViewById<View>(R.id.tv_ratio) as TextView
            val clSeek = rootView.findViewById<View>(R.id.cl_seek) as ConstraintLayout
            val tvTime2 = rootView.findViewById<View>(R.id.tv_time2) as TextView
            val pbLoading = rootView.findViewById<View>(R.id.pb) as ProgressBar
            return LandscapeViewHolder(clLandscapeTop, ivBack, tvTitle, ivMore, clLandscapeBottom, seekBar, ivAction, tvTime, tvRatio, clSeek, tvTime2, pbLoading)
        }
    }

    private var clLandscapeTop: ConstraintLayout? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private var ivMore: ImageView? = null
    private var clLandscapeBottom: ConstraintLayout? = null
    private var seekBar: SeekBar? = null
    private var ivAction: ImageView? = null
    private var tvTime: TextView? = null
    private var tvRatio: TextView? = null
    private var clSeek: ConstraintLayout? = null
    private var tvTime2: TextView? = null
    private var pbLoading: ProgressBar? = null

    private constructor(clLandscapeTop: ConstraintLayout, ivBack: ImageView, tvTitle: TextView, ivMore: ImageView, clLandscapeBottom: ConstraintLayout, seekBar: SeekBar, ivAction: ImageView, tvTime: TextView, tvRatio: TextView, clSeek: ConstraintLayout, tvTime2: TextView, pbLoading: ProgressBar) {
        this.clLandscapeTop = clLandscapeTop
        this.ivBack = ivBack
        this.tvTitle = tvTitle
        this.ivMore = ivMore
        this.clLandscapeBottom = clLandscapeBottom
        this.seekBar = seekBar
        this.ivAction = ivAction
        this.tvTime = tvTime
        this.tvRatio = tvRatio
        this.clSeek = clSeek
        this.tvTime2 = tvTime2
        this.pbLoading = pbLoading
    }

    override fun bind(attachmentControl: AttachmentControl?) {
        super.bind(attachmentControl)
        mediaPlayer = attachmentControl?.xmVideoView?.mediaPlayer
        xmVideoView = attachmentControl?.xmVideoView
        activity = attachmentControl?.context as Activity
        screenW = ScreenUtil.getNormalWH(activity)[0]
        screenH = ScreenUtil.getNormalWH(activity)[1]
        if (screenH < screenW) {
            val temp = screenH
            screenH = screenW
            screenW = temp
        }
        initEvent()
    }

    private fun initEvent() {
        ivBack?.setOnClickListener {
            BKLog.d(TAG, "Landscape -> Portrait")
            // 横屏高度 > 宽度
            //设置竖屏
            ScreenUtil.setPortrait(activity)
            //显示系统状态栏
            ScreenUtil.setDecorVisible(activity)
            //设置宽高
            if (portraitXmVideoViewRect != null) {
                //xmVideoView?.layout(portraitXmVideoViewRect?.left!!, portraitXmVideoViewRect?.top!!, portraitXmVideoViewRect?.right!!, portraitXmVideoViewRect?.bottom!!)
                xmVideoView?.layoutParams?.width = portraitXmVideoViewRect?.right!!
                xmVideoView?.layoutParams?.height = portraitXmVideoViewRect?.bottom!!

            } else {
                BKLog.e(TAG, "请给portraitXmVideoView属性赋值")
            }
            hideControlView()
            listener?.onState(AttachmentControl.PORTRAIT)
        }
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            /**
             * 设置progress 属性也会触发，所有设置进度完成的时候应该在DOWN_UP事件回调中设置播放器播放进度
             */
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                this@LandscapeViewHolder.progress = progress
                if (isHorizontalSlide) {
                    //用户在屏幕水平滑动，但未触碰到seekbar时
                    BKLog.i(AttachmentControl.TAG, "“未”触碰到Seekbar，滑动中... progress:$progress")
                } else {
                    //用户触碰了seekbar或者定时器一直设置progress属性值时
                    val pos = ((progress.toFloat() / 100f) * mediaPlayer?.getDuration()!!).toLong()
                    updateProgress(pos, false)
                    BKLog.i(AttachmentControl.TAG, "触碰到Seekbar，滑动中... progress:$progress")
                }
            }

            /**
             * 只有手指触控了滑块触发
             */
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                showControlView()
                showProgress()
                progressTimerStop()
                progress = 0
                BKLog.d(AttachmentControl.TAG, "开始触发滑动 progress:$progress")
            }

            /**
             * 只有手指触控了滑块释放后触发
             */
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                hideControlView()
                horizontalSlideStopSeekTo()
                progressTimerStart(period = 1000)
                BKLog.d(AttachmentControl.TAG, "结束触发滑动 progress:$progress")
            }
        })
    }

    override fun showOrHideControlView() {
        if (clLandscapeBottom?.visibility == View.VISIBLE) {
            hideControlView()
        } else {
            showControlView()
        }
    }

    override fun showProgress() {
        pbLoading?.visibility = View.VISIBLE
    }

    override fun showLoading() {
        pbLoading?.visibility = View.VISIBLE
    }

    override fun showTop() {
        clLandscapeTop?.visibility = View.VISIBLE
    }

    override fun showBottom() {
        clLandscapeBottom?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pbLoading?.visibility = View.GONE
    }

    override fun hideTop() {
        clLandscapeTop?.visibility = View.GONE
    }

    override fun hideBottom() {
        clLandscapeBottom?.visibility = View.GONE
    }

    override fun hideProgress() {
        pbLoading?.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    override fun updateProgress(pos: Long, isSetProgress: Boolean) {
        if (mediaPlayer == null) {
            BKLog.e(TAG, "updateProgress() mediaPlayer is null")
            return
        }
        val duration = mediaPlayer?.getDuration()!!
        if (isSetProgress) {
            seekBar?.progress = (pos * 100f / duration.toFloat()).toInt()
        }
        if (pos in 0..duration) {
            tvTime?.text = TimeUtil.hhmmss(pos) + "/" + TimeUtil.hhmmss(duration)
            tvTime2?.text = TimeUtil.hhmmss(pos) + "/" + TimeUtil.hhmmss(duration)
        }
    }

    override fun horizontalSlideStopSeekTo() {
        val seekPos = mediaPlayer?.getDuration()!! * (progress.toFloat() / 100f)
        mediaPlayer?.seekTo(seekPos.toInt())
    }

    override fun setActionResID(id: Int) {
        try {
            ivAction?.setImageResource(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun secondaryProgress(present: Int) {
        seekBar?.secondaryProgress = present
    }

    override fun progress(present: Int) {
        seekBar?.progress = present
    }
}

/**
 * 播放列表ViewHolder
 */
class PlayListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind() {

    }
}

/**
 * 播放列表Adapter
 */
class PlayListAdapter : RecyclerView.Adapter<PlayListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        return PlayListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
