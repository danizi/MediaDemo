package common.xm.com.xmcommon.media2.base

import android.content.Context
import android.media.MediaPlayer
import android.media.SubtitleData
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import common.xm.com.xmcommon.media2.attachment.*
import common.xm.com.xmcommon.media2.attachment.control.AttachmentControl
import common.xm.com.xmcommon.media2.base.XmMediaPlayer.Companion.TAG
import common.xm.com.xmcommon.media2.broadcast.BroadcastManager
import common.xm.com.xmcommon.media2.broadcast.receiver.*
import common.xm.com.xmcommon.media2.event.GestureObservable
import common.xm.com.xmcommon.media2.event.PhoneStateObservable
import common.xm.com.xmcommon.media2.event.PlayerObservable
import common.xm.com.xmcommon.media2.utils.GestureHelper
import common.xm.com.xmcommon.media2.log.BKLog
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue

class XmVideoView : FrameLayout {

    var mediaPlayer: XmMediaPlayer? = null //播放器
    //var attachmentViews: ConcurrentLinkedQueue<BaseAttachmentView>? = ConcurrentLinkedQueue() //附着页面集合
    private var urls: ConcurrentLinkedQueue<String>? = ConcurrentLinkedQueue() //保存播放记录
    private var sh: SurfaceHolder? = null //画布Holder
    var surfaceView: SurfaceView? = null
    private var autoPlay = false
    private var playerObservable: PlayerObservable? = null
    private var phoneStateObservable: PhoneStateObservable? = null
    private var gestureObservable: GestureObservable? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context) : super(context)

    init {
        initMediaPlayer()
    }

    private fun initMediaPlayer() {
        if (playerObservable == null) {
            playerObservable = PlayerObservable()
        }
        if (phoneStateObservable == null) {
            phoneStateObservable = PhoneStateObservable()
        }
        if (gestureObservable == null) {
            gestureObservable = GestureObservable()
        }
        //mediaPlayer?.release()
        if (mediaPlayer == null) {
            mediaPlayer = XmMediaPlayer()
            initMediaPlayerListener()
        }
    }

    private fun initMediaPlayerListener() {
        mediaPlayer?.setOnVideoSizeChangedListener(object : OnVideoSizeChangedListener {
            override fun onVideoSizeChanged(mp: IXmMediaPlayer, width: Int, height: Int, sar_num: Int, sar_den: Int) {
                playerObservable?.notifyObserversVideoSizeChanged(mp, width, height, sar_num, sar_den)
            }
        })

        mediaPlayer?.setOnErrorListener(object : OnErrorListener {
            override fun onError(mp: IXmMediaPlayer, what: Int, extra: Int): Boolean {
                playerObservable?.notifyObserversError(mp, what, extra)
                return false
            }
        })

        mediaPlayer?.setOnInfoListener(object : OnInfoListener {
            override fun onInfo(mp: IXmMediaPlayer, what: Int, extra: Int): Boolean {
                playerObservable?.notifyObserversInfo(mp, what, extra)
                return false
            }
        })

        mediaPlayer?.setOnPreparedListener(object : OnPreparedListener {
            override fun onPrepared(mp: IXmMediaPlayer) {
                if (autoPlay) {
                    mediaPlayer?.start()
                }
                playerObservable?.notifyObserversPrepared(mp)
            }
        })

        mediaPlayer?.setOnBufferingUpdateListener(object : OnBufferingUpdateListener {
            override fun onBufferingUpdate(mp: IXmMediaPlayer, percent: Int) {
                playerObservable?.notifyObserversBufferingUpdate(mp, percent)
            }
        })

        mediaPlayer?.setOnSeekCompleteListener(object : OnSeekCompleteListener {
            override fun onSeekComplete(mp: IXmMediaPlayer) {
                playerObservable?.notifyObserversSeekComplete(mp)
            }
        })

        mediaPlayer?.setOnCompletionListener(object : OnCompletionListener {
            override fun onCompletion(mp: IXmMediaPlayer) {
                playerObservable?.notifyObserversCompletion(mp)
            }
        })

        mediaPlayer?.setOnSubtitleDataListener(object : OnSubtitleDataListener {
            override fun onSubtitleData(mp: IXmMediaPlayer, data: SubtitleData) {
                playerObservable?.notifyObserversSubtitleData(mp, data)
            }
        })

        mediaPlayer?.setOnDrmInfoListener(object : OnDrmInfoListener {
            override fun onDrmInfo(mp: IXmMediaPlayer, drmInfo: MediaPlayer.DrmInfo) {
                playerObservable?.notifyObserversDrmInfo(mp, drmInfo)
            }
        })

        mediaPlayer?.setOnMediaCodecSelectListener(object : OnMediaCodecSelectListener {
            override fun onMediaCodecSelect(mp: IXmMediaPlayer, mimeType: String, profile: Int, level: Int): String {
                playerObservable?.notifyObserversMediaCodecSelect(mp, mimeType, profile, level)
                return ""
            }
        })

        mediaPlayer?.setOnControlMessageListener(object : OnControlMessageListener {
            override fun onControlResolveSegmentUrl(mp: IXmMediaPlayer, segment: Int): String {
                playerObservable?.notifyObserversControlResolveSegmentUrl(mp, segment)
                return ""
            }
        })
    }

    fun bindAttachmentView(attachment: BaseAttachmentView?) {
        /*添加在播放器附着的页面*/
        if (attachment != null) {
            if (attachment.parent != null) {//android.view.ViewGroup$LayoutParams cannot be cast to android.view.ViewGroup$MarginLayoutParams
                (attachment.parent as ViewGroup).removeView(attachment)
            }
            //this.addView(attachment, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            //attachmentViews?.add(attachment)
            playerObservable?.addObserver(attachment.observer)
            gestureObservable?.addObserver(attachment.gestureObserver)
            phoneStateObservable?.addObserver(attachment.phoneObserver)

            attachment.bind(this)
        } else {
            BKLog.e(TAG, "attachment is null")
        }
    }

    fun unBindAttachmentView(attachment: BaseAttachmentView?) {
        if (attachment != null) {
            //this.removeView(attachment)
            //attachmentViews?.remove(attachment)
            playerObservable?.deleteObserver(attachment.observer)
            gestureObservable?.deleteObserver(attachment.gestureObserver)
            phoneStateObservable?.deleteObserver(attachment.phoneObserver)
            attachment.unBind()
        } else {
            BKLog.e(TAG, "attachment is null")
        }
    }


    fun start(url: String, autoPlay: Boolean = false) {
        /*异步准备播放*/
        this.autoPlay = autoPlay
        if (surfaceView == null || sh == null) {
            //添加画布
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
        } else {
            mediaPlayer?.prepareAsync()
        }
        // surfaceView?.visibility= View.GONE  设置画布隐藏就无法播放了
        addView(surfaceView)
    }

    private var gestureHelper: GestureHelper? = null
    fun test() {
        //预览
        val pre = AttachmentPre(context, "https://img-blog.csdn.net/20160413112832792?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center")
        pre.url = "http://hls.videocc.net/26de49f8c2/9/26de49f8c273bbc8f6812d1422a11b39_2.m3u8"
        bindAttachmentView(pre)

        //加载
        //val loading = AttachmentLoading(context)
        //bindAttachmentView(loading)

        //控制器
        val control = AttachmentControl(context)
        bindAttachmentView(control)

        //手势
        bindAttachmentView(AttachmentGesture(context))

        //完成
        bindAttachmentView(AttachmentComplete(context))

        //手势监听
        gestureHelper = GestureHelper(context)
        gestureHelper?.bind(this)
        gestureHelper?.setOnGestureListener(object : GestureHelper.OnGestureListener {
            override fun onDown() {
                gestureObservable?.notifyObserversDown()
            }

            override fun onDownUp() {
                gestureObservable?.notifyObserversDownUp()
            }

            override fun onClick() {
                gestureObservable?.notifyObserversClick()
            }

            override fun onHorizontal(present: Int) {
                gestureObservable?.notifyObserversHorizontal(present)
            }

            override fun onVertical(type: String, present: Int) {
                gestureObservable?.notifyObserversVertical(type, present)
            }

            override fun onDoubleClick() {
                gestureObservable?.notifyObserversDoubleClick()
            }

            override fun onScaleEnd(scaleFactor: Float) {
                gestureObservable?.notifyObserversScaleEnd(scaleFactor)
            }
        })

        //各种系统广播监听
        if (broadcastManager == null) {
            broadcastManager = BroadcastManager.create(context)
        }

        //电量状态
        val batteryLevelReceiver = BatteryLevelReceiver(object : BatteryLevelReceiver.OnBatteryLevelListener {
            override fun onBatteryLevel(type: String, batteryPct: Float) {
                phoneStateObservable?.notifyObserversBatteryLevel(type,batteryPct)
            }
        })
        //耳机状态
        val headsetReceiver = HeadsetReceiver(object : HeadsetReceiver.OnHeadsetListener {
            override fun onHeadset(type: String, state: Int?) {
                phoneStateObservable?.notifyObserversHeadset(type,state)
            }
        })

        //网络状态
        val networkConnectChangedReceiver = NetworkConnectChangedReceiver(object : NetworkConnectChangedReceiver.OnNetworkConnectChangedListener {
            override fun onChange(isConnect: Boolean, type: Int) {
                BKLog.d(TAG, "isConnect:$isConnect type:$type")
                phoneStateObservable?.notifyObserversNetworkConnectChange(isConnect,type)
            }
        })

        //手机来电去电状态
        val phoneStateReceiver = PhoneStateReceiver(object : PhoneStateReceiver.OnPhoneStateListener {
            override fun onPhoneState() {
                phoneStateObservable?.notifyObserversPhoneState()
            }

            override fun onStateRinging() {
                phoneStateObservable?.notifyObserversStateRinging()
            }
        })
        //是否充电状态
        val powerConnectionReceiver = PowerConnectionReceiver(object :PowerConnectionReceiver.OnPowerConnectionListener{
            override fun onPowerConnection(charger: Boolean, type: String) {
                phoneStateObservable?.notifyObserversPowerConnection(charger,type)
            }
        })

        broadcastManager?.registerReceiver(batteryLevelReceiver.createIntentFilter(), batteryLevelReceiver)
        broadcastManager?.registerReceiver(headsetReceiver.createIntentFilter(), headsetReceiver)
        broadcastManager?.registerReceiver(networkConnectChangedReceiver.createIntentFilter(), networkConnectChangedReceiver)
        broadcastManager?.registerReceiver(phoneStateReceiver.createIntentFilter(), phoneStateReceiver)
        broadcastManager?.registerReceiver(powerConnectionReceiver.createIntentFilter(), powerConnectionReceiver)
    }

    private var broadcastManager: BroadcastManager? = null


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (sh != null) { //只要播放过则就手势处理
            return gestureHelper?.onTouchEvent(event)!!
        }
        return super.onTouchEvent(event)
    }
}