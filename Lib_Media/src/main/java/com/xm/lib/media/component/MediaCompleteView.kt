package com.xm.lib.media.component

import android.annotation.SuppressLint
import android.content.Context
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.media.mediaview.contract.MediaCompleteViewContract

@SuppressLint("ViewConstructor")
class MediaCompleteView(context: Context?, layoutID: Int?) : MediaViewObservable<MediaCompleteViewContract.Present>(context!!, layoutID!!), MediaCompleteViewContract.View {

    override fun getView(): MediaCompleteView {
        return this
    }

    override fun createPresent(): MediaCompleteViewContract.Present {
        return MediaCompleteViewContract.Present(context, this)
    }
}
