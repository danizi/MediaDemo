package com.xm.lib.media.core.attach

import android.content.Context
import android.widget.FrameLayout
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.attach.factory.MediaAttachLayoutFactory

/**
 * 播放器附着完成页面
 */
class AttachCompleteLayout(context: Context, layout: Int) : FrameLayout(context), MediaAttachLayoutFactory.IAttachLayout {
    override fun setPlayer(player: AbsMediaCore?) {
    }
}