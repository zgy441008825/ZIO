package com.zougy.ui.widget.setting

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import com.zougy.ziolib.R

/**
 * Description:
 * 带选项卡的设置
 * <br>
 * @author GaoYuanZou
 * @email v_gaoyuanzou@tinnove.com.cn
 * @date 2023/03/08
 */
class SettingTabControl @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RadioGroup(context, attrs) {

    private var tabItems = arrayOf<String>()
    private var tabItemTextSize = 20f
    private var tabItemTextColor = Color.BLACK
    private var tabItemTextColorStateList: ColorStateList? = null
    private var tabItemBg = 0
    private var tabLayoutBg = 0

    companion object {

        private const val TAG = "SettingTabControl"

        const val VIEW_ID_RADIO_BUTTON_START = 0x100A

    }

    var onItemClick: IOnItemClick? = null

    init {
        initAttrs(attrs)
        addViews()
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs?.apply {
            val typedArray = context.obtainStyledAttributes(this, R.styleable.SettingTabControl)
            val menusId = typedArray.getResourceId(R.styleable.SettingTabControl_tabItems, 0)
            if (menusId != 0) {
                tabItems = resources.getStringArray(menusId)
            }
            tabItemTextSize = typedArray.getDimension(R.styleable.SettingTabControl_tabItemTextSize, tabItemTextSize)
            tabItemTextColor = typedArray.getColor(R.styleable.SettingTabControl_tabItemTextColor, tabItemTextColor)
            tabItemTextColorStateList = typedArray.getColorStateList(R.styleable.SettingTabControl_tabItemTextColorStateList)
            tabItemBg = typedArray.getResourceId(R.styleable.SettingTabControl_tabItemBg, tabItemBg)
            tabLayoutBg = typedArray.getResourceId(R.styleable.SettingTabControl_tabLayoutBg, tabLayoutBg)

            typedArray.recycle()
        }
        orientation = LinearLayout.HORIZONTAL

        setOnCheckedChangeListener { group, checkedId ->
            Log.i(TAG, "initAttrs onClick checkedId:$checkedId")
            onItemClick?.onItemClick(group, checkedId - VIEW_ID_RADIO_BUTTON_START)
        }
    }

    private fun addViews() {
        Log.i(TAG, "addViews tabItems:${tabItems.contentToString()}")
        if (tabItems.isEmpty()) return
        for ((cnt, menu) in tabItems.withIndex()) {
            RadioButton(context).apply {
                id = VIEW_ID_RADIO_BUTTON_START + cnt
                text = menu
                gravity = Gravity.CENTER
                if (tabItemTextColorStateList != null) {
                    setTextColor(tabItemTextColorStateList)
                } else {
                    setTextColor(tabItemTextColor)
                }
                setTextSize(TypedValue.COMPLEX_UNIT_SP, tabItemTextSize)
                setBackgroundResource(tabItemBg)
                buttonDrawable = null
                val lp = LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
                lp.weight = 1f
                lp.gravity = Gravity.CENTER_VERTICAL
                addView(this, lp)
            }
            Log.i(TAG, "addViews cnt:$cnt menu:$menu")
        }
    }
}

interface IOnItemClick {

    fun onItemClick(group: RadioGroup, index: Int)

}