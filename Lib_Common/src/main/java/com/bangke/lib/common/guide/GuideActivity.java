package com.bangke.lib.common.guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bangke.lib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页面
 * Created by xlm on 03/10/2018.
 */

public class GuideActivity extends AppCompatActivity {
    private int[] guideArray={
            R.drawable.guide_01,
            R.drawable.guide_02,
            R.drawable.guide_03,
            R.drawable.guide_04
    };
    private List<View> guides = new ArrayList<>();
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        initView();
        initData();
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);
    }

    private void initData() {
        for(int i=0;i<guideArray.length;i++){
            ImageView img = new ImageView(this);
            img.setBackgroundResource(guideArray[i]);
            guides.add(img);
        }
        viewPager.setAdapter(new GuideAdapter(guides));
    }

    /**
     * 适配器
     */
    public static class GuideAdapter extends PagerAdapter{

        private List<View> data;

        public GuideAdapter(List data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.isEmpty()?0:data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View item = data.isEmpty()?null:data.get(position);
            return item;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
