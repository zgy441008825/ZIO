package com.zougy.ui.views

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Matrix
import androidx.core.animation.doOnEnd

/**
 * @Description: 矩阵动画
 *
 * @Author: GaoYuanZou
 *
 * @Email: zougaoy@cqyazaki.com.cn
 *
 * @CreateTime: 2024/04/01 19:14
 */
class MatrixAnimator {

    companion object {
        val instance: MatrixAnimator by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MatrixAnimator()
        }
    }

    /**
     * 保存开始的矩阵
     */
    private val startValue = FloatArray(9)

    /**
     * 结束状态的矩阵
     */
    private val endValue = FloatArray(9)

    /**
     * 中间结果
     */
    private val resultValue = FloatArray(9)

    private lateinit var animation: ValueAnimator

    fun getAnimator(
        start: Matrix,
        end: Matrix,
        duration: Long = 300L,
        onUpdate: (FloatArray) -> Unit = {},
        onEnd: (Animator) -> Unit = {}
    ): ValueAnimator {
        start.getValues(startValue)
        end.getValues(endValue)
        animation = ValueAnimator.ofFloat(0f, 1f)
        animation.duration = duration
        animation.addUpdateListener {
            val v = animation.getAnimatedValue() as Float
            for (i in resultValue.indices) {
                resultValue[i] = startValue[i] + (endValue[i] - startValue[i]) * v
            }
            onUpdate.invoke(resultValue)
        }
        animation.doOnEnd(onEnd)
        return animation
    }

}

fun Matrix.getAnimator(
    matrix: Matrix,
    onUpdate: (FloatArray) -> Unit = {},
    onEnd: (Animator) -> Unit = {},
    block: (ValueAnimator) -> Unit = {}
): ValueAnimator {
    val animator = MatrixAnimator.instance.getAnimator(this, matrix, onUpdate = onUpdate, onEnd = onEnd)
    block.invoke(animator)
    return animator
}