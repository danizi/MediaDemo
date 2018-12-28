package com.xm.lib.media.core.attach

import android.content.Context
import android.widget.FrameLayout
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.attach.factory.MediaAttachLayoutFactory

/**
 * 播放器附着预览页面
 */
class AttachPreViewLayout(context: Context, layout: Int) : FrameLayout(context), MediaAttachLayoutFactory.IAttachLayout {
    override fun setPlayer(player: AbsMediaCore?) {
    }
}