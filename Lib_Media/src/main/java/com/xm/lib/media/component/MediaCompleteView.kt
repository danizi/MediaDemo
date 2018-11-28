package com.xm.lib.media.component

import android.content.Context
import com.xm.lib.media.event.Event
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.media.mediaview.contract.MediaCompleteViewContract

class MediaCompleteView(context: Context?, layoutID: Int?) : MediaViewObservable<MediaCompleteViewContract.Present>(context!!, layoutID!!),MediaCompleteViewContract.View {
    override fun showView() {
    }

    override fun hideView() {
    }

    override fun getView(): MediaCompleteView {
        return this
    }

    override fun createPresent(): MediaCompleteViewContract.Present {
        return MediaCompleteViewContract.Present(context,this)
    }

    override fun findViews() {

    }

    override fun initListenner() {

    }

    override fun initData() {

    }

    override fun update(o: MediaViewObservable<*>, event: Event) {

    }
}
