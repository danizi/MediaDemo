package common.xm.com.xmcommon.media.mediaview

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.xm.lib.media.enum_.EnumMediaEventType
import com.xm.lib.media.event.Event
import com.xm.lib.media.event.EventConstant
import com.xm.lib.media.watcher.MediaViewObservable
import com.xm.lib.media.watcher.Observer
import common.xm.com.xmcommon.R


class MediaPreView(context: Context, layoutID: Int) : MediaViewObservable(context), Observer {

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
            notifyObservers(Event().setEventType(EnumMediaEventType.VIEW).setParameter(EventConstant.KEY_METHOD, "click"))
        }

        ivPlay?.setOnClickListener {

        }
    }

    override fun update(o: MediaViewObservable, event: Event) {
        //播放器事件
        if (event.eventType == EnumMediaEventType.MEDIA) {
            if ("onPrepared".equals(event.parameter?.get(EventConstant.KEY_METHOD))) {
                visibility = View.GONE
            }
        }
    }
}