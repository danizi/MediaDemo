package common.xm.com.xmcommon.mediaview

import android.content.Context
import android.util.Log
import com.xm.lib.media.watcher.MediaViewObservable
import com.xm.lib.media.watcher.Observer

class MediaControlView(context: Context, layoutID: Int) : MediaViewObservable(context), Observer {

    init {
        contentView = getContentView(layoutID)
        addView(contentView)
        hide()
    }

    override fun update(o: MediaViewObservable, vararg arg: Any) {
        Log.e("MediaControlView", "MediaControlView 观察者接受：" + arg[0])
        /*
         * 外部事件   ：横竖切屏,网络状态
         * 控件事件   ：点击一次显示，再点击一次隐藏(如果不点击开启计时器自定隐藏)
         * 播放器事件 ：播放状态
         */
    }
}