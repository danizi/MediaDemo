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
class LandscapeViewHolder2 : ControlViewHolder {


    companion object {
        fun create(rootView: View?): LandscapeViewHolder2 {
            val clLandscapeTop = rootView?.findViewById<View>(R.id.cl_landscape_top) as ConstraintLayout
            val ivBack = rootView.findViewById<View>(R.id.iv_back) as ImageView
            val tvTitle = rootView.findViewById<View>(R.id.tv_title) as TextView
            val ivMore = rootView.findViewById<View>(R.id.iv_more) as ImageView
            val clLandscapeBottom = rootView.findViewById<View>(R.id.cl_landscape_bottom) as ConstraintLayout
            val seekBar = rootView.findViewById<View>(R.id.seekBar) as SeekBar
            val ivAction = rootView.findViewById<View>(R.id.iv_action) as ImageView
            val tvTime = rootView.findViewById<View>(R.id.tv_time) as TextView
            val tvRatio = rootView.findViewById<View>(R.id.tv_ratio) as TextView
            return LandscapeViewHolder2(clLandscapeTop, ivBack, tvTitle, ivMore, clLandscapeBottom, seekBar, ivAction, tvTime, tvRatio)
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
        }
    }

    override fun bind(attachmentControl: AttachmentControl2?) {
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showBottom() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideTop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideBottom() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateProgress(pos: Long, isSetProgress: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun horizontalSlideStopSeekTo() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setActionResID(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun secondaryProgress(present: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun progress(present: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
