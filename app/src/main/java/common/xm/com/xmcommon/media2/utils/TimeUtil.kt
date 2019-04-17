package common.xm.com.xmcommon.media2.utils

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import java.util.*

object TimeUtil {

    fun unixStr() {
        println("" + java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Date(60 * 1000)))
    }

    fun unix() {
        print(java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2019/4/16 17:43:30"))
    }

    fun hhmmss(msec: Long): String {
        val h = msec / 1000 / 60 / 60 % 60
        return if (h > 0) {
            java.text.SimpleDateFormat("HH:mm:ss").format(Date(msec))
        } else {
            java.text.SimpleDateFormat("mm:ss").format(Date(msec))
        }
    }

//    //定时器
//    private val handler = Handler(Looper.getMainLooper())
//    //开启一个定时器显示当前播放时长
//    private var task: TimerTask? = null
//    private var timer: Timer? = null
//
//    fun start(listener: OnPeriodListener?, period: Long) {
//        task = object : TimerTask() {
//            @SuppressLint("SetTextI18n")
//            override fun run() {
//                handler.post {
//                    listener?.onPeriod()
//                }
//            }
//        }
//        if (null == timer) {
//            timer = Timer()
//        }
//        timer?.schedule(task, 0, period)
//    }
//
//    fun start(listener: OnDelayTimerListener?, delay: Long) {
//        task = object : TimerTask() {
//            @SuppressLint("SetTextI18n")
//            override fun run() {
//                handler.post {
//                    listener?.onDelayTimerFinish()
//                }
//            }
//        }
//        if (null == timer) {
//            timer = Timer()
//        }
//        timer?.schedule(task, delay)
//    }
//
//    fun stop() {
//        timer?.cancel()
//        task?.cancel()
//        timer = null
//        task = null
//    }
//
//    interface OnPeriodListener {
//        fun onPeriod()
//    }
//
//    interface OnDelayTimerListener {
//        fun onDelayTimerFinish()
//    }
}