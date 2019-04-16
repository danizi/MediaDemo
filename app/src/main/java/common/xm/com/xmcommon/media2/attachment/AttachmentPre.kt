package common.xm.com.xmcommon.media2.attachment

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media2.base.IXmMediaPlayer
import common.xm.com.xmcommon.media2.base.XmVideoView
import common.xm.com.xmcommon.media2.event.PlayerObserver

class AttachmentPre(context: Context?) : BaseAttachmentView(context!!) {


    private var ivPre: ImageView? = null
    private var ivStart: ImageView? = null
    private var pbLoading: ProgressBar? = null
    var preUrl: String = ""
    var url: String = ""

    init {
        observer = object : PlayerObserver {
            override fun onPrepared(mp: IXmMediaPlayer) {
                super.onPrepared(mp)
                xmVideoView?.mediaPlayer?.start()
                xmVideoView?.unBindAttachmentView(this@AttachmentPre)
            }
        }
    }

    override fun bind(xmVideoView: XmVideoView) {
//        this.xmVideoView = xmVideoView
//        this.xmVideoView?.addView(this)
    }

    override fun unBind() {
//        this.xmVideoView = null
//        this.xmVideoView?.removeView(this)
    }

    override fun layouId(): Int {
        return R.layout.attachment_pre
    }

    override fun findViews(view: View?) {
        ivPre = view?.findViewById(R.id.iv_pre)
        ivStart = view?.findViewById(R.id.iv_start)
        pbLoading = view?.findViewById(R.id.pb_loading)
    }

    override fun initEvent() {
        ivStart?.setOnClickListener {
            if (ivStart?.visibility == View.VISIBLE) {
                ivStart?.visibility = View.GONE
                pbLoading?.visibility = View.VISIBLE
                xmVideoView?.start(url, true) //播放视频
            }
        }
    }

    override fun initDisplay() {
        ivPre?.visibility = View.VISIBLE
        ivStart?.visibility = View.VISIBLE
        pbLoading?.visibility = View.GONE
    }

    fun setCover() {
        Glide.with(context).load(preUrl).into(ivPre)//加载图片
    }

    override fun initData() {

    }
}