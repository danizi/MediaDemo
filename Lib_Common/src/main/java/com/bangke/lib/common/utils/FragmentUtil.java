package com.bangke.lib.common.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

/**
 * fragment操作工具类
 */
public class FragmentUtil {
    private static FragmentUtil instance;
    private String preFragmentTag;
    private FragmentManager fragmentManager;

    public static FragmentUtil getInstance() {
        if (instance == null) {
            synchronized (FragmentUtil.class) {
                instance = new FragmentUtil();
            }
        }
        return instance;
    }

    /**
     * hide show方式
     *
     * @param object      Activity||Fragment实例
     * @param containerId 页面转载容器
     * @param tag         上一个fragment tag
     * @param curFragment 当前要显示的tag
     */
    public void displayFragment(Object object, int containerId, String tag, Fragment curFragment) {
        getFragmentManager(object);
        if (!TextUtils.isEmpty(preFragmentTag)) {
            hideFragment(preFragmentTag);
        }
        Fragment findFragment = fragmentManager.findFragmentByTag(tag);
        if (findFragment == null) {
            addFragment(containerId, curFragment, tag);
        } else {
            showFragment(tag);
        }
        preFragmentTag = tag;
    }

    private void getFragmentManager(Object object) {
        if (object instanceof FragmentActivity) {
            fragmentManager = ((FragmentActivity) object).getSupportFragmentManager();
        } else if (object instanceof Fragment) {
            fragmentManager = ((Fragment) object).getChildFragmentManager();
        }
    }

    private void addFragment(int containerId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerId, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    private void hideFragment(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (null == fragment) {
            return;
        }
        fragmentTransaction.hide(fragment);
        fragmentTransaction.addToBackStack("hide " + tag);
        fragmentTransaction.commit();
    }

    private void showFragment(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (null == fragment) {
            return;
        }
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.addToBackStack("show " + tag);
        fragmentTransaction.commit();
    }

    /**
     * detach attach 切换速度慢 方式
     *
     * @param object      Activity||Fragment实例
     * @param containerId 页面转载容器
     * @param tag         上一个fragment tag
     * @param curFragment 当前要显示的tag
     */
    public void displayFragment2(Object object, int containerId, String tag, Fragment curFragment) {

        getFragmentManager(object);
        if (!TextUtils.isEmpty(preFragmentTag)) {
            detachFragment(preFragmentTag);
        }
        attachFragment(tag, containerId, curFragment);
        preFragmentTag = tag;
    }

    private void detachFragment(String tag) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragment != null)
            transaction.detach(fragment).commit();
    }

    private void attachFragment(String tag, int containerId, Fragment curFragment) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragmentManager.beginTransaction().add(containerId, curFragment, tag).commit();
        } else {
            fragmentManager.beginTransaction().attach(fragment).commit();
        }
    }


    /*add replace方式 不推荐*/
}
