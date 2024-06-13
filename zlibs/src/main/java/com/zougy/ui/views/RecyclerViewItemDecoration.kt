package com.zougy.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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

    /**
     * 绘制分割线的宽度
     */
    var spaceSize = 1

    private val rect = Rect()

    init {
        drawable = ResourcesCompat.getDrawable(context.resources, drawableID, context.theme)
    }

    fun setDrawable(id: Int) {
        drawable = ResourcesCompat.getDrawable(context.resources, id, context.theme)
    }

    /**
     * 设置间距
     */
    fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        rect.left = left
        rect.top = top
        rect.right = right
        rect.bottom = bottom
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (spaceSize == 0) return
        when (val manager = parent.layoutManager) {
            is GridLayoutManager -> {
                drawGrid(c, parent)
            }

            is LinearLayoutManager -> {
                if (manager.orientation == LinearLayoutManager.VERTICAL) {
                    drawVertical(c, parent)
                } else {
                    drawHorizontal(c, parent)
                }
            }
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        if (childCount < 1) return
        for (i in 0 until childCount) {
            val childView = parent.getChildAt(i)
            val top = (childView.y + rect.top).toInt()
            val start = (childView.x + childView.width).toInt()
            drawable!!.setBounds(start + rect.left, top, start + rect.left + spaceSize, top + childView.height - rect.bottom * 2)
            drawable!!.draw(canvas)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        if (childCount < 1) return
        for (i in 0 until childCount) {
            val childView = parent.getChildAt(i)
            val top = childView.y.toInt() + childView.height
            val width = childView.width
            drawable!!.setBounds(rect.left, top + rect.top, width - rect.right, top + rect.top + spaceSize)
            drawable!!.draw(canvas)
        }
    }

    private fun drawGrid(canvas: Canvas, parent: RecyclerView) {
        /*val childCount = parent.childCount
        if (childCount < 1) return
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
        }*/
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        when (parent.layoutManager) {
            is GridLayoutManager -> {
                outRect.left = rect.left
                outRect.top = rect.top
                outRect.right = rect.right
                outRect.bottom = rect.bottom
            }

            is LinearLayoutManager -> {
                outRect.left = rect.left
                outRect.top = rect.top
                outRect.right = rect.right
                outRect.bottom = rect.bottom
            }
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
