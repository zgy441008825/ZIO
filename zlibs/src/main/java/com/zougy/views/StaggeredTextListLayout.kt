package com.zougy.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import com.zougy.ziolib.R

/**
 * Description:<br>
 * Author:邹高原<br>
 * Date:03/31 0031<br>
 * Email:441008824@qq.com
 */
class StaggeredTextListLayout : ViewGroup {

    private var children = emptyArray<String>()
    private var childStyle = 0
    private var childPadding = 10

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet? = null, style: Int = 0) : super(context, attributeSet, style) {
        if (attributeSet != null) {
            val typeValue = context.obtainStyledAttributes(attributeSet, R.styleable.StaggeredTextListLayout)
            val childrenId = typeValue.getResourceId(R.styleable.StaggeredTextListLayout_children, 0)
            childPadding = typeValue.getDimension(R.styleable.StaggeredTextListLayout_childPadding, 10f).toInt()
            childStyle = typeValue.getResourceId(R.styleable.StaggeredTextListLayout_childrenStyle, 0)
            Log.d("StaggeredTextListLayout", "ZLog childStyle:$childStyle ")
            typeValue.recycle()
            if (childrenId != 0) {
                children = context.resources.getStringArray(childrenId)
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 0) return
        var widthCount = paddingStart + childPadding
        var heightCount = paddingTop + childPadding
        Log.d("StaggeredTextListLayout", "ZLog onLayout $measuredWidth $measuredHeight")
        for (i in 0 until childCount) {
            val textView = getChildAt(i) as TextView
            val textW = (textView.measuredWidth + childPadding)
            val textH = (textView.measuredHeight + childPadding)
            var left: Int
            var top: Int
            if ((widthCount + textW) > (width - paddingRight)) {//换行
                heightCount += textH
                left = paddingLeft
                top = heightCount
            } else {
                left = widthCount
                top = heightCount
                widthCount += textW
            }
            Log.d("StaggeredTextListLayout", "ZLog onLayout $left $top ${left + textW} ${top + textH}")
            textView.layout(
                left,
                top,
                (left + textW),
                (top + textH)
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (children.isEmpty()) return
        removeAllViews()
        val widthMax = measureWidth(widthMeasureSpec)

        var widthCount = paddingStart
        var heightCount = 0
        var childMaxHeight = 0
        for (child in children) {
            val textView = TextView(context, null, childStyle)
            textView.text = child
            addView(textView)
            measureChild(textView, widthMeasureSpec, heightMeasureSpec)

            val textW = textView.measuredWidth + childPadding
            val textH = textView.measuredHeight + childPadding
            childMaxHeight = if (textH > childMaxHeight) textH else childMaxHeight
            if ((widthCount + textW) > (widthMax - paddingRight)) {//换行
                heightCount += textH
                widthCount = paddingStart
            } else {
                widthCount += textW
            }
        }
        if (heightCount == 0) heightCount = childMaxHeight
        heightCount += paddingTop + childPadding * 2 + paddingBottom
        val wSpec = MeasureSpec.makeMeasureSpec(widthMax, MeasureSpec.EXACTLY)
        val hSpec = MeasureSpec.makeMeasureSpec(heightCount, MeasureSpec.EXACTLY)
        super.onMeasure(wSpec, hSpec)
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        return when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.AT_MOST -> {
                MeasureSpec.getSize(widthMeasureSpec)
            }
            MeasureSpec.EXACTLY -> {
                MeasureSpec.getSize(widthMeasureSpec)
            }
            else -> {
                MeasureSpec.getSize(widthMeasureSpec)
            }
        }
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        return when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.AT_MOST -> {
                MeasureSpec.getSize(heightMeasureSpec)
            }
            MeasureSpec.EXACTLY -> {
                MeasureSpec.getSize(heightMeasureSpec)
            }
            else -> {
                MeasureSpec.getSize(heightMeasureSpec)
            }
        }
    }

}