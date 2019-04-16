package common.xm.com.xmcommon.media2.attachment

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media2.base.IXmMediaPlayer
import common.xm.com.xmcommon.media2.base.XmVideoView
import common.xm.com.xmcommon.media2.event.PlayerObserver

class AttachmentLoading(context: Context?) : BaseAttachmentView(context) {

    var pb: ProgressBar? = null
    var tvDes: TextView? = null

    init {
        observer = object : PlayerObserver {
            override fun onInfo(mp: IXmMediaPlayer, what: Int, extra: Int) {
                super.onInfo(mp, what, extra)
                when (what) {
                    701 -> {
                        pb?.visibility = View.VISIBLE
                        tvDes?.visibility = View.VISIBLE
                        xmVideoView?.bindAttachmentView(this@AttachmentLoading)
                    }
                    702 -> {
                        xmVideoView?.unBindAttachmentView(this@AttachmentLoading)
                    }
                }
            }
        }
    }

    override fun bind(xmVideoView: XmVideoView) {
        this.xmVideoView = xmVideoView
//        this.xmVideoView?.addView(this)
    }

    override fun unBind() {
//        this.xmVideoView = null
//        this.xmVideoView?.removeView(this)
    }

    override fun layouId(): Int {
        return R.layout.attachment_loading
    }

    override fun findViews(view: View?) {
        pb = view?.findViewById(R.id.pb)
        tvDes = view?.findViewById(R.id.tv_des)
    }

    override fun initDisplay() {
        pb?.visibility = View.GONE
        tvDes?.visibility = View.GONE
    }

    override fun initEvent() {

    }

    override fun initData() {

    }

}