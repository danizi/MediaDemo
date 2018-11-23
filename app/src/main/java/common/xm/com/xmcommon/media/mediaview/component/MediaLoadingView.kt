package common.xm.com.xmcommon.media.mediaview.component

import android.content.Context
import android.view.View
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable
import com.xm.lib.media.watcher.Observer

class MediaLoadingView(context: Context, layoutID: Int) : MediaViewObservable(context) {

    init {
        contentView = getContentView(layoutID)
        addView(contentView)
        hide()
    }

    override fun update(o: MediaViewObservable, event: Event) {
        // 控件事件   ：点击了播放按钮，加载控件显示
        // 播放器事件 ：资源缓存过程中，加载控件显示
        // 播放器事件 ：资源缓冲完成，加载控件隐藏
        if (event.eventType == EnumMediaEventType.VIEW) {
            if ("click" == event.parameter?.get(EventConstant.KEY_METHOD)) {
                visibility = View.VISIBLE
            }
        }

        if (event.eventType == EnumMediaEventType.MEDIA) {
            if ("onPrepared".equals(event.parameter?.get(EventConstant.KEY_METHOD))) {
                visibility = View.GONE
            }
        }
    }
}