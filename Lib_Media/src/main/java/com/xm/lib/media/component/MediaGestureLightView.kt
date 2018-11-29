package com.xm.lib.media.component

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.xm.lib.media.R
import com.xm.lib.media.event.Event
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.media.mediaview.contract.MediaGestureLightViewContract

class MediaGestureLightView(context: Context?, layoutID: Int?) : MediaViewObservable<MediaGestureLightViewContract.Present>(context!!,layoutID!!), MediaGestureLightViewContract.View {

    var iv: ImageView? = null
    var progress: ProgressBar? = null

    override fun createPresent(): MediaGestureLightViewContract.Present {
        return MediaGestureLightViewContract.Present(context, this)
    }

    override fun findViews(contentView: View?) {
        iv =  contentView?.findViewById(R.id.iv)
        progress =  contentView?.findViewById(R.id.progress)
    }

    override fun initData() {
        getPresent()?.process()
    }

    override fun getView(): MediaGestureLightView {
        return this
    }

}
