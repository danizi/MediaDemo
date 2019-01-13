package com.xm.lib.media.core.attach.layout

import android.content.Context
import android.util.AttributeSet
import com.xm.lib.media.core.AbsAttachLayout

/**
 * 播放器附着加载页面
 */
class AttachLoadingLayout: AbsAttachLayout {

    constructor(context: Context, layout: Int) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}