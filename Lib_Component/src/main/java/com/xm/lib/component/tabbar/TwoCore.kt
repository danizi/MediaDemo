package com.xm.lib.component.tabbar

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bangke.lib.common.utils.DisplayUtils
import com.bangke.lib.common.utils.FragmentUtil

/**
 * 引擎
 */
class TwoCore(val context: Context, val view: XmTabbarContract.View, val model: XmTabbarContract.Model) : AbsCreateTabbarCore() {

    override fun build() {
        try {
            view!!.getXmTabbar().addView(setupTabLayout(), 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupTabLayout(): TabLayout {
        //创建ItemView并添加到TabLayout
        val tabLayout = TabLayout(context)

        //TabLayout的滑块
        tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT)//滑块设置透明
        tabLayout.setSelectedTabIndicatorHeight(0)               //设置滑块的高度
        tabLayout.setSelectedTabIndicatorColor(Color.CYAN)       //滑块的颜色
        //tabLayout.clipChildren = true                            //子控件可以超过父控件
        model.viewPager!!.adapter = MyAdapter((context as FragmentActivity).supportFragmentManager, model.fragmentList)

        tabLayout.setupWithViewPager(model.viewPager)
        //为TabLayout添加item
        addTab(tabLayout)

        //设置监听
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) { //重复选择相同的item
                //afterTabState(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { //上一次item的位置
                //beforeTabState(tab)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {   //选中
                //afterTabState(tab)
            }
        })
        //默认选中第一个item
        tabLayout.getTabAt(model!!.index)!!.select()
        tabLayout.tabGravity=TabLayout.GRAVITY_FILL
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        return tabLayout
    }

    fun beforeTabState(tab: TabLayout.Tab?) {
        if (null == tab) return
        getIcon(tab).setImageResource(model!!.beforeIconIDList[tab.tag as Int])
        getText(tab).setTextColor(model!!.beforeColorID!!)

    }

    fun afterTabState(tab: TabLayout.Tab?) {
        if (null == tab) return
        //设置展示的内容
        val tag = tab.tag as Int
        val fragmentActivity = context as FragmentActivity
        val container = model!!.container!!
        val framgent = model!!.fragmentList[tag]
        FragmentUtil.getInstance().displayFragment(fragmentActivity, container, tag.toString(), framgent)

        //设置图标和内容
        getIcon(tab).setImageResource(model!!.afterIconIDList[tab.tag as Int])
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getText(tab).setTextColor(context!!.getColor(model!!.afterColorID!!))
        } else {
            getText(tab).setTextColor(context!!.resources.getColor(model!!.afterColorID!!))
        }

        //设置动画
        getItemView(tab)
    }

    fun addTab(tabLayout: TabLayout) {
        var tab: TabLayout.Tab?
        for (index in 0..(model!!.fragmentList.size - 1)) {
            tab = tabLayout.newTab()
            tab.customView = createTabCusView(index)
            tab.tag = index
            tabLayout.addTab(tab)
        }
    }

    private fun getItemView(tab: TabLayout.Tab?): android.view.View {
        return (tab!!.customView as LinearLayout)
    }

    private fun getIcon(tab: TabLayout.Tab?): ImageView {
        return (tab!!.customView as LinearLayout).getChildAt(0) as ImageView
    }

    private fun getText(tab: TabLayout.Tab?): TextView {
        return (tab!!.customView as LinearLayout).getChildAt(1) as TextView
    }

    private fun createTabContainerView(): LinearLayout {
        val ll = LinearLayout(context)
        ll.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        ll.gravity = Gravity.CENTER
        ll.orientation = LinearLayout.VERTICAL
        return ll
    }

    private fun createTvView(index: Int): TextView {
        val tv = TextView(context)
        tv.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val p: LinearLayout.LayoutParams = tv.layoutParams as LinearLayout.LayoutParams
        p.topMargin = DisplayUtils.dp2px(context, 6F)
        tv.text = model.desList[index]
        tv.setTextColor(model.beforeColorID!!)
        return tv
    }

    private fun createIconView(index: Int): ImageView {
        val iv = ImageView(context)
        iv.layoutParams = ViewGroup.LayoutParams(DisplayUtils.dp2px(context, 22F), DisplayUtils.dp2px(context, 19F))
        iv.setImageResource(model.beforeIconIDList[index])
        return iv
    }

    private fun createTabCusView(index: Int): android.view.View? {
        val ll = createTabContainerView()
        val iv = createIconView(index)
        val tv = createTvView(index)
        ll.addView(iv)
        ll.addView(tv)
        return ll
    }
}

class MyAdapter(fragmentManager: FragmentManager,  val datas: List<Fragment>) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return datas[position]
    }

    override fun getCount(): Int {
        return datas.size
    }

}