package common.xm.com.xmcommon.media

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatSeekBar
import android.util.Log
import android.view.*
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.xm.lib.ijkplayer.IJKPlayer
import com.xm.lib.media.core.AbsMediaCore
import common.xm.com.xmcommon.R
import java.text.SimpleDateFormat
import java.util.*

class ActivityIjkplayer : AppCompatActivity() {
    private val tag = "ActivityIjkplayer"
    /*---------------------------------
     * 播放器
     */
    private var player: AbsMediaCore? = IJKPlayer()
    /*---------------------------------
     * 展示播放器信息的控件
     */
    private var sv: SurfaceView? = null
    private var seekBar: AppCompatSeekBar? = null
    private var tvPos: TextView? = null
    private var tvDuration: TextView? = null
    private var tvTcp: TextView? = null
    /*---------------------------------
     * 定时器
     */
    val handler = Handler()
    val task = object : TimerTask() {
        override fun run() {
            handler.post {
                val pos: Int = player?.getCurrentPosition()?.toInt()!!
                tvPos?.text = "播放进度:${hhmmss(pos.toLong())}"
                seekBar?.progress = pos
                tvTcp?.text = "资源下行速度:" + player?.getTcpSpeed()
            }
        }
    }
    val timer = Timer()
    /*---------------------------------
     * 保存播放器的状态
     */
    private var savaPos: Long? = 0
    private var isSurfaceDestroyed: Boolean? = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ijkplayer)
        findViews()
        initEvent()
    }

    private fun findViews() {
        sv = findViewById(R.id.sv)
        seekBar = findViewById(R.id.seekbar)
        tvPos = findViewById(R.id.tv_pos)
        tvDuration = findViewById(R.id.tv_duration)
        tvTcp = findViewById(R.id.tv_tcp)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEvent() {
        sv?.holder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                showLog("surfaceChanged")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                showLog("surfaceDestroyed")
                savaPos = player?.getCurrentPosition()
                player?.stop()
                isSurfaceDestroyed = true
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                showLog("surfaceCreated")
                savaPos = 0
                //todo 必须等待画布创建完成设置,不然会出现有声音没画面的现象
                player?.setDisplay(holder)
                isSurfaceDestroyed = false
            }
        })
        player?.setOnPreparedListener(object : AbsMediaCore.OnPreparedListener {
            override fun onPrepared(mp: AbsMediaCore) {
                player?.start()
                seekBar?.max = player?.getDuration()?.toInt()!!
                tvDuration?.text = "视频总时长:" + hhmmss(player?.getDuration())
                //开启一个定时器显示当前播放时长
                timer.schedule(task, 0, 1000)
            }
        })
        player?.setOnErrorListener(object : AbsMediaCore.OnErrorListener {
            override fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                showToast("onError what:" + what + "extra:" + extra)
                return false
            }
        })
        player?.setOnBufferingUpdateListener(object : AbsMediaCore.OnBufferingUpdateListener {
            override fun onBufferingUpdate(mp: AbsMediaCore, percent: Int) {
                seekBar?.secondaryProgress = ((percent.toFloat() / 100f) * player?.getDuration()?.toFloat()!!).toInt()
            }
        })
        player?.setOnCompletionListener(object : AbsMediaCore.OnCompletionListener {
            override fun onCompletion(mp: AbsMediaCore) {
                showToast("播放完成")
            }
        })
        player?.setOnInfoListener(object : AbsMediaCore.OnInfoListener {
            override fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                return false
            }
        })
        player?.setOnVideoSizeChangedListener(object : AbsMediaCore.OnVideoSizeChangedListener {
            override fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int) {

            }
        })
        player?.setOnTimedTextListener(object : AbsMediaCore.OnTimedTextListener {
            override fun onTimedText(mp: AbsMediaCore, text: String) {

            }
        })
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            var progress: Int? = null
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                this.progress = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                player?.seekTo(progress?.toLong())
            }
        })
        sv?.setOnTouchListener { v, event ->
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_media_action, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_prepareAsync -> {
                player?.setDataSource("http://hls.videocc.net/26de49f8c2/9/26de49f8c273bbc8f6812d1422a11b39_2.m3u8")
                player?.prepareAsync()
            }
            R.id.menu_start -> {
                player?.start()
            }
            R.id.menu_pause -> {
                player?.pause()
            }
            R.id.menu_stop -> {
                player?.stop()
            }
            R.id.menu_state -> {
                showToast("当前播放器播放状态:" + player?.playerState)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun showLog(msg: String?) {
        Log.e(tag, msg)
    }

    private fun hhmmss(ms: Long?): String {
        val format = SimpleDateFormat("hh:mm:ss")
        Calendar.getInstance().timeInMillis = ms!!
        return format.format(Calendar.getInstance().time)
    }

    override fun onRestart() {
        super.onRestart()
        if (false) {
            sv?.holder?.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                    showLog("surfaceChanged")
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    showLog("surfaceDestroyed")
                    savaPos = player?.getCurrentPosition()
                    player?.stop()
                    isSurfaceDestroyed = true
                }

                override fun surfaceCreated(holder: SurfaceHolder?) {
                    showLog("surfaceCreated")
                    //todo 必须等待画布创建完成设置,不然会出现有声音没画面的现象
                    player?.setDisplay(holder)
                    savaPos = 0
                    isSurfaceDestroyed = false
                }
            })
            player?.setOnPreparedListener(object : AbsMediaCore.OnPreparedListener {
                override fun onPrepared(mp: AbsMediaCore) {
                    player?.seekTo(savaPos)
                    seekBar?.max = player?.getDuration()?.toInt()!!
                    tvDuration?.text = "视频总时长:" + player?.getDuration()
                    //开启一个定时器显示当前播放时长
                    timer.schedule(task, 0, 1000)
                }
            })
            player?.setOnErrorListener(object : AbsMediaCore.OnErrorListener {
                override fun onError(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                    showToast("onError what:" + what + "extra:" + extra)
                    return false
                }
            })
            player?.setOnBufferingUpdateListener(object : AbsMediaCore.OnBufferingUpdateListener {
                override fun onBufferingUpdate(mp: AbsMediaCore, percent: Int) {
                    seekBar?.secondaryProgress = ((percent.toFloat() / 100f) * player?.getDuration()?.toFloat()!!).toInt()
                }
            })
            player?.setOnCompletionListener(object : AbsMediaCore.OnCompletionListener {
                override fun onCompletion(mp: AbsMediaCore) {
                    showToast("播放完成")
                }
            })
            player?.setOnInfoListener(object : AbsMediaCore.OnInfoListener {
                override fun onInfo(mp: AbsMediaCore, what: Int, extra: Int): Boolean {
                    return false
                }
            })
            player?.setOnVideoSizeChangedListener(object : AbsMediaCore.OnVideoSizeChangedListener {
                override fun onVideoSizeChanged(mp: AbsMediaCore, width: Int, height: Int, sar_num: Int, sar_den: Int) {

                }
            })
            player?.setOnTimedTextListener(object : AbsMediaCore.OnTimedTextListener {
                override fun onTimedText(mp: AbsMediaCore, text: String) {

                }
            })
            seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                var progress: Int? = null
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    this.progress = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    player?.seekTo(progress?.toLong())
                }
            })
            player?.setDataSource("http://hls.videocc.net/26de49f8c2/9/26de49f8c273bbc8f6812d1422a11b39_2.m3u8")
            player?.prepareAsync()
        }
    }
}
