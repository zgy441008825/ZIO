package com.zougy.ui.widget.setting

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.allViews
import com.zougy.log.LogUtils
import com.zougy.ziolib.R

/**
 * Description:
 * 带选项卡的设置
 * <br>
 * @author GaoYuanZou
 * @email v_gaoyuanzou@tinnove.com.cn
 * @date 2023/03/08
 */
class SettingTabControl : LinearLayout {

    /**
     * 要显示的菜单
     */
    private var tabItems = mutableListOf<String>()

    /**
     * 点击是回调的tag
     */
    private var tabItemsTag = mutableListOf<String>()

    /**
     * 每个菜单对应的图标  可以不设置，如果设置了必须和菜单数量一致
     */
    private var tabItemIcons = mutableListOf<Int>()

    /**
     * 每项Item文字的大小
     */
    private var itemTextSize = 20f

    /**
     * Item文字的颜色
     */
    private var itemTextColor = Color.BLACK

    /**
     * Item文字的颜色(对应各种状态)
     */
    private var itemTextColorStateList: ColorStateList? = null

    /**
     * Item的背景
     */
    private var itemBackground = 0

    /**
     * Item之间的距离
     */
    private var itemMargin = 0

    /**
     * 默认选中项
     */
    private var itemDefCheck = -1

    /**
     * Item的宽度
     */
    private var itemWidth = -2

    /**
     * Item的高度
     */
    private var itemHeight = -2

    /**
     * Item的图标和文字的间距
     */
    private var itemDrawablePadding = 0

    /**
     * Item的的对齐方式
     */
    private var itemGravity = ITEM_GRAVITY_START or ITEM_GRAVITY_CENTER_VERTICAL

    /**
     * Item图标显示的位置
     */
    private var itemDrawableLocation = DRAWABLE_LOCATION_LEFT

    companion object {

        private const val TAG = "SettingTabControl"

        const val VIEW_ID_RADIO_BUTTON_START = 0x100A

        const val DRAWABLE_LOCATION_LEFT = 0

        const val DRAWABLE_LOCATION_TOP = 1

        const val DRAWABLE_LOCATION_RIGHT = 2

        const val DRAWABLE_LOCATION_BOTTOM = 3

        const val ITEM_GRAVITY_LEFT = 0x03
        const val ITEM_GRAVITY_RIGHT = 0x05
        const val ITEM_GRAVITY_START = 0x00800003
        const val ITEM_GRAVITY_END = 0x00800005
        const val ITEM_GRAVITY_CENTER = 0x11
        const val ITEM_GRAVITY_CENTER_VERTICAL = 0x10
        const val ITEM_GRAVITY_CENTER_HORIZONTAL = 0x01
    }

    var onItemClick: IOnItemClick? = null

