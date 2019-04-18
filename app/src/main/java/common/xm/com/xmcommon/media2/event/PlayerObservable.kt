package common.xm.com.xmcommon.media2.event

/**
 * 播放回调观察者
 * 手势回调观察者
 * 来电提醒者
 * 拔出、插上耳机观察者
 * 网络变化观察者
 * 息屏观察者
 * 重力感应观察者
 */
class PlayerObservable {

    @Synchronized
    fun addObserver(o: PlayerObserver) {

    }

    @Synchronized
    fun deleteObserver(o: PlayerObserver) {

    }

    fun notifyObservers() {

    }

}