package com.zougy.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * Description:<br>
 * @author GaoYuanZou
 * @email v_gaoyuanzou@tinnove.com.cn
 * @date 2023/03/15
 */
class IndicatorViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    var indicator: PageIndicator? = null

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        ev?.apply {
            indicator?.onPagerTouch(this)
        }
        return super.onTouchEvent(ev)
    }


}