package com.bangke.lib.common.utils;

import android.annotation.SuppressLint;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

public class TextViewUtil {

    public interface ClickableSpanText {
        void onClick(View widget);
    }
    /**
     * @param textView          目标控件
     * @param text              显示的内容
     * @param keyword           需要改变颜色的关键字
     * @param colorId           关键字设置的颜色
     * @param clickableSpanText 点击监听
     */
    @SuppressLint("ResourceAsColor")
    public static void getSpannableTextColor(final TextView textView, String text, String keyword, final int colorId, final ClickableSpanText clickableSpanText) {

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        if (text.contains(keyword)) {
            int spanStartIndex = text.indexOf(keyword);
            int spanEndIndex = spanStartIndex + keyword.length();
            //改变字体颜色,不知道为什么无效
            spannableStringBuilder.setSpan(new ForegroundColorSpan(colorId), spanStartIndex, spanEndIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            //跳转
            spannableStringBuilder.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    clickableSpanText.onClick(widget);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    //改变字体颜色
                    ds.setColor(textView.getContext().getResources().getColor(colorId));
                    //去掉下划线
                    ds.setUnderlineText(false);
                }
            }, spanStartIndex, spanEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spannableStringBuilder);
        //加上这句话，跳转才能成功
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
