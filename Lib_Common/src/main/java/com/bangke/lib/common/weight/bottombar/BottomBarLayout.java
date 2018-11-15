package com.bangke.lib.common.weight.bottombar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 底部菜单
 * 1 传入图标 文字
 * 2 绑定容器（ViewpPager、普通容器）
 * 3 选择动画
 * 4 链式调用
 */
public class BottomBarLayout extends FrameLayout {
    public BottomBarLayout(Context context) {
        super(context);
    }

    public BottomBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
