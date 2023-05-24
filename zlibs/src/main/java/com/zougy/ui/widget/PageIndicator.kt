package com.zougy.ui.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.zougy.ziolib.R

/**
 * Description:
 * 仿小红书/图虫 分页指示器
 *
 * 显示超过[PageIndicator.itemTotal]时 则第一个或者最后一个显示为更小的点，翻页时进行切换
 * <br>
 * @author GaoYuanZou
 * @email v_gaoyuanzou@tinnove.com.cn
 * @date 2023/03/15
 */
class PageIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /**
     * 显示item的最大数量，如果页数<=5 就直接绘制完，如果>=5 则开启多页模式
     */
    private var itemTotal = 5

    /**
     * 非活动页的颜色
     */
    private var itemColorNormal = Color.GRAY

    /**
     * 当前页面的颜色
     */
    private var itemColorCurrent = Color.RED

    /**
     * 非活动页的圆点半径
     */
    private var itemRadiusNormal = 4f

    /**
     * 活动页的圆点半径
     */
    private var itemRadiusCurrent = 8f

    /**
     * 页面总数
     */
    private var itemCount = 0

    /**
     * item间距
     */
    private var itemSpace = 30f
        set(value) {
            field = value
            Log.i(TAG, " setItemSpace :$field")
        }

    /**
     * 当前显示页面索引，跟踪页面滑动时切换的index
     */
    private var itemCurrentIndex = 0

    /**
     * 保存上一次的位置
     */
    private var indexBack = 0

    private lateinit var paint: Paint

    /**
     * 保存每个圆点类型,
     */
    private var indicatorItemState = IndicatorItemState()

    /**
     * 滑动方向，判断用户是向左还是向右滑动，在用户抬起手指绘制圆点时需要通过这个方向来从新给indicatorItemState 中的数组赋值
     */
    private var slideDirection = SLIDE_DIRECTION_NORMAL


    companion object {
        const val TAG = "PageIndicator"

        /**
         * 无滑动状态
         */
        const val SLIDE_DIRECTION_NORMAL = -1

        /**
         * 向左滑动
         */
        const val SLIDE_DIRECTION_LEFT = 0

        /**
         * 向右滑动
         */
        const val SLIDE_DIRECTION_RIGHT = 1
    }

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        attrs?.apply {
            val ta = context.obtainStyledAttributes(this, R.styleable.PageIndicator)
            initAttrs(ta)
            ta.recycle()
        }
    }

    private fun initAttrs(typedArray: TypedArray) {
        itemTotal = typedArray.getInt(R.styleable.PageIndicator_itemTotal, itemTotal)
        itemColorNormal = typedArray.getColor(R.styleable.PageIndicator_itemColorNormal, itemColorNormal)
        itemColorCurrent = typedArray.getColor(R.styleable.PageIndicator_itemColorCurrent, itemColorCurrent)
        itemRadiusNormal = typedArray.getFloat(R.styleable.PageIndicator_itemRadiusNormal, itemRadiusNormal)
        itemRadiusCurrent = typedArray.getFloat(R.styleable.PageIndicator_itemRadiusCurrent, itemRadiusCurrent)
        itemSpace = typedArray.getFloat(R.styleable.PageIndicator_itemSpace, itemSpace)
    }

    fun setViewPager(viewPager: ViewPager) {
        viewPager.adapter?.apply {
            itemCount = count
            indicatorItemState = IndicatorItemState()
            indicatorItemState.init(itemCount, itemTotal)
        }

        viewPager.addOnPageChangeListener(onPageChangeListener)
        postInvalidate()
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            Log.i(TAG, "onPageSelected position:$position")
            itemCurrentIndex = position
        }

        override fun onPageScrollStateChanged(state: Int) {
        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (itemCount == 0) return
        canvas?.apply {
            if (itemCount <= itemTotal) {
                drawAllItem(this)
            } else {
                drawMoreItem(canvas)
            }
        }
    }

    /**
     * 页面数量<=itemTotal 直接绘制完所有的item
     */
    private fun drawAllItem(canvas: Canvas) {
        val allItemWidth = measureItemWidth()
        val startX = measureStartX(allItemWidth)
        for (i in 0 until itemCount) {
            paint.color = if (i == itemCurrentIndex) itemColorCurrent else itemColorNormal
            val xPoint = startX + i * itemSpace + itemRadiusCurrent
            canvas.drawCircle(xPoint, height / 2f, itemRadiusCurrent, paint)
        }
    }

    private fun drawMoreItem(canvas: Canvas) {
        val allItemWidth = measureItemWidth()
        val startX = measureStartX(allItemWidth)
        for (i in 0 until itemTotal) {
            paint.color = if (i == itemCurrentIndex) itemColorCurrent else itemColorNormal
            val xPoint = startX + i * itemSpace + itemRadiusCurrent
            if (indicatorItemState.isBig(i)) {
                canvas.drawCircle(xPoint, height / 2f, itemRadiusCurrent, paint)
            } else {
                canvas.drawCircle(xPoint, height / 2f, itemRadiusNormal, paint)
            }
        }
    }

    private fun measureItemWidth(): Float {
        return if (itemCount <= itemTotal) {
            itemCount * itemRadiusCurrent * 2 + (itemCount - 1) * itemSpace
        } else {
            itemTotal * itemRadiusCurrent * 2 + (itemTotal - 1) * itemSpace
        }
    }

    private fun measureStartX(allWidth: Float): Float {
        return (width - allWidth) / 2f
    }

    fun onPagerTouch(ev: MotionEvent) {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                slideDirection = SLIDE_DIRECTION_NORMAL
                Log.i(TAG, "onPagerTouch down")
            }
            MotionEvent.ACTION_UP -> {
                Log.i(TAG, "onPagerTouch up")
                onTouchUp()
                postInvalidate()
            }
        }
    }

    private fun onTouchUp() {
        if (indexBack != itemCurrentIndex) {
            slideDirection = if (itemCurrentIndex > indexBack) SLIDE_DIRECTION_LEFT else SLIDE_DIRECTION_RIGHT
            indicatorItemState.updateState(itemCurrentIndex, slideDirection)
        }
    }
}

