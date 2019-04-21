package common.xm.com.xmcommon.media2.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class XmMediaPlayerService : Service() {

    var binder: XmMediaPlayerBinder? = null

    override fun onBind(intent: Intent?): IBinder? {
        if (binder == null)
            binder = XmMediaPlayerBinder(this)
        return binder
    }

    class XmMediaPlayerBinder(private val xmMediaPlayerService: XmMediaPlayerService) : Binder() {

        fun getService(): XmMediaPlayerService {
            return xmMediaPlayerService
        }

    }

}