    private val onItemClickListener = object : OnClickListener {
        override fun onClick(v: View) {
            val index = v.id - VIEW_ID_RADIO_BUTTON_START
            if (index == itemDefCheck) {
                return
            }
            allViews.forEach { it.isSelected = false }
            var viewTag: String? = null
            v.tag?.also {
                viewTag = v.tag as String
            }
            onItemClick?.onItemClick(index, viewTag)
            v.isSelected = true
            itemDefCheck = index
        }

    }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        orientation = VERTICAL
        initAttrs(attrs)
        addViews()
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs?.apply {
            val typedArray = context.obtainStyledAttributes(this, R.styleable.SettingTabControl)
            val menusId = typedArray.getResourceId(R.styleable.SettingTabControl_tabItemsTexts, 0)
            if (menusId != 0) {
                tabItems = resources.getStringArray(menusId).toMutableList()
            }
            val tags = typedArray.getResourceId(R.styleable.SettingTabControl_tabItemsTags, 0)
            if (tags != 0) {
                tabItemsTag = resources.getStringArray(tags).toMutableList()
                if (tabItems.size != tabItemsTag.size) {
                    LogUtils.e(TAG, "tabItemsTag size error")
                }
            }

            val menusIconId = typedArray.getResourceId(R.styleable.SettingTabControl_tabItemIcons, 0)
            if (menusIconId != 0) {
                val ar: TypedArray = resources.obtainTypedArray(menusIconId)
                for (i in 0 until ar.length())
                    tabItemIcons.add(ar.getResourceId(i, 0))
                ar.recycle()
            }

            itemTextSize = typedArray.getDimension(R.styleable.SettingTabControl_itemTextSize, itemTextSize)
            itemTextColor = typedArray.getColor(R.styleable.SettingTabControl_itemTextColor, itemTextColor)
            itemTextColorStateList = typedArray.getColorStateList(R.styleable.SettingTabControl_itemTextColorStateList)
            itemBackground = typedArray.getResourceId(R.styleable.SettingTabControl_itemBackground, itemBackground)
            itemMargin = typedArray.getDimension(R.styleable.SettingTabControl_itemMargin, itemMargin.toFloat()).toInt()
            itemDefCheck = typedArray.getInt(R.styleable.SettingTabControl_itemDefCheck, itemDefCheck)
            itemWidth = typedArray.getLayoutDimension(R.styleable.SettingTabControl_itemWidth, itemWidth)
            itemHeight = typedArray.getLayoutDimension(R.styleable.SettingTabControl_itemHeight, itemHeight)
            itemDrawablePadding = typedArray.getDimension(R.styleable.SettingTabControl_itemDrawablePadding, itemDrawablePadding.toFloat()).toInt()
            itemGravity = typedArray.getInt(R.styleable.SettingTabControl_itemGravity, itemGravity)
            itemDrawableLocation = typedArray.getInt(R.styleable.SettingTabControl_itemDrawableLocation, itemDrawableLocation)

            typedArray.recycle()
        }
    }

    fun setItems(menus: Array<String>, icons: Array<Int>, tags: Array<String>? = null) {
        tabItems.clear()
        tabItems.addAll(menus)
        tabItemsTag.clear()
        tags?.also {
            tabItemsTag.addAll(it)
        }
        tabItemIcons.clear()
        tabItemIcons.addAll(icons)
        addViews()
    }

    private fun addViews() {
        if (tabItems.isEmpty()) return
        if (tabItemIcons.isNotEmpty() && tabItemIcons.size != tabItems.size) {
            LogUtils.e(TAG, "addViews error tabItemIcons.size != tabItems.size")
            return
        }
        removeAllViews()
        for ((cnt, menu) in tabItems.withIndex()) {
            TextView(context).apply {
                id = VIEW_ID_RADIO_BUTTON_START + cnt
                text = menu
                gravity = itemGravity
                if (itemTextColorStateList != null) {
                    setTextColor(itemTextColorStateList)
                } else {
                    setTextColor(itemTextColor)
                }
                setTextSize(TypedValue.COMPLEX_UNIT_SP, itemTextSize)
                setBackgroundResource(itemBackground)
                if (tabItemIcons.isNotEmpty()) {
                    when (itemDrawableLocation) {
                        DRAWABLE_LOCATION_LEFT -> {
                            setCompoundDrawablesWithIntrinsicBounds(tabItemIcons[cnt], 0, 0, 0)
                        }

                        DRAWABLE_LOCATION_TOP -> {
                            setCompoundDrawablesWithIntrinsicBounds(0, tabItemIcons[cnt], 0, 0)
                        }

                        DRAWABLE_LOCATION_RIGHT -> {
                            setCompoundDrawablesWithIntrinsicBounds(0, 0, tabItemIcons[cnt], 0)
                        }

                        DRAWABLE_LOCATION_BOTTOM -> {
                            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, tabItemIcons[cnt])
                        }
                    }

                    compoundDrawablePadding = itemDrawablePadding
                }
                val lp = LayoutParams(itemWidth, itemHeight)
                lp.also {
                    if (orientation == LinearLayout.HORIZONTAL) {
                        if (cnt < tabItems.size - 1)
                            it.rightMargin = itemMargin
                    } else {
                        if (cnt != 0)
                            it.topMargin = itemMargin
                    }
                }
                isSelected = cnt == itemDefCheck
                setOnClickListener(onItemClickListener)
                if (tabItems.size == tabItemsTag.size) {
                    tag = tabItemsTag[cnt]
                }
                addView(this, lp)
            }
        }
    }

    fun checkIndex(index: Int) {
        if (index !in 0 until tabItems.size) return
        if (index == itemDefCheck) return
        val textView = getChildAt(index) as TextView
        textView.callOnClick()
    }

    fun getSelectIndex(): Int = itemDefCheck
}

interface IOnItemClick {

    fun onItemClick(index: Int, tag: String?)

}