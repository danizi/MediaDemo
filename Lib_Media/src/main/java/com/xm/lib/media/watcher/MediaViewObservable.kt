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
 *
 * 事件驱动   ->  视图
 *
 * 外部事件   ：网络变化,横竖切屏
 * 播放器事件 ：各种播放的监听事件
 * 视图事件   ：触控事件(手势,点击,长按)
 *
 * 1 所以视图是事件生产者也是消费者，所以在这里加了观察者-被观察者
 * 2 所有的视图都相互绑定了，即事件会一一全部下发
 *
 */
abstract class MediaViewObservable<T :BaseMediaContract.Present> : FrameLayout, Observer {
    private var present: T? = null
    private var contentView: View? = null
    private var observers: ArrayList<Observer>? = ArrayList()
    private var layoutID: Int? = null

    constructor(context: Context, layoutID: Int) : super(context) {
        present = createPresent()
        contentView = getContentView(layoutID)      // todo 为非播放控件提供
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
    open fun findViews(contentView: View?) {

    }

    open fun initListenner() {

    }

    open fun initData() {

    }

    /*
     * -------
     * 创建P层
     */
    abstract fun createPresent(): T

    fun getPresent(): T? {
        return present
    }

    override fun update(o: MediaViewObservable<*>, event: Event) {
        getPresent()?.handleReceiveEvent(o, event)
    }

    /*
     * -------
     * 被观察者
     */
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
     * 内容装载控件相关处理
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