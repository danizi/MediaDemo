package com.bangke.lib.common.base.ui.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bangke.lib.common.base.ui.general.AbsGeneralFragment;

/**
 * {@link AbsMvpFragment#onAttach(Context)}
 * @param <V>
 * @param <P>
 */
public abstract class AbsMvpFragment<V extends IView, P extends AbsPresenter> extends AbsGeneralFragment implements View.OnClickListener {

    private static final String TAG = "AbsMvpFragment";
    protected P p;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getRootView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        p = presenter();
        onAttach();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public abstract P presenter();

    @Override
    public void onResume() {
        super.onResume();
        onAttach();
    }

    private void onAttach() {
        if (p != null) {
            p.onAttach((V) this);
        } else {
            Log.e(TAG, "p层获取为null无法绑定v层,请设置P层");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (p != null) {
            p.onDetch();
        } else {
            Log.e(TAG, "p层获取为null无法回收v层,请设置P层");
        }
    }

}
