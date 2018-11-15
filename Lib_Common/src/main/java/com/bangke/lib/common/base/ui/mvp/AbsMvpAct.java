package com.bangke.lib.common.base.ui.mvp;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.bangke.lib.common.base.ui.general.AbsGeneralActivity;

/**
 * mvp模式抽象基类
 *
 * @param <V>
 * @param <P>
 */
public abstract class AbsMvpAct<V extends IView, P extends AbsPresenter> extends AbsGeneralActivity {
    private static final String TAG = "AbsMvpAct";
    protected P p;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        p = presenter();
        onAttach();
        super.onCreate(savedInstanceState);
    }

    public abstract P presenter();

    private void onAttach() {
        if (p != null) {
            p.onAttach((V) this);
        } else {
            Log.e(TAG, "p层获取为null无法绑定v层,请设置P层");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onAttach();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (p != null) {
            p.onDetch();
        } else {
            Log.e(TAG, "p层获取为null无法回收v层,请设置P层");
        }
    }
}
