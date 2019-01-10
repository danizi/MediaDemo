package com.xm.lib.ijkplayer

import android.content.Context
import android.util.AttributeSet
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.AbsVideoView
import com.xm.lib.media.core.MediaHelp
import com.xm.lib.media.core.attach.AbsAttachLayout
import com.xm.lib.media.core.constant.Constant
import com.xm.lib.media.core.listener.MediaListener

/**
 * 播放
 */
class VideoView(context: Context?, attrs: AttributeSet?) : AbsVideoView(context, attrs) {

    private var player: AbsMediaCore? = null
    private val attachLayouts: ArrayList<AbsAttachLayout>? = ArrayList()
    /**
     * 播放器工具类
     */
    private var mediaHelp = object : MediaHelp() {
        override fun createPlayer(): AbsMediaCore {
            player = IJKPlayer()
            return player!!
        }
    }

    override fun init(context: Context?, attrs: AttributeSet?) {
        mediaHelp.setupPlayer()
        mediaHelp.mediaListener = object : MediaHelp.HelpMediaListener {
            override fun onPrepared(mp: AbsMediaCore) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(mp: AbsMediaCore, what: Int?, extra: Int?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onBufferingUpdate(mp: AbsMediaCore, percent: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onCompletion(mp: AbsMediaCore) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTimedText(mp: AbsMediaCore, text: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        mediaHelp.gestureListener = object : MediaHelp.HelpGestureListener {
            override fun onDown() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onUp() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onVolume() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onLight() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onProgress() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onClickListener() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }

    override fun isPlaying(): Boolean? {
        return player?.isPlaying()
    }

    override fun canPause(): Boolean? {
        return player?.canPause()
    }

    override fun canSeekBackward(): Boolean? {
        return player?.canSeekTo()
    }

    override fun canSeekForward(): Boolean? {
        return player?.canSeekTo()
    }

    override fun getCurrentPosition(): Long? {
        return player?.getCurrentPosition()
    }

    override fun pause() {
        return player?.pause()!!
    }

    override fun resume() {
        // 暂停 -> 播放
        if (player?.mediaLifeState == Constant.MediaLifeState.Paused) {
            player?.start()
        }
        // 窗治愈后台 ->播放
    }

    override fun seekTo(msec: Int) {
        player?.seekTo(msec.toLong())
    }

    override fun start() {
        player?.start()
    }

    override fun realse() {
        player?.release()
        player = null
    }

    override fun setVideoPath(path: String?) {
        if (!path.isNullOrEmpty()) {
            player?.setDataSource(path!!)
            player?.prepareAsync()
        }
    }

    override fun setAttachLayout(attachLayout: AbsAttachLayout?) {
        if (attachLayout != null) {
            attachLayouts?.add(attachLayout)
        }
    }

    override fun setOnCompletionListener(l: MediaListener.OnCompletionListener) {
        player?.setOnCompletionListener(l)
    }

    override fun setOnErrorListener(l: MediaListener.OnErrorListener) {
        player?.setOnErrorListener(l)
    }

    override fun setOnInfoListener(l: MediaListener.OnInfoListener) {
        player?.setOnInfoListener(l)
    }

    override fun setOnPreparedListener(l: MediaListener.OnPreparedListener) {
        player?.setOnPreparedListener(l)
    }
}