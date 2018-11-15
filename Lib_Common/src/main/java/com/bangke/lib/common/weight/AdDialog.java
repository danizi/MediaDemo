package com.bangke.lib.common.weight;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by byp on 2018/10/7.
 */
public class AdDialog extends Dialog {
    private final String Tag = this.getClass().getSimpleName();

    private ImageView adImg;
    private ImageView closeImg;


    public AdDialog(@NonNull Context context) {
        super(context);
    }

    public AdDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected AdDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private View view;

    public View getContentView() {
        return view;
    }

    //链接
    public AdDialog set(int dialogLayoutId) {
        view = LayoutInflater.from(getContext()).inflate(dialogLayoutId, null);
        //设置对话框UI界面
        setContentView(view);
        //设置透明背景
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //设置点击屏幕dialog不消失
        setCanceledOnTouchOutside(false);
        //设置监听
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d(Tag, "dismiss");
            }
        });
        return this;
    }
}
