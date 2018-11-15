package com.bangke.lib.common.base.ui.general;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bangke.lib.common.base.imp.Init;
import com.bangke.lib.common.utils.NetworkUtil;
import com.bangke.lib.common.utils.StatusBarUtil;
import com.bangke.lib.common.utils.ToolUtil;

import java.util.Map;

/**
 * 抽象Activity
 */
public abstract class AbsGeneralActivity extends AppCompatActivity implements Init, View.OnClickListener {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewbefore();
        setContentView(getLayoutView());
        setContentViewEnd();
        try {
            initView();
            initEvent();
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getLayoutView() {
        return LayoutInflater.from(this).inflate(getLayoutId(), null);
    }

    public void startActivity(Map<String, String> map, Class cls) {
        if (map.isEmpty()) return;

        Intent intent = new Intent(this, cls);
        for (Map.Entry entry : map.entrySet()) {
            intent.putExtra((String) entry.getKey(), (String) entry.getValue());
        }
        startActivity(intent);
    }

    public void startActivity(String key, String value, Class cls) {
        if (key.isEmpty() || value.isEmpty()) return;

        Intent intent = new Intent(this, cls);
        intent.putExtra(key, value);
        startActivity(intent);
    }

    public void setContentViewbefore() {
    }

    private class NetworkConnectChangedReceiver extends BroadcastReceiver {
        private static final String TAG = "NetworkConnectChanged";

        @Override
        public void onReceive(Context context, Intent intent) {
            //**判断当前的网络连接状态是否可用*/
            boolean isConnected = NetworkUtil.isNetworkConnected(context);
            boolean is3G = NetworkUtil.is3GNet(context);
            Log.d(TAG, "onReceive: isConnected " + isConnected);
            Log.d(TAG, "onReceive: is3G " + is3G);
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {
                    String name = netInfo.getTypeName();//网络连接名称

                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {//WiFi网络
                        //Toast.makeText(context, "wifi : " + name, Toast.LENGTH_SHORT).show();
                    } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {//有线网络
                        //Toast.makeText(context, "有线 : " + name, Toast.LENGTH_SHORT).show();
                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {//3g网络
                        Toast.makeText(context, "3G : " + name, Toast.LENGTH_SHORT).show();
                    }
                } else {//网络断开
                    Toast.makeText(context, "断开网络", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean networkConnectChangedReceiverFlag = false;
    private NetworkConnectChangedReceiver networkConnectChangedReceiver;
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo netInfo;

    public void setContentViewEnd() {
        //判断是否为刘海屏幕
        //修改状态栏字体颜色值
        StatusBarUtil.StatusBarLightMode(this);

        //注册网络状态广播
        networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkConnectChangedReceiver, intentFilter);
        networkConnectChangedReceiverFlag = true;
    }

    /*以下方法过滤掉*/
    @Override
    public void initView(View view) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁广播
        if (networkConnectChangedReceiverFlag && null != networkConnectChangedReceiver) {
            unregisterReceiver(networkConnectChangedReceiver);
        }
    }
}
