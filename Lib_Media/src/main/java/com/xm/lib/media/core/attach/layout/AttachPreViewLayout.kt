package com.xm.lib.media.core.attach.layout

import android.content.Context
import android.util.AttributeSet
import com.xm.lib.media.core.attach.AbsAttachLayout

/**
 * 播放器附着预览页面
 */
class AttachPreViewLayout : AbsAttachLayout {

    constructor(context: Context, layout: Int) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

}