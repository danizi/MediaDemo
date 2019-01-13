package com.xm.lib.media.core.attach.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.xm.lib.media.R
import com.xm.lib.media.core.AbsMediaCore
import com.xm.lib.media.core.AbsAttachLayout
import com.xm.lib.media.core.utils.Util

/**
 * 播放器附着预览页面
 */
class AttachPreViewLayout : AbsAttachLayout {

    private var preView: View? = null
    private var imgPreview: ImageView? = null
    private var imgPlay: ImageView? = null
    private var seekBar: SeekBar? = null

    constructor(context: Context, layout: Int) : super(context) {
        preView = Util.getView(context, layout)!!

        imgPreview = preView?.findViewById(R.id.img_preview)
        imgPlay = preView?.findViewById(R.id.img_play)
        seekBar = preView?.findViewById(R.id.seekbar)
        imgPlay?.visibility = View.VISIBLE
        seekBar?.visibility = View.GONE

        imgPreview?.setOnClickListener {
            seekBar?.visibility = View.VISIBLE
            imgPlay?.visibility = View.GONE
            videoView?.prepareAsync()
        }

        Glide.with(context).load("http://img4.imgtn.bdimg.com/it/u=546534595,3069204591&fm=26&gp=0.jpg").into(imgPreview)
        addView(preView)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onPrepared(mp: AbsMediaCore) {
        super.onPrepared(mp)
        preView?.visibility = View.GONE
    }
}