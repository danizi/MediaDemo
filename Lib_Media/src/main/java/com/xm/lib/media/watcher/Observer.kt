package com.xm.lib.media.watcher

import com.xm.lib.media.event.Event

/**
 * 观察者
 */
interface Observer {
    fun update(o: MediaViewObservable<*>, event: Event)
}