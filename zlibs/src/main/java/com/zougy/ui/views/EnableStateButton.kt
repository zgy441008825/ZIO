package com.zougy.ui.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import com.zougy.log.LogUtils
import com.zougy.tools.ThreadUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @Description:
 *
 * @Author: GaoYuanZou
 *
 * @Email: zougaoy@cqyazaki.com.cn
 *
 * @CreateTime: 2024/10/16 09:07
 */
class EnableStateButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatButton(context, attrs) {

    companion object {
        private const val TAG = "EnableStateButton"

        interface IOnEnableClickCallback {

            fun checkEnable(view: View): Boolean {
                return false
            }

            fun onSetEnable() {

            }
        }
    }

    /**
     * 自动超时时间
     */
    var enableTimeOut = 5000L

    private var checkDisposable: Disposable? = null

    private var listener: View.OnClickListener? = null

    var onEnableClickCallback: IOnEnableClickCallback? = null

    private val onClick = OnClickListener { _ ->
        Log.i(TAG, "enable view onclick listener:$listener")
        listener?.also {
            it.onClick(this)
            isEnabled = false
            startCheckEnable()
        }
    }

    init {
        setOnClickListener(onClick)
    }


    fun setOnClickListener(listener: View.OnClickListener, callback: IOnEnableClickCallback) {
        this.listener = listener
        onEnableClickCallback = callback
    }

    private fun startCheckEnable() {
        stopCheck()
        checkDisposable = Flowable.interval(100, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .takeWhile {
                if (onEnableClickCallback != null) {
                    onEnableClickCallback!!.checkEnable(this) && it * 100 < enableTimeOut
                } else {
                    it * 100 < enableTimeOut
                }
            }
            .subscribe({

            }, {
                stopCheck()
            }, {
                stopCheck()
            })
    }

    private fun stopCheck() {
        checkDisposable?.also {
            it.dispose()
            ThreadUtils.runMainThread {
                isEnabled = true
                onEnableClickCallback?.onSetEnable()
            }
        }
        checkDisposable = null
    }


}