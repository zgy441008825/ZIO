package com.zougy.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * Description:<br>给RecyclerView增加分割线</br>
 * Author:邹高原<br></br>
 * Date:10/15 015<br></br>
 * Email:441008824@qq.com
 */
class RecyclerViewItemDecoration @JvmOverloads constructor(
    private val context: Context,
    private val layoutStyle: Int = LAYOUT_STYLE_VERTICAL,
    drawableID: Int = android.R.color.darker_gray
) : RecyclerView.ItemDecoration() {

    private var drawable: Drawable? = null

    private var spaceSize = 1

    init {
        drawable = context.resources.getDrawable(drawableID)
    }

    fun setDrawable(id: Int): RecyclerViewItemDecoration {
        drawable = context.resources.getDrawable(id)
        return this
    }

    fun setSpaceSize(spaceSize: Int): RecyclerViewItemDecoration {
        this.spaceSize = spaceSize
        return this
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        when (layoutStyle) {
            LAYOUT_STYLE_GRIDLAYOUT -> drawGrid(c, parent)
            LAYOUT_STYLE_HORIZONTAL -> drawHorizontal(c, parent)
            LAYOUT_STYLE_VERTICAL -> drawVertical(c, parent)
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val height = parent.width
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val childView = parent.getChildAt(i)
            val left = ViewCompat.getX(childView).toInt() + childView.width
            drawable!!.setBounds(left, 0, left + spaceSize, height)
            drawable!!.draw(canvas)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val width = parent.width
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val childView = parent.getChildAt(i)
            val top = ViewCompat.getY(childView).toInt() + childView.height
            drawable!!.setBounds(0, top, width, top + spaceSize)
            drawable!!.draw(canvas)
        }
    }

    private fun drawGrid(canvas: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        val manager = parent.layoutManager as GridLayoutManager?
        val spanCount = manager!!.spanCount
        val itemWidth = (parent.width - parent.paddingLeft - parent.paddingRight) / spanCount
        val row = if (childCount % spanCount == 0) childCount / spanCount else childCount / spanCount + 1
        val itemHeight = (parent.height - parent.paddingTop - parent.paddingBottom) / row
        for (i in 0 until childCount) {
            val childView = parent.getChildAt(i)
            //竖线
            if (i == 0 || i % (spanCount - 1) != 0) {
                drawable!!.setBounds(
                    childView.right,
                    childView.top,
                    childView.right + drawable!!.intrinsicWidth,
                    childView.top + itemHeight
                )
                drawable!!.draw(canvas)
            }
            //横线
            if (i < (row - 1) * spanCount) {
                drawable!!.setBounds(
                    childView.left,
                    childView.bottom,
                    childView.left + itemWidth,
                    childView.bottom + drawable!!.intrinsicHeight
                )
                drawable!!.draw(canvas)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        when (layoutStyle) {
            LAYOUT_STYLE_GRIDLAYOUT -> {
                outRect.right = if (spaceSize != 0) spaceSize else drawable!!.intrinsicWidth
                outRect.bottom = if (spaceSize != 0) spaceSize else drawable!!.intrinsicHeight
            }
            LAYOUT_STYLE_HORIZONTAL -> outRect.right = if (spaceSize != 0) spaceSize else drawable!!.intrinsicWidth
            LAYOUT_STYLE_VERTICAL -> outRect.bottom = if (spaceSize != 0) spaceSize else drawable!!.intrinsicHeight
        }
    }

    companion object {

        /**
         * 网格布局
         */
        val LAYOUT_STYLE_GRIDLAYOUT = 0

        /**
         * 线性布局——垂直方向
         */
        val LAYOUT_STYLE_VERTICAL = 1

        /**
         * 线性布局——水平方向
         */
        val LAYOUT_STYLE_HORIZONTAL = 2
    }
}
