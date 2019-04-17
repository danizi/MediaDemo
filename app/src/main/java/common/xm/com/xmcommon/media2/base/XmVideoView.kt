package common.xm.com.xmcommon.media2.base

import android.content.Context
import android.media.MediaPlayer
import android.media.SubtitleData
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.FrameLayout
import common.xm.com.xmcommon.media2.attachment.*
import common.xm.com.xmcommon.media2.base.XmMediaPlayer.Companion.TAG
import common.xm.com.xmcommon.media2.gesture.GestureHelp
import common.xm.com.xmcommon.media2.log.BKLog
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue

class XmVideoView : FrameLayout {

    var mediaPlayer: XmMediaPlayer? = null //播放器
    var attachmentViews: ConcurrentLinkedQueue<BaseAttachmentView>? = ConcurrentLinkedQueue() //附着页面集合
    private var urls: ConcurrentLinkedQueue<String>? = ConcurrentLinkedQueue() //保存播放记录
    private var sh: SurfaceHolder? = null //画布Holder
    var surfaceView: SurfaceView? = null
    private var autoPlay = false

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context) : super(context)

    init {
        initMediaPlayer()
    }

    private fun initMediaPlayer() {
        //mediaPlayer?.release()
        if (mediaPlayer == null) {
            mediaPlayer = XmMediaPlayer()
            initMediaPlayerListener()
        }
    }

    private fun initMediaPlayerListener() {
        mediaPlayer?.setOnVideoSizeChangedListener(object : OnVideoSizeChangedListener {
            override fun onVideoSizeChanged(mp: IXmMediaPlayer, width: Int, height: Int, sar_num: Int, sar_den: Int) {
                notifyObserversVideoSizeChanged(mp, width, height, sar_num, sar_den)
            }
        })

        mediaPlayer?.setOnErrorListener(object : OnErrorListener {
            override fun onError(mp: IXmMediaPlayer, what: Int, extra: Int): Boolean {
                notifyObserversError(mp, what, extra)
                return false
            }
        })

        mediaPlayer?.setOnInfoListener(object : OnInfoListener {
            override fun onInfo(mp: IXmMediaPlayer, what: Int, extra: Int): Boolean {
                notifyObserversInfo(mp, what, extra)
                return false
            }
        })

        mediaPlayer?.setOnPreparedListener(object : OnPreparedListener {
            override fun onPrepared(mp: IXmMediaPlayer) {
                if (autoPlay) {
                    mediaPlayer?.start()
                }
                notifyObserversPrepared(mp)
            }
        })

        mediaPlayer?.setOnBufferingUpdateListener(object : OnBufferingUpdateListener {
            override fun onBufferingUpdate(mp: IXmMediaPlayer, percent: Int) {
                notifyObserversBufferingUpdate(mp, percent)
            }
        })

        mediaPlayer?.setOnSeekCompleteListener(object : OnSeekCompleteListener {
            override fun onSeekComplete(mp: IXmMediaPlayer) {
                notifyObserversSeekComplete(mp)
            }
        })

        mediaPlayer?.setOnCompletionListener(object : OnCompletionListener {
            override fun onCompletion(mp: IXmMediaPlayer) {
                notifyObserversCompletion(mp)
            }
        })

        mediaPlayer?.setOnSubtitleDataListener(object : OnSubtitleDataListener {
            override fun onSubtitleData(mp: IXmMediaPlayer, data: SubtitleData) {
                notifyObserversSubtitleData(mp, data)
            }
        })

        mediaPlayer?.setOnDrmInfoListener(object : OnDrmInfoListener {
            override fun onDrmInfo(mp: IXmMediaPlayer, drmInfo: MediaPlayer.DrmInfo) {
                notifyObserversDrmInfo(mp, drmInfo)
            }
        })

        mediaPlayer?.setOnMediaCodecSelectListener(object : OnMediaCodecSelectListener {
            override fun onMediaCodecSelect(mp: IXmMediaPlayer, mimeType: String, profile: Int, level: Int): String {
                notifyObserversMediaCodecSelect(mp, mimeType, profile, level)
                return ""
            }
        })

        mediaPlayer?.setOnControlMessageListener(object : OnControlMessageListener {
            override fun onControlResolveSegmentUrl(mp: IXmMediaPlayer, segment: Int): String {
                notifyObserversControlResolveSegmentUrl(mp, segment)
                return ""
            }
        })
    }

    private fun notifyObserversControlResolveSegmentUrl(mp: IXmMediaPlayer, segment: Int) {
        for (attachmentView in attachmentViews!!) {
            attachmentView.observer?.onControlResolveSegmentUrl(mp, segment)
        }
    }

    private fun notifyObserversMediaCodecSelect(mp: IXmMediaPlayer, mimeType: String, profile: Int, level: Int) {
        for (attachmentView in attachmentViews!!) {
            attachmentView.observer?.onMediaCodecSelect(mp, mimeType, profile, level)
        }
    }

    private fun notifyObserversDrmInfo(mp: IXmMediaPlayer, drmInfo: MediaPlayer.DrmInfo) {
        for (attachmentView in attachmentViews!!) {
            attachmentView.observer?.onDrmInfo(mp, drmInfo)
        }
    }

    private fun notifyObserversSubtitleData(mp: IXmMediaPlayer, data: SubtitleData) {
        for (attachmentView in attachmentViews!!) {
            attachmentView.observer?.onSubtitleData(mp, data)
        }
    }

    private fun notifyObserversCompletion(mp: IXmMediaPlayer) {
        for (attachmentView in attachmentViews!!) {
            attachmentView.observer?.onCompletion(mp)
        }
    }

    private fun notifyObserversSeekComplete(mp: IXmMediaPlayer) {
        for (attachmentView in attachmentViews!!) {
            attachmentView.observer?.onSeekComplete(mp)
        }
    }

    private fun notifyObserversBufferingUpdate(mp: IXmMediaPlayer, percent: Int) {
        for (attachmentView in attachmentViews!!) {
            attachmentView.observer?.onBufferingUpdate(mp, percent)
        }
    }

    private fun notifyObserversPrepared(mp: IXmMediaPlayer) {
        for (attachmentView in attachmentViews!!) {
            attachmentView.observer?.onPrepared(mp)
        }
    }

    private fun notifyObserversInfo(mp: IXmMediaPlayer, what: Int, extra: Int) {
        for (attachmentView in attachmentViews!!) {
            attachmentView.observer?.onInfo(mp, what, extra)
        }
    }

    private fun notifyObserversError(mp: IXmMediaPlayer, what: Int, extra: Int) {
        for (attachmentView in attachmentViews!!) {
            attachmentView.observer?.onError(mp, what, extra)
        }
    }

    private fun notifyObserversVideoSizeChanged(mp: IXmMediaPlayer, width: Int, height: Int, sar_num: Int, sar_den: Int) {
        for (attachmentView in attachmentViews!!) {
            attachmentView.observer?.onVideoSizeChanged(mp, width, height, sar_num, sar_den)
        }
    }

    fun bindAttachmentView(attachment: BaseAttachmentView?) {
        /*添加在播放器附着的页面*/
        if (attachment != null) {
            attachment.bind(this)
            attachment.xmVideoView = this
            attachmentViews?.add(attachment)
            this.addView(attachment, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        } else {
            BKLog.e(TAG, "attachment is null")
        }
    }

    fun unBindAttachmentView(attachment: BaseAttachmentView?) {
        if (attachment != null) {
            attachment.unBind()
            attachment.xmVideoView = null
            attachmentViews?.remove(attachment)
        } else {
            BKLog.e(TAG, "attachment is null")
        }
    }


    fun start(url: String, autoPlay: Boolean = false) {
        /*异步准备播放*/
        this.autoPlay = autoPlay
        if (sh == null) {
            surfaceView = SurfaceView(context)
            surfaceView?.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            sh = surfaceView?.holder
            sh?.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                    BKLog.d(TAG, "surfaceChanged width:$width height:$height")
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    BKLog.d(TAG, "surfaceDestroyed")
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    if (!TextUtils.isEmpty(url)) {
                        if (mediaPlayer == null) {
                            initMediaPlayer()
                        } else {
                            mediaPlayer?.reset()
                        }
                        mediaPlayer?.setDisplay(holder)
                        try {
                            mediaPlayer?.setDataSource(url)
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        mediaPlayer?.prepareAsync()
                    } else {
                        BKLog.e(TAG, "url is null")
                    }
                    BKLog.d(IXmMediaPlayer.TAG, "surfaceCreated")
                }
            })
            addView(surfaceView)
        } else {
            mediaPlayer?.prepareAsync()
        }
    }

    private var gestureHelp: GestureHelp? = null
    fun test() {
        val pre = AttachmentPre(context)
        pre.preUrl = "http://pic1.nipic.com/2008-08-14/2008814183939909_2.jpg"
        pre.url = "http://hls.videocc.net/26de49f8c2/9/26de49f8c273bbc8f6812d1422a11b39_2.m3u8"
        pre.url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
        bindAttachmentView(pre)

        val loading = AttachmentLoading(context)
        bindAttachmentView(loading)

        val control = AttachmentControl(context)
        bindAttachmentView(control)

        bindAttachmentView(AttachmentGesture(context))

        gestureHelp = GestureHelp(context)
        gestureHelp?.bind(this)
        gestureHelp?.setOnGestureListener(object : GestureHelp.OnGestureListener {
            override fun onDown() {
                notifyObserversDown()
            }

            override fun onDownUp() {
                notifyObserversDownUp()
            }

            override fun onClick() {
                notifyObserversClick()
            }

            override fun onHorizontal(present: Int) {
                notifyObserversHorizontal(present)
            }


            override fun onVertical(type: String, present: Int) {
                notifyObserversVertical(type, present)
            }

            override fun onDoubleClick() {
                notifyObserversDoubleClick()
            }

            override fun onScaleEnd(scaleFactor: Float) {
                notifyObserversScaleEnd(scaleFactor)
            }


            private fun notifyObserversScaleEnd(scaleFactor: Float) {
                for (attachmentView in attachmentViews!!) {
                    attachmentView.observer?.onScaleEnd(mediaPlayer, scaleFactor)
                }
            }

            private fun notifyObserversDoubleClick() {
                for (attachmentView in attachmentViews!!) {
                    attachmentView.observer?.onDoubleClick(mediaPlayer)
                }
            }

            private fun notifyObserversVertical(type: String, present: Int) {
                for (attachmentView in attachmentViews!!) {
                    attachmentView.observer?.onVertical(mediaPlayer, type, present)
                }
            }

            private fun notifyObserversHorizontal(present: Int) {
                for (attachmentView in attachmentViews!!) {
                    attachmentView.observer?.onHorizontal(mediaPlayer, present)
                }
            }

            private fun notifyObserversClick() {
                for (attachmentView in attachmentViews!!) {
                    attachmentView.observer?.onClick(mediaPlayer)
                }
            }
        })
    }

    private fun notifyObserversDown() {
        for (attachmentView in attachmentViews!!) {
            attachmentView.onDown()
        }
    }

    private fun notifyObserversDownUp() {
        for (attachmentView in attachmentViews!!) {
            attachmentView.onDownUp()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (sh != null) { //只要播放过则就手势处理
            return gestureHelp?.onTouchEvent(event)!!
        }
        return super.onTouchEvent(event)
    }
}