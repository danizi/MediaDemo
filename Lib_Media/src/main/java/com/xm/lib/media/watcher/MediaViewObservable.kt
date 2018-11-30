package com.xm.lib.media.watcher

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.xm.lib.media.contract.base.BaseMediaContract
import com.xm.lib.media.event.Event
import com.xm.lib.media.imp.IInit

/**
 * 所有播放视图的基类
 * 1 采用mvp模式编写,对外提供获取控制层方法
 * 2 播放视图既要发送事件又要处理事件,所有在这里采用观察者模式，所以播放视图即是一个观察者又是被观察者
 */
abstract class MediaViewObservable<T : BaseMediaContract.Present> : FrameLayout, Observer {

    constructor(context: Context, layoutID: Int) : super(context) {
        present = createPresent()
        contentView = getContentView(layoutID)
        addView(contentView)
        hide()
        findViews(contentView)
        initListenner()
        initData()
    }

    constructor(context: Context) : super(context) {
        present = createPresent()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        present = createPresent()
    }

    /*
     * -------
     * 初始化
     */
    private var contentView: View? = null

    private var layoutID: Int? = null

    open fun findViews(contentView: View?) {}

    open fun initListenner() {}

    open fun initData() {
        getPresent()?.process()
    }

    /*
     * -------
     * 创建P层
     */
    private var present: T? = null

    abstract fun createPresent(): T

    fun getPresent(): T? {
        return present
    }

    /*
     * -------
     * 观察者接受信息
     */
    override fun update(o: MediaViewObservable<*>, event: Event) {
        getPresent()?.handleReceiveEvent(o, event)
    }

    /*
     * -------
     * 被观察者相关
     */
    private var observers: ArrayList<Observer>? = ArrayList()

    @Synchronized
    fun addObserver(o: Observer?) {
        if (o == null)
            throw NullPointerException()
        if (!observers?.contains(o)!!) {
            observers?.add(o)
        }
    }

    @Synchronized
    fun deleteObserver(o: Observer) {
        observers?.remove(o)
    }

    @Synchronized
    fun notifyObservers(event: Event) {
        for (obs in this!!.observers!!) {
            obs.update(this, event)
        }
    }

    @Synchronized
    fun countObservers(): Int {
        return observers?.size!!
    }

    /*
     * -------
     * 内容装载控件相关
     */
    private fun getContentView(layoutID: Int, root: ViewGroup, attachToRoot: Boolean): View {
        return LayoutInflater.from(context).inflate(layoutID, root, attachToRoot)
    }

    private fun getContentView(layoutID: Int): View {
        return LayoutInflater.from(context).inflate(layoutID, this, false)
    }

    fun hide() {
        if (this.visibility == View.VISIBLE) {
            this.visibility = View.GONE
        }
    }

    fun show() {
        if (this.visibility == View.GONE) {
            this.visibility = View.VISIBLE
        }
    }
}