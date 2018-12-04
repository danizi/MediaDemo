package com.xm.lib.media.component.control

import android.content.Context
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.media.mediaview.contract.MediaControlViewContract

class MediaControlView(context: Context?, layoutID: Int?) : MediaViewObservable<MediaControlViewContract.Present>(context!!, layoutID!!), MediaControlViewContract.View {

    override fun createPresent(): MediaControlViewContract.Present {
        return MediaControlViewContract.Present(context, this)
    }

    override fun getView(): MediaControlView {
        return this
    }
}

