package common.xm.com.xmcommon.media2.attachment

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media2.base.XmVideoView
import common.xm.com.xmcommon.media2.event.PlayerObserver

abstract class BaseAttachmentView : FrameLayout {

    var observer: PlayerObserver? = null  //观察者
    var xmVideoView: XmVideoView? = null  //播放实例

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    fun getView(layoutID: Int): View {
        return LayoutInflater.from(context).inflate(layoutID, null, false)
    }

    abstract fun unBind()

    abstract fun bind(xmVideoView: XmVideoView)

    abstract fun findViews(view: View?)

    abstract fun initDisplay()

    abstract fun initEvent()

    abstract fun initData()
}