package com.zougy.tools

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi

/**
 * Description: 动画辅助类<br>
 * @author GaoYuanZou
 * @email v_gaoyuanzou@tinnove.com.cn
 * @date 2024/01/02
 */

/**
 * 设置背景颜色动画
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.bgColorAnimator(
    startColor: Int,
    endColor: Int,
    duration: Long,
    repeatCount: Int = 0,
    repeatMode: Int = ValueAnimator.REVERSE,
    update: (Int) -> Unit = {},
    onAnimatorStart: (animation: Animator) -> Unit = {},
    onAnimationEnd: (animation: Animator) -> Unit = {},
    onAnimationCancel: (animation: Animator) -> Unit = {},
    onAnimationRepeat: (animation: Animator) -> Unit = {}
): Animator {
    return ValueAnimator.ofArgb(startColor, endColor)
        .apply {
            this.duration = duration
            this.repeatCount = repeatCount
            this.repeatMode = repeatMode
            addUpdateListener {
                update(it.animatedValue as Int)
                setBackgroundColor(it.animatedValue as Int)
            }

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    onAnimatorStart(animation)
                }

                override fun onAnimationEnd(animation: Animator) {
                    onAnimationEnd(animation)
                }

                override fun onAnimationCancel(animation: Animator) {
                    onAnimationCancel(animation)
                }

                override fun onAnimationRepeat(animation: Animator) {
                    onAnimationRepeat(animation)
                }

            })
        }
}