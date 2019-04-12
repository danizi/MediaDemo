package common.xm.com.xmcommon.media2.event

import common.xm.com.xmcommon.media2.base.XmMediaPlayer

interface PlayerObserver {
    fun onPrepared(mp: XmMediaPlayer)
}