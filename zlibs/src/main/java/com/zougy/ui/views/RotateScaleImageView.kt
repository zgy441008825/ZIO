package com.zougy.ui.views

import android.content.Context
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.zougy.log.LogUtils
import java.io.File
import kotlin.math.abs
import kotlin.math.min

/**
 * @Description: 自定义ImageView，实现缩放、旋转功能。
 *
 * @Author: GaoYuanZou
 *
 * @Email: zougaoy@cqyazaki.com.cn
 *
 * @CreateTime: 2024/03/28 12:57
 */
class RotateScaleImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "RotateScaleImageView"

        /**
         * 缩放的最大值
         */
        private const val SCALE_MAX = 3.0F

        /**
         * 每次点击是放大缩小的倍数
         */
        private const val SCALE_VALUE = 0.3f

        /**
         * 手势移动模式——正常状态
         */
        private const val TOUCH_MODE_NORMAL = 0

        /**
         * 手势移动模式——缩放模式
         */
        private const val TOUCH_MODE_SCALE = 1

        /**
         * 手势移动模式——移动模式
         */
        private const val TOUCH_MODE_MOVE = 2
    }

    private var touchMode = TOUCH_MODE_NORMAL

    /**
     * 图片的宽
     */
    private var bitmapWidth: Float = 0f

    /**
     * 图片的高
     */
    private var bitmapHeight: Float = 0f

    /**
     * 缩放的最小值
     */
    private var scaleMin = 1.0f

    /**
     * 移动和双击的手势
     */
    private var gestureDetector: GestureDetectorCompat

    /**
     * 缩放手势
     */
    private var scaleGestureDetector: ScaleGestureDetector

    /**
     * 操作图片的矩阵
     */
    private val mMatrix = Matrix()

    /**
     * 初始设置图片后保留矩阵数据
     */
    private val initImageMatrixValues = FloatArray(9)

    /**
     * 图片实际显示的坐标
     */
    private val drawableRectF = RectF()

    /**
     * View的坐标
     */
    private val viewRect = RectF()

    /**
     * 旋转角度
     */
    private var rotateAngle = 0f

    /**
     * 保存变换的数组
     */
    private val matrixValues = FloatArray(9)

    /**
     * 图片加载状态  true:加载成功   false:加载失败
     */
    var loadState = true

    /**
     * 图片地址
     */
    var imageFilePath: String? = null
        set(value) {
            field = value
            initShowFile()
        }

    init {
        gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {

            override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                move(distanceX, distanceY)
                return false
            }

            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                return true
            }

            override fun onDoubleTap(e: MotionEvent): Boolean {
                resetScale(e.x, e.y)
                return true
            }
        })

        scaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

            override fun onScale(detector: ScaleGestureDetector): Boolean {
                LogUtils.i(TAG, "onScale detector:${detector.scaleFactor}")
                mMatrix.set(imageMatrix)
                mMatrix.postScale(detector.scaleFactor, detector.scaleFactor, detector.focusX, detector.focusY)
                updateImage()
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {
                super.onScaleEnd(detector)
                scaleEndWithAnimator()
            }
        })
        scaleType = ScaleType.CENTER_INSIDE
    }

    private var drawable: Drawable? = null

    fun reset() {
        try {
            rotateAngle = 0f
            mMatrix.setValues(initImageMatrixValues)
            updateImage()
        } catch (e: Exception) {
            showError()
        }
    }

    private fun initShowFile() {
        try {
            if (imageFilePath == null) {
                showError()
                return
            }
            if (!File(imageFilePath!!).exists()) {
                showError()
                return
            }

            val source = ImageDecoder.createSource(File(imageFilePath!!))
            drawable = ImageDecoder.decodeDrawable(source) { _, info, _ ->
                bitmapWidth = info.size.width.toFloat()
                bitmapHeight = info.size.height.toFloat()
                LogUtils.i(TAG, "initShowFile imageFilePath:$imageFilePath bitmapWidth:$bitmapWidth bitmapHeight:$bitmapHeight")
            }

            setImageDrawable(drawable)
            (drawable as? AnimatedImageDrawable)?.start()
            postDelayed({
                scaleType = ScaleType.MATRIX
                imageMatrix.getValues(initImageMatrixValues)
                scaleMin = initImageMatrixValues[0]
            }, 100)
            loadState = true
        } catch (e: Exception) {
            e.printStackTrace()
            showError()
            return
        }
    }

    private fun showError() {
        isEnabled = false
        loadState = false
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewRect.run { set(0f, 0f, w.toFloat(), h.toFloat()) }
        mMatrix.reset()
        val scaling = getMinScaling()
        mMatrix.postScale(scaling, scaling, w / 2f, h / 2f)
        mMatrix.postRotate(rotateAngle)

        //修正位置 将图片移动到中心点
        resetDrawableRectF()
        mMatrix.mapRect(drawableRectF)
        mMatrix.postTranslate(w / 2f - (drawableRectF.left + drawableRectF.width() / 2), h / 2f - (drawableRectF.top + drawableRectF.height() / 2f))
        mMatrix.getValues(initImageMatrixValues)
        updateImage()
    }

    /**
     * 移动 会判断边界
     */
    private fun move(distanceX: Float, distanceY: Float) {
        resetDrawableRectF()
        imageMatrix.mapRect(drawableRectF)
        val curImgRect = RectF(drawableRectF)
        if (distanceX > 0) {
            if (drawableRectF.right.toInt() <= viewRect.right.toInt()) {
                return
            }
        } else if (distanceX < 0) {
            if (drawableRectF.left.toInt() >= viewRect.left.toInt()) {
                return
            }
        }

        var dx = distanceX
        var dy = distanceY
        val tMatrix = Matrix(imageMatrix)
        tMatrix.set(imageMatrix)
        tMatrix.postTranslate(-distanceX, -distanceY)
        tMatrix.getValues(matrixValues)

        resetDrawableRectF()
        tMatrix.mapRect(drawableRectF)
        LogUtils.i(TAG, "move drawableRectF:$drawableRectF width:$width distanceX:$distanceX")
        if (distanceX > 0 && drawableRectF.right <= width) { //左移 //如果移动后右边超过了边界 则不移动
            dx = curImgRect.right - viewRect.right
        } else if (distanceX < 0 && drawableRectF.left > viewRect.left) { //右移
            dx = curImgRect.left - viewRect.left
        }

        if (distanceY > 0 && drawableRectF.bottom < height) { //往上移
            dy = 0f
        } else //往下移
            if (distanceY < 0 && drawableRectF.top > 0) {//如果移动后右边超过了边界 则不移动
                dy = 0f
            }

        if (dx == 0f && dy == 0f) {
            return
        }
        LogUtils.i(TAG, "move dx:$dx dy:$dy")
        mMatrix.set(imageMatrix)
        mMatrix.postTranslate(-dx, -dy)
        updateImage()
    }

    /**
     * 双击重置缩放
     */
    private fun resetScale(x: Float, y: Float, animator: Boolean = true) {
        mMatrix.set(imageMatrix)
        mMatrix.getValues(matrixValues)
        scaleMin = getMinScaling()
        val curScaling = getScale(matrixValues)
        val endMatrix = Matrix()
        if (abs(curScaling - scaleMin) < 0.00001 || abs(curScaling) <= scaleMin) {
            endMatrix.postScale(SCALE_MAX, SCALE_MAX, x, y)
        } else {
            endMatrix.postScale(scaleMin, scaleMin, width / 2f, height / 2f)
        }
        endMatrix.postRotate(rotateAngle, width / 2f, height / 2f)

        //修正位置 将图片移动到中心点
        resetDrawableRectF()
        endMatrix.mapRect(drawableRectF)
        endMatrix.postTranslate(width / 2f - (drawableRectF.left + drawableRectF.width() / 2), height / 2f - (drawableRectF.top + drawableRectF.height() / 2f))

        MatrixAnimator.instance.getAnimator(imageMatrix, endMatrix, duration = if (animator) 300 else 0, onUpdate = {
            setImageMatrixValues(it)
        }).start()
    }

    /**
     * 双指缩放完后，根据最后是放大还是缩小和最后的缩放比例，以动画的形式缩放到最大或者最小
     */
    private fun scaleEndWithAnimator() {
        imageMatrix.getValues(matrixValues)
        val curScaling = abs((getScale(matrixValues)))
        scaleMin = getMinScaling()
        if (abs(curScaling) in scaleMin..SCALE_MAX) {
            return
        }
        val endMatrix = Matrix()
        if (abs(curScaling) < scaleMin) {
            endMatrix.postScale(scaleMin, scaleMin, width / 2f, height / 2f)
        } else {
            endMatrix.postScale(SCALE_MAX, SCALE_MAX, width / 2f, height / 2f)
        }
        endMatrix.postRotate(rotateAngle, width / 2f, height / 2f)

        //修正位置 将图片移动到中心点
        resetDrawableRectF()
        endMatrix.mapRect(drawableRectF)
        endMatrix.postTranslate(width / 2f - (drawableRectF.left + drawableRectF.width() / 2), height / 2f - (drawableRectF.top + drawableRectF.height() / 2f))

        MatrixAnimator.instance.getAnimator(imageMatrix, endMatrix, onUpdate = {
            setImageMatrixValues(it)
        }).start()
    }

    /**
     * 缩放图片，这个方法用在按钮操作放大缩小图片时使用
     *
     * 缩放比例固定[SCALE_VALUE]
     * @param flag true:放大图片  false:缩小图片
     */
    fun scaleImage(flag: Boolean) {
        val scale = if (flag) {
            1 + SCALE_VALUE
        } else {
            1 - SCALE_VALUE
        }
        val tmpMatrix = Matrix(imageMatrix)
        tmpMatrix.postScale(scale, scale, width / 2f, height / 2f)
        tmpMatrix.getValues(matrixValues)
        val s = abs(getScale(matrixValues))
        scaleMin = getMinScaling()

        if (s >= SCALE_MAX) {
            val endMatrix = Matrix()
            endMatrix.setValues(matrixValues)
            endMatrix.postScale(SCALE_MAX / s, SCALE_MAX / s, width / 2f, height / 2f)
            imageMatrix.getAnimator(tmpMatrix, onUpdate = {
                setImageMatrixValues(it)
            }, onEnd = {
                tmpMatrix.getAnimator(endMatrix, onUpdate = {
                    setImageMatrixValues(it)
                }).start()
            }).start()
        } else if (s <= scaleMin) {
            val endMatrix = Matrix()
            endMatrix.setValues(matrixValues)
            endMatrix.postScale(scaleMin / s, scaleMin / s, width / 2f, height / 2f)
            imageMatrix.getAnimator(tmpMatrix, onUpdate = {
                setImageMatrixValues(it)
            }, onEnd = {
                tmpMatrix.getAnimator(endMatrix, onUpdate = {
                    setImageMatrixValues(it)
                }).start()
            }).start()
        } else {
            mMatrix.set(imageMatrix)
            mMatrix.postScale(scale, scale, width / 2f, height / 2f)
            imageMatrix.getAnimator(mMatrix, onUpdate = {
                setImageMatrixValues(it)
            }).start()
        }
    }

    private fun setImageMatrixValues(value: FloatArray) {
        mMatrix.setValues(value)
        updateImage()
    }

    /**
     * 获取当前的缩放比例
     */
    private fun getScale(matrixValue: FloatArray): Float {
        if (matrixValue.size != 9) return 1f
        return if (matrixValue[0] == 0f) matrixValue[1] else matrixValue[0]
    }

    /**
     * 获取最小缩放比例
     */
    private fun getMinScaling(): Float {
        if (height > bitmapHeight && width > bitmapWidth) return 1f
        return if (rotateAngle.toInt() % 180 == 0) { //水平放置
            min(height.toFloat() / bitmapHeight, width.toFloat() / bitmapWidth)
        } else {
            min(width.toFloat() / bitmapHeight, height.toFloat() / bitmapWidth)
        }
    }

    /**
     * 旋转图片 每次旋转图片，都会将图片缩放值适合屏幕大小。丢弃之前的缩放比例
     */
    fun rotateImage(angle: Float = 90f) {
        if (angle == 0f) return
        rotateAngle = (rotateAngle + angle.toInt()) % 360
        scaleMin = getMinScaling()
        mMatrix.reset()
        mMatrix.postRotate(rotateAngle, width / 2f, height / 2f)
        mMatrix.postScale(scaleMin, scaleMin, width / 2f, height / 2f)
        resetDrawableRectF()
        mMatrix.mapRect(drawableRectF)
        mMatrix.postTranslate(width / 2f - (drawableRectF.left + drawableRectF.width() / 2), height / 2f - (drawableRectF.top + drawableRectF.height() / 2f))
        MatrixAnimator.instance.getAnimator(imageMatrix, mMatrix, onUpdate = {
            setImageMatrixValues(it)
        }).start()
    }

    private fun updateImage() {
        imageMatrix = mMatrix
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> { //第一根手指按下的时候 判断当前图片都否在view内，如果是则当前为正常状态。否则默认为滑动状态
                resetDrawableRectF()
                imageMatrix.mapRect(drawableRectF)
                drawableRectF.inset(5f, 5f)//这里+5 是为了消除误差，原本所有的位置计算都基于float，有可能存在一定的误差。
                touchMode = if (viewRect.contains(drawableRectF)) {
                    TOUCH_MODE_NORMAL
                } else {
                    TOUCH_MODE_MOVE
                }
            }

            MotionEvent.ACTION_POINTER_DOWN -> {//当前有多跟手指按下时 状态为缩放状态
                touchMode = TOUCH_MODE_SCALE
            }

            MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_UP -> { //手指抬起后 根据当前按下的手指数量判断当前状态
                touchMode = if (event.pointerCount > 1) {
                    TOUCH_MODE_SCALE
                } else {
                    TOUCH_MODE_NORMAL
                }
            }
        }
        gestureDetector.onTouchEvent(event)
        if (event.pointerCount > 1) {
            scaleGestureDetector.onTouchEvent(event)
        }
        return true
    }

    private fun resetDrawableRectF() {
        drawableRectF.run { set(0f, 0f, bitmapWidth, bitmapHeight) }
    }

    /**
     * 判断是否能进行水平滚动，在嵌套ViewPager时判断是否可以水平滑动，返回true时，不可用滑动;false 可滑动
     */
    override fun canScrollHorizontally(direction: Int): Boolean {
        if (touchMode == TOUCH_MODE_SCALE) {
            return true
        }
        resetDrawableRectF()
        imageMatrix.mapRect(drawableRectF)
        LogUtils.i(TAG, "canScrollHorizontally drawableRectF:$drawableRectF viewRect:$viewRect")
        if (direction > 0) {
            return drawableRectF.right - viewRect.right > 1f
        } else if (direction < 0) {
            return drawableRectF.left - viewRect.left < -1f
        }

        return false
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return super.canScrollVertically(direction)
    }

    fun release() {
        LogUtils.i(TAG, "release drawable:$drawable")
        isEnabled = false
        (drawable as? AnimatedImageDrawable)?.stop()
        (drawable as? BitmapDrawable)?.bitmap?.recycle()
    }

    override fun toString(): String {
        return "RotateScaleImageView(imageFilePath=$imageFilePath)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RotateScaleImageView

        return imageFilePath == other.imageFilePath
    }

    override fun hashCode(): Int {
        return (imageFilePath?.hashCode() ?: 0)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        (drawable as? AnimatedImageDrawable)?.stop()
        (drawable as? BitmapDrawable)?.bitmap?.recycle()
        drawable = null
    }

}