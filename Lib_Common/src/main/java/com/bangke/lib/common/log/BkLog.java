package com.bangke.lib.common.log;

import android.util.Log;

public class BkLog {
    private static final String TAG = "bangke";
    private static boolean isDebug = true;

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }
}
