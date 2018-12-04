package com.xm.lib.media.component

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.xm.lib.media.R
import com.xm.lib.media.contract.MediaGestureProgressViewContract
import com.xm.lib.media.watcher.MediaViewObservable

class MediaGestureProgressView(context: Context?, layoutID: Int?) : MediaViewObservable<MediaGestureProgressViewContract.Present>(context!!, layoutID!!), MediaGestureProgressViewContract.View {

    var iv: ImageView? = null
    var progress: ProgressBar? = null

    override fun createPresent(): MediaGestureProgressViewContract.Present {
        return MediaGestureProgressViewContract.Present(context, this)
    }
    override fun findViews(contentView: View?) {
        iv =  contentView?.findViewById(R.id.iv)
        progress =  contentView?.findViewById(R.id.progress)
    }

    override fun getView(): MediaGestureProgressView {
        return this
    }
}