/**
 * 圆点状态管理
 *
 * 当翻页时当前状态中的数组需要重新赋值
 */
class IndicatorItemState {

    private lateinit var stateArray: BooleanArray

    /**
     *指示器显示的最大数量
     */
    private var ITEM_MAX = 0

    companion object {

        const val TAG = "IndicatorItemState"

    }

    fun init(count: Int, max: Int) {
        Log.i(TAG, "init count:$count max:$max")
        ITEM_MAX = max;
        stateArray = BooleanArray(count) {
            if (count <= max)
                true
            else
                it < max - 1
        }
        Log.i(TAG, "init:${stateArray.contentToString()}")
    }

    /**
     * 更新每个item的状态，切换两边大小圆点的核心代码
     * @param index 切换后当前的位置
     * @param direction 滑动的方向 [PageIndicator.SLIDE_DIRECTION_LEFT] [PageIndicator.SLIDE_DIRECTION_RIGHT]
     */
    fun updateState(index: Int, direction: Int):Boolean {
        if (index >= stateArray.size) return false

        val firstSmall = stateArray.indexOfFirst { false }
        if (direction == PageIndicator.SLIDE_DIRECTION_LEFT) {
            if (index <= 2) {
                resetState(0, ITEM_MAX - 1)
                return false
            }
            if (stateArray[index - 1]) return false
            resetState(firstSmall - 1, firstSmall + ITEM_MAX - 1)
        } else {
            //这里是最后一个，直接重置最后的ITEM_MAX-1个item
            if (index == stateArray.size - 1) {
                resetState(stateArray.size - ITEM_MAX - 1, stateArray.size)
                return false
            }
            if (stateArray[index + 1]) return false
            resetState(firstSmall + 1, firstSmall + ITEM_MAX - 1)
        }
        return true
    }

    private fun resetState(start: Int, end: Int) {
        for (i in stateArray.indices) {
            stateArray[i] = (i >= start) && (i <= end)
        }
    }

    /**
     * 判断当前索引对应的状态是大圆点还是小圆点
     *
     * @return true:大圆点  false:小圆点
     */
    fun isBig(index: Int): Boolean = stateArray[index]

}