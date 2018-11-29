package com.xm.lib.media.contract.base

import android.content.Context
import android.widget.ProgressBar
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.event.Event
import com.xm.lib.media.help.LightHelper
import com.xm.lib.media.help.VolumeHelper

class BaseGestureContract {
    interface View<T> : BaseMediaContract.View<T>

    open class Model : BaseMediaContract.Model() {
        var present: Float? = 0f
    }

    abstract class Present(context: Context?) : BaseMediaContract.Present(context) {
        val DEFUAT: Float = -1F
        var curProgress: Float? = DEFUAT
        val PERCENT: String = "percent"

        val tag: String? = "BaseGestureContract"

        /**
         * 音频
         * @return 返回0~100
         */
        fun getVolumePresent(processBar: ProgressBar?, event: Event?): Int {
            if (curProgress == DEFUAT) {
                curProgress = VolumeHelper.getVolume(context)
                //Log.d(tag, "getVolumePresent getLightPresent first curVolume:$curProgress")
            }
            //Log.d(tag, "getVolumePresent PERCENT:" + event?.parameter?.get(PERCENT))
            val presentF: Float = curProgress!! - event?.parameter?.get(PERCENT) as Float
            VolumeHelper.setVolume(context, presentF)                               //设置音量
            return (presentF * 100).toInt()
        }

        /**
         * 亮度
         * @return 返回0~100
         */
        fun getLightPresent(processBar: ProgressBar?, event: Event?): Int {
            if (curProgress == DEFUAT) {
                curProgress = LightHelper.getScreenBrightness(context).toFloat() / 255F
                //Log.d(tag, "getLightPresent first curLight:$curProgress")
            }
            val presentF: Float = curProgress!! - event?.parameter?.get(PERCENT) as Float
            //Log.d(tag, "getLightPresent PERCENT:" + event?.parameter?.get(PERCENT))
            LightHelper.setSystemBrightness(context, (presentF * 255).toInt())    //设置亮度
            return (presentF * 100).toInt()
        }

        /**
         * 进度
         * @return 返回0~100
         */
        fun getProgresPresent(processBar: ProgressBar?, event: Event?, media: AbsMediaCore?): Int {
            if (curProgress == DEFUAT) {
                curProgress = media?.getCurrentPosition()?.toFloat()!! / media.getDuration().toFloat()
            }
            val presentF: Float = curProgress!! + event?.parameter?.get(PERCENT) as Float  //当前的播放进度
            return (presentF * 100).toInt()
        }

        /**
         * 重置进度条
         */
        fun resetCurProgress() {
            curProgress = DEFUAT
        }
    }
}