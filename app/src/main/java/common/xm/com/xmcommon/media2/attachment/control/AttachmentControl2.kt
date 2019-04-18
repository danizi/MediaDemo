package common.xm.com.xmcommon.media2.attachment.control

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media2.attachment.BaseAttachmentView
import common.xm.com.xmcommon.media2.base.IXmMediaPlayer
import common.xm.com.xmcommon.media2.base.XmMediaPlayer
import common.xm.com.xmcommon.media2.base.XmVideoView
import common.xm.com.xmcommon.media2.event.PlayerObserver
import common.xm.com.xmcommon.media2.log.BKLog
import common.xm.com.xmcommon.media2.utils.ScreenUtil

class AttachmentControl2(context: Context?) : BaseAttachmentView(context) {

    private var controlViewHolder: ControlViewHolder? = null
    private val period: Int = 1000
    private val delay: Int = 5000

    companion object {
        const val TAG = "AttachmentControl"
        const val PORTRAIT = "portrait"
        const val LANDSCAPE = "landscape"
    }

    private var horizontalSlidePos: Long = -1  //记录手指滑动时，第一次播放进度。主要在回调中使用
    override fun onDownUp() {
        super.onDownUp()
        horizontalSlidePos = -1
        //手指滑动设置进度释放处理进度
        if (controlViewHolder?.isHorizontalSlide!!) {
            controlViewHolder?.isHorizontalSlide = false
            controlViewHolder?.hideControlView()
            controlViewHolder?.horizontalSlideStopSeekTo()
            controlViewHolder?.progressTimerStart(period.toLong())
            controlViewHolder?.horizontalSlideStopSeekTo()
        }

        if (controlViewHolder?.isClick!!) {
            controlViewHolder?.isClick = false
            controlViewHolder?.startDelayTimerHideControlView(delay)
        }
    }

    init {
        observer = object : PlayerObserver {
            override fun onScaleEnd(mediaPlayer: XmMediaPlayer?, scaleFactor: Float) {
                super.onScaleEnd(mediaPlayer, scaleFactor)
                if (context == null) {
                    BKLog.e(TAG, "context is null")
                    return
                }

                if (ScreenUtil.isLandscape(context)) {

                }
            }

            override fun onPrepared(mp: IXmMediaPlayer) {
                super.onPrepared(mp)
                xmVideoView?.bringChildToFront(this@AttachmentControl2)
            }

            override fun onClick(mediaPlayer: XmMediaPlayer?) {
                super.onClick(mediaPlayer)
                controlViewHolder?.isClick = true
                controlViewHolder?.showOrHideControlView()
            }

            override fun onSeekComplete(mp: IXmMediaPlayer) {
                super.onSeekComplete(mp)
                controlViewHolder?.hideLoading()
            }

            override fun onInfo(mp: IXmMediaPlayer, what: Int, extra: Int) {
                super.onInfo(mp, what, extra)
                if (what == IXmMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    controlViewHolder?.setActionResID(R.mipmap.media_control_pause)
                    controlViewHolder?.progressTimerStart(period.toLong())
                }
                when (what) {
                    IXmMediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                        controlViewHolder?.showLoading()
                    }
                    IXmMediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                        controlViewHolder?.hideLoading()
                    }
                }
            }

            override fun onBufferingUpdate(mp: IXmMediaPlayer, percent: Int) {
                super.onBufferingUpdate(mp, percent)
                controlViewHolder?.secondaryProgress(percent)
            }


            @SuppressLint("SetTextI18n")
            override fun onHorizontal(mediaPlayer: XmMediaPlayer?, present: Int) {
                super.onHorizontal(mediaPlayer, present)
                //记录手指滑动时播放器的位置 ps:只记录一次
                if (horizontalSlidePos == -1L) {
                    horizontalSlidePos = xmVideoView?.mediaPlayer?.getCurrentPosition()!!
                }
                //手指处于水平滑动中
                controlViewHolder?.isHorizontalSlide = true

                //停止所有的计时器
                controlViewHolder?.stopDelayTimerHideControlView()
                controlViewHolder?.progressTimerStop()

                //显示控制器界面
                val slidePresent = horizontalSlidePos.toInt() + present * 1000//转化毫秒
                mediaPlayer?.getCurrentPosition()!!
                controlViewHolder?.updateProgress(slidePresent.toLong()) // ps: 更新进度条 播放进度文本
                controlViewHolder?.showProgress()
                controlViewHolder?.showControlView()
            }
        }

    }

    override fun layouId(): Int {
        return R.layout.attachment_control
    }

    override fun unBind() {}

    override fun bind(xmVideoView: XmVideoView) {
        this.xmVideoView = xmVideoView

        val portraitView = getView(R.layout.attachment_control_portrait)
        val landscapeView = getView(R.layout.attachment_control_landscape)
        portraitView.visibility = View.GONE
        landscapeView.visibility = View.GONE
        addView(portraitView, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        addView(landscapeView, LayoutParams(MATCH_PARENT, MATCH_PARENT))

        controlViewHolder = PortraitViewHolder2.create(portraitView)
        controlViewHolder?.playResID = R.mipmap.media_control_play
        controlViewHolder?.pauseResID = R.mipmap.media_control_pause
        controlViewHolder?.bind(this)
    }
}