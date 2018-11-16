package common.xm.com.xmcommon;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.xm.lib.component.tabbar.XmTabbar;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 第一种 底部菜单+Fragment
//        XmTabbar barLayout = findViewById(R.id.tabbar1);
//        barLayout.setContainer(R.id.container)
//                .setColor(R.color.activity_color_translucence, R.color.center_bottom_text_color_red)
//                .addItem(new MyFragment(), "button1", R.mipmap.bottom_icon_study_n, R.mipmap.bottom_icon_study_h)
//                .addItem(new MyFragment(), "button2", R.mipmap.bottom_tab_icon_exchange_n, R.mipmap.bottom_tab_icon_exchange_h)
//                .addItem(new MyFragment(), "button3", R.mipmap.bottom_tab_icon_free_n, R.mipmap.bottom_tab_icon_free_h)
//                .addItem(new MyFragment(), "button4", R.mipmap.bottom_tab_icon_my_n, R.mipmap.bottom_tab_icon_my_h)
//                .builde();

        // 第二种 底部菜单+ViewPager
        XmTabbar barLayout2 = findViewById(R.id.tabbar2);
        barLayout2.bindViewPager((ViewPager) findViewById(R.id.viewpager))
                .setColor(R.color.activity_color_translucence, R.color.center_bottom_text_color_red)
                .addItem(new MyFragment(), "button1", R.mipmap.bottom_icon_study_n, R.mipmap.bottom_icon_study_h)
                .addItem(new MyFragment(), "button2", R.mipmap.bottom_tab_icon_exchange_n, R.mipmap.bottom_tab_icon_exchange_h)
                .addItem(new MyFragment(), "button3", R.mipmap.bottom_tab_icon_free_n, R.mipmap.bottom_tab_icon_free_h)
                .addItem(new MyFragment(), "button4", R.mipmap.bottom_tab_icon_my_n, R.mipmap.bottom_tab_icon_my_h)
                .builde();

        // 第三种 顶部菜单 + Viewpager + 滑块
//        XmTabbar barLayout3 = findViewById(R.id.tabbar3);
//        barLayout3.setContainer(R.id.container)
//                .setColor(R.color.activity_color_translucence, R.color.center_bottom_text_color_red)
//                .addItem(new MyFragment(), "button1", R.mipmap.bottom_icon_study_n, R.mipmap.bottom_icon_study_h)
//                .addItem(new MyFragment(), "button2", R.mipmap.bottom_tab_icon_exchange_n, R.mipmap.bottom_tab_icon_exchange_h)
//                .addItem(new MyFragment(), "button3", R.mipmap.bottom_tab_icon_free_n, R.mipmap.bottom_tab_icon_free_h)
//                .addItem(new MyFragment(), "button4", R.mipmap.bottom_tab_icon_my_n, R.mipmap.bottom_tab_icon_my_h)
//                .builde();
    }
}
