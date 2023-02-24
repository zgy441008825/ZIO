package com.zougy.ui.widget.setting

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import com.zougy.commons.ZLog
import com.zougy.ziolib.R

/**
 * Description:<br>
 * @author GaoYuanZou
 * @email v_gaoyuanzou@tinnove.com.cn
 * @date 2023/02/22
 */
class SettingView(context: Context, attrs: AttributeSet? = null) : BaseWidgetGroup(context, attrs) {

    /**
     * 标题文字
     */
    private var titleText: String? = ""

    /**
     * 标题字体大小
     */
    private var titleSize = 24f

    /**
     * 标题字体颜色
     */
    private var titleColor = Color.BLACK

    /**
     * 副标题文字
     */
    private var subTitleText: String? = ""

    /**
     * 副标题字体大小
     */
    private var subTitleSize = 20f

    /**
     * 副标题字体颜色
     */
    private var subTitleColor = Color.GRAY

    /**
     * 主标题和副标题的间距
     */
    private var titlePadding = 0f

    /**
     * 标题距离左边开始图标的距离
     */
    private var titleMarginMenuIcon = 0f

    /**
     * 设置项的图标
     */
    private var menuIconSrc = 0

    /**
     * 设置项图标对其方式
     */
    private var menuIconGravity = MENU_ICON_GRAVITY_TOP_CENTER_VERTICAL

    private var paddingStart = 0f
    private var paddingTop = 0f
    private var paddingBottom = 0f


    private lateinit var imgStartIcon: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvSubTitle: TextView

    private lateinit var rootLayout: ConstraintLayout

