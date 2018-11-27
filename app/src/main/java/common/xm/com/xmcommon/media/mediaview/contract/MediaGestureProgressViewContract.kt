package common.xm.com.xmcommon.media.mediaview.contract

import android.content.Context
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.contract.BaseMediaContract
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.R
import common.xm.com.xmcommon.media.mediaview.component.MediaGestureProgressView

class MediaGestureProgressViewContract {
    interface View : BaseMediaContract.View<MediaGestureProgressView> {

    }

    class Model : BaseMediaContract.Model() {
        var fastBackwardResID: Int = R.mipmap.media_fast_backward  //快退
        var fastForwardResID: Int = R.mipmap.media_fast_forward    //快进
        var progressResID: Int = fastForwardResID
        var media: AbsMediaCore? = null
    }

    class Present(val context: Context?, val view: View?) : BaseMediaContract.Present() {
        var model: Model = Model()

        override fun process() {
            view?.getView()?.iv?.setImageResource(model.progressResID)
        }

        override fun handleMediaEvent(o: MediaViewObservable<*>?, event: Event?) {
            if (event?.parameter?.get(EventConstant.KEY_FROM) == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
                when (event.parameter?.get(EventConstant.KEY_METHOD)) {
                    EventConstant.VALUE_METHOD_CORE -> {
                        model.media = event.parameter?.get("mp") as AbsMediaCore?
                    }
                }
            }
        }

        var curProgress: Int = -1
        var present: Float? = 0f
        override fun handleViewEvent(o: MediaViewObservable<*>?, event: Event?) {
            if (event?.parameter?.get(EventConstant.KEY_FROM) == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
                when (event.parameter?.get(EventConstant.KEY_METHOD)) {
                    EventConstant.VALUE_METHOD_ONPROGRESS -> {
                        //当前媒体音量

                        if (curProgress == -1) {
                            curProgress = view?.getView()?.progress?.progress!!
                        }
                        //当前的播放进度
                        present = (model.media?.getCurrentPosition()!! / model.media?.getDuration()!!) + event.parameter?.get("percent") as Float
                        //获取滑动的距离再加
                        view?.getView()?.progress?.progress = curProgress + (present!! * 100).toInt()
                        view?.showView()
                    }
                    EventConstant.VALUE_METHOD_UP -> {
                        view?.hideView()
                        curProgress = -1
                        //设置进度
                        model.media?.seekTo((present!! * model.media?.getDuration()!!).toLong())
                    }
                }
            }
        }

        override fun handleOtherEvent(o: MediaViewObservable<*>?, event: Event?) {

        }

    }
}