package common.xm.com.xmcommon.media2.attachment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media2.base.XmMediaPlayer
import common.xm.com.xmcommon.media2.base.XmVideoView
import common.xm.com.xmcommon.media2.event.PlayerObserver
import common.xm.com.xmcommon.media2.log.BKLog
import common.xm.com.xmcommon.media2.utils.ScreenUtil
import common.xm.com.xmcommon.media2.utils.TimeUtil
import common.xm.com.xmcommon.media2.utils.TimeUtil.start
import android.widget.TextView
import android.widget.SeekBar
import android.support.constraint.ConstraintLayout
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import common.xm.com.xmcommon.media2.base.IXmMediaPlayer
import common.xm.com.xmcommon.media2.utils.TimeUtil.hhmmss
import common.xm.com.xmcommon.media2.utils.TimeUtil.stop
import common.xm.com.xmcommon.media2.utils.ViewUtil


class AttachmentControl(context: Context?) : BaseAttachmentView(context) {
    private var portraitViewHolder: PortraitViewHolder? = null
    private var landscapeViewHolder: LandscapeViewHolder? = null
    private var landscapeView: View? = null
    private var portraitView: View? = null

    companion object {
        const val PORTRAIT = "portrait"
        const val LANDSCAPE = "landscape"
    }

    override fun onDownUp() {
        super.onDownUp()
        stop()
        start(object : TimeUtil.OnDelayTimerListener {
            override fun onDelayTimerFinish() {
                hideAll()
            }
        }, 10000)
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

            override fun onDoubleClick(mediaPlayer: XmMediaPlayer?) {
                super.onDoubleClick(mediaPlayer)
            }

            override fun onClick(mediaPlayer: XmMediaPlayer?) {
                super.onClick(mediaPlayer)

                if (context == null) {
                    BKLog.e(TAG, "context is null")
                    return
                }

                if (ScreenUtil.isPortrait(context)) {
                    showAndHide(PORTRAIT)
                } else {
                    showAndHide(LANDSCAPE)
                }

            }

            override fun onInfo(mp: IXmMediaPlayer, what: Int, extra: Int) {
                super.onInfo(mp, what, extra)
                if (what == 3) {
                    portraitViewHolder?.ivAction?.setImageResource(R.mipmap.media_control_pause)
                    start(object : TimeUtil.OnPeriodListener {
                        @SuppressLint("SetTextI18n")
                        override fun onPeriod() {
                            val pos = xmVideoView?.mediaPlayer?.getCurrentPosition()!!
                            val duration = xmVideoView?.mediaPlayer?.getDuration()!!
                            portraitViewHolder?.seekBar?.progress = (pos * 100f / duration.toFloat()).toInt()
                            portraitViewHolder?.tvTime?.text = hhmmss(pos) + ":" + hhmmss(duration)
                        }
                    }, 1000)
                }
            }

            override fun onBufferingUpdate(mp: IXmMediaPlayer, percent: Int) {
                super.onBufferingUpdate(mp, percent)
                portraitViewHolder?.seekBar?.secondaryProgress = percent
            }
        }

    }

    override fun layouId(): Int {
        return R.layout.attachment_control
    }

    override fun unBind() {}

    override fun bind(xmVideoView: XmVideoView) {
        this.xmVideoView = xmVideoView
    }

    override fun findViews(view: View?) {
        portraitView = getView(R.layout.attachment_control_portrait)
        landscapeView = getView(R.layout.attachment_control_landscape)
        addView(portraitView, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        addView(landscapeView, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        portraitViewHolder = PortraitViewHolder.create(portraitView)
        landscapeViewHolder = LandscapeViewHolder.create(landscapeView)
    }

    override fun initDisplay() {
        portraitView?.visibility = View.GONE
        landscapeView?.visibility = View.GONE
    }

    private var w = 0
    private var h = 0
    override fun initEvent() {
        portraitViewHolder?.ivAction?.setOnClickListener {
            if (xmVideoView?.mediaPlayer?.isPlaying() == true) {
                portraitViewHolder?.ivAction?.setImageResource(R.mipmap.media_control_play)
                xmVideoView?.mediaPlayer?.pause()
            } else {
                portraitViewHolder?.ivAction?.setImageResource(R.mipmap.media_control_pause)
                xmVideoView?.mediaPlayer?.start()
            }
        }
        portraitViewHolder?.ivScreenFull?.setOnClickListener {
            ScreenUtil.setLandscape(context as Activity)
            ViewUtil.setDisplaySize(xmVideoView, ScreenUtil.getNormalWH(context as Activity)[1], ScreenUtil.getNormalWH(context as Activity)[0])
            //xmVideoView?.layout(0, 0, ScreenUtil.getNormalWH(context as Activity)[1], ScreenUtil.getNormalWH(context as Activity)[0])
            setViewSize(xmVideoView)
            ViewUtil.setDisplaySize(xmVideoView?.surfaceView, ScreenUtil.getNormalWH(context as Activity)[1], ScreenUtil.getNormalWH(context as Activity)[0])
            hide(PORTRAIT)
            show(LANDSCAPE)
            w = measuredWidth
            h = measuredHeight
        }

        landscapeViewHolder?.ivBack?.setOnClickListener {
            ScreenUtil.setPortrait(context as Activity)
            ViewUtil.setDisplaySize(xmVideoView, w, h)
            ViewUtil.setDisplaySize(xmVideoView?.surfaceView, w, h)
            hide(LANDSCAPE)
            show(PORTRAIT)
        }
    }

    private fun setViewSize(view: View?) {
        val margin = MarginLayoutParams(view?.layoutParams)
        margin.setMargins(0, -view?.top!!, view?.right!!, view.bottom)
        val layoutParams = LinearLayout.LayoutParams(margin)
        view.layoutParams = layoutParams
    }

    override fun initData() {}

    private fun showAndHide(type: String) {
        if (type == PORTRAIT) {
            if (portraitView?.visibility == View.VISIBLE) {
                hide(type)
            } else {
                show(type)
            }
        } else {
            if (landscapeView?.visibility == View.VISIBLE) {
                hide(type)
            } else {
                show(type)
            }
        }
    }

    private fun show(type: String) {
        if (type == PORTRAIT) {
            portraitView?.visibility = View.VISIBLE
            landscapeView?.visibility = View.GONE
            xmVideoView?.bringChildToFront(this)
        } else {
            portraitView?.visibility = View.GONE
            landscapeView?.visibility = View.VISIBLE
        }
    }

    private fun hide(type: String) {
        if (type == PORTRAIT) {
            portraitView?.visibility = View.GONE
            landscapeView?.visibility = View.GONE
        } else {
            portraitView?.visibility = View.GONE
            landscapeView?.visibility = View.GONE
        }
    }

    private fun hideAll() {
        portraitView?.visibility = View.GONE
        landscapeView?.visibility = View.GONE
    }

    private class PortraitViewHolder private constructor(val clPortraitTop: ConstraintLayout, val ivBack: ImageView, val tvTitle: TextView, val ivListener: ImageView, val ivMiracast: ImageView, val ivShare: ImageView, val ivMore: ImageView, val clPortraitBottom: ConstraintLayout, val ivAction: ImageView, val seekBar: SeekBar, val tvTime: TextView, val ivScreenFull: ImageView) {
        companion object {

            fun create(rootView: View?): PortraitViewHolder {
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
                return PortraitViewHolder(clPortraitTop, ivBack, tvTitle, ivListener, ivMiracast, ivShare, ivMore, clPortraitBottom, ivAction, seekBar, tvTime, ivScreenFull)
            }


        }
    }

    private class LandscapeViewHolder private constructor(val clLandscapeTop: ConstraintLayout, val ivBack: ImageView, val tvTitle: TextView, val ivMore: ImageView, val clLandscapeBottom: ConstraintLayout, val seekBar: SeekBar, val ivAction: ImageView, val tvTime: TextView, val tvRatio: TextView) {
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
                return LandscapeViewHolder(clLandscapeTop, ivBack, tvTitle, ivMore, clLandscapeBottom, seekBar, ivAction, tvTime, tvRatio)
            }

        }
    }


}