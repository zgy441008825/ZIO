package com.zougy.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * Description:
 * Author:GaoYuanZou
 * Date:2021/12/20
 */
class HarmonyProgress : View {

    companion object {
        const val TAG = "HarmonyProgress"
    }

    private val paint = Paint()

    private val drawRectF = RectF()

    private var viewWidth = 0f

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawRectF.apply {
            left = 0f
            top = 0f
            val min = minOf(w, h).toFloat()
            right = min
            bottom = min
        }
        viewWidth = drawRectF.width()
        scale = viewWidth / (proMax - proMin)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        attrs?.apply {

        }
        paint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            strokeWidth = 10f
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawColor(Color.BLACK)

            paint.color = Color.WHITE
            drawCircle(drawRectF.height() / 2, drawRectF.height() / 2, drawRectF.height() / 2, paint)

            paint.color = Color.parseColor("#55000000")
            drawArc(0f, 0f, viewWidth, drawRectF.height(), 270f, 180f, true, paint)

            if (sweepAngle) {
                drawArc(drawRectF, 270f, -180f, false, paint)
            } else {
                paint.color = Color.WHITE
                drawArc(drawRectF, 270f, 180f, false, paint)
            }
        }
    }

    private var sweepAngle = true

    private val proMax = 100
    private val proMin = 0
    private var scale = 0f

    var progress = 0
        set(value) {
            field = value
            val value = value * scale
            if (value < viewWidth / 2) {
                sweepAngle = true
                drawRectF.left = value
                drawRectF.right = viewWidth - drawRectF.left
            } else {
                sweepAngle = false
                drawRectF.left = viewWidth - value
                drawRectF.right = value
            }
            invalidate()
        }
}