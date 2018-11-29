package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import android.widget.SeekBar
import com.bangke.lib.common.utils.ToolUtil
import com.xm.lib.media.R
import com.xm.lib.media.component.MediaControlView
import com.xm.lib.media.contract.base.BaseMediaContract
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.enum_.EnumMediaState
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable
import java.util.*

class MediaControlViewContract {
    interface View : BaseMediaContract.View<MediaControlView>

    class Model : BaseMediaContract.Model() {
        var startResId: Int? = R.mipmap.media_play
        var pauseResId: Int? = R.mipmap.media_pause

        var visibilityFlag: Int? = android.view.View.GONE             //显示标识位,是否显示控制视图
        var prepared: Boolean? = false                                //准备状态标志位
        var curScreenMode: String = EventConstant.VALUE_SCREEN_SMALL  //当前窗口模式 全屏/小窗口

    }

    class Present(context: Context?, val view: View?) : BaseMediaContract.Present(context) {
        var model: Model? = Model()

        override fun process() {

        }

        override fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {
            if (null == model?.media && null != media) {
                model?.media = media
            }

            when (event?.parameter?.get(EventConstant.KEY_METHOD)) {
                EventConstant.VALUE_METHOD_ONPREPARED -> {
                    model?.prepared = true
                    timer()
                }
                EventConstant.VALUE_METHOD_ONERROR -> {
                    model?.prepared = false
                }
                EventConstant.VALUE_METHOD_ONCOMPLETION -> {
                    model?.prepared = false
                }
            }
        }

        /**
         * 开启定时器
         */
        private fun timer() {
            view?.getView()?.seekBar?.max = 100
            view?.getView()?.tvDuration?.text = ToolUtil.formatTime(model?.media?.getDuration())
            val timer = Timer()
            val timerTask = object : TimerTask() {
                override fun run() {
                    view?.getView()?.seekBar?.post {
                        try {
                            view.getView().tvCurrentPosition?.text = ToolUtil.formatTime(model?.media?.getCurrentPosition())
                            var percentF = model?.media?.getCurrentPosition()?.toFloat()!! / model?.media?.getDuration()!!
                            view.getView().seekBar?.progress = (percentF * 100).toInt()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            timer.schedule(timerTask, 0, 1000)
        }

        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
            var eventFrom = event?.parameter?.get(EventConstant.KEY_FROM)
            var eventMethod = event?.parameter?.get(EventConstant.KEY_METHOD)
            when (eventMethod) {
                EventConstant.VALUE_METHOD_CLICK -> {
                    model?.visibilityFlag = if (model?.visibilityFlag == android.view.View.GONE) {
                        view?.showView()
                        android.view.View.VISIBLE
                    } else {
                        view?.hideView()
                        android.view.View.GONE
                    }
                }
            }
        }

        override fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?) {

        }

        /**
         * 暂停&播放
         */
        fun startOrPause() {
            if (model?.media?.playerState == EnumMediaState.PLAYING) {
                model?.media?.pause()
                view?.getView()?.imgPlayPause?.setImageResource(model?.startResId!!)
            } else {
                model?.media?.start()
                view?.getView()?.imgPlayPause?.setImageResource(model?.pauseResId!!)
            }
        }

        /**
         * 全屏&小窗口
         */
        fun fullOrSmall() {
            if (model?.curScreenMode == EventConstant.VALUE_SCREEN_SMALL) {
                view?.getView()?.notifyObservers(Event().setEventType(EnumMediaEventType.VIEW).setParameter(EventConstant.KEY_SCREEN_MODE, EventConstant.VALUE_SCREEN_FULL))
                model?.curScreenMode = EventConstant.VALUE_SCREEN_FULL
            } else if (model?.curScreenMode == EventConstant.VALUE_SCREEN_FULL) {
                view?.getView()?.notifyObservers(Event().setEventType(EnumMediaEventType.VIEW).setParameter(EventConstant.KEY_SCREEN_MODE, EventConstant.VALUE_SCREEN_SMALL))
                model?.curScreenMode = EventConstant.VALUE_SCREEN_SMALL
            }
        }

        fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//            view?.getView()?.notifyObservers(
//                    Event().setEventType(EnumMediaEventType.VIEW)
//                            .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_CONTROLVIEW)
//                            .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONPROGRESSCHANGED)
//                            .setParameter("seekBar", seekBar!!)
//                            .setParameter("progress", progress)
//                            .setParameter("fromUser", fromUser))
        }

        fun onStartTrackingTouch(seekBar: SeekBar?) {
//            view?.getView()?.notifyObservers(
//                    Event().setEventType(EnumMediaEventType.VIEW)
//                            .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_CONTROLVIEW)
//                            .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONSTARTTRACKINGTOUCH)
//                            .setParameter("seekBar", seekBar!!))
        }

        fun onStopTrackingTouch(seekBar: SeekBar?) {
            val msec: Long = (seekBar?.progress!!.toFloat() / 100F * mediaView?.getDuration()!! as Long).toLong()
            mediaView?.seekTo(msec)
            //拖动了进度条,通知加载页面
//            view?.getView()?.notifyObservers(
//                    Event().setEventType(EnumMediaEventType.VIEW)
//                            .setParameter(EventConstant.KEY_FROM, EventConstant.VALUE_FROM_CONTROLVIEW)
//                            .setParameter(EventConstant.KEY_METHOD, EventConstant.VALUE_METHOD_ONSTOPTRACKINGTOUCH)
//                            .setParameter("progress",seekBar?.progress!! ))
        }

        private fun obtainMedia() {
            if (null == model?.media && media != null) {
                model?.media = media
            }
        }

        private fun obtainMediaView() {
            if (null == model?.mediaView && mediaView != null) {
                model?.mediaView = mediaView
            }
        }
    }
}