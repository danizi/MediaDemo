package com.bangke.lib.common.weight;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

/**
 * 广告接口
 */
interface IAdView {
    AdView url(String url);

    AdView adType(AdView.AdType adType);

    AdView setDiaLogContentView(int layoutId);

    void show();
}

/**
 * 广告组件展示图片&视频
 */
public class AdView extends FrameLayout implements IAdView {
    enum AdType {
        IMAGE, //展示图片
        VIDEO, //视频
        DIALG, //弹框
    }

    //图片|| 视频|| 本地 地址
    private String url;
    //广告类型
    private AdType adType;
    //广告
    private int dlgLayoutId;

    public AdView(Context context) {
        super(context);
    }

    public AdView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public AdView url(String url) {
        Build.newInstance().setUrl(url);
        return this;
    }

    @Override
    public AdView adType(AdType adType) {
        Build.newInstance().setAdType(adType);
        return this;
    }

    @Override
    public AdView setDiaLogContentView(int layoutId) {
        Build.newInstance().setDlgLayoutId(layoutId);
        return this;
    }

    @Override
    public void show() {
        if (adType == AdType.DIALG) {
            Dialog adDlg = new Dialog(getContext());
            adDlg.setContentView(Build.newInstance().getDlgLayoutId());
            //设置透明背景
            adDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            //设置点击屏幕dialog不消失
            adDlg.setCancelable(false);
            //设置监听
            adDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
            adDlg.show();
        } else if (adType == AdType.IMAGE) {
            ImageView img = new ImageView(getContext());
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            img.setLayoutParams(layoutParams);
            addView(img);
            Glide.with(getContext()).load(Build.newInstance().getUrl()).into(img);

        } else if (adType == AdType.VIDEO) {
            VideoView vv = new VideoView(getContext());
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            vv.setLayoutParams(layoutParams);
            addView(vv);
            String url = Build.newInstance().getUrl();
            if (!!TextUtils.isEmpty(url)) {
                if (url.startsWith("http") || url.startsWith("https")) {
                    vv.setVideoURI(Uri.parse(url));
                } else {
                    vv.setVideoPath(url);
                }
            }
        }
    }

    /**
     * 建造类
     */
    public static class Build {
        private static Build INSTANCE = null;

        public static Build newInstance() {
            if (INSTANCE == null) {
                synchronized (Build.class) {
                    if (INSTANCE == null) {
                        INSTANCE = new Build();
                    }
                }
            }
            return INSTANCE;
        }

        //图片|| 视频|| 本地 地址
        private String url;
        //广告类型
        private AdType adType;
        //广告
        private int dlgLayoutId;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public AdType getAdType() {
            return adType;
        }

        public void setAdType(AdType adType) {
            this.adType = adType;
        }

        public int getDlgLayoutId() {
            return dlgLayoutId;
        }

        public void setDlgLayoutId(int dlgLayoutId) {
            this.dlgLayoutId = dlgLayoutId;
        }
    }
}

