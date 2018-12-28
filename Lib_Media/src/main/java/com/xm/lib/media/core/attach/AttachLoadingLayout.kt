package com.xm.lib.media.core.attach

import android.content.Context
import android.widget.FrameLayout
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.attach.factory.MediaAttachLayoutFactory

/**
 * 播放器附着加载页面
 */
class AttachLoadingLayout(context: Context, layout: Int) : FrameLayout(context), MediaAttachLayoutFactory.IAttachLayout {
    override fun setPlayer(player: AbsMediaCore?) {

    }
}