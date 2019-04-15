package common.xm.com.xmcommon.media2.base

import android.content.Context
import android.media.MediaPlayer
import android.media.SubtitleData
import android.text.TextUtils
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.FrameLayout
import common.xm.com.xmcommon.media2.attachment.AttachmentPre
import common.xm.com.xmcommon.media2.attachment.BaseAttachmentView
import common.xm.com.xmcommon.media2.base.XmMediaPlayer.Companion.TAG
import common.xm.com.xmcommon.media2.log.BKLog
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class XmVideoView : FrameLayout {

    var mediaPlayer: XmMediaPlayer? = null //播放器
    var attachmentViews: ArrayList<BaseAttachmentView>? = null //附着页面集合
    private var urls: ArrayList<String>? = null //保存播放记录
    private var sh: SurfaceHolder? = null //画布Holder
    private var autoPlay = false

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context) : super(context)

    init {
        urls = ArrayList()
        attachmentViews = ArrayList()
        mediaPlayer = XmMediaPlayer()
        initMediaPlayerListener()
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
            attachmentViews?.add(attachment)
        } else {
            BKLog.e(TAG, "attachment is null")
        }
    }

    fun removeAttachmentView(attachment: BaseAttachmentView?) {
        if (attachment != null) {
            attachment.unBind()
            attachmentViews?.remove(attachment)
        } else {
            BKLog.e(TAG, "attachment is null")
        }
    }


    fun start(url: String, autoPlay: Boolean = false) {
        /*异步准备播放*/
        this.autoPlay = autoPlay
        if (sh == null) {
            val surfaceView = SurfaceView(context)
            surfaceView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            sh = surfaceView.holder
            sh?.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                    BKLog.d(IXmMediaPlayer.TAG, "surfaceChanged width:$width height:$height")
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    BKLog.d(IXmMediaPlayer.TAG, "surfaceDestroyed")
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    if (!TextUtils.isEmpty(url)) {

                        //重新创建
                        mediaPlayer?.stop()
                        mediaPlayer?.reset()
                        mediaPlayer?.release()
                        mediaPlayer = null
                        mediaPlayer = XmMediaPlayer()

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

    fun test() {
        val url = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
        val preUrl = "http://pic32.nipic.com/20130823/13339320_183302468194_2.jpg"
        val pre = AttachmentPre(context, preUrl, url)
        bindAttachmentView(pre)
    }
}