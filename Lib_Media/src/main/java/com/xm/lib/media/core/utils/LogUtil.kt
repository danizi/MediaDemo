package com.xm.lib.media.core.utils

import android.util.Log

class LogUtil {
    companion object {
        private const val I: Int = 4
        private const val D: Int = 3
        private const val W: Int = 2
        private const val E: Int = 1
        /**
         * 从高到底排列 I > D > W >E
         */
        var level: Int = E

        fun i(tag: String?, msg: String?) {
            if (level >= I) {
                Log.i("Media", "$tag -> $msg")
            }
        }

        fun d(tag: String?, msg: String?) {
            if (level >= D) {
                Log.d("Media", "$tag -> $msg")
            }
        }

        fun w(tag: String?, msg: String?) {
            if (level >= W) {
                Log.w("Media", "$tag -> $msg")
            }
        }

        fun e(tag: String?, msg: String?) {
            if (level >= E) {
                Log.e("Media", "$tag -> $msg")
            }
        }
    }
}