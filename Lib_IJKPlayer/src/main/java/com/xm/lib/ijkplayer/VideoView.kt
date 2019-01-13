package com.xm.lib.ijkplayer

import android.content.Context
import android.util.AttributeSet
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.AbsVideoView
import com.xm.lib.media.core.MediaHelp
import com.xm.lib.media.core.AbsAttachLayout
import com.xm.lib.media.core.constant.Constant

/**
 * 播放
 */
class VideoView : AbsVideoView {

    constructor(context: Context, builder: Builder?) : super(context) {
        //基本配置信息赋值
        attachLayouts = builder?.attachLayouts
        auto = builder?.auto
        path = builder?.path
        preUrl = builder?.preUrl
        width = builder?.width
        height = builder?.height

        //创建一个快速播放的帮助类
        mediaHelp = MediaHelp.Builder()
                .index(0)
                .context(context)
                .player(IJKPlayer())
                .mediaContain(this)
                .gestureListener(object : MediaHelp.HelpGestureListener {
                    override fun onDown() {
                        for (attach in attachLayouts?.entries!!) {
                            attach.value.onDown()
                        }
                    }

                    override fun onUp(gestureState: Constant.EnumGestureState?) {
                        for (attach in attachLayouts?.entries!!) {
                            attach.value.onUp(gestureState)
                        }
                    }

                    override fun onVolume(slidePercent: Float) {
                        for (attach in attachLayouts?.entries!!) {
                            attach.value.onVolume(slidePercent)
                        }
                    }

                    override fun onLight(slidePercent: Float) {
                        for (attach in attachLayouts?.entries!!) {
                            attach.value.onLight(slidePercent)
                        }
                    }

                    override fun onProgress(slidePercent: Float) {
                        for (attach in attachLayouts?.entries!!) {
                            attach.value.onProgress(slidePercent)
                        }
                    }

                    override fun onClickListener() {
                        for (attach in attachLayouts?.entries!!) {
                            attach.value.onClickListener()
                        }
                    }

                    override fun onGesture(gestureState: Constant.EnumGestureState?, curPercent: Float?, player: AbsMediaCore?) {
                        for (attach in attachLayouts?.entries!!) {
                            attach.value.onGesture(gestureState, curPercent, player)
                        }
                    }
                })
                .mediaListener(object : MediaHelp.HelpMediaListener {
                    override fun onPrepared(mp: AbsMediaCore) {
                        for (attach in attachLayouts?.entries!!) {
                            attach.value.onPrepared(mp)
                        }
                    }

                    override fun onError(mp: AbsMediaCore, what: Int?, extra: Int?): Boolean {
                        for (attach in attachLayouts?.entries!!) {
                            attach.value.onError(mp, what, extra)
                        }
                        return false
                    }

                    override fun onBufferingUpdate(mp: AbsMediaCore, percent: Int) {
                        for (attach in attachLayouts?.entries!!) {
                            attach.value.onBufferingUpdate(mp, percent)
                        }
                    }

                    override fun onCompletion(mp: AbsMediaCore) {
                        for (attach in attachLayouts?.entries!!) {
                            attach.value.onCompletion(mp)
                        }
                    }

                    override fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                        for (attach in attachLayouts?.entries!!) {
                            attach.value.onInfo(mp, what, extra)
                        }
                        return false
                    }

                    override fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int) {
                        for (attach in attachLayouts?.entries!!) {
                            attach.value.onVideoSizeChanged(mp, width, height, sar_num, sar_den)
                        }
                    }

                    override fun onTimedText(mp: AbsMediaCore, text: String) {
                        for (attach in attachLayouts?.entries!!) {
                            attach.value.onTimedText(mp, text)
                        }
                    }
                })
                .height(height)
                .width(width)
                .build()
        mediaHelp?.setupPlayer(IJKPlayer())
        //添加附着View到播放器的展示容器上
        addAttachLayouts()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun reStart() {
        mediaHelp?.setupPlayer(IJKPlayer())
        addAttachLayouts()
        prepareAsync()
    }

    override fun prepareAsync() {
        super.prepareAsync()
        mediaHelp?.player?.setDataSource(path!!)
        mediaHelp?.player?.prepareAsync()
        mediaHelp?.player?.setSpeed(2.0f)
    }

    /**
     * 播放信息的建造类
     */
    class Builder(var context: Context?) {
        var attachLayouts: HashMap<Constant.EnumMediaView, AbsAttachLayout>? = HashMap()
        var auto: Boolean? = false
        var path: String? = null
        var preUrl: String? = null
        var width: Int? = 0
        var height: Int? = 0

        fun addAttachLayout(type: Constant.EnumMediaView?, attachLayout: AbsAttachLayout?): Builder {
            if (type != null && attachLayout != null) {
                attachLayouts?.put(type, attachLayout)
            }
            return this
        }

        fun auto(): Builder {
            this.auto = false
            return this
        }

        fun path(path: String?): Builder {
            this.path = path
            return this
        }

        fun preUrl(preUrl: String?): Builder {
            this.preUrl = preUrl
            return this
        }

        fun width(width: Int): Builder {
            this.width = width
            return this
        }

        fun height(height: Int): Builder {
            this.height = height
            return this
        }

        fun build(): VideoView? {
            return context?.let { VideoView(it, this) }
        }
    }
}