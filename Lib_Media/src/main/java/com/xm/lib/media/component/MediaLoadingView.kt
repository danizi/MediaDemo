package com.xm.lib.media.component

import android.annotation.SuppressLint
import android.content.Context
import com.xm.lib.media.contract.MediaLoadingViewContract
import com.xm.lib.media.watcher.MediaViewObservable

@SuppressLint("ViewConstructor")
class MediaLoadingView(context: Context?, layoutID: Int?) : MediaViewObservable<MediaLoadingViewContract.Present>(context!!, layoutID!!), MediaLoadingViewContract.View {

    override fun createPresent(): MediaLoadingViewContract.Present {
        return MediaLoadingViewContract.Present(context, this)
    }

    override fun getView(): MediaLoadingView {
        return this
    }

}