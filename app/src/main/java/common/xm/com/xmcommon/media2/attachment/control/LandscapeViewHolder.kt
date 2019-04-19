package common.xm.com.xmcommon.media2.attachment.control

import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media2.log.BKLog
import common.xm.com.xmcommon.media2.utils.ScreenUtil

/**
 * 横屏界面
 */
class LandscapeViewHolder : ControlViewHolder {


    companion object {
        fun create(rootView: View?): LandscapeViewHolder {
            val clLandscapeTop = rootView?.findViewById<View>(R.id.cl_landscape_top) as ConstraintLayout
            val ivBack = rootView.findViewById<View>(R.id.iv_back) as ImageView
            val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
            val ivMore = rootView.findViewById<View>(R.id.iv_more) as ImageView
            val clLandscapeBottom = rootView.findViewById<View>(R.id.cl_landscape_bottom) as ConstraintLayout
            val seekBar = rootView.findViewById<View>(R.id.seekBar) as SeekBar
            val ivAction = rootView.findViewById<View>(R.id.iv_action) as ImageView
            val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
            val tvRatio = rootView.findViewById<View>(R.id.tv_ratio) as TextView
            return LandscapeViewHolder(clLandscapeTop, ivBack, tvTitle, ivMore, clLandscapeBottom, seekBar, ivAction, tvTime, tvRatio)
        }
    }

    private var clLandscapeTop: ConstraintLayout? = null
    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private var ivMore: ImageView? = null
    private var clLandscapeBottom: ConstraintLayout? = null
    private var seekBar: SeekBar? = null
    private var ivAction: ImageView? = null
    private var tvTime: TextView? = null
    private var tvRatio: TextView? = null

    private constructor(clLandscapeTop: ConstraintLayout, ivBack: ImageView, tvTitle: TextView, ivMore: ImageView, clLandscapeBottom: ConstraintLayout, seekBar: SeekBar, ivAction: ImageView, tvTime: TextView, tvRatio: TextView) {
        this.clLandscapeTop = clLandscapeTop
        this.ivBack = ivBack
        this.tvTitle = tvTitle
        this.ivMore = ivMore
        this.clLandscapeBottom = clLandscapeBottom
        this.seekBar = seekBar
        this.ivAction = ivAction
        this.tvTime = tvTime
        this.tvRatio = tvRatio
    }

    private fun initEvent() {
        ivBack?.setOnClickListener {
            BKLog.d(TAG, "Landscape -> Portrait")
            // 横屏高度 > 宽度
            //设置竖屏
            ScreenUtil.setPortrait(activity)
            //显示系统状态栏
            ScreenUtil.setDecorVisible(activity)
            //设置宽高
            if (portraitXmVideoView == null) {
                xmVideoView?.layout(portraitXmVideoView?.left!!, portraitXmVideoView?.top!!, portraitXmVideoView?.right!!, portraitXmVideoView?.bottom!!)
            } else {
                BKLog.e(TAG, "请给portraitXmVideoView属性赋值")
            }
            hideControlView()
            listener?.onState(AttachmentControl.PORTRAIT)
        }
    }

    override fun bind(attachmentControl: AttachmentControl?) {
        super.bind(attachmentControl)
        initEvent()
    }

    override fun showOrHideControlView() {
        if (clLandscapeBottom?.visibility == View.VISIBLE) {
            hideControlView()
        } else {
            showControlView()
        }
    }

    override fun showProgress() {
    }

    override fun showLoading() {
    }

    override fun showTop() {
    }

    override fun showBottom() {
    }

    override fun hideLoading() {
    }

    override fun hideTop() {
    }

    override fun hideBottom() {
    }

    override fun hideProgress() {
    }

    override fun updateProgress(pos: Long, isSetProgress: Boolean) {
    }

    override fun horizontalSlideStopSeekTo() {
    }

    override fun setActionResID(id: Int) {
    }

    override fun secondaryProgress(present: Int) {
    }

    override fun progress(present: Int) {
    }
}
