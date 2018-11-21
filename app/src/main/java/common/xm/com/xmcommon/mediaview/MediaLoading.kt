package common.xm.com.xmcommon.mediaview

import android.content.Context
import android.util.Log
import com.xm.lib.media.watcher.MediaViewObservable
import com.xm.lib.media.watcher.Observer

class MediaLoading(context: Context, layoutID: Int) : MediaViewObservable(context), Observer {

    init {
        contentView = getContentView(layoutID)
        addView(contentView)
        hide()
    }

    override fun update(o: MediaViewObservable, vararg arg: Any) {
        Log.e("MediaLoading", "MediaLoading 观察者接受：" + arg[0])
        // 控件事件   ：点击了播放按钮，加载控件显示
        // 播放器事件 ：资源缓存过程中，加载控件显示
        // 播放器事件 ：资源缓冲完成，加载控件隐藏
    }
}