package com.xm.lib.media.component

import android.annotation.SuppressLint
import android.content.Context
import com.xm.lib.media.contract.MediaErrorViewContract
import com.xm.lib.media.watcher.MediaViewObservable

@SuppressLint("ViewConstructor")
class MediaErrorView(context: Context?, layoutID: Int?) : MediaViewObservable<MediaErrorViewContract.Present>(context!!, layoutID!!), MediaErrorViewContract.View {

    override fun getView(): MediaErrorView {
        return this
    }

    override fun createPresent(): MediaErrorViewContract.Present {
        return MediaErrorViewContract.Present(context, this)
    }
    
}
