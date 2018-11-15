package com.bangke.lib.common.base.ui.mvp;

import android.content.Context;
import android.util.Log;

/**
 * 抽象P层
 *
 * @param <V>
 */
public abstract class AbsPresenter<V extends IView> extends Object {
    private static final String TAG = "AbsPresenter";
    protected V v = null;

    /**
     * 绑定View层
     *
     * @param v
     */
    protected void onAttach(V v) {
        if (v == null) {
            throw new NullPointerException("异常:传入v层是null");
        }
        this.v = v;
    }

    /**
     * 解除View层
     */
    protected void onDetch() {
        if (v != null) {
            v.onClear();
            v = null;
        }
    }

    protected boolean checkNull(String flag, Context context, Object model, Object view) {
        if (null == context) {
            Log.e(TAG, "context is null");
            return true;
        }

        if (null == model) {
            Log.e(TAG, "model is null");
            return true;
        }

        if (null == view) {
            Log.e(TAG, "view is null");
            return true;
        }
        return false;
    }

    public void process() {

    }

}
