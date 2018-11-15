package com.bangke.lib.common.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

public class StateView extends FrameLayout {

    private static final String TAG = "StateView";
    /**
     * 存储用户配置的View,Key是View的类型,valule是View
     */
    private Map<State, View> stateViews = new HashMap<>();

    /**
     * 页面状态
     */
    public enum State {
        NETWORK_LOADING, //加载网络数据中
        NETWORK_ERROR,   //加载网络数据错误
        NODATA,          //无数据
        COMPLETE         //加载完成
    }

    public StateView(Context context) {
        super(context);
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addStatePage(State state, int viewId) {
        View child = LayoutInflater.from(getContext()).inflate(viewId, null);
        addView(child);
        stateViews.put(state, child);
    }

    public View getErrorView() {
        View view = stateViews.get(State.NETWORK_ERROR);
        if (view == null) {
            throw new NullPointerException("StateView:网络加载错误页面未添加");
        }
        return view;
    }

    public void show(State state) {
        if (state == StateView.State.COMPLETE) {
            hide();
            return;
        }

        if (state == StateView.State.NETWORK_LOADING || state == StateView.State.NETWORK_ERROR || state == StateView.State.NETWORK_ERROR) {
            View child = stateViews.get(state);
            if (null != child) {
                removeAllViews();//清除之前所有的页面
                addView(child); //添加View
                this.setVisibility(VISIBLE);
            } else {
                Log.e(TAG, "child is null");
            }
        }
    }

    private void hide() {
        this.setVisibility(View.GONE);
    }
}
