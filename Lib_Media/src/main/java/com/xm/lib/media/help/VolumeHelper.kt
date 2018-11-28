package com.xm.lib.media.help

import android.content.Context
import android.media.AudioManager
import android.util.Log


class VolumeHelper {
    companion object {
        /**
         * 系统的是：0到Max，Max不确定，这个称为：系统的音量范围。
         * STREAM_ALARM 警报
         * STREAM_MUSIC 音乐回放即媒体音量
         * STREAM_RING 铃声
         * STREAM_SYSTEM 系统
         * STREAM_VOICE_CALL 通话
         */
        fun getVolume(context: Context?): Float {
            val audioManager: AudioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat() / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        }

        /**
         * KeyEvent.KEYCODE_VOLUME_UP   +
         * KeyEvent.KEYCODE_VOLUME_DOWN - 不靠谱 要换另一种
         * @param index 0~100
         */
        fun setVolume(context: Context?, percent: Float) {
            val audioManager: AudioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val index: Int = (percent * audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).toInt()
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, AudioManager.FLAG_PLAY_SOUND)
        }
    }
}
