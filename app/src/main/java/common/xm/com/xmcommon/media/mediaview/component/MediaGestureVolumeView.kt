package common.xm.com.xmcommon.media.mediaview.component

import android.content.Context
import android.widget.ImageView
import android.widget.ProgressBar
import com.xm.lib.media.event.Event
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media.mediaview.contract.MediaGestureVolumeViewContract

class MediaGestureVolumeView(context: Context?, layoutID: Int?) : MediaViewObservable<MediaGestureVolumeViewContract.Present>(context!!,layoutID!!), MediaGestureVolumeViewContract.View {

    var iv: ImageView? = null
    var progress: ProgressBar? = null

    override fun createPresent(): MediaGestureVolumeViewContract.Present {
        return MediaGestureVolumeViewContract.Present(context, this)
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

    override fun getView(): MediaGestureVolumeView {
        return this
    }
}
