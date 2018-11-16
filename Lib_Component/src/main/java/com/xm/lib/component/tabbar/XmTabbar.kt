package com.xm.lib.component.tabbar

import android.content.Context
import android.support.v4.app.Fragment
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.xm.lib.contract.XmTabbarContract

class XmTabbar(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs), XmTabbarContract.View {

    private var presenter: XmTabbarContract.Presenter? = null

    init {
        presenter = XmTabbarContract.Presenter(getContext(), this)
    }

    override fun setColor(beforeColorID: Int, afterColorID: Int): XmTabbar {
        presenter!!.model().setColorID(beforeColorID, afterColorID)
        return this
    }

    override fun setColor(beforeColorID: String, afterColorID: String): XmTabbar {
        presenter!!.model().setColorID(beforeColorID, afterColorID)
        return this
    }

    override fun addItem(instance: Fragment, des: String, beforeIconID: Int, afterIconID: Int): XmTabbar {
        presenter!!.model().addItem(instance, des, beforeIconID, afterIconID)
        return this
    }

    override fun addItem(cls: Class<*>, des: Int, beforeIconID: Int, afterIconID: Int): XmTabbar {
        presenter!!.model().addItem(cls, des, beforeIconID, afterIconID)
        return this
    }

    override fun setContainer(layoutID: Int): XmTabbar {
        presenter!!.model().setContainer(layoutID)
        return this
    }

    override fun setTextLayoutParams(params: ViewGroup.LayoutParams) {

    }

    override fun setIconLayoutParams(params: ViewGroup.LayoutParams) {

    }

    override fun select(i: Int): XmTabbar {
        presenter!!.model().select(i)
        return this
    }

    override fun builde() {
        presenter!!.builde()
    }

    override fun getXmTabbar(): XmTabbar {
        return this
    }
}