    companion object {
        /**
         * 顶部和主标题对其
         */
        const val MENU_ICON_GRAVITY_TOP = 1

        /**
         * 垂直居中
         */
        const val MENU_ICON_GRAVITY_TOP_CENTER_VERTICAL = 2

        /**
         * 底部 和副标题对其
         */
        const val MENU_ICON_GRAVITY_TOP_BOTTOM = 3
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_widget_setting_view, this)
        initAttrs(attrs)
        initView()
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs?.apply {
            val ty = context.obtainStyledAttributes(this, R.styleable.SettingView)

            titleText = ty.getString(R.styleable.SettingView_titleText)
            titleSize = ty.getDimension(R.styleable.SettingView_titleSize, titleSize)
            titleColor = ty.getColor(R.styleable.SettingView_titleColor, titleColor)
            subTitleText = ty.getString(R.styleable.SettingView_subTitleText)
            subTitleSize = ty.getDimension(R.styleable.SettingView_subTitleSize, subTitleSize)
            subTitleColor = ty.getColor(R.styleable.SettingView_subTitleColor, subTitleColor)

            paddingStart = ty.getDimension(R.styleable.SettingView_android_paddingStart, paddingStart)
            paddingTop = ty.getDimension(R.styleable.SettingView_android_paddingTop, paddingTop)
            paddingBottom = ty.getDimension(R.styleable.SettingView_android_paddingBottom, paddingBottom)

            titlePadding = ty.getDimension(R.styleable.SettingView_titlePadding, titlePadding)
            titleMarginMenuIcon = ty.getDimension(R.styleable.SettingView_titleMarginMenuIcon, titleMarginMenuIcon)

            menuIconSrc = ty.getResourceId(R.styleable.SettingView_menuIconSrc, menuIconSrc)
            menuIconGravity = ty.getInt(R.styleable.SettingView_menuIconGravity, menuIconGravity)

            ty.recycle()
        }
    }

    private fun initView() {
        imgStartIcon = findViewById(R.id.layoutWidgetSettingViewImgStartIcon)
        tvTitle = findViewById(R.id.layoutWidgetSettingViewTvTitle)
        tvSubTitle = findViewById(R.id.layoutWidgetSettingViewTvSubTitle)
        rootLayout = findViewById(R.id.layoutWidgetSettingViewRoot)

        setStartIcon()
        setSubTitle()
        setTitle()

        postDelayed({ remeasure() }, 10L)
    }

    private fun remeasure() {
        if (!tvSubTitle.isVisible) {
            val h1 = imgStartIcon.measuredHeight + imgStartIcon.marginTop + imgStartIcon.marginBottom
            val h2 = tvTitle.measuredHeight + tvTitle.marginTop + tvTitle.marginBottom
            ZLog.i("remeasure h1:$h1 h2:$h2")
            measure(
                MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(h1.coerceAtLeast(h2), MeasureSpec.AT_MOST)
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        ZLog.i("onMeasure w:${MeasureSpec.getSize(widthMeasureSpec)} h:${MeasureSpec.getSize(heightMeasureSpec)}")
    }

    private fun setStartIcon() {
        if (menuIconSrc > 0) {
            imgStartIcon.isVisible = true
            imgStartIcon.setImageResource(menuIconSrc)
            setMenuIconGravity()
        } else {
            imgStartIcon.isVisible = false
            imgStartIcon.updateLayoutParams<LayoutParams> {
                setMargins(0, 0, 0, 0)
            }
        }
    }

    private fun setTitle() {
        tvTitle.textSize = titleSize
        tvTitle.setTextColor(titleColor)
        tvTitle.text = if (TextUtils.isEmpty(titleText)) "" else titleText

        if (tvSubTitle.isVisible) {
            if (imgStartIcon.isVisible) {
                tvTitle.updateLayoutParams<LayoutParams> {
                    setMargins(titleMarginMenuIcon.toInt(), marginTop, marginEnd, (titlePadding / 2).toInt())
                }
            } else {
                tvTitle.updateLayoutParams<LayoutParams> {
                    setMargins(marginStart, marginTop, marginEnd, (titlePadding / 2).toInt())
                }
            }
        } else {
            ConstraintSet().apply {
                clone(rootLayout)
                clear(tvTitle.id)
                constrainWidth(tvTitle.id, LayoutParams.WRAP_CONTENT)
                constrainHeight(tvTitle.id, LayoutParams.WRAP_CONTENT)
                connect(tvTitle.id, ConstraintSet.TOP, LayoutParams.PARENT_ID, ConstraintSet.TOP)
                connect(tvTitle.id, ConstraintSet.BOTTOM, LayoutParams.PARENT_ID, ConstraintSet.BOTTOM)
                if (imgStartIcon.isVisible) {
                    connect(tvTitle.id, ConstraintSet.START, imgStartIcon.id, ConstraintSet.END)
                    setMargin(tvTitle.id, ConstraintSet.START, titleMarginMenuIcon.toInt())
                } else {
                    connect(tvTitle.id, ConstraintSet.START, LayoutParams.PARENT_ID, ConstraintSet.START)
                    setMargin(tvTitle.id, ConstraintSet.START, paddingStart.toInt())
                }
                setMargin(tvTitle.id, ConstraintSet.TOP, paddingTop.toInt())
                setMargin(tvTitle.id, ConstraintSet.BOTTOM, paddingBottom.toInt())
                applyTo(rootLayout)
            }
        }
    }

    private fun setSubTitle() {
        if (TextUtils.isEmpty(subTitleText)) {
            tvSubTitle.isVisible = false
        } else {
            tvSubTitle.textSize = subTitleSize
            tvSubTitle.setTextColor(subTitleColor)
            tvSubTitle.text = subTitleText
            /*if (imgStartIcon.isVisible) {
                tvSubTitle.updateLayoutParams<LayoutParams> {
                    setMargins(titleMarginMenuIcon.toInt(), (titlePadding / 2).toInt(), marginEnd, paddingBottom.toInt())
                }
            } else {
                tvSubTitle.updateLayoutParams<LayoutParams> {
                    setMargins(0, (titlePadding / 2).toInt(), marginEnd, paddingBottom.toInt())
                }
            }*/
        }
    }

    private fun setMenuIconGravity() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(rootLayout)
        constraintSet.clear(imgStartIcon.id)
        constraintSet.constrainWidth(imgStartIcon.id, LayoutParams.WRAP_CONTENT)
        constraintSet.constrainHeight(imgStartIcon.id, LayoutParams.WRAP_CONTENT)
        if (TextUtils.isEmpty(subTitleText)) menuIconGravity = MENU_ICON_GRAVITY_TOP_CENTER_VERTICAL
        when (menuIconGravity) {
            MENU_ICON_GRAVITY_TOP -> {
                constraintSet.connect(
                    imgStartIcon.id,
                    ConstraintSet.TOP,
                    tvTitle.id,
                    ConstraintSet.TOP
                )
                constraintSet.connect(
                    imgStartIcon.id,
                    ConstraintSet.BOTTOM,
                    tvTitle.id,
                    ConstraintSet.BOTTOM
                )
            }
            MENU_ICON_GRAVITY_TOP_CENTER_VERTICAL -> {
                constraintSet.connect(
                    imgStartIcon.id,
                    ConstraintSet.TOP,
                    LayoutParams.PARENT_ID,
                    ConstraintSet.TOP
                )
                constraintSet.connect(
                    imgStartIcon.id,
                    ConstraintSet.BOTTOM,
                    LayoutParams.PARENT_ID,
                    ConstraintSet.BOTTOM
                )
            }
            MENU_ICON_GRAVITY_TOP_BOTTOM -> {
                constraintSet.connect(
                    imgStartIcon.id,
                    ConstraintSet.TOP,
                    tvSubTitle.id,
                    ConstraintSet.TOP
                )
                constraintSet.connect(
                    imgStartIcon.id,
                    ConstraintSet.BOTTOM,
                    tvSubTitle.id,
                    ConstraintSet.BOTTOM
                )
            }
        }

        constraintSet.connect(
            imgStartIcon.id,
            ConstraintSet.START,
            LayoutParams.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.setMargin(imgStartIcon.id, ConstraintSet.START, paddingStart.toInt())
        constraintSet.setMargin(imgStartIcon.id, ConstraintSet.TOP, paddingTop.toInt())
        constraintSet.setMargin(imgStartIcon.id, ConstraintSet.BOTTOM, paddingBottom.toInt())
        constraintSet.applyTo(rootLayout)
    }


}