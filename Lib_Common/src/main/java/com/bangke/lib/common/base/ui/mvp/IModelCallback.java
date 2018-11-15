package com.bangke.lib.common.base.ui.mvp;

/**
 * model层数据回调基类接口
 *
 * @param <B>
 */
public interface IModelCallback<B> {
    void success(B data);

    void failure(Throwable t);
}
