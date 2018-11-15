package com.bangke.lib.common.base.imp;

import android.view.View;

/**
 * activity和fragment的初始化接口
 */
public interface Init {
    /**
     * 布局id
     *
     * @return
     */
    int getLayoutId();

    /**
     * 布局View
     *
     * @return
     */
    View getLayoutView();

    /**
     * fragment初始化View
     *
     * @param view
     */
    void initView(View view);

    /**
     * Activity初始化View
     */
    void initView();

    /**
     * 初始化事件
     */
    void initEvent();

    /**
     * 初始化数据
     */
    void initData();
}
