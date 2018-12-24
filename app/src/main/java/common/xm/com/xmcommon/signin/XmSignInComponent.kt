package common.xm.com.xmcommon.signin

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

/**
 * 七天签到组件
 */
class XmSignInComponent(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    init {

    }

    /**
     * 测量
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * 发生变化触发该方法
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    /**
     * 绘制 调用invalidate或者刷新接口触发该方法
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制七个圆
    }
}