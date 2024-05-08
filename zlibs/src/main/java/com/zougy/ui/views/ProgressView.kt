package com.zougy.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.SweepGradient
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.zougy.log.LogUtils
import com.zougy.ziolib.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * @Description:
 *
 * @Author: GaoYuanZou
 *
 * @Email: zougaoy@cqyazaki.com.cn
 *
 * @CreateTime: 2024/05/07 14:20
 */
class ProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {

        private const val TAG = "ProgressView"

        /**
         * 进度条样式——圆形
         */
        const val STYLE_CIRCLE = 0

        /**
         * 进度条样式——水平
         */
        const val STYLE_BAR_HORIZONTAL = 1

        /**
         * 进度条样式——垂直
         */
        const val STYLE_BAR_VERTICAL = 2

        const val STROKE_CAP_ROUND = 1

        const val STROKE_CAP_SQUARE = 2


        const val PROGRESS_MAX = 100f

        const val PROGRESS_MIN = 0f

    }

    private val paintProgress = Paint()

    private val paintText = TextPaint()

    /**
     * 进度条样式 取值为[STYLE_CIRCLE]和[STYLE_BAR]
     */
    private var style = STYLE_BAR_HORIZONTAL

    /**
     * 进度条大小
     */
    private var progressSize = 10

    /**
     * 进度条背景色
     */
    private var progressBgColor = Color.BLACK

    /**
     * 进度条渐变色
     */
    private var progressColors: IntArray? = null

    /**
     * 进度条颜色；如果设置了进度条的渐变色，这里的设置就无效
     */
    private var progressColor = Color.BLACK

    /**
     * 进度条的渐变Shader
     */
    private var shader: Shader? = null

    /**
     * 旋转角度；当为圆环无限进度时，开启的动画。
     */
    private var degrees = 0f

    /**
     * 是否顺时针旋转；当进度条为圆环进度时配置有效
     */
    private var isClockwise = true

    /**
     * 是否开启无限循环动画；当设置进度为圆环进度时可配置
     */
    private var isLoop = false

    /**
     * 圆环进度动画
     */
    private var loopAnimation: ValueAnimator? = null

    /**
     * 画笔笔头样式
     */
    private var strokeCap = STROKE_CAP_SQUARE

    /**
     * 绘制的矩形区域
     */
    private val drawRectF = RectF()

    /**
     * 当前的进度
     */
    var progress = 0f
        set(value) {
            if (style == STYLE_CIRCLE && isLoop) return
            if (value > PROGRESS_MAX)
                field = PROGRESS_MAX
            else if (value < PROGRESS_MIN)
                field = PROGRESS_MIN
            else
                field = value
            postInvalidate()
        }

    init {
        if (attrs != null) {
            val type = context.obtainStyledAttributes(attrs, R.styleable.ProgressView)
            progress = type.getFloat(R.styleable.ProgressView_progressViewValue, progress)
            progressSize = type.getInt(R.styleable.ProgressView_progressSize, progressSize)
            progressBgColor = type.getColor(R.styleable.ProgressView_progressViewBgColor, progressBgColor)
            progressColor = type.getColor(R.styleable.ProgressView_progressViewColor, progressColor)
            isLoop = type.getBoolean(R.styleable.ProgressView_progressViewIsLoop, false)
            style = type.getInt(R.styleable.ProgressView_progressViewStyle, style)
            strokeCap = type.getInt(R.styleable.ProgressView_progressStrokeCap, strokeCap)
            isClockwise = type.getBoolean(R.styleable.ProgressView_progressViewIsClockwise, isClockwise)
            type.getString(R.styleable.ProgressView_progressGradient)?.apply {
                val array = this.split(",")
                if (array.isNotEmpty()) {
                    val colors = array.map { Color.parseColor(it) }
                    progressColors = colors.toIntArray()
                    progressColor = progressColors!![0]
                }
            }

            type.recycle()
        }

        paintText.isAntiAlias = true

        paintProgress.isAntiAlias = true
        paintProgress.strokeWidth = progressSize.toFloat()
        paintProgress.color = progressColor

        if (strokeCap == STROKE_CAP_ROUND) {
            paintProgress.strokeCap = Paint.Cap.ROUND
        } else {
            paintProgress.strokeCap = Paint.Cap.SQUARE
        }

        if (style == STYLE_CIRCLE && isLoop) {
            progress = 100f
            loopAnimation = if (isClockwise) ValueAnimator.ofInt(0, 360) else ValueAnimator.ofInt(0, -360)
                .apply {
                    duration = 1000L
                    repeatCount = ValueAnimator.INFINITE
                    addUpdateListener {
                        degrees = (it.animatedValue as Int).toFloat()
                        postInvalidate()
                    }
                }
            startAnimator()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (style == STYLE_CIRCLE) {
            val size = min(width, height)
            super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(size, MeasureSpec.AT_MOST))
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawRectF.left = progressSize / 2f
        drawRectF.top = progressSize / 2f
        drawRectF.right = w - progressSize / 2f
        drawRectF.bottom = h - progressSize / 2f

        if (progressColors != null && progressColors!!.isNotEmpty()) {
            progressColors?.apply {
                if (size > 1) {
                    val positions = mutableListOf<Float>()
                    positions.add(0f)
                    val s = 1f / (size - 1).toFloat()
                    for (i in 1..size - 2) {
                        positions.add(i * s)
                    }
                    positions.add(1f)
                    shader = if (style == STYLE_CIRCLE)
                        SweepGradient(width / 2f, height / 2f, if (isClockwise) this else this.reversedArray(), positions.toFloatArray())
                    else
                        LinearGradient(0f, 0f, 0f, width.toFloat(), this, positions.toFloatArray(), Shader.TileMode.CLAMP)
                }
            }
        }
    }

    fun startAnimator() {
        loopAnimation?.start()
    }

    fun stopAnimator() {
        loopAnimation?.cancel()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.TRANSPARENT)
        when (style) {
            STYLE_CIRCLE -> {
                drawCircleProgress(canvas)
            }

            STYLE_BAR_HORIZONTAL -> {
                drawHorizontal(canvas)
            }

            STYLE_BAR_VERTICAL -> {
                drawVertical(canvas)
            }
        }

    }

    /**
     * 根据当前的进度值计算实际需要绘制的进度
     */
    private fun getProgressValue(): Float {
        return if (style == STYLE_CIRCLE) {
            if (isLoop) {
                360f
            } else
                360f / PROGRESS_MAX * progress
        } else {
            if (style == STYLE_BAR_HORIZONTAL)
                (drawRectF.width() / PROGRESS_MAX) * progress
            else
                (drawRectF.height() / PROGRESS_MAX) * progress
        }
    }

    /**
     * 绘制圆环进度条
     */
    private fun drawCircleProgress(canvas: Canvas) {
        paintProgress.style = Paint.Style.STROKE
        paintProgress.strokeCap = Paint.Cap.ROUND

        //绘制背景
        paintProgress.shader = null
        paintProgress.color = progressBgColor
        canvas.drawArc(
            drawRectF, 0f, 360f, false, paintProgress
        )

        //绘制进度
        canvas.save()
        canvas.rotate(if (isLoop) degrees else -90f, width / 2f, height / 2f)
        shader?.apply {
            paintProgress.shader = this
        }
        paintProgress.color = progressColor
        canvas.drawArc(
            drawRectF, 0f, if (isClockwise) getProgressValue() else -getProgressValue(), false, paintProgress
        )

        //绘制最后一个点上的圆点，因为如果有渐变色，收尾相连的地方颜色过度是一条线。
        progressColors?.apply {
            paintProgress.shader = null
            paintProgress.color = this[0]
            paintProgress.style = Paint.Style.FILL
            canvas.drawCircle(width - progressSize / 2f, height / 2f, progressSize / 2f, paintProgress)
        }

        canvas.restore()
    }

    /**
     * 计算弧形终点的位置
     */
    private fun calculateArcStartPoint(cx: Float, cy: Float, radius: Float, startAngleDegrees: Float): Pair<Float, Float> {
        // 确保角度转换为弧度
        val startAngleRadians = startAngleDegrees * PI / 180.0

        // 计算起始点坐标
        val startX = cx + radius * cos(startAngleRadians)
        val startY = cy + radius * sin(startAngleRadians)
        return Pair(startX.toFloat(), startY.toFloat())
    }

    /**
     * 绘制水平进度条
     */
    private fun drawHorizontal(canvas: Canvas) {
        //绘制背景
        paintProgress.shader = null
        paintProgress.color = progressBgColor
        canvas.drawLine(drawRectF.left, height / 2f, drawRectF.right, height / 2f, paintProgress)

        if (shader != null) {
            paintProgress.shader = shader
        } else {
            paintProgress.color = progressColor
        }
        canvas.drawLine(drawRectF.left, height / 2f, drawRectF.left + getProgressValue(), height / 2f, paintProgress)
    }

    /**
     * 绘制垂直进度条
     */
    private fun drawVertical(canvas: Canvas) {
        //绘制背景
        paintProgress.shader = null
        paintProgress.color = progressBgColor
        canvas.drawLine(width / 2f, drawRectF.top, width / 2f, drawRectF.bottom, paintProgress)

        if (shader != null) {
            paintProgress.shader = shader
        } else {
            paintProgress.color = progressColor
        }
        canvas.drawLine(width / 2f, drawRectF.bottom, width / 2f, drawRectF.bottom - getProgressValue(), paintProgress)
    }

}