package com.zougy.views

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.zougy.ziolib.R

/**
 * Description:用于在textView的前、后添加一个标签。例如必填项的*、表格输入的“:”
 *
 * 使用自定义属性设置相关信息：
 *
 * labelString：要显示的字符，如：* # ：
 *
 * labelPlace：标记的位置。true在前面，false 在最后。
 *
 * labelColor：标记文本的颜色
 *
 *
 * Author:邹高原<br></br>
 * Date:02/20 0020<br></br>
 * Email:441008824@qq.com
 */
class LabelTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    /**
     * 标记字符串
     */
    private var labelString: String? = null

    /**
     * 标记的位置。true在前面，false 在最后。
     */
    private var labelPlace = true

    /**
     * 标记文字的颜色
     */
    private var labelColor = Color.RED

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView)
            labelString = typedArray.getString(R.styleable.LabelTextView_labelString)
            labelColor = typedArray.getColor(R.styleable.LabelTextView_labelColor, textColors.defaultColor)
            labelPlace = typedArray.getBoolean(R.styleable.LabelTextView_labelPlace, false)
            if (!TextUtils.isEmpty(labelString)) {
                text = text
            }
            typedArray.recycle()
        }
    }

    /**
     * 设置标记文本
     */
    fun setLabelString(labelString: String?): LabelTextView {
        this.labelString = labelString
        text = text
        return this
    }

    /**
     * 设置标记的颜色<br></br>
     * 注：需在调用[LabelTextView.setLabelString]之前调用
     */
    fun setLabelColor(labelColor: Int): LabelTextView {
        this.labelColor = labelColor
        return this
    }

    /**
     * 设置标记的位置
     * 注：需在调用[LabelTextView.setLabelString]之前调用
     *
     * @param labelPlace true，在最前面，false 在最后面
     */
    fun setLabelPlace(labelPlace: Boolean): LabelTextView {
        this.labelPlace = labelPlace
        return this
    }

    override fun setText(text: CharSequence, type: BufferType) {
        if (!TextUtils.isEmpty(labelString)) {
            val label = if (labelPlace) {
                "<html><font color=$labelColor>$labelString</font>$text</html>"
            } else {
                "<html>$text<font color=$labelColor>$labelString</font></html>"
            }
            super.setText(Html.fromHtml(label), type)
        } else {
            super.setText(text, type)
        }
    }

}