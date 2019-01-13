package com.xm.lib.media.core.attach.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.xm.lib.media.R
import com.xm.lib.media.core.AbsAttachLayout
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.constant.Constant
import com.xm.lib.media.core.utils.TimerUtil
import com.xm.lib.media.core.utils.Util

/**
 * 播放器附着完成页面
 * 1 窗口未销毁进行恢复播放
 * 2 窗口销毁进行恢复播放    缓存起来
 */
class AttachControlLayout : AbsAttachLayout {
    private var player: AbsMediaCore? = null
    /**
     * 竖着屏幕的布局
     */
    private var controlView: View? = null
    private var imgPlayPause: ImageView? = null
    private var tvCurrentPosition: TextView? = null
    private var seekBar: SeekBar? = null
    private var tvDuration: TextView? = null
    private var imgScreenMode: ImageView? = null

    constructor(context: Context, layout: Int) : super(context) {
        controlView = Util.getView(context, layout)

        //findView
        imgPlayPause = controlView?.findViewById(R.id.img_play_pause)
        tvCurrentPosition = controlView?.findViewById(R.id.tv_currentPosition)
        seekBar = controlView?.findViewById(R.id.seekBar)
        tvDuration = controlView?.findViewById(R.id.tv_duration)
        imgScreenMode = controlView?.findViewById(R.id.img_screen_mode)

        //initEvent
        imgPlayPause?.setOnClickListener {
            if (player?.isPlaying() == true) {
                player?.pause()
                imgPlayPause?.setImageResource(R.mipmap.media_play)
            } else {
                player?.start()
                imgPlayPause?.setImageResource(R.mipmap.media_pause)
            }
        }
        imgScreenMode?.setOnClickListener {
            videoView?.choiceScreenMode()
        }
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var progress: Int? = null
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                this.progress = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                player?.seekTo((progress?.toLong()!! / 100.toFloat() * player?.getDuration()!!).toLong())
            }
        })

        seekBar?.max = 100

        controlView?.visibility = View.GONE
        addView(controlView)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var curPercent: Float = 0f
    override fun onGesture(gestureState: Constant.EnumGestureState?, curPercent: Float?, player: AbsMediaCore?) {
        super.onGesture(gestureState, curPercent, player)
        this.curPercent = curPercent!!
    }

    override fun onProgress(slidePercent: Float) {
        super.onProgress(slidePercent)
        seekBar?.progress = ((slidePercent + curPercent) * 100).toInt()
    }

    override fun onPrepared(mp: AbsMediaCore) {
        super.onPrepared(mp)
        if (videoView?.auto == true) {
            player?.start()
        }
        player = mp
        imgPlayPause?.setImageResource(R.mipmap.media_pause)
        TimerUtil.getInstance().start(object : TimerUtil.PeriodListenner {
            override fun onPeriodListenner() {
                try {
                    val pos: Long? = player?.getCurrentPosition()
                    val duration: Long? = player?.getDuration()
                    if (duration != null && pos != null) {
                        seekBar?.progress = ((pos / duration.toFloat()) * 100).toInt()
                        tvCurrentPosition?.text = Util.hhmmss(pos)
                        tvDuration?.text = Util.hhmmss(duration)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    override fun onError(mp: AbsMediaCore, what: Int?, extra: Int?): Boolean {
        TimerUtil.getInstance().stop()
        return super.onError(mp, what, extra)
    }

    override fun onBufferingUpdate(mp: AbsMediaCore, percent: Int) {
        super.onBufferingUpdate(mp, percent)
        seekBar?.secondaryProgress = ((percent.toFloat() / 100f) * player?.getDuration()?.toFloat()!!).toInt()
    }

    override fun onCompletion(mp: AbsMediaCore) {
        super.onCompletion(mp)
        TimerUtil.getInstance().stop()
    }

    override fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
        return super.onInfo(mp, what, extra)
    }

    override fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int) {
        super.onVideoSizeChanged(mp, width, height, sar_num, sar_den)
    }

    override fun onTimedText(mp: AbsMediaCore, text: String) {
        super.onTimedText(mp, text)
    }

    override fun onClickListener() {
        super.onClickListener()
        if (controlView?.visibility == View.GONE) {
            controlView?.visibility = View.VISIBLE
        } else {
            controlView?.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        TimerUtil.getInstance().stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        TimerUtil.getInstance().stop()
    }

    override fun onRestart() {
        super.onRestart()
        videoView?.reStart()
    }
}