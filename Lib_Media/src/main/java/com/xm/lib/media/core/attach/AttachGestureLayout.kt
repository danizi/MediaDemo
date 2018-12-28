package com.xm.lib.media.core.attach

import android.content.Context
import android.widget.FrameLayout
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.attach.factory.MediaAttachLayoutFactory

/**
 * 播放器附着手势页面
 */
class AttachGestureLayout(context: Context, layout: Int) : FrameLayout(context), MediaAttachLayoutFactory.IAttachLayout {
    override fun setPlayer(player: AbsMediaCore?) {
    }
}