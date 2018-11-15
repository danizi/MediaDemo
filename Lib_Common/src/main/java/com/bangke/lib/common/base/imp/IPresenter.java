package com.bangke.lib.common.base.imp;

import com.bangke.lib.common.base.ui.mvp.IView;

public interface IPresenter<V extends IView> {
    /**
     * 绑定View层
     *
     * @param v
     */
    void onAttach(V v);

    /**
     * 解除View层
     */
    void onDetch();
}
