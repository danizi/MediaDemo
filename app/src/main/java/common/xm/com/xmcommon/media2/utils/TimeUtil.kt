package common.xm.com.xmcommon.media2.utils

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
}