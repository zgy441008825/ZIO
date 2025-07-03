package com.zougy.ui.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IntDef
import com.zougy.ziolib.R

/**
 * Description:
 *
 * Author: zougaoyuan
 *
 * Email: zougaoy@cqyazaki.com.cn
 *
 * CreateTime: 2025/07/03 10:53
 */
class TaggedTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        /**
         * 标签位置常量 左边
         */
        const val LEFT = 0

        /**
         * 标签位置常量 右边
         */
        const val RIGHT = 1

        /**
         * 垂直对齐常量 顶部
         */
        const val TOP = 0

        /**
         * 垂直对齐常量 中间
         */
        const val CENTER = 1

        /**
         * 垂直对齐常量 底部
         */
        const val BOTTOM = 2
    }

    @IntDef(LEFT, RIGHT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class TagPosition

    @IntDef(TOP, CENTER, BOTTOM)
    @Retention(AnnotationRetention.SOURCE)
    annotation class VerticalAlignment

    // 自定义属性

    /**
     * 正常文本
     */
    private var normalText: String = ""

    /**
     * 标签文本
     */
    private var tagText: String = ""

    /**
     * 正常文本颜色
     */
    private var normalTextColor: Int = Color.BLACK

    /**
     * 标签文本颜色
     */
    private var tagTextColor: Int = Color.RED

    /**
     * 正常文本大小
     */
    private var normalTextSize: Float = 16f

    /**
     * 标签文本大小
     */
    private var tagTextSize: Float = 16f

    /**
     * 标签位置
     */
    private var tagPosition: Int = LEFT

    /**
     * 标签垂直对齐方式
     */
    private var tagVerticalAlignment: Int = CENTER

    /**
     * 正常文本画笔
     */
    private val normalTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.LEFT
    }

    /**
     * 标签文本画笔
     */
    private val tagTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.LEFT
    }

    /**
     * 正常文本绘制区域
     */
    private val normalTextBounds = Rect()

    /**
     * 标签文本绘制区域
     */
    private val tagTextBounds = Rect()

    /**
     * 标签X坐标
     */
    private var tagX = 0f

    /**
     * 标签Y坐标
     */
    private var tagY = 0f

    /**
     * 正常文本X坐标
     */
    private var normalX = 0f

    /**
     * 正常文本Y坐标
     */
    private var normalY = 0f

    init {
        // 加载自定义属性
        context.obtainStyledAttributes(attrs, R.styleable.TaggedTextView).apply {
            normalText = getString(R.styleable.TaggedTextView_normalText) ?: ""
            tagText = getString(R.styleable.TaggedTextView_tagText) ?: ""
            normalTextColor = getColor(R.styleable.TaggedTextView_normalTextColor, Color.BLACK)
            tagTextColor = getColor(R.styleable.TaggedTextView_tagTextColor, Color.RED)
            normalTextSize = getDimension(R.styleable.TaggedTextView_normalTextSize, 16f)

            // 标签文本大小默认为正常文本大小
            tagTextSize = getDimension(R.styleable.TaggedTextView_tagTextSize, normalTextSize)

            tagPosition = getInt(R.styleable.TaggedTextView_tagPosition, LEFT)
            tagVerticalAlignment = getInt(R.styleable.TaggedTextView_tagVerticalAlignment, CENTER)
            recycle()
        }

        // 初始化画笔
        updateTextPaints()
    }

    private fun updateTextPaints() {
        // 配置正常文本画笔
        normalTextPaint.color = normalTextColor
        normalTextPaint.textSize = normalTextSize

        // 配置标签文本画笔
        tagTextPaint.color = tagTextColor
        tagTextPaint.textSize = tagTextSize
    }

    // 设置标签位置
    fun setTagPosition(@TagPosition position: Int) {
        tagPosition = position
        requestLayout()
        invalidate()
    }

    // 设置垂直对齐方式
    fun setTagVerticalAlignment(@VerticalAlignment alignment: Int) {
        tagVerticalAlignment = alignment
        requestLayout()
        invalidate()
    }

    // 设置正常文本
    fun setNormalText(text: String) {
        normalText = text
        requestLayout()
        invalidate()
    }

    // 设置标签文本
    fun setTagText(text: String) {
        tagText = text
        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        updateTextPaints()

        // 测量文本尺寸
        normalTextPaint.getTextBounds(normalText, 0, normalText.length, normalTextBounds)
        tagTextPaint.getTextBounds(tagText, 0, tagText.length, tagTextBounds)

        // 计算总宽度
        val totalWidth = normalTextBounds.width() + tagTextBounds.width()

        // 计算最大高度（包含垂直对齐的偏移）
        val textHeight = maxOf(normalTextBounds.height(), tagTextBounds.height()).toFloat()
        val maxAscent = maxOf(normalTextPaint.fontMetrics.ascent, tagTextPaint.fontMetrics.ascent)
        val maxDescent = maxOf(normalTextPaint.fontMetrics.descent, tagTextPaint.fontMetrics.descent)
        val totalHeight = maxDescent - maxAscent

        // 添加必要的内边距
        val desiredWidth = totalWidth + paddingLeft + paddingRight
        val desiredHeight = totalHeight + paddingTop + paddingBottom

        // 设置测量尺寸
        setMeasuredDimension(
            resolveSize(desiredWidth.toInt(), widthMeasureSpec),
            resolveSize(desiredHeight.toInt(), heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 计算基线Y坐标（垂直居中）
        val centerY = height / 2f - (normalTextPaint.descent() + normalTextPaint.ascent()) / 2

        when (tagPosition) {
            LEFT -> {
                normalX = tagTextBounds.width().toFloat() + paddingLeft
                tagX = paddingLeft.toFloat()
            }

            RIGHT -> {
                normalX = paddingLeft.toFloat()
                tagX = normalTextBounds.width().toFloat() + paddingLeft
            }
        }

        // 计算标签垂直位置
        tagY = when (tagVerticalAlignment) {
            TOP -> centerY + normalTextPaint.ascent() - tagTextPaint.ascent()
            BOTTOM -> centerY + normalTextPaint.descent() - tagTextPaint.descent()
            else -> centerY  // CENTER
        }

        normalY = centerY

        // 绘制文本
        canvas.drawText(normalText, normalX, normalY, normalTextPaint)
        canvas.drawText(tagText, tagX, tagY, tagTextPaint)
    }
}