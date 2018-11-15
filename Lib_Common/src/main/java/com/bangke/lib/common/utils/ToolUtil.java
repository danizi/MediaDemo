package com.bangke.lib.common.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一般方法
 */
public class ToolUtil {

  /**
   * 跳转到主页面
   *
   * @param activity
   */
  public static void joinMainActivity(Activity activity) {
    String pkg = "com.ponko.cn";
    String cls = "com.ponko.cn.ui.main.MainActivity";
    Intent mainIntent = new Intent();
    mainIntent.setComponent(new ComponentName(pkg, cls));
    activity.startActivity(mainIntent);
  }

  /**
   * 检查电话号码
   *
   * @param context
   * @param phone
   * @return 号码正常返回true, 反之false
   */
  public static boolean verifyPhoneNumber(Context context, String phone) {
    String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
    if (phone.length() != 11) {
      Toast.makeText(context, "手机号应为11位数", Toast.LENGTH_SHORT).show();
      return false;
    } else {
      Pattern p = Pattern.compile(regex);
      Matcher m = p.matcher(phone);
      boolean isMatch = m.matches();
      if (!isMatch) {
        Toast.makeText(context, "请填入正确的手机号", Toast.LENGTH_SHORT).show();
      }
      return isMatch;
    }
  }


  /**
   * 毫秒转化时分秒毫秒
   */
  public static String formatTime(Long ms) {
    Integer ss = 1000;
    Integer mi = ss * 60;
    Integer hh = mi * 60;
    Integer dd = hh * 24;

    Long day = ms / dd;
    Long hour = (ms - day * dd) / hh;
    Long minute = (ms - day * dd - hour * hh) / mi;
    Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
    Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

    StringBuffer sb = new StringBuffer();
    if (day > 0) {
      sb.append(day + "天");
    }
    if (hour > 0) {
      sb.append(hour + "小时");
    }
    if (minute > 0) {
      sb.append(minute + ":");
    }
    if (second > 0) {
      sb.append(second);
    }
//        if (milliSecond > 0) {
//            sb.append(milliSecond);
//        }
    return sb.toString();
  }

  /**
   * 秒转时分
   *
   * @param ms
   * @return
   */
  public static String formatTime(int ms) {
    Integer ss = 1;
    Integer mi = ss * 60;
    Integer hh = mi * 60;
    Integer dd = hh * 24;

    int day = ms / dd;
    int hour = (ms - day * dd) / hh;
    int minute = (ms - day * dd - hour * hh) / mi;
    int second = (ms - day * dd - hour * hh - minute * mi) / ss;
    int milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

    StringBuffer sb = new StringBuffer();
    if (day > 0) {
      sb.append(day + "天");
    }
    if (hour > 0) {
      sb.append(hour + "小时");
    }
    if (minute > 0) {
      sb.append(minute + ":");
    }
    if (second > 0) {
      sb.append(second);
    }
    return sb.toString();
  }

  /**
   * yy/MM/dd HH:mm:ss 如 '2018/1/1 17:55:00'
   * yy/MM/dd HH:mm:ss pm 如 '2018/1/1 17:55:00 pm'
   * yy-MM-dd HH:mm:ss 如 '2018-1-1 17:55:00'
   * yy-MM-dd HH:mm:ss am 如 '2018-1-1 17:55:00 am' 
   *
   * @param pattern
   * @param dateTime
   * @return
   */
  public static String getFormatedDateTime(String pattern, Double dateTime) {
    SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
    return sDateFormat.format(new Date((long) (dateTime + 0)));
  }
}
