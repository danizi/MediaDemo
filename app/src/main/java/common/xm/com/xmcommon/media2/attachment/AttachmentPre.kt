package common.xm.com.xmcommon.media2.attachment

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media2.base.IXmMediaPlayer
import common.xm.com.xmcommon.media2.base.XmVideoView
import common.xm.com.xmcommon.media2.event.PlayerObserver

class AttachmentPre(context: Context?) : ConstraintLayout(context), PlayerObserver {
    init {
        val preView = LayoutInflater.from(context).inflate(R.layout.attachment_pre, null, false)
    }

    override fun onPrepared(mp: IXmMediaPlayer) {

    }
}