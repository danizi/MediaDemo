package com.bangke.lib.common.base.ui.mvp;

/**
 * View层基类接口
 */
public interface IView {

    /**
     * 页面状态枚举
     */
    public enum EnumPageState {
        LOADING,     //页面加载中
        LOAD_FAILURE,//加载失败
        NO_NETWORK   //断网状态
    }

    /**
     * 回收缓存
     */
    void onClear();


    /**
     * 页面状态
     */
    void state(EnumPageState enumPageState);
}
