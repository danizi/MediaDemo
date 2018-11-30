package com.xm.lib.media.component

import android.content.Context
import com.xm.lib.media.event.Event
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.media.mediaview.contract.MediaLoadingViewContract

class MediaLoadingView(context: Context?, layoutID: Int?) : MediaViewObservable<MediaLoadingViewContract.Present>(context!!, layoutID!!), MediaLoadingViewContract.View {

    override fun createPresent(): MediaLoadingViewContract.Present {
        return MediaLoadingViewContract.Present(context, this)
    }

    override fun getView(): MediaLoadingView {
        return this
    }

}