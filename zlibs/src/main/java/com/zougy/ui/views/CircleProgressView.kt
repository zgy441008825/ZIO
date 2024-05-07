package com.zougy.ui.views

import android.animation.ValueAnimator
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
 * proInfiniteMode:无限循环模式，此时设置进度值无效。
 *
 * progressRingSize：进度条宽度
 *
 * progressBgColor：进度条背景颜色
 *
 * progressColor：进度条颜色
 *
 * proTextColor：进度文本颜色
 *
 * proTextShow：是否显示进度值
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
 * infoTextShow:是否显示提示信息
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

    companion object {
        private const val TAG = "CircleProgressView"
    }

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
    private var progressColorStart = -2

    /**
     * 圆环渐变色结束颜色
     */
    private var progressColorEnd = -2

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
     * 是否显示进度值
     */
    private var proTextShow = true

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
    private var proInfoText: String? = ""

    /**
     * 是否显示提示信息
     */
    private var infoTextShow = false

    /**
     * 没有进度值，无限循环模式
     */
    var proInfiniteMode = false
        set(value) {
            field = value
            if (value) {
                startInfinite()
            } else {
                stopInfinite()
            }
        }

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
            progressColorStart = type.getColor(R.styleable.CircleProgressView_progressColorStart, progressColorStart)
            progressColorEnd = type.getColor(R.styleable.CircleProgressView_progressColorEnd, progressColorEnd)
            proTextColor = type.getColor(R.styleable.CircleProgressView_proTextColor, Color.BLACK)
            proTextSize = type.getDimension(R.styleable.CircleProgressView_proTextSize, 30f)
            proTextShow = type.getBoolean(R.styleable.CircleProgressView_proTextShow, true)
            progress = type.getFloat(R.styleable.CircleProgressView_proValue, 0f)
            progressMax = type.getFloat(R.styleable.CircleProgressView_progressMax, 100f)
            proInfoTextColor =
                type.getColor(R.styleable.CircleProgressView_infoTextColor, Color.BLACK)
            proInfoTextSize = type.getDimension(R.styleable.CircleProgressView_infoTextSize, 20f)
            proInfoText = type.getString(R.styleable.CircleProgressView_infoTextString)
            infoTextShow = type.getBoolean(R.styleable.CircleProgressView_infoTextShow, false)
            proInfiniteMode = type.getBoolean(R.styleable.CircleProgressView_proInfiniteMode, false)
            type.recycle()
        }
        paint.isAntiAlias = true
//        if (proInfiniteMode) startInfinite()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            if (proInfiniteMode) {
                drawInfiniteMode(this)
            } else {
                drawCenterCircle(this)
                drawProRing(this)
                drawProgress(this)
                drawInfo(this)
            }
        }
    }

    private fun drawInfiniteMode(canvas: Canvas) {
        paint.shader = null
        paint.style = Paint.Style.STROKE
        paint.color = progressBgColor
        paint.strokeWidth = progressRingSize
        paint.strokeCap = Paint.Cap.ROUND

        canvas.drawCircle(width / 2f, height / 2f, width / 2f - progressRingSize, paint)
        Log.i(TAG, "drawInfiniteMode progressColorStart:$progressColorStart  progressColorEnd:$progressColorEnd")
        paint.shader = SweepGradient(
            width / 2f,
            height / 2f,
            intArrayOf(progressColorStart, progressColorEnd, progressColorEnd,progressColorStart),
            floatArrayOf(0.0f, 0.2f, 0.8f,1f)
        )

        val progressFloat = (progress / progressMax) * 360f
        val rectF = RectF(progressRingSize, progressRingSize, width - progressRingSize, height - progressRingSize)
        canvas.save()
        canvas.rotate(startAngle, width / 2f, height / 2f)
        canvas.drawArc(rectF, 0f, progressFloat, false, paint)
        canvas.restore()
    }

    private var startAngle = 0.0f

    private val valueAnimation = ValueAnimator.ofFloat(90f, -270f)

    private fun startInfinite() {
        progress = 80f
        if (!valueAnimation.isRunning) {
            valueAnimation.duration = 1500
            valueAnimation.repeatCount = ValueAnimator.INFINITE
            valueAnimation.addUpdateListener {
                startAngle = it.animatedValue as Float
                invalidate()
            }
            valueAnimation.start()
        }
    }

    private fun stopInfinite() {
        if (valueAnimation.isRunning)
            valueAnimation.cancel()
    }

    private fun drawCenterCircle(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = centerColor
        val min = min(width, height)
        centerRadius = (min - progressRingSize) / 2f
        canvas.drawCircle(width / 2f, height / 2f, centerRadius, paint)
    }

    /**
     * 绘制背景圆环
     */
    private fun drawProRing(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.color = progressBgColor
        paint.strokeWidth = progressRingSize
        canvas.drawCircle(width / 2f, height / 2f, centerRadius, paint)
    }

    private fun drawProgress(canvas: Canvas) {
        if (progress > 0) {
            paint.style = Paint.Style.STROKE
            if (progressColorStart == -1 || progressColorEnd == -1) {
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
            canvas.save()
            canvas.rotate(-90f, width / 2f, height / 2f)
            canvas.drawArc(rectF, 0f, progressFloat, false, paint)
            canvas.restore()
        }
        if (proTextShow) {
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
            canvas.drawText(progressString, (width - textWidth) / 2f, baseLine, paint)
        }
    }

    private fun drawInfo(canvas: Canvas) {
        proInfoText?.apply {
            if (infoTextShow && TextUtils.isEmpty(this)) return
            paint.style = Paint.Style.FILL
            paint.color = proInfoTextColor
            paint.textSize = proInfoTextSize
            val textWidth = paint.measureText(this)
            val baseLine = height / 2 + centerRadius - progressRingSize / 2 - 20
            canvas.drawText(this, (width - textWidth) / 2f, baseLine, paint)
        }
    }

}