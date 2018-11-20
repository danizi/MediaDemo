package com.xm.lib.abs

import android.content.Context
import com.xm.lib.contract.XmTabbarContract

/**
 * 抽象创建引擎
 */
abstract class AbsTabbarCore {

    var context: Context? = null
    var view: XmTabbarContract.View? = null
    var model: XmTabbarContract.Model? = null
    abstract fun build()
}