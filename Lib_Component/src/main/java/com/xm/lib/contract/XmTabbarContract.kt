package com.xm.lib.contract

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bangke.lib.common.utils.DisplayUtils
import com.bangke.lib.common.utils.FragmentUtil
import com.xm.lib.component.tabbar.XmTabbar

/**
 *  契约类
 */
class XmTabbarContract {

    interface View {
        /**
         * 设置文字的布局信息
         */
        fun setTextLayoutParams(params: ViewGroup.LayoutParams)

        /**
         * 设置图标的布局信息
         */
        fun setIconLayoutParams(params: ViewGroup.LayoutParams)

        /**
         * 设置颜色
         */
        fun setColor(beforeColorID: Int, afterColorID: Int): XmTabbar

        fun setColor(beforeColorID: String, afterColorID: String): XmTabbar

        /**
         * 添加菜单项
         */
        fun addItem(instance: Fragment, des: String, beforeIconID: Int, afterIconID: Int): XmTabbar

        fun addItem(cls: Class<*>, des: Int, beforeIconID: Int, afterIconID: Int): XmTabbar

        /**
         * 装载容器
         */
        fun setContainer(layoutID: Int): XmTabbar

        /**
         * 创建菜单
         */
        fun builde()

        /**
         * 获取当前控件
         */
        fun getXmTabbar(): XmTabbar

        /**
         * 选中项
         */
        fun select(i: Int): XmTabbar
    }

    class Model(var context: Context?) {
        private var index: Int = 0
        private var tvParams: ViewGroup.LayoutParams? = null
        private var ivParams: ViewGroup.LayoutParams? = null
        private var beforeColorID: Int? = Color.parseColor("#666666")
        private var afterColorID: Int? = Color.parseColor("#FF6666")
        private var fragmentList: ArrayList<Fragment> = ArrayList()
        private var desList: ArrayList<String> = ArrayList()
        private var beforeIconIDList: ArrayList<Int> = ArrayList()
        private var afterIconIDList: ArrayList<Int> = ArrayList()
        private var container: Int? = null

        fun setColorID(beforeColorID: Int?, afterColorID: Int?) {
            this.beforeColorID = beforeColorID
            this.afterColorID = afterColorID
        }

        fun setColorID(beforeColorString: String?, afterColorString: String?) {
            this.beforeColorID = Color.parseColor(beforeColorString)
            this.afterColorID = Color.parseColor(afterColorString)
        }

        fun addItem(instance: Fragment, des: String, beforeIconID: Int, afterIconID: Int) {
            this.fragmentList.add(instance)
            this.desList.add(des)
            this.beforeIconIDList.add(beforeIconID)
            this.afterIconIDList.add(afterIconID)

        }

        fun addItem(cls: Class<*>, des: Int, beforeIconID: Int, afterIconID: Int) {
            //this.fragmentList.add(instance)
            this.desList.add(context!!.resources.getString(des))
            this.beforeIconIDList.add(beforeIconID)
            this.afterIconIDList.add(afterIconID)
        }

        fun setContainer(layoutID: Int) {
            this.container = layoutID
        }

        fun select(i: Int) {
            this.index = i;
        }

        fun beforeTabState(tab: TabLayout.Tab?) {
            if (null == tab) return
            getIcon(tab).setImageResource(beforeIconIDList[tab.tag as Int])
            getText(tab).setTextColor(beforeColorID!!)

        }

        fun afterTabState(tab: TabLayout.Tab?) {
            if (null == tab) return
            //设置展示的内容
            val tag = tab.tag as Int
            val fragmentActivity = context as FragmentActivity
            val container = container!!
            val framgent = fragmentList[tag]
            FragmentUtil.getInstance().displayFragment(fragmentActivity, container, tag.toString(), framgent)

            //设置图标和内容
            getIcon(tab).setImageResource(afterIconIDList[tab.tag as Int])
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getText(tab).setTextColor(context!!.getColor(afterColorID!!))
            } else {
                getText(tab).setTextColor(context!!.resources.getColor(afterColorID!!))
            }

            //设置动画
            getItemView(tab)
        }

        fun addTab(tabLayout: TabLayout) {
            var tab: TabLayout.Tab?
            for (index in 0..(fragmentList.size - 1)) {
                tab = tabLayout.newTab()                             //创建Tab
                tab.customView = createTabCusView(index)          //获取ItemView
                tab.tag = index                                      //为每一个创建Tab添加一个tag
                tabLayout.addTab(tab)                                //TabLayout添加创建Tab
            }
        }

        fun getIndex(): Int {
            return index
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
            tv.text = desList[index]
            tv.setTextColor(beforeColorID!!)
            return tv
        }

        private fun createIconView(index: Int): ImageView {
            val iv = ImageView(context)
            iv.layoutParams = ViewGroup.LayoutParams(DisplayUtils.dp2px(context, 22F), DisplayUtils.dp2px(context, 19F))
            iv.setImageResource(beforeIconIDList[index])
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

    class Presenter(context: Context, view: View) {
        private var model: Model? = null
        private var view: View? = view
        private var context: Context? = context

        fun model(): Model {
            return model!!
        }

        init {
            model = Model(context)
        }

        fun builde() {
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
            tabLayout.clipChildren = true                            //子控件可以超过父控件

            //为TabLayout添加item
            model!!.addTab(tabLayout)

            //设置监听
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) { //重复选择相同的item
                    model!!.afterTabState(tab)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) { //上一次item的位置
                    model!!.beforeTabState(tab)
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {   //选中
                    model!!.afterTabState(tab)
                }
            })
            //默认选中第一个item
            tabLayout.getTabAt(model!!.getIndex())!!.select()
            return tabLayout
        }
    }
}