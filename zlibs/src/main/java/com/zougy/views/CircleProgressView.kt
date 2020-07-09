package com.zougy.views

import android.content.Context
import android.graphics.*
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.zougy.ziolib.R
import java.text.DecimalFormat
import kotlin.math.min

/**
 * Description:圆环进度条。可使用自定义属性进行相关设置
 *
 * centerColor：中间圆形区域颜色
 *
 * progressRingSize：进度条宽度
 *
 * progressBgColor：进度条背景颜色
 *
 * progressColor：进度条颜色
 *
 * proTextColor：进度文本颜色
 *
 * proTextSize：进度文本字体大小
 *
 * proValue：当前进度
 *
 * progressMax：进度最大值
 *
 * infoTextColor：信息文本颜色
 *
 * infoTextSize：信息文本字体大小
 *
 * infoTextString：信息文本内容
 *
 * progressColorStart：进度条渐变色的开始颜色
 *
 * progressColorEnd：进度条渐变色的结束颜色，同时设置了开始和结束颜色时，progressColor失效。
 *
 *  Author:邹高原
 *
 * Date:11/04 0004
 *
 * Email:441008824@qq.com
 */
class CircleProgressView : View {

    private val paint = Paint()

    /**
     * 中间圆形区域颜色
     */
    private var centerColor: Int = Color.WHITE

    /**
     * 中间圆形区域半径
     */
    private var centerRadius = 10f

    /**
     * 圆环进度背景色
     */
    private var progressBgColor = Color.parseColor("#E1E1E1")

    /**
     * 圆环渐变色开始颜色
     */
    private var progressColorStart = 0

    /**
     * 圆环渐变色结束颜色
     */
    private var progressColorEnd = 0

    /**
     * 圆环进度条颜色
     */
    private var progressColor: Int = Color.RED

    private var progressRingSize = 20f

    /**
     * 进度字体颜色
     */
    private var proTextColor: Int = Color.BLACK

    /**
     * 进度字体大小
     */
    private var proTextSize = 30f

    /**
     * 进度值
     */
    var progress = 0f
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 进度最大值
     */
    var progressMax = 100f
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 显示的提示信息字体颜色
     */
    private var proInfoTextColor = Color.GRAY

    /**
     * 显示的提示信息字体大小
     */
    private var proInfoTextSize = 20f

    /**
     * 显示的提示信息文本
     */
    private var proInfoText: String = ""

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, style: Int) : super(context, attrs, style) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val type = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView)
            centerColor = type.getColor(R.styleable.CircleProgressView_centerColor, Color.WHITE)
            progressRingSize = type.getFloat(R.styleable.CircleProgressView_progressRingSize, 10f)
            progressBgColor = type.getColor(
                R.styleable.CircleProgressView_progressBgColor,
                Color.parseColor("#E1E1E1")
            )
            progressColor = type.getColor(R.styleable.CircleProgressView_progressColor, Color.RED)
            progressColorStart = type.getColor(R.styleable.CircleProgressView_progressColorStart, 0)
            progressColorEnd = type.getColor(R.styleable.CircleProgressView_progressColorEnd, 0)
            proTextColor = type.getColor(R.styleable.CircleProgressView_proTextColor, Color.BLACK)
            proTextSize = type.getDimension(R.styleable.CircleProgressView_proTextSize, 30f)
            progress = type.getFloat(R.styleable.CircleProgressView_proValue, 0f)
            progressMax = type.getFloat(R.styleable.CircleProgressView_progressMax, 100f)
            proInfoTextColor =
                type.getColor(R.styleable.CircleProgressView_infoTextColor, Color.BLACK)
            proInfoTextSize = type.getDimension(R.styleable.CircleProgressView_infoTextSize, 20f)
            proInfoText = type.getString(R.styleable.CircleProgressView_infoTextString).toString()
            type.recycle()
        }
        paint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCenterCircle(canvas)
        drawProRing(canvas)
        drawProgress(canvas)
        drawInfo(canvas)
    }

    private fun drawCenterCircle(canvas: Canvas?) {
        paint.style = Paint.Style.FILL
        paint.color = centerColor
        val min = min(width, height)
        centerRadius = (min - progressRingSize) / 2f
        canvas!!.drawCircle(width / 2f, height / 2f, centerRadius, paint)
    }

    /**
     * 绘制背景圆环
     */
    private fun drawProRing(canvas: Canvas?) {
        paint.style = Paint.Style.STROKE
        paint.color = progressBgColor
        paint.strokeWidth = progressRingSize
        canvas!!.drawCircle(width / 2f, height / 2f, centerRadius, paint)
    }

    private fun drawProgress(canvas: Canvas?) {
        if (progress > 0) {
            paint.style = Paint.Style.STROKE
            if (progressColorStart == 0 || progressColorEnd == 0) {
                paint.color = progressColor
            } else {
                paint.shader = SweepGradient(
                    width / 2f,
                    height / 2f,
                    intArrayOf(progressColorStart, progressColorEnd, progressColorStart),
                    null
                )
            }
            paint.strokeWidth = progressRingSize
            paint.strokeCap = Paint.Cap.ROUND
            val progressFloat = (progress / progressMax) * 360f
            val rectF = RectF(
                (width - centerRadius * 2) / 2f, (height - centerRadius * 2) / 2f,
                (width + centerRadius * 2) / 2f, (height + centerRadius * 2) / 2f
            )
            canvas!!.save()
            canvas.rotate(-90f, width / 2f, height / 2f)
            canvas.drawArc(rectF, 0f, progressFloat, false, paint)
            canvas.restore()
        }
        paint.shader = null
        paint.style = Paint.Style.FILL
        paint.color = proTextColor
        paint.textSize = proTextSize
        var progressString = DecimalFormat("#.0").format(progress)
        progressString =
            if (progressString.endsWith("0")) DecimalFormat("#").format(progress) else progressString
        val textWidth = paint.measureText(progressString)
        val fm = paint.fontMetrics
        val baseLine = (height + (fm.bottom - fm.top)) / 2 - fm.descent
        canvas!!.drawText(progressString, (width - textWidth) / 2f, baseLine, paint)
    }

    private fun drawInfo(canvas: Canvas?) {
        if (TextUtils.isEmpty(proInfoText)) return
        paint.style = Paint.Style.FILL
        paint.color = proInfoTextColor
        paint.textSize = proInfoTextSize
        val textWidth = paint.measureText(proInfoText)
        val baseLine = height / 2 + centerRadius - progressRingSize / 2 - 20
        canvas!!.drawText(proInfoText, (width - textWidth) / 2f, baseLine, paint)
    }

}