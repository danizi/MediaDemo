package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import com.bangke.lib.common.utils.ToolUtil
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.contract.BaseMediaContract
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.enum_.EnumMediaState
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media.mediaview.component.MediaControlView
import java.util.*

class MediaControlViewContract {
    interface View : BaseMediaContract.View<MediaControlView>

    class Model : BaseMediaContract.Model() {
        var startResId: Int? = R.mipmap.media_play
        var pauseResId: Int? = R.mipmap.media_pause

        var media: AbsMediaCore? = null                               //播放接口
        var visibilityFlag: Int? = android.view.View.GONE             //显示标识位,是否显示控制视图
        var prepared: Boolean? = false                                //准备状态标志位
        var curScreenMode: String = EventConstant.VALUE_SCREEN_SMALL  //当前窗口模式 全屏/小窗口
    }

    class Present(context: Context?, view: View?) : BaseMediaContract.Present() {


        var context: Context? = context
        var view: View? = view
        var model: Model? = Model()

        override fun process() {

        }

        override fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {
            //获取播放器实例
            if (model?.media != event?.parameter?.get("mp") as AbsMediaCore?) {
                model?.media = event?.parameter?.get("mp") as AbsMediaCore?
            }

            when (event?.parameter?.get(EventConstant.KEY_METHOD)) {
                EventConstant.VALUE_METHOD_ONPREPARED -> {
                    model?.prepared = true
                    //开启计时器了
                    view?.getView()?.seekBar?.max = model?.media?.getDuration()!!.toInt()
                    view?.getView()?.tvDuration?.text = ToolUtil.formatTime(model?.media?.getDuration())
                    val timer = Timer()
                    val timerTask = object : TimerTask() {
                        override fun run() {
                            view?.getView()?.seekBar?.post {
                                try {
                                    view?.getView()?.tvCurrentPosition?.text = ToolUtil.formatTime(model?.media?.getCurrentPosition())
                                    view?.getView()?.seekBar?.progress = model?.media?.getCurrentPosition()!!.toInt()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                    timer.schedule(timerTask, 0, 1000)
                }
                EventConstant.VALUE_METHOD_ONERROR -> {
                    model?.prepared = false
                }
                EventConstant.VALUE_METHOD_ONCOMPLETION -> {
                    model?.prepared = false
                }
            }
        }

        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
            when (event?.parameter?.get(EventConstant.KEY_METHOD)) {
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

        fun startOrPause() {
            if (model?.media?.playerState == EnumMediaState.PLAYING) {
                model?.media?.pause()
                view?.getView()?.imgPlayPause?.setImageResource(model?.startResId!!)
            } else {
                model?.media?.start()
                view?.getView()?.imgPlayPause?.setImageResource(model?.pauseResId!!)
            }
        }

        fun fullOrSmall() {
            if (model?.curScreenMode == EventConstant.VALUE_SCREEN_SMALL) {
                view?.getView()?.notifyObservers(Event().setEventType(EnumMediaEventType.VIEW).setParameter(EventConstant.KEY_SCREEN_MODE, EventConstant.VALUE_SCREEN_FULL))
                model?.curScreenMode = EventConstant.VALUE_SCREEN_FULL
            } else if (model?.curScreenMode == EventConstant.VALUE_SCREEN_FULL) {
                view?.getView()?.notifyObservers(Event().setEventType(EnumMediaEventType.VIEW).setParameter(EventConstant.KEY_SCREEN_MODE, EventConstant.VALUE_SCREEN_SMALL))
                model?.curScreenMode = EventConstant.VALUE_SCREEN_SMALL
            }
        }
    }
}