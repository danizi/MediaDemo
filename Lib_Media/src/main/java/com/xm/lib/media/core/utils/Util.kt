package com.xm.lib.media.core.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Point
import android.media.AudioManager
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

/**
 * 播放器使用的工具类
 */
class Util {

    companion object {
        fun showToast(context: Context?, msg: String?) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }

        fun showLog(msg: String?) {
            Log.e("Media", msg)
        }

        @SuppressLint("SimpleDateFormat")
        fun hhmmss(ms: Long?): String {
            val format = SimpleDateFormat("hh:mm:ss")
            Calendar.getInstance().timeInMillis = ms!!
            return format.format(Calendar.getInstance().time)
        }

        @SuppressLint("ObsoleteSdkInt")
        fun getNormalWH(activity: Activity): IntArray {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                val dm = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(dm)
                return intArrayOf(dm.widthPixels, dm.heightPixels)
            } else {
                val point = Point()
                val wm = activity.windowManager
                wm.defaultDisplay.getSize(point)
                //{宽,高}
                return intArrayOf(point.x, point.y)
            }
        }

        fun reSetStatusBar(activity: Activity) {
            // 重置状态栏
            if (isLandscape(activity)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    hideStatusBar(activity)
                }
            } else {
                setDecorVisible(activity)
            }
        }

        fun hideStatusBar(activity: Activity) {
            // 隐藏状态栏
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN)
            } else {
                val decorView = activity.window.decorView
                activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN)
                val uiOptions = (
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                                or
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                or
                                View.SYSTEM_UI_FLAG_IMMERSIVE
                                or
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                decorView.systemUiVisibility = uiOptions
            }
        }

        fun setDecorVisible(activity: Activity) {
            // 恢复为不全屏状态
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            } else {
                val decorView = activity.window.decorView
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
                decorView.systemUiVisibility = uiOptions
            }
        }

        /*---------------------------------------------
         * 横竖切屏幕相关
         */
        fun isPortrait(context: Context): Boolean {
            // 是否竖屏
            return context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        }

        fun isLandscape(context: Context): Boolean {
            // 是否横屏
            return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        }

        fun setPortrait(activity: Activity) {
            // 设置竖屏
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        }

        fun setLandscape(activity: Activity) {
            // 设置横屏
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        }

        /*---------------------------------------------
         * 设置手机屏幕亮度相关
         */
        fun getScreenBrightness(context: Context?): Float {
            //获取当前屏幕的亮度
            var nowBrightnessValue = 0
            val resolver = context?.contentResolver
            try {
                nowBrightnessValue = android.provider.Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return nowBrightnessValue / 255F
        }

        fun setCurWindowBrightness(context: Context?, brightness: Int?) {
            /*
             *设置亮度
             * 程序退出之后亮度失效
             * @param brightness 0 ` 255
             */
            var brightness = brightness
            // 如果开启自动亮度，则关闭。否则，设置了亮度值也是无效的
            if (IsAutoBrightness(context)) {
                stopAutoBrightness(context)
            }

            // context转换为Activity
            val activity = context as Activity
            val lp = activity.window.attributes

            // 异常处理
            if (brightness != null) {
                if (brightness < 1) {
                    brightness = 1
                }
            }
            // 异常处理
            if (brightness != null) {
                if (brightness > 255) {
                    brightness = 255
                }
            }
            lp.screenBrightness = java.lang.Float.valueOf(brightness?.toFloat()!!) * (1f / 255f)
            activity.window.attributes = lp
        }

        fun setSystemBrightness(context: Context?, brightness: Float?) {
            /*
             * 设置系统亮度
             * 程序退出之后亮度依旧有效
             *  @param brightness 0 ` 255
             */
            var brightness = brightness
            // 异常处理
            if (brightness != null) {
                if (brightness < 0) {
                    brightness = 0.0f
                }
            }
            // 异常处理
            if (brightness != null) {
                if (brightness > 1) {
                    brightness = 1f
                }
            }
            saveBrightness(context, (brightness!! * 255).toInt())
        }

        fun stopAutoBrightness(context: Context?) {
            /*
             * 停止自动亮度调节
             */
            Settings.System.putInt(
                    context?.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
        }

        fun IsAutoBrightness(context: Context?): Boolean {
            /*
             * 判断是否开启了自动亮度调节
             */
            var IsAutoBrightness = false
            try {
                IsAutoBrightness = Settings.System.getInt(context?.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE) === Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
            }
            return IsAutoBrightness
        }

        fun startAutoBrightness(context: Context?) {
            /*
             * 开启亮度自动调节
             * 首先，需要明确屏幕亮度有两种调节模式：
             * Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC：值为1，自动调节亮度。
             * Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL：值为0，手动模式。
             * 如果需要实现亮度调节，首先需要设置屏幕亮度调节模式为手动模式。
             */
            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
            Settings.System.putInt(context?.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
        }

        fun saveBrightness(context: Context?, brightness: Int?) {
            /*
             * 保存亮度设置状态
             */
            val uri = android.provider.Settings.System.getUriFor("screen_brightness")
            android.provider.Settings.System.putInt(context?.contentResolver, "screen_brightness", brightness!!)
            context?.contentResolver?.notifyChange(uri, null)
        }

        /*---------------------------------------------
         * 设置手机音量相关
         */
        fun getVolume(context: Context?): Float {
            /*
             * 系统的是：0到Max，Max不确定，这个称为：系统的音量范围。
             * STREAM_ALARM 警报
             * STREAM_MUSIC 音乐回放即媒体音量
             * STREAM_RING 铃声
             * STREAM_SYSTEM 系统
             * STREAM_VOICE_CALL 通话
             */
            val audioManager: AudioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat() / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        }

        fun setVolume(context: Context?, percent: Float) {
            /*
             * KeyEvent.KEYCODE_VOLUME_UP   +
             * KeyEvent.KEYCODE_VOLUME_DOWN - 不靠谱 要换另一种
             * @param index 0~100
             */
            val audioManager: AudioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val index: Int? = if (percent > 1) {
                (1f * audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).toInt()
            } else {
                (percent * audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).toInt()
            }
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index!!, AudioManager.FLAG_PLAY_SOUND)
        }
    }
}