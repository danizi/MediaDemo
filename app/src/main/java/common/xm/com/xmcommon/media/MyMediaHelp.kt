package common.xm.com.xmcommon.media

import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.MediaHelp

class MyMediaHelp(private var player: AbsMediaCore?) : MediaHelp() {

    override fun createPlayer(): AbsMediaCore {
        return player!!
    }
}