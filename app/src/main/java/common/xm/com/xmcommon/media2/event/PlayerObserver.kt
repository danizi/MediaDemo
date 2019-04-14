package common.xm.com.xmcommon.media2.event

import common.xm.com.xmcommon.media2.base.IXmMediaPlayer

interface PlayerObserver {
    fun onPrepared(mp: IXmMediaPlayer)
}