package common.xm.com.xmcommon.mediaview

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
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
            notifyObserver("MediaPreView", "ivPre click")
        }

        ivPlay?.setOnClickListener {

        }
    }

    override fun update(o: MediaViewObservable, vararg arg: Any) {
        Log.e("MediaPreView", "MediaPreView 观察者接受：" + arg[0])
    }

}