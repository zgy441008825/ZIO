package com.zougy.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.SweepGradient
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.zougy.ziolib.R

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


        const val PROGRESS_MAX = 100

        const val PROGRESS_MIN = 0

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
     * 圆点的渐变shader；如果设置圆环进度，并且进度条是渐变色，会使用这个shader在圆环开始的绘制一个圆形，不然渐变进度显示会有问题
     */
    private var circlePointShader: Shader? = null

    /**
     * 旋转角度；当为圆环无限进度时，开启的动画。
     */
    private var degrees = 0f

    /**
     * 是否顺时针旋转；当进度条为圆环进度时配置有效
     */
    private var isClockwise = false

    /**
     * 是否开启无限循环动画；当设置进度为圆环进度时可配置
     */
    private var isLoop = false

    /**
     * 圆环进度动画
     */
    private var loopAnimation: ValueAnimator? = null

    /**
     * 当前的进度
     */
    var progress = 0
        set(value) {
            if (style == STYLE_CIRCLE && isLoop) return
            field = value
            postInvalidate()
        }

    init {
        if (attrs != null) {
            val type = context.obtainStyledAttributes(attrs, R.styleable.ProgressView)
            progressSize = type.getInt(R.styleable.ProgressView_progressSize, progressSize)
            progressBgColor = type.getColor(R.styleable.ProgressView_progressViewBgColor, progressBgColor)
            progressColor = type.getColor(R.styleable.ProgressView_progressViewColor, progressColor)
            isLoop = type.getBoolean(R.styleable.ProgressView_progressViewIsLoop, false)
            style = type.getInt(R.styleable.ProgressView_progressViewStyle, style)
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

        paintProgress.isAntiAlias = true
        paintText.isAntiAlias = true
        paintProgress.strokeWidth = progressSize.toFloat()
        paintProgress.style = Paint.Style.STROKE
        paintProgress.strokeCap = Paint.Cap.ROUND
        paintProgress.color = progressColor

        if (style == STYLE_CIRCLE && isLoop) {
            progress = 100
            loopAnimation = if (isClockwise) ValueAnimator.ofInt(0, 360) else ValueAnimator.ofInt(0, -360)
                .apply {
                    duration = 1000L
                    repeatCount = ValueAnimator.INFINITE
                    addUpdateListener {
                        degrees = (it.animatedValue as Int).toFloat()
                        postInvalidate()
                    }
                }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (progressColors != null && progressColors!!.isNotEmpty()) {
            progressColors?.apply {
                paintProgress.color = this[0]
                if (size > 1) {
                    val positions = mutableListOf<Float>()
                    positions.add(0f)
                    val s = 1f / (size - 1).toFloat()
                    for (i in 1..size - 2) {
                        positions.add(i * s)
                    }
                    positions.add(1f)
                    shader = if (style == STYLE_CIRCLE)
                        SweepGradient(width / 2f, height / 2f, this, positions.toFloatArray())
                    else
                        LinearGradient(0f, 0f, 0f, width.toFloat(), this, positions.toFloatArray(), Shader.TileMode.CLAMP)
                }
                circlePointShader = LinearGradient(
                    (width - progressSize).toFloat(), height / 2f, (width - progressSize).toFloat(), (height + progressSize) / 2f,
                    paintProgress.color, Color.TRANSPARENT, Shader.TileMode.CLAMP
                )
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

            }

            STYLE_BAR_VERTICAL -> {

            }
        }

    }

    /**
     * 根据当前的进度值计算实际需要绘制的进度
     */
    private fun getProgress(): Float {
        if (style == STYLE_CIRCLE) {
            if (isLoop) {
                return 360f
            }
            return 360f / PROGRESS_MAX * progress
        } else {
            return (if (style == STYLE_BAR_HORIZONTAL) width else height).toFloat() / PROGRESS_MAX * progress
        }
    }

    private fun drawCircleProgress(canvas: Canvas) {
        canvas.save()
        canvas.rotate(degrees, width / 2f, height / 2f)
        shader?.apply {
            paintProgress.shader = this
        }
        paintProgress.style = Paint.Style.STROKE
        paintProgress.strokeCap = Paint.Cap.ROUND
        canvas.drawArc(
            0f + progressSize / 2f, 0f + progressSize / 2f, width.toFloat() - progressSize / 2f, height.toFloat() - progressSize / 2f,
            0f, getProgress(), false, paintProgress
        )
        circlePointShader?.apply {
            paintProgress.shader = this
        }
        paintProgress.style = Paint.Style.FILL
        canvas.drawCircle(width - progressSize / 2f, height / 2f, progressSize / 2f, paintProgress)
        canvas.restore()
    }

}