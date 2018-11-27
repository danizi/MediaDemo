package common.xm.com.xmcommon.media.mediaview.component

import android.content.Context
import com.xm.lib.media.event.Event
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.media.mediaview.contract.MediaErrorViewContract

class MediaErrorView(context: Context?, layoutID: Int?) : MediaViewObservable<MediaErrorViewContract.Present>(context!!, layoutID!!) ,MediaErrorViewContract.View{

    override fun showView() {
    }

    override fun hideView() {
    }

    override fun getView(): MediaErrorView {
        return this
    }


    override fun createPresent(): MediaErrorViewContract.Present {
        return MediaErrorViewContract.Present(context,this)

    }

    override fun findViews() {
    }

    override fun initListenner() {
    }

    override fun initData() {
    }

    init {
        contentView = getContentView(layoutID!!)
        addView(contentView)
        hide()
    }

    override fun update(o: MediaViewObservable<*>, event: Event) {

    }
}
