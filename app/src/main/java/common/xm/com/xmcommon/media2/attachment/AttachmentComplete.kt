package common.xm.com.xmcommon.media2.attachment

import android.content.Context
import android.view.View
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media2.base.IXmMediaPlayer
import common.xm.com.xmcommon.media2.base.XmVideoView
import common.xm.com.xmcommon.media2.event.PlayerObserver

class AttachmentComplete(context: Context?) : BaseAttachmentView(context) {

    init {
        observer = object : PlayerObserver {
            override fun onCompletion(mp: IXmMediaPlayer) {
                super.onCompletion(mp)
                xmVideoView?.bringChildToFront(this@AttachmentComplete)
                this@AttachmentComplete.visibility = View.VISIBLE
            }

            override fun onPrepared(mp: IXmMediaPlayer) {
                super.onPrepared(mp)
                this@AttachmentComplete.visibility = View.GONE
            }
        }
    }

    override fun bind(xmVideoView: XmVideoView) {
        super.bind(xmVideoView)
        this.visibility = View.GONE
    }

    override fun layouId(): Int {
        return R.layout.attachment_complete
    }

}