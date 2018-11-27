package common.xm.com.xmcommon.media.mediaview.component

import android.content.Context
import android.widget.ImageView
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.event.Event
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media.mediaview.contract.MediaPreViewContract


class MediaPreView(context: Context?, layoutID: Int?, preUrl: String?) : MediaViewObservable<MediaPreViewContract.Present>(context!!, layoutID!!), MediaPreViewContract.View {
    var preUrl: String? = preUrl
    var ivPre: ImageView? = null
    var ivPlay: ImageView? = null

    override fun createPresent(): MediaPreViewContract.Present {
        return MediaPreViewContract.Present(context, this)
    }

    override fun findViews() {
        ivPre = contentView?.findViewById(R.id.img_preview)
        ivPlay = contentView?.findViewById(R.id.img_play)
    }

    override fun initListenner() {
        ivPre?.setOnClickListener {
            play()
        }
        ivPlay?.setOnClickListener {
            play()
        }
    }

    override fun getView(): MediaPreView {
        return this
    }

    override fun showView() {
        show()
    }

    override fun hideView() {
        hide()
    }

    override fun initData() {
        getPresent()?.model?.preUrl = "http://img.videocc.net/uimage/2/26de49f8c2/9/26de49f8c273bbc8f6812d1422a11b39_1.jpg"
        getPresent()?.process()
    }

    override fun update(o: MediaViewObservable<*>, event: Event) {
        getPresent()?.handleReceiveEvent(o, event)
    }

    private fun play() {
        getPresent()?.prepareAsync()
        hideView()
        getPresent()?.notifyObservers()
    }
}


