package com.zougy.overtime.ui.View

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.zougy.ziolib.R
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin


/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:10/30 0030<br>
 * Email:441008824@qq.com
 */

open class WatermarkView : View {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, style: Int) : super(context, attrs, style) {
        initView(context, attrs)
    }

    private val paint = TextPaint()

    /**
     * 要显示的字符串
     */
    var textString = ""
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 文字颜色
     */
    private var color = Color.parseColor("#55FFFFFF")
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 字体大小
     */
    private var textSize = 30f
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 旋转的角度
     */
    private var angle = 135f
        set(value) {
            field = value
            invalidate()
        }

    private var viewWidth = 0
    private var viewHeight = 0

    private fun initView(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.WatermarkView)
            color = a.getColor(R.styleable.WatermarkView_markViewColor, Color.parseColor("#55FFFFFF"))
            textString = a.getString(R.styleable.WatermarkView_markViewText).toString()
            textSize = a.getDimensionPixelSize(R.styleable.WatermarkView_markViewTextSize, 30).toFloat()
            angle = a.getFloat(R.styleable.WatermarkView_markViewAngle, 135f)
            a.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        paint.textSize = textSize
        val fm = paint.fontMetrics
        val w: Int = paint.measureText(textString).toInt()
        val h = abs(fm.bottom - fm.top)
        val width = (w * cos(abs(angle)) + h * sin(abs(angle))).toInt()
        val height = (h * cos(abs(angle)) + w * sin(abs(angle))).toInt()
        val ws = MeasureSpec.makeMeasureSpec(width + 20, MeasureSpec.getMode(widthMeasureSpec))
        val hs = MeasureSpec.makeMeasureSpec(height + 20, MeasureSpec.getMode(heightMeasureSpec))
        super.onMeasure(ws, hs)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.d(this::class.java.name, "onDraw: $viewWidth $viewHeight")
        canvas!!.save()
        canvas.rotate(angle, viewWidth / 2f, viewHeight / 2f)
        paint.textSize = textSize
        paint.color = color
        val fm = paint.fontMetrics
        val x = (width - paint.measureText(textString)) / 2f
        val baseLine = (height + (fm.bottom - fm.top)) / 2f - fm.descent
        canvas.drawText(textString, x, baseLine, paint)
        canvas.restore()
    }
}