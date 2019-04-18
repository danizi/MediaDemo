package common.xm.com.xmcommon.media2.attachment.control

import android.app.Activity
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media2.base.XmMediaPlayer
import common.xm.com.xmcommon.media2.base.XmVideoView
import common.xm.com.xmcommon.media2.utils.ScreenUtil

@Deprecated("")
class LandscapeViewHolder private constructor(val attachmentControl: AttachmentControl?, val clLandscapeTop: ConstraintLayout, val ivBack: ImageView, val tvTitle: TextView, val ivMore: ImageView, val clLandscapeBottom: ConstraintLayout, val seekBar: SeekBar, val ivAction: ImageView, val tvTime: TextView, val tvRatio: TextView) {
    companion object {

        fun create(attachmentControl: AttachmentControl?, rootView: View?): LandscapeViewHolder {
            val clLandscapeTop = rootView?.findViewById<View>(R.id.cl_landscape_top) as ConstraintLayout
            val ivBack = rootView.findViewById<View>(R.id.iv_back) as ImageView
            val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
            val ivMore = rootView.findViewById<View>(R.id.iv_more) as ImageView
            val clLandscapeBottom = rootView.findViewById<View>(R.id.cl_landscape_bottom) as ConstraintLayout
            val seekBar = rootView.findViewById<View>(R.id.seekBar) as SeekBar
            val ivAction = rootView.findViewById<View>(R.id.iv_action) as ImageView
            val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
            val tvRatio = rootView.findViewById<View>(R.id.tv_ratio) as TextView
            return LandscapeViewHolder(attachmentControl, clLandscapeTop, ivBack, tvTitle, ivMore, clLandscapeBottom, seekBar, ivAction, tvTime, tvRatio)
        }

    }


    private var activity: Activity? = null
    private var mediaPlayer: XmMediaPlayer? = null
    private var xmVideoView: XmVideoView? = null
    private var screenW = 0
    private var screenH = 0
    private var controlHelper: ControlHelper? = null
    var isHorizontalSlide = false
    var isClick = false

    init {
        controlHelper = attachmentControl?.controlHelper
        screenW = ScreenUtil.getNormalWH(activity)[0]
        screenH = ScreenUtil.getNormalWH(activity)[1]
    }

    fun initEvent() {
        ivBack.setOnClickListener {
            controlHelper?.clickScreenSwitch(activity, xmVideoView)
            hide()
            attachmentControl?.portraitViewHolder?.hide()
        }
    }

    fun showAndHide() {
        if (ScreenUtil.isLandscape(activity!!)) {
            if (clLandscapeBottom.visibility == View.VISIBLE) {
                show()
            } else {
                hide()
            }
        }
    }

    fun show() {
        clLandscapeTop.visibility = View.VISIBLE
        clLandscapeBottom.visibility = View.VISIBLE
    }

    fun hide() {
        clLandscapeTop.visibility = View.GONE
        clLandscapeBottom.visibility = View.GONE
    }

    fun bind(xmVideoView: XmVideoView) {
        activity = attachmentControl?.context as Activity
        mediaPlayer = attachmentControl.xmVideoView?.mediaPlayer
        this.xmVideoView = xmVideoView
    }

    fun showProgress(slidePresent: Int) {
    }

    fun hideProgress() {

    }
}
