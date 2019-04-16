package common.xm.com.xmcommon.media2.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.*

object ViewUtil {

    fun setDisplaySize(view: View?, width: Int, height: Int) {
        /*设置宽高*/
        view?.layoutParams?.width = width
        view?.layoutParams?.height = height
    }

    @SuppressLint("InflateParams")
    fun getView(context: Context?, resID: Int): View? {
        /*通过layoutid获取view*/
        return LayoutInflater.from(context).inflate(resID, null, false)
    }

    fun show(view: View?) {
        /*显示*/
        if (view?.visibility == View.GONE) {
            view.visibility = View.VISIBLE
        }
    }

    fun hide(view: View?) {
        /*隐藏*/
        if (view?.visibility == View.VISIBLE) {
            view.visibility = View.GONE
        }
    }

    private fun createSurfaceView(context: Context?, width: Int = ViewGroup.LayoutParams.MATCH_PARENT, height: Int = ViewGroup.LayoutParams.MATCH_PARENT): SurfaceView? {
        /*创建画布*/
        val surfaceView = SurfaceView(context)
        surfaceView.holder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        surfaceView.layoutParams = ViewGroup.LayoutParams(width, height)
        return surfaceView
    }
}