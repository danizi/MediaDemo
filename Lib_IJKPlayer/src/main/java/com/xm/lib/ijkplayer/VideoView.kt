package com.xm.lib.ijkplayer

import android.content.Context
import android.util.AttributeSet
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.AbsVideoView
import com.xm.lib.media.core.attach.factory.MediaAttachLayoutFactory

/**
 * 播放
 */
class VideoView(context: Context?, attrs: AttributeSet?) : AbsVideoView(context, attrs) {

    val player: AbsMediaCore? = IJKPlayer()

    override fun isPlaying(): Boolean? {
        return true
    }

    override fun canPause(): Boolean? {
        return true
    }

    override fun canSeekBackward(): Boolean? {
        return true
    }

    override fun canSeekForward(): Boolean? {
        return true
    }

    override fun getCurrentPosition(): Long? {
        return player?.getCurrentPosition()
    }

    override fun pause() {
        player?.pause()
    }

    override fun resume() {

    }

    override fun seekTo(msec: Int) {
        player?.seekTo(msec = msec.toLong())
    }

    override fun start() {
        player?.start()
    }

    override fun realse() {
        player?.release()
    }

    override fun setVideoPath(path: String?) {
        if (path != null) {
            player?.setDataSource(path)
        }
    }

    override fun setAttachLayout(attachLayout: MediaAttachLayoutFactory.IAttachLayout?) {

    }

    override fun setOnCompletionListener(l: AbsMediaCore.OnCompletionListener) {
        player?.setOnCompletionListener(object : AbsMediaCore.OnCompletionListener {
            override fun onCompletion(mp: AbsMediaCore) {
                l.onCompletion(mp)
            }
        })
    }

    override fun setOnErrorListener(l: AbsMediaCore.OnErrorListener) {
        player?.setOnErrorListener(object : AbsMediaCore.OnErrorListener {
            override fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                return l.onError(mp, what, extra)
            }
        })
    }

    override fun setOnInfoListener(l: AbsMediaCore.OnInfoListener) {
        player?.setOnInfoListener(object : AbsMediaCore.OnInfoListener {
            override fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                return l.onInfo(mp, what, extra)
            }
        })
    }

    override fun setOnPreparedListener(l: AbsMediaCore.OnPreparedListener) {
        player?.setOnPreparedListener(object : AbsMediaCore.OnPreparedListener {
            override fun onPrepared(mp: AbsMediaCore) {
                l.onPrepared(mp)
            }
        })
    }
}