package common.xm.com.xmcommon.media

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import common.xm.com.xmcommon.R
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import com.xm.lib.ijkplayer.IJKPlayer
import com.xm.lib.media.core.AbsMediaCore

class ActivityIjkplayer : AppCompatActivity(), View.OnClickListener {
    private val tag = "ActivityIjkplayer"
    private var player: IJKPlayer? = null
    private var sh: SurfaceHolder? = null
    private var sv: SurfaceView? = null
    private var btnStart: Button? = null
    private var btnPause: Button? = null
    private var btnStop: Button? = null
    private var btnSpeed: Button? = null
    private var btnSeek: Button? = null
    private var tate: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ijkplayer)
        findViews()

        player = IJKPlayer()
        player?.init()
        sh = sv?.holder
        sh?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                Log.e(tag, "surfaceChanged")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                Log.e(tag, "surfaceDestroyed")
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                Log.e(tag, "surfaceCreated")
            }
        })
        player?.setDataSource("http://hls.videocc.net/26de49f8c2/9/26de49f8c273bbc8f6812d1422a11b39_2.m3u8")
        player?.setDisplay(sh)
        player?.setOnPreparedListener(object : AbsMediaCore.OnPreparedListener {
            override fun onPrepared(mp: AbsMediaCore) {
                player?.start()
            }
        })
    }

    private fun findViews() {
        sv = findViewById(R.id.sv)
        btnStart = findViewById(R.id.btn_start)
        btnPause = findViewById(R.id.btn_pause)
        btnStop = findViewById(R.id.btn_stop)
        btnSpeed = findViewById(R.id.btn_speed)
        btnSeek = findViewById(R.id.btn_seek)
        tate = findViewById(R.id.tate)

        btnStart!!.setOnClickListener(this)
        btnPause!!.setOnClickListener(this)
        btnStop!!.setOnClickListener(this)
        btnSpeed!!.setOnClickListener(this)
        btnSeek!!.setOnClickListener(this)
        tate!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when {
            v === btnStart -> {
                player?.prepareAsync()
            }
            v === btnPause -> {
            }
            v === btnStop -> {
            }
            v === btnSpeed -> {
            }
            v === btnSeek -> {
            }
            v === tate -> {
            }
        }
    }
}
