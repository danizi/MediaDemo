package common.xm.com.xmcommon.media2.event

import android.media.MediaPlayer
import android.media.SubtitleData
import common.xm.com.xmcommon.media2.base.IXmMediaPlayer

interface PlayerObserver {
    fun onPrepared(mp: IXmMediaPlayer) {}

    fun onCompletion(mp: IXmMediaPlayer) {}

    fun onSeekComplete(mp: IXmMediaPlayer) {}

    fun onBufferingUpdate(mp: IXmMediaPlayer, percent: Int) {
    }

    fun onInfo(mp: IXmMediaPlayer, what: Int, extra: Int) {
    }

    fun onError(mp: IXmMediaPlayer, what: Int, extra: Int) {
    }

    fun onVideoSizeChanged(mp: IXmMediaPlayer, width: Int, height: Int, sar_num: Int, sar_den: Int) {
    }

    fun onControlResolveSegmentUrl(mp: IXmMediaPlayer, segment: Int) {
    }

    fun onMediaCodecSelect(mp: IXmMediaPlayer, mimeType: String, profile: Int, level: Int) {
    }

    fun onDrmInfo(mp: IXmMediaPlayer, drmInfo: MediaPlayer.DrmInfo) {
    }

    fun onSubtitleData(mp: IXmMediaPlayer, data: SubtitleData) {
    }
}