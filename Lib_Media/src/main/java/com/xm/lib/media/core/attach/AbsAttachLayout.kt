package com.xm.lib.media.core.attach

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * 控制器
 */
open class AbsAttachLayout : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}