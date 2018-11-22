package common.xm.com.xmcommon.tabar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.xm.lib.component.XmTabbarComponent
import com.xm.lib.core.OneCore
import com.xm.lib.core.TwoCore
import common.xm.com.xmcommon.R

class ActTabbar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_tabbar)
        // 第一种 底部菜单+Fragment
        val barLayout = findViewById<XmTabbarComponent>(R.id.tabbar1)
        barLayout.setContainer(R.id.container)
                .addCore(OneCore())
                .setColor(R.color.activity_color_translucence, R.color.center_bottom_text_color_red)
                .addItem(MyFragment(), "button1", R.mipmap.bottom_icon_study_n, R.mipmap.bottom_icon_study_h)
                .addItem(MyFragment(), "button2", R.mipmap.bottom_tab_icon_exchange_n, R.mipmap.bottom_tab_icon_exchange_h)
                .addItem(MyFragment(), "button3", R.mipmap.bottom_tab_icon_free_n, R.mipmap.bottom_tab_icon_free_h)
                .addItem(MyFragment(), "button4", R.mipmap.bottom_tab_icon_my_n, R.mipmap.bottom_tab_icon_my_h)
                .builde()

        // 第二种 底部菜单+ViewPager
        val barLayout2 = findViewById<XmTabbarComponent>(R.id.tabbar2)
        barLayout2.bindViewPager(findViewById<XmTabbarComponent>(R.id.viewpager) as ViewPager)
                .addCore(TwoCore())
                .setColor(R.color.activity_color_translucence, R.color.center_bottom_text_color_red)
                .addItem(MyFragment(), "button1", R.mipmap.bottom_icon_study_n, R.mipmap.bottom_icon_study_h)
                .addItem(MyFragment(), "button2", R.mipmap.bottom_tab_icon_exchange_n, R.mipmap.bottom_tab_icon_exchange_h)
                .addItem(MyFragment(), "button3", R.mipmap.bottom_tab_icon_free_n, R.mipmap.bottom_tab_icon_free_h)
                .addItem(MyFragment(), "button4", R.mipmap.bottom_tab_icon_my_n, R.mipmap.bottom_tab_icon_my_h)
                .builde()

        // 第三种 顶部菜单 + Viewpager + 滑块
        val barLayout3 = findViewById<XmTabbarComponent>(R.id.tabbar3)
        barLayout3.setContainer(R.id.ViewPager3)
                .addCore(TwoCore())
                .setColor(R.color.activity_color_translucence, R.color.center_bottom_text_color_red)
                .addItem(MyFragment(), "button1", R.mipmap.bottom_icon_study_n, R.mipmap.bottom_icon_study_h)
                .addItem(MyFragment(), "button2", R.mipmap.bottom_tab_icon_exchange_n, R.mipmap.bottom_tab_icon_exchange_h)
                .addItem(MyFragment(), "button3", R.mipmap.bottom_tab_icon_free_n, R.mipmap.bottom_tab_icon_free_h)
                .addItem(MyFragment(), "button4", R.mipmap.bottom_tab_icon_my_n, R.mipmap.bottom_tab_icon_my_h)
                .builde()
    }
}
