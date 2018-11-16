package com.xm.lib.component.tabbar

import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.ViewGroup

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

        /**
         * 添加菜单项
         */
        fun addItem(instance: Fragment, des: String, beforeIconID: Int, afterIconID: Int): XmTabbar

        /**
         * 装载容器
         */
        fun setContainer(layoutID: Int): XmTabbar

        /**
         * 设置容器的颜色
         */
        fun setContainerBackgroundColor(containerColorID: Int)

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

        /**
         * 绑定ViewPager
         */
        fun bindViewPager(viewPager: ViewPager): XmTabbar
    }

    class Model {
        var index: Int = 0
        var tvParams: ViewGroup.LayoutParams? = null
        var ivParams: ViewGroup.LayoutParams? = null
        var containerColorID: Int? = null
        var beforeColorID: Int? = Color.parseColor("#666666")
        var afterColorID: Int? = Color.parseColor("#FF6666")
        var fragmentList: ArrayList<Fragment> = ArrayList()
        var desList: ArrayList<String> = ArrayList()
        var beforeIconIDList: ArrayList<Int> = ArrayList()
        var afterIconIDList: ArrayList<Int> = ArrayList()
        var container: Int? = null
        var viewPager: ViewPager? = null
    }

    class Presenter(context: Context, view: View) {
        private var model: Model? = null
        private var view: View? = view
        private var context: Context? = context
        private var core: AbsCreateTabbarCore? = null

        fun model(): Model {
            return model!!
        }

        init {
            model = Model()
            core = TwoCore(this.context!!, this.view!!, this.model!!)
        }

        fun build() {
            core!!.build()
        }
    }


}