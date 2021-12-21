package com.zougy.ui.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.zougy.ziolib.R
import java.io.File

/**
 * Description:可以通过fontType属性加载assets中字体
 *
 * Author:邹高原<br>
 * Date:11/14 0014<br>
 * Email:441008824@qq.com
 */
class CustomFontTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    styleId: Int = 0
) : AppCompatTextView(context, attributeSet, styleId) {

    init {
        if (attributeSet != null) {
            val valueType =
                context.obtainStyledAttributes(attributeSet, R.styleable.CustomFontTextView)
            val font = valueType.getString(R.styleable.CustomFontTextView_fontType)
            if (font != null) {
                try {
                    val face = Typeface.createFromAsset(context.assets, font)
                    typeface = face
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
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