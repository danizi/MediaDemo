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

@Deprecated("")
class AttachmentControl(context: Context?) : BaseAttachmentView(context) {
    var controlHelper: ControlHelper? = null
    var portraitViewHolder: PortraitViewHolder? = null
    var landscapeViewHolder: LandscapeViewHolder? = null
    private val period: Int = 1000
    private val delay: Int = 5000

    companion object {
        const val TAG = "AttachmentControl"
        const val PORTRAIT = "portrait"
        const val LANDSCAPE = "landscape"
    }

    private var horizontalSlidePos: Long = -1
    override fun onDownUp() {
        super.onDownUp()
        horizontalSlidePos = -1
        //手指滑动设置进度释放处理进度
        if (portraitViewHolder?.isHorizontalSlide!!) {
            portraitViewHolder?.hide()
            portraitViewHolder?.hideProgress()
            portraitViewHolder?.isHorizontalSlide = false

            portraitViewHolder?.clSeek?.visibility = View.GONE
            val seekPos = xmVideoView?.mediaPlayer?.getDuration()!! * (portraitViewHolder?.progress?.toFloat()!! / 100f)
            xmVideoView?.mediaPlayer?.seekTo(seekPos.toInt())
            portraitViewHolder?.pbLoading?.visibility = View.VISIBLE
            controlHelper?.progressTimerStart(xmVideoView, portraitViewHolder, landscapeViewHolder, period)
        }
        if (landscapeViewHolder?.isHorizontalSlide!!) {
            landscapeViewHolder?.hide()
            landscapeViewHolder?.hideProgress()
            landscapeViewHolder?.isHorizontalSlide = false
        }
        //定时隐藏控制器页面
        if (portraitViewHolder?.isClick!!) {
            controlHelper?.startDelayTimerHideControlView(portraitViewHolder, landscapeViewHolder, delay)
            portraitViewHolder?.isClick = false
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

            override fun onClick(mediaPlayer: XmMediaPlayer?) {
                super.onClick(mediaPlayer)
                portraitViewHolder?.isClick = true
                landscapeViewHolder?.isClick = true
                xmVideoView?.bringChildToFront(this@AttachmentControl)  //ps:可能会被其他控件置顶
                portraitViewHolder?.showAndHide()
                landscapeViewHolder?.showAndHide()
            }

            override fun onSeekComplete(mp: IXmMediaPlayer) {
                super.onSeekComplete(mp)
                portraitViewHolder?.pbLoading?.visibility = View.GONE
            }

            override fun onInfo(mp: IXmMediaPlayer, what: Int, extra: Int) {
                super.onInfo(mp, what, extra)
                if (what == IXmMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    portraitViewHolder?.ivAction?.setImageResource(R.mipmap.media_control_pause)
                    controlHelper?.progressTimerStart(xmVideoView, portraitViewHolder, landscapeViewHolder, period)
                }
                when (what) {
                    IXmMediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                        portraitViewHolder?.pbLoading?.visibility = View.VISIBLE
                    }
                    IXmMediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                        portraitViewHolder?.pbLoading?.visibility = View.GONE
                    }
                }
            }

            override fun onBufferingUpdate(mp: IXmMediaPlayer, percent: Int) {
                super.onBufferingUpdate(mp, percent)
                portraitViewHolder?.seekBar?.secondaryProgress = percent
                landscapeViewHolder?.seekBar?.secondaryProgress = percent
            }


            @SuppressLint("SetTextI18n")
            override fun onHorizontal(mediaPlayer: XmMediaPlayer?, present: Int) {
                super.onHorizontal(mediaPlayer, present)
                //记录手指滑动时播放器的位置 ps:只记录一次
                if (horizontalSlidePos == -1L) {
                    horizontalSlidePos = xmVideoView?.mediaPlayer?.getCurrentPosition()!!
                }
                //手指处于水平滑动中
                portraitViewHolder?.isHorizontalSlide = true
                landscapeViewHolder?.isHorizontalSlide = true

                //停止所有的计时器
                controlHelper?.stopDelayTimerHideControlView()
                controlHelper?.progressTimerStop()

                //显示控制器界面
                val slidePresent = horizontalSlidePos.toInt() + present * 1000//转化毫秒
                mediaPlayer?.getCurrentPosition()!!
                portraitViewHolder?.showProgress(slidePresent) //显示“竖屏”进度UI界面
                portraitViewHolder?.show() //显示“竖屏”控制界面

                landscapeViewHolder?.showProgress(slidePresent) //显示“横向”进度UI界面
                landscapeViewHolder?.show()//显示“横向”控制界面
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

        controlHelper = ControlHelper(portraitViewHolder, landscapeViewHolder)
        portraitViewHolder = PortraitViewHolder.create(this, portraitView)
        landscapeViewHolder = LandscapeViewHolder.create(this, landscapeView)

        portraitViewHolder?.bind(xmVideoView)
        landscapeViewHolder?.bind(xmVideoView)

        portraitViewHolder?.initEvent()
        landscapeViewHolder?.initEvent()


    }
}