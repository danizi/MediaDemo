package common.xm.com.xmcommon.media2.attachment.control

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import common.xm.com.xmcommon.media2.base.XmMediaPlayer
import common.xm.com.xmcommon.media2.base.XmVideoView
import common.xm.com.xmcommon.media2.log.BKLog
import common.xm.com.xmcommon.media2.utils.ScreenUtil
import common.xm.com.xmcommon.media2.utils.TimeUtil
import common.xm.com.xmcommon.media2.utils.TimerHelper

@Deprecated("")
class ControlHelper(private val portraitViewHolder: PortraitViewHolder?, private val landscapeViewHolder: LandscapeViewHolder?) {
    companion object {
        const val TAG = "ControlHelper"
    }

    private var progressTimer: TimerHelper? = TimerHelper()
    private var controlViewHideTimer: TimerHelper? = TimerHelper()

    fun progressTimerStart(xmVideoView: XmVideoView?, portraitViewHolder: PortraitViewHolder?, landscapeViewHolder: LandscapeViewHolder?, period: Int) {
        /*定时更新进度*/
        progressTimer?.start(object : TimerHelper.OnPeriodListener {
            @SuppressLint("SetTextI18n")
            override fun onPeriod() {
                val pos = xmVideoView?.mediaPlayer?.getCurrentPosition()!!
                val duration = xmVideoView.mediaPlayer?.getDuration()!!
                if (pos > duration) {
                    progressTimer?.stop()
                    return
                }
                updateProgress(portraitViewHolder?.seekBar, portraitViewHolder?.tvTime, portraitViewHolder?.tvTime2, pos, duration)
                updateProgress(landscapeViewHolder?.seekBar, landscapeViewHolder?.tvTime, portraitViewHolder?.tvTime2, pos, duration)
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
    fun updateProgress(seekBar: SeekBar?, time2: TextView?/*视频居中显示的进度*/, time1: TextView?/**/, pos: Long, duration: Long) {
        /*更新进度*/
        seekBar?.progress = (pos * 100f / duration.toFloat()).toInt()
        if (pos <= duration) {
            time2?.text = TimeUtil.hhmmss(pos) + ":" + TimeUtil.hhmmss(duration)
            time1?.text = TimeUtil.hhmmss(pos) + ":" + TimeUtil.hhmmss(duration)
        }
    }

}