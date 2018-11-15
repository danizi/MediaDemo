package com.bangke.lib.common.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bangke.lib.common.log.BkLog;
import com.bangke.lib.common.utils.SpUtil;

import java.util.HashMap;
import java.util.Map;

//import ponko.com.core.Constants;
//import ponko.com.core.net.BaseApi;

public class CusWebView extends WebView {

    private Map<String, Object> injection = new HashMap<>();

    public CusWebView(Context context) {
        super(context);
        init();
    }

    public CusWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CusWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("JavascriptInterface")
    public void injection(String injectionName, Object injectionObj) {
        this.addJavascriptInterface(injectionObj, injectionName);
        injection.put(injectionName, injectionObj);
    }

    private void init() {
        initSetting();
        initEvent();
        initData();
    }

    void initSetting() {
        WebSettings webSettings = this.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 默认浏览器缓存
        webSettings.setJavaScriptEnabled(true);            // 支持js
        webSettings.setDomStorageEnabled(true);
        webSettings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    private IWebViewClient iWebViewClient;

    public void setWebViewClient(IWebViewClient iWebViewClient) {
        this.iWebViewClient = iWebViewClient;
    }

    public interface IWebViewClient {
        void onPageFinished(WebView view, String url);
    }

    private void initEvent() {
        //支持获取手势焦点，输入用户名、密码或其他
        this.requestFocusFromTouch();
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });

        this.setWebViewClient(new WebViewClient() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:window.local_obj.getValueById(document.getElementById('productId').value);");
                view.loadUrl("javascript:window.local_obj.getWebHeight(document.body.scrollHeight);");
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                BkLog.e("加载网页失败:" + error);
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                // 接受所有网站的证书
                handler.proceed();
            }
        });
    }

    private void initData() {
        this.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
    }

    public Map<String, Object> getInjection() {
        return injection;
    }

    String HEADER_TOKEN_KEY = "x-tradestudy-access-token";
    String HEADER_ID_KEY = "x-tradestudy-access-key-id";
    String HEADER_VERSION_KEY = "x-tradestudy-client-version";
    String HEADER_DEVICE_KEY = "x-tradestudy-client-device";
    String TOKEN_KEY = "token_key";

    public Map<String, String> createaHttpHeaders() {
        Map<String, String> additionalHttpHeaders = new HashMap<>();
        additionalHttpHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        additionalHttpHeaders.put(HEADER_ID_KEY, "c");
        additionalHttpHeaders.put(HEADER_VERSION_KEY, "3.0.2");
        additionalHttpHeaders.put(HEADER_DEVICE_KEY, "android_phone");
        additionalHttpHeaders.put(HEADER_TOKEN_KEY, (String) SpUtil.getInstance().get(TOKEN_KEY, String.class));
        return additionalHttpHeaders;
    }

    /*注入对象*/
    public static String productId = null;
    public static int webHeight = 0;

    /**
     * 从H5中 获取 productId
     */
    public class InJavaScriptLocalObj {

        @JavascriptInterface
        public void getValueById(String value) {
            productId = value;
        }

        @JavascriptInterface
        public void getWebHeight(int value) {
            webHeight = value;
        }
    }
}
