package com.xm.lib.media.component.control

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.xm.lib.media.R
import com.xm.lib.media.watcher.MediaViewObservable
import common.xm.com.xmcommon.media.mediaview.contract.MediaVControlViewContract

class MediaVControlView(context: Context?, layoutID: Int?) : MediaViewObservable<MediaVControlViewContract.Present>(context!!, layoutID!!), MediaVControlViewContract.View {

    var imgPlayPause: ImageView? = null
    var tvCurrentPosition: TextView? = null
    var seekBar: SeekBar? = null
    var tvDuration: TextView? = null
    var imgScreenMode: ImageView? = null

    override fun createPresent(): MediaVControlViewContract.Present {
        return MediaVControlViewContract.Present(context, this)
    }

    override fun findViews(contentView: View?) {
        imgPlayPause = contentView?.findViewById(R.id.img_play_pause)
        tvCurrentPosition = contentView?.findViewById(R.id.tv_currentPosition)
        seekBar = contentView?.findViewById(R.id.seekBar)
        tvDuration = contentView?.findViewById(R.id.tv_duration)
        imgScreenMode = contentView?.findViewById(R.id.img_screen_mode)
    }

    override fun initListenner() {
        imgPlayPause?.setOnClickListener {
            getPresent()?.startOrPause()
        }

        imgScreenMode?.setOnClickListener {
            getPresent()?.fullOrSmall()
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                getPresent()?.onProgressChanged(progress, fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                getPresent()?.onStartTrackingTouch(seekBar)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                getPresent()?.onStopTrackingTouch(seekBar)
            }
        })
    }

    override fun getView(): MediaVControlView {
        return this
    }

}

