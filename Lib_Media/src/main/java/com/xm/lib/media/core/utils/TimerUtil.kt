package com.xm.lib.media.core.utils

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import java.util.*

/**
 * 播放器定时器管理类
 */
class TimerUtil {
    private val handler = Handler(Looper.getMainLooper())
    //开启一个定时器显示当前播放时长
    private var task: TimerTask? = null
    private var timer: Timer? = null

    fun start(periodListenner: PeriodListenner?) {
        task = object : TimerTask() {
            @SuppressLint("SetTextI18n")
            override fun run() {
                handler.post {
                    periodListenner?.onPeriodListenner()
                }
            }
        }
        if (null == timer) {
            timer = Timer()
        }
        timer?.schedule(task, 0, 1000)
    }

    fun stop() {
        timer?.cancel()
        task?.cancel()
        timer = null
        task = null
    }

    companion object {
        fun getInstance(): TimerUtil {
            return TimerUtil()
        }
    }

    interface PeriodListenner {
        fun onPeriodListenner()
    }
}