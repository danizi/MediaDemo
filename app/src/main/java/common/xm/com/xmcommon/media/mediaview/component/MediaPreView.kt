package common.xm.com.xmcommon.media.mediaview.component

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.xm.lib.media.AbsMediaCore
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable
import com.xm.lib.media.watcher.Observer
import common.xm.com.xmcommon.R


class MediaPreView(context: Context, layoutID: Int) : MediaViewObservable(context){

    var media: AbsMediaCore? = null
    var preUrl = "http://img.videocc.net/uimage/2/26de49f8c2/9/26de49f8c273bbc8f6812d1422a11b39_1.jpg"
    var ivPre: ImageView? = null
    var ivPlay: ImageView? = null

    init {
        contentView = getContentView(layoutID)
        addView(contentView)

        ivPre = findViewById(R.id.img_preview)
        ivPlay = findViewById(R.id.img_play)

        Glide.with(context).load(preUrl).into(ivPre)

        ivPre?.setOnClickListener {
            visibility = View.GONE
            media?.prepareAsync()

        }

        ivPlay?.setOnClickListener {
            media?.prepareAsync()
        }
    }

    override fun update(o: MediaViewObservable, event: Event) {
        //播放器事件
        if (event.eventType == EnumMediaEventType.MEDIA) {

            //播放器创建完成通知
            if (EventConstant.VALUE_METHOD_CORE == event.parameter?.get(EventConstant.KEY_METHOD)) {
                media = event.parameter?.get("mp") as AbsMediaCore?
            }

            if (EventConstant.VALUE_METHOD_ONPREPARED == event.parameter?.get(EventConstant.KEY_METHOD)) {
                visibility = View.GONE
            }
        }

//        if (event.eventType == EnumMediaEventType.VIEW) {
//            if (event.parameter?.get(EventConstant.KEY_FROM) == EventConstant.VALUE_FROM_MEDIACOMPONENT) {
//                when (event.parameter?.get(EventConstant.KEY_METHOD)) {
//                    EventConstant.VALUE_METHOD_CLICK -> {
//                        visibility = View.GONE
//                    }
//                }
//            }
//        }
    }
}