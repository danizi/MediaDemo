package common.xm.com.xmcommon.media.mediaview

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.bangke.lib.common.utils.ToolUtil
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.enum_.EnumMediaState
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable
import com.xm.lib.media.watcher.Observer
import common.xm.com.xmcommon.R
import java.util.*

class MediaControlView(context: Context, layoutID: Int) : MediaViewObservable(context), Observer {
    private var media: AbsMediaCore? = null
    private var prepared: Boolean? = false
    private var imgPlayPause: ImageView? = null
    private var tvCurrentPosition: TextView? = null
    private var seekBar: SeekBar? = null
    private var tvDuration: TextView? = null
    private var imgScreenMode: ImageView? = null
    private var curScreenMode: String = EventConstant.VALUE_SCREEN_SMALL

    init {
        contentView = getContentView(layoutID)
        addView(contentView)
        hide()

        imgPlayPause = contentView?.findViewById(R.id.img_play_pause)
        tvCurrentPosition = contentView?.findViewById(R.id.tv_currentPosition)
        seekBar = contentView?.findViewById(R.id.seekBar)
        tvDuration = contentView?.findViewById(R.id.tv_duration)
        imgScreenMode = contentView?.findViewById(R.id.img_screen_mode)

        imgPlayPause?.setOnClickListener {
            if (media?.playerState == EnumMediaState.PLAYING) {
                media?.pause()
                imgPlayPause?.setImageResource(R.mipmap.media_play)
            } else {
                media?.start()
                imgPlayPause?.setImageResource(R.mipmap.media_pause)
            }
        }

        imgScreenMode?.setOnClickListener {
            if (curScreenMode == EventConstant.VALUE_SCREEN_SMALL) {
                notifyObservers(Event().setEventType(EnumMediaEventType.VIEW).setParameter(EventConstant.KEY_SCREEN_MODE, EventConstant.VALUE_SCREEN_FULL))
                curScreenMode = EventConstant.VALUE_SCREEN_FULL
            } else if (curScreenMode.equals(EventConstant.VALUE_SCREEN_FULL)) {
                notifyObservers(Event().setEventType(EnumMediaEventType.VIEW).setParameter(EventConstant.KEY_SCREEN_MODE, EventConstant.VALUE_SCREEN_SMALL))
                curScreenMode = EventConstant.VALUE_SCREEN_SMALL
            }
        }
    }

    override fun update(o: MediaViewObservable, event: Event) {
        /*
         * 外部事件   ：横竖切屏,网络状态
         * 控件事件   ：点击一次显示，再点击一次隐藏(如果不点击开启计时器自定隐藏)
         * 播放器事件 ：播放状态
         */
        if (event.eventType == EnumMediaEventType.MEDIA) {

            if (media != event.parameter?.get("mp") as AbsMediaCore?) {
                media = event.parameter?.get("mp") as AbsMediaCore?
            }

            when (event.parameter?.get(EventConstant.KEY_METHOD)) {
                "onPrepared" -> {
                    prepared = true
                    if (true) {
                        //开启计时器了
                        seekBar?.max = media?.getDuration()!!.toInt()
                        tvDuration?.text = ToolUtil.formatTime(media?.getDuration())

                        var count = 0
                        var timer = Timer()
                        var timerTask = object : TimerTask() {
                            override fun run() {
                                seekBar?.post {
                                    try {
                                        tvCurrentPosition?.text = ToolUtil.formatTime(media?.getCurrentPosition())
                                        seekBar?.progress = media?.getCurrentPosition()!!.toInt()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }
                        timer.schedule(timerTask, 0, 1000)
                    }
                }
                "onError" -> {
                    prepared = false
                }
                "onCompletion" -> {
                    prepared = false
                }
            }
        }

        if (event.eventType == EnumMediaEventType.VIEW) {
            when (event.parameter?.get(EventConstant.KEY_METHOD)) {
                "onTouchEvent" -> {
                    setVisibity(event)
                }
            }
        }
    }

    private fun setVisibity(event: Event) {
        var motionEvent = event.parameter?.get("event") as MotionEvent
        if (motionEvent.action == MotionEvent.ACTION_DOWN && prepared!!) {
            visibility = if (visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }
}