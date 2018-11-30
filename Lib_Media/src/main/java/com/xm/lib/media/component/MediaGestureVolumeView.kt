package com.xm.lib.media.component

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.xm.lib.media.R
import com.xm.lib.media.event.Event
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.media.mediaview.contract.MediaGestureVolumeViewContract

class MediaGestureVolumeView(context: Context?, layoutID: Int?) : MediaViewObservable<MediaGestureVolumeViewContract.Present>(context!!,layoutID!!), MediaGestureVolumeViewContract.View {

    var iv: ImageView? = null
    var progress: ProgressBar? = null

    override fun createPresent(): MediaGestureVolumeViewContract.Present {
        return MediaGestureVolumeViewContract.Present(context, this)
    }

    override fun findViews(contentView: View?) {
        iv =  contentView?.findViewById(R.id.iv)
        progress =  contentView?.findViewById(R.id.progress)
    }

    override fun getView(): MediaGestureVolumeView {
        return this
    }
}
