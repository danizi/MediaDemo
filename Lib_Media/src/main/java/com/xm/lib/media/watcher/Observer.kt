package com.xm.lib.media.watcher

/**
 * 观察者
 */
interface Observer {
    fun update(o: MediaViewObservable, vararg args: Any)
}