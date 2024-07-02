package com.zougy.tools

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Process
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @Description:
 *
 * @Author: GaoYuanZou
 *
 * @Email: zougaoy@cqyazaki.com.cn
 *
 * @CreateTime: 2024/07/02 09:02
 */
object ThreadUtils {

    /**
     * 主线程Handler
     */
    private val MAIN_THREAD_HANDLER = Handler(Looper.getMainLooper())

    /**
     * 高优先级线程
     */
    private val THREAD_HANDLER_H: Handler

    /**
     * 低优先级——后台线程
     */
    private val THREAD_HANDLER_B: Handler

    /**
     * 线程池
     */
    private val executor = Executors.newScheduledThreadPool(10)

    init {
        val handlerThread = HandlerThread("subThread", Process.THREAD_PRIORITY_MORE_FAVORABLE)
        handlerThread.start()
        THREAD_HANDLER_H = Handler(handlerThread.looper)

        val backThread = HandlerThread("backgroundThread", Process.THREAD_PRIORITY_BACKGROUND)
        backThread.start()
        THREAD_HANDLER_B = Handler(backThread.looper)
    }

    private fun runInThread(handler: Handler, runnable: Runnable, delay: Long) {
        if (delay > 0) {
            handler.postDelayed(runnable, delay)
        } else {
            handler.post(runnable)
        }
    }

    /**
     * 运行在主线程
     */
    fun runMainThread(delay: Long = 0, runnable: Runnable) {
        if (isMainThread()) {
            runnable.run()
        } else {
            runInThread(MAIN_THREAD_HANDLER, runnable, delay)
        }
    }

    /**
     * 运行在高优先级的子线程
     */
    fun runHighThread(delay: Long = 0, runnable: Runnable) {
        runInThread(THREAD_HANDLER_H, runnable, delay)
    }

    /**
     * 运行在低优先级的子线程
     */
    fun runBackgroundThread(delay: Long = 0, runnable: Runnable) {
        runInThread(THREAD_HANDLER_B, runnable, delay)
    }

    fun runInExecutor(delay: Long = 0, runnable: Runnable) {
        if (delay > 0) {
            executor.schedule(runnable, delay, TimeUnit.MILLISECONDS)
        } else {
            executor.execute(runnable)
        }
    }

    fun isMainThread(): Boolean = Looper.getMainLooper() == Looper.myLooper()

}