package common.xm.com.xmcommon.media.mediaview.component

import android.content.Context
import android.widget.ImageView
import android.widget.ProgressBar
import com.xm.lib.media.event.Event
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media.mediaview.contract.MediaGestureProgressViewContract

class MediaGestureProgressView(context: Context, layoutID: Int) : MediaViewObservable<MediaGestureProgressViewContract.Present>(context, layoutID), MediaGestureProgressViewContract.View {

    var iv: ImageView? = null
    var progress: ProgressBar? = null


    override fun createPresent(): MediaGestureProgressViewContract.Present {
        return MediaGestureProgressViewContract.Present(context, this)
    }
    override fun findViews() {
        iv =  contentView?.findViewById(R.id.iv)
        progress =  contentView?.findViewById(R.id.progress)
    }

    override fun initListenner() {

    }

    override fun initData() {
        getPresent()?.process()
    }

    override fun update(o: MediaViewObservable<*>, event: Event) {
        getPresent()?.handleReceiveEvent(o, event)
    }

    override fun showView() {
        show()
    }

    override fun hideView() {
        hide()
    }

    override fun getView(): MediaGestureProgressView {
        return this
    }
}