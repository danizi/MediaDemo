package com.bangke.lib.common.act;

import android.view.View;

import com.bangke.lib.R;
import com.bangke.lib.common.base.ui.general.AbsGeneralActivity;
import com.bangke.lib.common.weight.CusWebView;

public class ActPayWebView extends AbsGeneralActivity {

    private CusWebView webView;

    @Override
    public void initView() {
        webView = findViewById(R.id.webview);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.web;
    }

    @Override
    public void onClick(View v) {

    }
}
