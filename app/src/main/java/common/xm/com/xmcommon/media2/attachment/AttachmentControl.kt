package common.xm.com.xmcommon.media2.attachment

import android.content.Context
import android.view.View
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media2.base.XmMediaPlayer
import common.xm.com.xmcommon.media2.base.XmVideoView
import common.xm.com.xmcommon.media2.event.PlayerObserver
import common.xm.com.xmcommon.media2.gesture.GestureHelp.Companion.VERTICAL_LEFT_VALUE
import common.xm.com.xmcommon.media2.gesture.GestureHelp.Companion.VERTICAL_RIGHT_VALUE
import common.xm.com.xmcommon.media2.log.BKLog
import common.xm.com.xmcommon.media2.utils.ScreenUtil

class AttachmentControl(context: Context?) : BaseAttachmentView(context) {
    private var landscapeView: View? = null
    private var portraitView: View? = null

    companion object {
        const val PORTRAIT = "portrait"
        const val LANDSCAPE = "landscape"
    }

    init {
        observer = object : PlayerObserver {
            override fun onScaleEnd(mediaPlayer: XmMediaPlayer?, scaleFactor: Float) {
                super.onScaleEnd(mediaPlayer, scaleFactor)
            }

            override fun onDoubleClick(mediaPlayer: XmMediaPlayer?) {
                super.onDoubleClick(mediaPlayer)
            }

            override fun onClick(mediaPlayer: XmMediaPlayer?) {
                super.onClick(mediaPlayer)
                if (context != null) {
                    if (ScreenUtil.isPortrait(context)) {
                        showAndHide(PORTRAIT)
                    } else {
                        showAndHide(LANDSCAPE)
                    }
                } else {
                    BKLog.e(TAG, "context is null")
                }
            }
        }
        portraitView = getView(R.layout.attachment_control_portrait)
        landscapeView = getView(R.layout.attachment_control_landscape)
    }

    override fun layouId(): Int {
        return R.layout.attachment_control
    }

    override fun unBind() {}

    override fun bind(xmVideoView: XmVideoView) {
        addView(portraitView)
        addView(landscapeView)
        portraitView?.visibility = View.GONE
        landscapeView?.visibility = View.GONE
    }

    override fun findViews(view: View?) {}

    override fun initDisplay() {}

    override fun initEvent() {}

    override fun initData() {}

    private fun showAndHide(type: String) {
        if (type == PORTRAIT) {
            if (portraitView?.visibility == View.VISIBLE) {
                hide(type)
            } else {
                show(type)
            }
        } else {
            if (landscapeView?.visibility == View.VISIBLE) {
                hide(type)
            } else {
                show(type)
            }
        }
    }

    private fun show(type: String) {
        if (type == PORTRAIT) {
            portraitView?.visibility = View.VISIBLE
            landscapeView?.visibility = View.GONE
        } else {
            portraitView?.visibility = View.GONE
            landscapeView?.visibility = View.VISIBLE
        }
    }

    private fun hide(type: String) {
        if (type == PORTRAIT) {
            portraitView?.visibility = View.GONE
            landscapeView?.visibility = View.VISIBLE
        } else {
            portraitView?.visibility = View.VISIBLE
            landscapeView?.visibility = View.GONE
        }
    }
}