package com.zougy.ui.widget

import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import java.util.Arrays

/**
 * @Description:
 *
 * @Create:03/28 0028 21:11:35
 *
 * @Author:GaoYuanZou
 *
 * @Email:441008825@qq.com
 */
class RotateScaleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    companion object{
        private const val TAG = "RotateScaleImageView"
    }


    private val mMatrix = Matrix()

    private val tmpMatrix = Matrix()

    init {
        scaleType = ScaleType.MATRIX
    }

    fun scale(value: Float) {
        val matrixValues = FloatArray(9)
        matrix.getValues(matrixValues)
        Log.i(TAG, "scale begen values:${matrixValues.contentToString()}")
        mMatrix.set(matrix)
        mMatrix.preScale(value, value)
        mMatrix.getValues(matrixValues)
        Log.i(TAG, "scale mMatrix value:${matrixValues.contentToString()}")
        imageMatrix = mMatrix
        matrix.getValues(matrixValues)
        Log.i(TAG, "scale after values:${matrixValues.contentToString()}")
    }

}