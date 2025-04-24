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
import android.view.MotionEvent
import android.view.View
import com.zougy.log.LogUtils
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
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

        /**
         * 画笔样式——圆角
         */
        const val STROKE_CAP_ROUND = 1

        /**
         * 画笔样式——方形
         */
        const val STROKE_CAP_SQUARE = 2

        /**
         * 进度走势方向
         */
        object ORIENTATION {

            /**
             * 顺时针
             */
            const val CIRCLE_CLOCKWISE = 0

            /**
             * 逆时针
             */
            const val CIRCLE_ANTICLOCKWISE = 1

            /**
             * 水平方向——从左往右
             */
            const val H_LEFT_TO_RIGHT = 2

            /**
             * 水平方向——从右往左
             */
            const val H_RIGHT_TO_LEFT = 3

            /**
             * 垂直方向——从上往下
             */
            const val V_TOP_TO_BOTTOM = 4

            /**
             * 垂直方向——从下往上
             */
            const val V_BOTTOM_TO_TOP = 5

        }

    }

    private val paintProgress = Paint()

    private val paintText = TextPaint()

    private val paintShadow = Paint()

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
     * 是否显示进度值
     */
    private var showProgressValue = true

    /**
     * 进度值文字大小
     */
    private var valueTextSize = 10f

    /**
     * 显示的进度值是否采用整数
     */
    private var valueTextInt = false

    /**
     * 进度值文字颜色
     */
    private var valueTextColor = Color.BLACK

    /**
     * 使能滑动进度功能
     */
    var isSeekBar = false

    /**
     * 是否显示滑块
     */
    private var showThumb = false

    /**
     * 滑块半径
     */
    private var thumbRadius = 0f

    /**
     * 滑块颜色
     */
    private var thumbColor = Color.WHITE

    /**
     * 是否显示滑块阴影
     */
    private var thumbShadow = true

    /**
     * 滑块阴影颜色
     */
    private var thumbShadowColor = Color.BLACK

    /**
     * 滑块阴影大小
     */
    private var thumbShadowSize = 2f

    /**
     * 进度最大值
     */
    private var progressMax = 100f

    /**
     * 进度最小值
     */
    private var progressMin = 0f

    val onProgressChange: IOnProgressChange? = null

    /**
     * 当前的进度
     */
    private var progress = 0f

    init {
        if (attrs != null) {
            val type = context.obtainStyledAttributes(attrs, R.styleable.ProgressView)
            progress = type.getFloat(R.styleable.ProgressView_progressViewValue, progress)
            progressSize = type.getDimension(R.styleable.ProgressView_progressViewSize, progressSize.toFloat()).toInt()
            progressBgColor = type.getColor(R.styleable.ProgressView_progressViewBgColor, progressBgColor)
            progressColor = type.getColor(R.styleable.ProgressView_progressViewColor, progressColor)
            isLoop = type.getBoolean(R.styleable.ProgressView_progressViewIsLoop, false)
            style = type.getInt(R.styleable.ProgressView_progressViewStyle, style)
            strokeCap = type.getInt(R.styleable.ProgressView_progressViewStrokeCap, strokeCap)
            isClockwise = type.getBoolean(R.styleable.ProgressView_progressViewIsClockwise, isClockwise)
            type.getString(R.styleable.ProgressView_progressViewGradient)?.apply {
                val array = this.split(",")
                if (array.isNotEmpty()) {
                    val colors = array.map { Color.parseColor(it) }
                    progressColors = colors.toIntArray()
                    progressColor = progressColors!![0]
                }
            }

            //进度值相关
            showProgressValue = type.getBoolean(R.styleable.ProgressView_progressViewValueShow, showProgressValue)
            valueTextSize = type.getDimension(R.styleable.ProgressView_progressViewValueTextSize, valueTextSize)
            valueTextColor = type.getColor(R.styleable.ProgressView_progressViewValueTextColor, valueTextColor)
            valueTextInt = type.getBoolean(R.styleable.ProgressView_progressViewValueInt, valueTextInt)

            //滑块相关属性
            isSeekBar = type.getBoolean(R.styleable.ProgressView_progressViewIsSeekBar, isSeekBar)
            showThumb = type.getBoolean(R.styleable.ProgressView_progressViewShowThumb, showThumb)
            thumbRadius = type.getDimension(R.styleable.ProgressView_progressViewThumbRadius, thumbRadius)
            thumbColor = type.getColor(R.styleable.ProgressView_progressViewThumbColor, thumbColor)
            if (thumbRadius == 0f) {
                thumbRadius = progressSize.toFloat()
            }

            thumbShadow = type.getBoolean(R.styleable.ProgressView_progressViewThumbShowShadow, thumbShadow)
            thumbShadowColor = type.getColor(R.styleable.ProgressView_progressViewThumbShadowColor, thumbShadowColor)
            thumbShadowSize = type.getDimension(R.styleable.ProgressView_progressViewThumbShadowSize, thumbShadowSize)

            progressMax = type.getInt(R.styleable.ProgressView_android_max, progressMax.toInt()).toFloat()
            progressMin = type.getInt(R.styleable.ProgressView_android_min, progressMin.toInt()).toFloat()

            type.recycle()
        }

        paintText.isAntiAlias = true
        paintText.color = valueTextColor
        paintText.textSize = valueTextSize

        paintProgress.isAntiAlias = true
        paintProgress.strokeWidth = progressSize.toFloat()
        paintProgress.color = progressColor

        paintShadow.isAntiAlias = true
        paintShadow.color = thumbShadowColor

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
        LogUtils.i(TAG, "onMeasure ")
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        if (style == STYLE_CIRCLE) {
            val size = min(width, height)
            super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(size, MeasureSpec.AT_MOST))
        } else {
            var w = MeasureSpec.getSize(widthMeasureSpec)
            var h = MeasureSpec.getSize(heightMeasureSpec)

            if (!showThumb) {
                thumbRadius = 0f
                if (!thumbShadow)
                    thumbShadowSize = 0f
            }
            val t = max(progressSize.toFloat(), thumbRadius * 2 + thumbShadowSize * 2).toInt()

            if (style == STYLE_BAR_HORIZONTAL) {
                h = t
            } else {
                w = t
            }
            super.onMeasure(
                MeasureSpec.makeMeasureSpec(w, MeasureSpec.getMode(widthMeasureSpec)),
                MeasureSpec.makeMeasureSpec(h, MeasureSpec.getMode(heightMeasureSpec))
            )
        }
    }

    private fun initDrawRect(w: Int, h: Int) {
        if (!showThumb) {
            thumbRadius = 0f
            if (!thumbShadow)
                thumbShadowSize = 0f
        }
        val thumbSize = thumbRadius + thumbShadowSize
        val t = max(progressSize / 2f, thumbSize)

        if (style == STYLE_CIRCLE || strokeCap == STROKE_CAP_ROUND) {
            drawRectF.left = t
            drawRectF.top = t
            drawRectF.right = w - t
            drawRectF.bottom = h - t
        } else {
            drawRectF.left = thumbSize
            drawRectF.top = thumbSize
            drawRectF.right = w - thumbSize
            drawRectF.bottom = h - thumbSize
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        LogUtils.i(TAG, "onSizeChanged ")
        initDrawRect(w, h)
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
                    else if (style == STYLE_BAR_HORIZONTAL)
                        LinearGradient(0f, h / 2f, w.toFloat(), h / 2f, this, positions.toFloatArray(), Shader.TileMode.CLAMP)
                    else
                        LinearGradient(w / 2f, 0f, w / 2f, h.toFloat(), this, positions.toFloatArray(), Shader.TileMode.CLAMP)
                }
            }
        }
    }

    fun setProgress(value: Int) {
        setProgress(value, false)
    }

    private fun setProgress(value: Int, fromUser: Boolean) {
        if (style == STYLE_CIRCLE && isLoop) return
        progress = if (value > progressMax)
            progressMax
        else if (value < progressMin)
            progressMin
        else
            value.toFloat()
        postInvalidate()
        onProgressChange?.onProgressChange(this, value, fromUser)
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
                360f / progressMax * progress
        } else {
            if (style == STYLE_BAR_HORIZONTAL)
                (drawRectF.width() / progressMax) * progress
            else
                (drawRectF.height() / progressMax) * progress
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
            canvas.drawCircle(width - drawRectF.left, height / 2f, progressSize / 2f, paintProgress)
        }

        canvas.restore()

        if (!showProgressValue) return
        //绘制进度值
        val fm = paintText.fontMetrics
        val showText = if (valueTextInt) "${progress.toInt()}%" else "${progress}%"
        val x = drawRectF.left + (drawRectF.width() - paintText.measureText(showText)) / 2f
        val baseLine = drawRectF.top + (drawRectF.height() + (fm.bottom - fm.top)) / 2f - fm.descent
        canvas.drawText(showText, x, baseLine, paintText)
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
        val curProgress = drawRectF.left + getProgressValue()
        canvas.drawLine(drawRectF.left, height / 2f, curProgress, height / 2f, paintProgress)

        if (showThumb) {
            paintProgress.shader = null
            paintProgress.color = thumbColor
            if (thumbShadow) {
                paintShadow.setShadowLayer(thumbShadowSize, 0f, 0f, thumbShadowColor)
                canvas.drawCircle(curProgress, height / 2f, thumbRadius, paintShadow)
            }
            canvas.drawCircle(curProgress, height / 2f, thumbRadius, paintProgress)
        }
    }

    /**
     * 绘制垂直进度条
     */
    private fun drawVertical(canvas: Canvas) {
        //绘制背景
        paintProgress.shader = null
        paintProgress.color = progressBgColor
        paintProgress.clearShadowLayer()
        canvas.drawLine(width / 2f, drawRectF.top, width / 2f, drawRectF.bottom, paintProgress)

        if (shader != null) {
            paintProgress.shader = shader
        } else {
            paintProgress.color = progressColor
        }
        val curProgress = drawRectF.bottom - getProgressValue()
        canvas.drawLine(width / 2f, drawRectF.bottom, width / 2f, curProgress, paintProgress)
        if (showThumb) {
            paintProgress.shader = null
            paintProgress.color = thumbColor
            if (thumbShadow) {
                paintProgress.setShadowLayer(thumbShadowSize, 0f, 0f, thumbShadowColor)
//                canvas.drawCircle(width / 2f, curProgress, thumbRadius, paintShadow)
            }
            canvas.drawCircle(width / 2f, curProgress, thumbRadius, paintProgress)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (style == STYLE_CIRCLE) return super.onTouchEvent(event)
        if (!isSeekBar) return super.onTouchEvent(event)

        event?.also {
            calculateTouchProgress(if (style == STYLE_BAR_HORIZONTAL) it.x else it.y)
        }

        return true
    }

    /**
     * 计算当前点击的位置进度
     */
    private fun calculateTouchProgress(touchPoint: Float) {
        val range = progressMax - progressMin
        val tp = if (style == STYLE_BAR_HORIZONTAL) {
            val step = drawRectF.width() / range
            (touchPoint - drawRectF.left) / step
        } else {
            val step = drawRectF.height() / range
            (drawRectF.height() - touchPoint - drawRectF.top) / step
        }
        val p = (tp + 0.5f).roundToInt()
        LogUtils.i(TAG, "calculateTouchProgress p:$p tp:$tp")
        setProgress(p, true)
    }

}

interface IOnProgressChange {
    fun onProgressChange(view: ProgressView, progress: Int, fromUser: Boolean)
}