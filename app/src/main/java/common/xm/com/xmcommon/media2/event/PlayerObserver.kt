package common.xm.com.xmcommon.media2.event

import android.media.MediaPlayer
import android.media.SubtitleData
import common.xm.com.xmcommon.media2.base.IXmMediaPlayer
import common.xm.com.xmcommon.media2.log.BKLog

interface PlayerObserver {
    var TAG: String
        get() = "PlayerObserver"
        set(value) = TODO()

    fun onPrepared(mp: IXmMediaPlayer) {
        BKLog.d(TAG, "onPrepared")
    }

    fun onCompletion(mp: IXmMediaPlayer) {
        BKLog.d(TAG, "onCompletion")
    }

    fun onSeekComplete(mp: IXmMediaPlayer) {
        BKLog.d(TAG, "onSeekComplete")
    }

    fun onBufferingUpdate(mp: IXmMediaPlayer, percent: Int) {
        BKLog.d(TAG, "onBufferingUpdate percent:$percent")
    }

    fun onInfo(mp: IXmMediaPlayer, what: Int, extra: Int) {
        BKLog.d(TAG, "onInfo what:$what extra:$extra")
    }

    fun onError(mp: IXmMediaPlayer, what: Int, extra: Int) {
        BKLog.d(TAG, "onError what:$what extra:$extra")
    }

    fun onVideoSizeChanged(mp: IXmMediaPlayer, width: Int, height: Int, sar_num: Int, sar_den: Int) {
        BKLog.d(TAG, "onVideoSizeChanged width:$width height:$height sar_num:$sar_num sar_den:$sar_den")
    }

    fun onControlResolveSegmentUrl(mp: IXmMediaPlayer, segment: Int) {
        BKLog.d(TAG, "onControlResolveSegmentUrl segment:$segment")
    }

    fun onMediaCodecSelect(mp: IXmMediaPlayer, mimeType: String, profile: Int, level: Int) {
        BKLog.d(TAG, "onMediaCodecSelect mimeType:$mimeType profile:$profile level:$level")
    }

    fun onDrmInfo(mp: IXmMediaPlayer, drmInfo: MediaPlayer.DrmInfo) {
        BKLog.d(TAG, "onDrmInfo drmInfo:$drmInfo")
    }

    fun onSubtitleData(mp: IXmMediaPlayer, data: SubtitleData) {
        BKLog.d(TAG, "onSubtitleData data:$data")
    }
}