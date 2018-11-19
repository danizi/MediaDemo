package com.xm.lib.component.tabbar

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout

class XmTabbarComponent(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs), XmTabbarContract.View {
    override fun addCore(core: AbsCreateTabbarCore) :XmTabbarComponent{
        presenter!!.model().core = core
        return this
    }

    override fun bindViewPager(viewPager: ViewPager): XmTabbarComponent {
        presenter!!.model().viewPager = viewPager
        return this
    }

    private var presenter: XmTabbarContract.Presenter? = null

    init {
        presenter = XmTabbarContract.Presenter(getContext(), this)
    }

    override fun setColor(beforeColorID: Int, afterColorID: Int): XmTabbarComponent {
        presenter!!.model().beforeColorID = beforeColorID
        presenter!!.model().afterColorID = afterColorID
        return this
    }

    override fun addItem(instance: Fragment, des: String, beforeIconID: Int, afterIconID: Int): XmTabbarComponent {
        presenter!!.model().fragmentList.add(instance)
        presenter!!.model().desList.add(des)
        presenter!!.model().beforeIconIDList.add(beforeIconID)
        presenter!!.model().afterIconIDList.add(afterIconID)
        return this
    }

    override fun setContainer(layoutID: Int): XmTabbarComponent {
        presenter!!.model().container = layoutID
        return this
    }

    override fun setTextLayoutParams(params: ViewGroup.LayoutParams) {

    }

    override fun setIconLayoutParams(params: ViewGroup.LayoutParams) {

    }

    override fun select(i: Int): XmTabbarComponent {
        presenter!!.model().index = i
        return this
    }

    override fun setContainerBackgroundColor(containerColorID: Int) {
        presenter!!.model().containerColorID = containerColorID
    }

    override fun builde() {
        presenter!!.build()
    }

    override fun getXmTabbar(): XmTabbarComponent {
        return this
    }

}



