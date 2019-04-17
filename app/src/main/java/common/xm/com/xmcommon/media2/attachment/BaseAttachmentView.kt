package common.xm.com.xmcommon.media2.attachment

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media2.base.IXmMediaPlayer
import common.xm.com.xmcommon.media2.base.XmVideoView
import common.xm.com.xmcommon.media2.event.PlayerObserver

abstract class BaseAttachmentView : FrameLayout {

    var observer: PlayerObserver? = null  //观察者
    var xmVideoView: XmVideoView? = null  //播放实例
    var view: View? = null //当前页面

    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)

    init {
        try {
            view = getView(layouId())
            addView(view)
            findViews(view)
            initDisplay()
            initEvent()
            initData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getView(layoutID: Int): View {
        return LayoutInflater.from(context).inflate(layoutID, null, false)
    }

    abstract fun layouId(): Int

    open fun findViews(view: View?) {}

    open fun initDisplay() {}

    open fun initEvent() {}

    open fun initData() {}

    open fun bind(xmVideoView: XmVideoView) {}

    open fun unBind() {}

    open fun onDownUp() {}

    open fun onDown() {}
}