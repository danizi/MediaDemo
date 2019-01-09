package com.xm.lib.ijkplayer

import android.content.Context
import android.util.AttributeSet
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.AbsVideoView
import com.xm.lib.media.core.attach.factory.MediaAttachLayoutFactory
import com.xm.lib.media.core.constant.Constant

/**
 * 播放
 */
class VideoView(context: Context?, attrs: AttributeSet?) : AbsVideoView(context, attrs) {

    private var player: AbsMediaCore? = IJKPlayer()
    private val attachLayouts: ArrayList<MediaAttachLayoutFactory.IAttachLayout>? = ArrayList()

    override fun init(context: Context?, attrs: AttributeSet?) {

    }

    override fun isPlaying(): Boolean? {
        return player?.mediaLifeState == Constant.MediaLifeState.Started
    }

    override fun canPause(): Boolean? {
        return isPlaying()
    }

    override fun canSeekBackward(): Boolean? {
        return player?.mediaLifeState != Constant.MediaLifeState.Stop && player?.mediaLifeState != Constant.MediaLifeState.Error
    }

    override fun canSeekForward(): Boolean? {
        return player?.mediaLifeState != Constant.MediaLifeState.Stop && player?.mediaLifeState != Constant.MediaLifeState.Error
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
        player?.reset()
        player?.stop()
        player?.release()
        player = null
    }

    override fun setVideoPath(path: String?) {
        if (!path.isNullOrEmpty()) {
            player?.setDataSource(path!!)
        }
    }

    override fun setAttachLayout(attachLayout: MediaAttachLayoutFactory.IAttachLayout?) {
        if (attachLayout != null) {
            attachLayouts?.add(attachLayout)
        }
    }

    override fun setOnCompletionListener(l: AbsMediaCore.OnCompletionListener) {
        player?.setOnCompletionListener(l)
    }

    override fun setOnErrorListener(l: AbsMediaCore.OnErrorListener) {
        player?.setOnErrorListener(l)
    }

    override fun setOnInfoListener(l: AbsMediaCore.OnInfoListener) {
        player?.setOnInfoListener(l)
    }

    override fun setOnPreparedListener(l: AbsMediaCore.OnPreparedListener) {
        player?.setOnPreparedListener(l)
    }
}