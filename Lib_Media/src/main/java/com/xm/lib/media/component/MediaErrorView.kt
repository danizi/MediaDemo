package com.xm.lib.media.component

import android.content.Context
import android.view.View
import com.xm.lib.media.event.Event
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.media.mediaview.contract.MediaErrorViewContract

class MediaErrorView(context: Context?, layoutID: Int?) : MediaViewObservable<MediaErrorViewContract.Present>(context!!, layoutID!!), MediaErrorViewContract.View {

    override fun getView(): MediaErrorView {
        return this
    }

    override fun createPresent(): MediaErrorViewContract.Present {
        return MediaErrorViewContract.Present(context, this)
    }
    
}
