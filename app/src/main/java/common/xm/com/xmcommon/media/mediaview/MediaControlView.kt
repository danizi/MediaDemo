package common.xm.com.xmcommon.media.mediaview

import android.content.Context
import android.view.MotionEvent
import android.view.View
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable
import com.xm.lib.media.watcher.Observer

class MediaControlView(context: Context, layoutID: Int) : MediaViewObservable(context), Observer {
    var media: AbsMediaCore? = null
    var prepared: Boolean? = false

    init {
        contentView = getContentView(layoutID)
        addView(contentView)
        hide()
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

            when (event.parameter?.get(EventConstant.METHOD)) {
                "onPrepared" -> {
                    prepared = true
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
            when (event.parameter?.get(EventConstant.METHOD)) {
                "onTouchEvent" -> {
                    setVisibity(event)
                }
            }
        }
    }

    private fun setVisibity(event: Event) {
        var motionEvent = event.parameter?.get("event") as MotionEvent
        if (motionEvent.action == MotionEvent.ACTION_DOWN && prepared!!) {
            if (visibility == View.VISIBLE) {
                visibility = View.GONE
            } else {
                visibility = View.VISIBLE
            }
        }
    }
}