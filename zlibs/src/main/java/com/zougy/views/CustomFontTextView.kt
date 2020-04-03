package com.zougy.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.zougy.ziolib.R
import java.io.File

/**
 * Description:可以通过fontType属性加载assets中字体<br>
 * Author:邹高原<br>
 * Date:11/14 0014<br>
 * Email:441008824@qq.com
 */
class CustomFontTextView : androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, style: Int) : super(
        context,
        attributeSet,
        style
    ) {
        if (attributeSet != null) {
            val valueType =
                context.obtainStyledAttributes(attributeSet, R.styleable.CustomFontTextView)
            val font = valueType.getString(R.styleable.CustomFontTextView_fontType)
            val face = Typeface.createFromAsset(context.assets, font)
            typeface = face
            valueType.recycle()
        }
    }

    fun setTypefaceFromAsset(assetsFontName: String) {
        val face = Typeface.createFromAsset(context.assets, assetsFontName)
        typeface = face
    }

    fun setTypeface(file: File) {
        val face = Typeface.createFromFile(file)
        typeface = face
    }

    fun setTypeface(path: String) {
        val face = Typeface.createFromFile(path)
        typeface = face
    }
}