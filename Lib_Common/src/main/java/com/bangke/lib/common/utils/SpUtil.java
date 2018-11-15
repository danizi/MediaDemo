package com.bangke.lib.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

public class SpUtil {
    private static SpUtil instance = new SpUtil();
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor = null;

    public static SpUtil getInstance() {
        return instance;
    }

    public static void clear(Context context, String SharedPreferencesName) {
        SharedPreferences preferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();

        editor.commit();
    }

    /**
     * @param SharedPreferencesName 文件名称
     * @param mode                  Context.MODE_PRIVATE         指定该SharedPreferences数据只能被本应用读、写。
     *                              Context.MODE_WORLD_READABLE  指定该SharedPreferences数据也能被其他应用程序读，但不能写。
     *                              Context.MODE_WORLD_WRITEABLE 指定该SharedPreferences数据也能被其他应用程序读、写。
     *                              Context.MODE_WORLD_APPEND    文件是否存在，存在那么追加，否则新建
     * @param context
     */
    public void init(String SharedPreferencesName, int mode, Context context) {
        sharedPreferences = context.getSharedPreferences(SharedPreferencesName, mode);
        editor = sharedPreferences.edit();
    }

    /**
     * 添加数据项与key对应
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        if (value == null) return;

        if (value.getClass() == String.class) {
            editor.putString(key, (String) value);
        } else if (value.getClass() == Integer.class) {
            editor.putInt(key, (Integer) value);
        } else if (value.getClass() == Set.class) {
            editor.putStringSet(key, (Set<String>) value);
        } else if (value.getClass() == Boolean.class) {
            editor.putBoolean(key, (Boolean) value);
        }
        editor.commit();
    }

    /**
     * 获取对应key的数据
     *
     * @param key
     * @param targeTypeCls
     * @return
     */
    public Object get(String key, Class targeTypeCls) {
        Object value = null;
        if (targeTypeCls == String.class) {
            value = sharedPreferences.getString(key, "defValue");
        } else if (targeTypeCls == Boolean.class) {
            value = sharedPreferences.getBoolean(key, false);
        }
        return value;
    }

    /**
     * 清空SharedPreferences里所有数据。
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * 移除对应key的数据
     *
     * @param key
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 获取所有的数据项
     *
     * @return
     */
    public Map all() {
        return sharedPreferences.getAll();
    }

}
