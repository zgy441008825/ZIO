package com.zougy.zio

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.TextPaint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.core.animation.addListener
import androidx.core.view.GestureDetectorCompat
import com.zougy.commons.ZLog
import kotlin.math.abs

class MyView : View {

    private val paint = TextPaint()

    private val scroller: Scroller

    private val touchSlop: Int

    private val mMaxVelocity: Float

    private val gestureDetector: GestureDetectorCompat

    private var animation: ValueAnimator? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        paint.color = Color.RED
        paint.textSize = 60f
        scroller = Scroller(context)
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mMaxVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity.toFloat()
        gestureDetector = GestureDetectorCompat(context, detectorListener)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            val metrics = paint.fontMetrics
            val textHeight = metrics.bottom - metrics.top
            var textTop = 0f
            while (textTop < height) {
                textTop += textHeight + 10f
                it.drawText(textTop.toString(), 10f, textTop - metrics.descent, paint)
            }
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }

    private var mLastY = 0
    private var mLastX = 0

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        /*val x = event!!.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!scroller.isFinished) {
                    scroller.abortAnimation()
                }
                mLastX = x
                mLastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                scrollBy(-(x - mLastX), -(y - mLastY))
                mLastX = x
                mLastY = y
            }
            MotionEvent.ACTION_UP -> {
            }
        }*/
        return gestureDetector.onTouchEvent(event)
    }

    private val detectorListener = object : GestureDetector.OnGestureListener {
        override fun onShowPress(e: MotionEvent?) {
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return false
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            ZLog.d("$velocityX  $velocityY")
            startFling(velocityY.toInt())
            return false
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            scrollBy(((distanceX)).toInt(), ((distanceY)).toInt())
            return false
        }

        override fun onLongPress(e: MotionEvent?) {
        }
    }

    private fun startFling(velocityY: Int) {
        if (abs(velocityY) < 500) return
        if (animation != null && animation!!.isRunning) return
        animation = ValueAnimator.ofInt(0, velocityY)
        animation?.let { m ->
            m.duration = abs(velocityY / 20).toLong()
            m.interpolator = DecelerateInterpolator()
            m.addUpdateListener {
//                ZLog.d("updateListener:${it.animatedValue}")
            }
            m.addListener(onEnd = {
                ZLog.d("end")
            })
            m.start()
        }
    }
}