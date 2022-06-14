package com.zougy.ui.views

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View

/**
 * Description: 自定义的基类，主要完成各种属性的初始化。
 * @author GaoYuanZou
 * @date 2022/06/14
 * @Email :441008824@qq.com
 */
abstract class BaseView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    init {
        initView(context, attrs)
    }

    /**
     * 初始化自定义属性
     */
    abstract fun initAttrs(typedArray: TypedArray)

    /**
     * 获取自定义属性
     */
    abstract fun getStyleableRes(): IntArray

    private fun initView(context: Context, attrs: AttributeSet?) {
        attrs?.apply {
            val ta = context.obtainStyledAttributes(this, getStyleableRes())
            initAttrs(ta)
            ta.recycle()
        }
    }

}