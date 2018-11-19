package com.xm.lib.component.tabbar

import android.content.Context

/**
 * 抽象创建引擎
 */
abstract class AbsCreateTabbarCore {

    var context: Context? = null
    var view: XmTabbarContract.View? = null
    var model: XmTabbarContract.Model? = null
    abstract fun build()
}