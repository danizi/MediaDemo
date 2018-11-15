package com.bangke.lib.common.base.ui.general;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bangke.lib.common.base.imp.Init;

/**
 * 抽象Fragment
 */
public abstract class AbsGeneralFragment extends Fragment implements Init {

    private View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            try {
                rootView = inflater.inflate(getLayoutId(), container, false);
                initView(rootView);
                initEvent();
                initData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rootView;
    }

    public View getRootView() {
        return rootView;
    }

    /*以下方法过滤掉*/
    @Override
    public void initView() {
    }

    @Override
    public View getLayoutView() {
        return null;
    }
}
