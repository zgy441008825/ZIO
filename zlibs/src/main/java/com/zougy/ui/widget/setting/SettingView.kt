package com.zougy.ui.widget.setting

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
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


    private var imgStartIcon: ImageView? = null
    private lateinit var tvTitle: TextView
    private var tvSubTitle: TextView? = null

    private val constraintSet = ConstraintSet()

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

        /**
         * 界面内控件ID——开头显示的icon
         */
        const val VIEW_ID_IMG_START_ICON = 0x1001

        /**
         * 界面内控件ID——标题
         */
        const val VIEW_ID_TV_TITLE = 0x1002

        /**
         * 界面内控件ID——副标题
         */
        const val VIEW_ID_TV_SUBTITLE = 0x1003

        /**
         * 界面内控件ID——右侧显示的图标
         */
        const val VIEW_ID_IMG_RIGHT_ICON = 0x1004

        /**
         * 界面内控件ID——右侧显示的文本
         */
        const val VIEW_ID_TV_MSG = 0x1005

        const val VIEW_ID_GUIDELINE = 0x1006

        const val VIEW_ID_ROOT_LAYOUT = 0x1007
    }

    init {
        id = VIEW_ID_ROOT_LAYOUT
        constraintSet.clone(this)
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
        addContentView()

        imgStartIcon?.apply { addView(this) }
        addView(tvTitle)
        tvSubTitle?.apply { addView(this) }

        setStartIcon()
        setTitle()
        setSubTitle()
        constraintSet.applyTo(this)
    }

    private fun addContentView() {
        if (menuIconSrc > 0) {
            ImageView(context).apply {
                imgStartIcon = this
                id = VIEW_ID_IMG_START_ICON
            }
        }
        if (!TextUtils.isEmpty(titleText)) {
            TextView(context).apply {
                tvTitle = this
                id = VIEW_ID_TV_TITLE
            }
        } else {
            throw NullPointerException("title is null")
        }
        if (!TextUtils.isEmpty(subTitleText)) {
            TextView(context).apply {
                tvSubTitle = this
                id = VIEW_ID_TV_SUBTITLE
            }
        }
    }

    private fun setStartIcon() {
        imgStartIcon?.apply {
            setImageResource(menuIconSrc)
            setMenuIconGravity(this)
        }
    }

    private fun setTitle() {
        tvTitle.textSize = titleSize
        tvTitle.setTextColor(titleColor)
        tvTitle.text = if (TextUtils.isEmpty(titleText)) "" else titleText
        constraintSet.clear(tvTitle.id)
        constraintSet.constrainWidth(tvTitle.id, LayoutParams.WRAP_CONTENT)
        constraintSet.constrainHeight(tvTitle.id, LayoutParams.WRAP_CONTENT)
        if (imgStartIcon == null) {
            constraintSet.connect(tvTitle.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, paddingStart.toInt())
        } else {
            constraintSet.connect(
                tvTitle.id,
                ConstraintSet.START,
                VIEW_ID_IMG_START_ICON,
                ConstraintSet.END,
                titleMarginMenuIcon.toInt()
            )
        }
        constraintSet.connect(tvTitle.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)

        if (tvSubTitle != null) {
            val line = Guideline(context).apply {
                id = VIEW_ID_GUIDELINE
                addView(this)
                setGuidelinePercent(0.5f)
            }
            constraintSet.centerVertically(line.id, VIEW_ID_ROOT_LAYOUT)
            constraintSet.connect(tvTitle.id, ConstraintSet.BOTTOM, line.id, ConstraintSet.TOP, (titlePadding / 2).toInt())
        } else {
            constraintSet.connect(
                tvTitle.id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                paddingBottom.toInt()
            )
        }
    }

    private fun setSubTitle() {
        tvSubTitle?.apply {
            textSize = subTitleSize
            setTextColor(subTitleColor)
            text = subTitleText
            setSubTitleLayout(this)
        }
    }

    private fun setMenuIconGravity(imageView: ImageView) {
        constraintSet.clear(imageView.id)
        constraintSet.constrainWidth(imageView.id, LayoutParams.WRAP_CONTENT)
        constraintSet.constrainHeight(imageView.id, LayoutParams.WRAP_CONTENT)
        if (tvSubTitle == null) menuIconGravity = MENU_ICON_GRAVITY_TOP_CENTER_VERTICAL

        when (menuIconGravity) {
            MENU_ICON_GRAVITY_TOP -> {
                constraintSet.connect(
                    imageView.id,
                    ConstraintSet.TOP,
                    tvTitle.id,
                    ConstraintSet.TOP
                )
                constraintSet.connect(
                    imageView.id,
                    ConstraintSet.BOTTOM,
                    tvTitle.id,
                    ConstraintSet.BOTTOM
                )
            }
            MENU_ICON_GRAVITY_TOP_CENTER_VERTICAL -> {
                constraintSet.connect(
                    imageView.id,
                    ConstraintSet.TOP,
                    LayoutParams.PARENT_ID,
                    ConstraintSet.TOP
                )
                constraintSet.connect(
                    imageView.id,
                    ConstraintSet.BOTTOM,
                    LayoutParams.PARENT_ID,
                    ConstraintSet.BOTTOM
                )
            }
            MENU_ICON_GRAVITY_TOP_BOTTOM -> {
                constraintSet.connect(
                    imageView.id,
                    ConstraintSet.TOP,
                    tvSubTitle!!.id,
                    ConstraintSet.TOP
                )
                constraintSet.connect(
                    imageView.id,
                    ConstraintSet.BOTTOM,
                    tvSubTitle!!.id,
                    ConstraintSet.BOTTOM
                )
            }
        }

        constraintSet.connect(
            imageView.id,
            ConstraintSet.START,
            LayoutParams.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.setMargin(imageView.id, ConstraintSet.START, paddingStart.toInt())
        constraintSet.setMargin(imageView.id, ConstraintSet.TOP, paddingTop.toInt())
        constraintSet.setMargin(imageView.id, ConstraintSet.BOTTOM, paddingBottom.toInt())
    }


    private fun setSubTitleLayout(textView: TextView) {
        constraintSet.apply {
            clear(textView.id)
            constrainWidth(textView.id, LayoutParams.WRAP_CONTENT)
            constrainHeight(textView.id, LayoutParams.WRAP_CONTENT)

            connect(textView.id, ConstraintSet.TOP, VIEW_ID_GUIDELINE, ConstraintSet.BOTTOM, (titlePadding / 2).toInt())
            connect(textView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, paddingBottom.toInt())
            connect(textView.id, ConstraintSet.START, tvTitle.id, ConstraintSet.START)
        }
    }
}