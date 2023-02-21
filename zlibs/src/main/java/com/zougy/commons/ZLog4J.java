package com.zougy.commons;

import android.text.TextUtils;

/**
 * 自定义Log，提供给java代码调用。<br>
 * 通过设置{@link ZLog4J#logger}注入自定义Log方法
 */
public final class ZLog4J {

    private static final String TAG = "ZLog";

    public static boolean isDebug = true;

    private static ILog logger = new ZLogger();

    private static String getMethodInfo() {
        try {
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                if (element.isNativeMethod()) {
                    continue;
                }
                if (TextUtils.equals(element.getClassName(), Thread.currentThread().getName())) {
                    continue;
                }
                if (TextUtils.equals(element.getClassName(), ZLog4J.class.getName())) {
                    continue;
                }
                return "[" + element.getClassName() + " :" + element.getMethodName() + " :" + element.getLineNumber() + "]:";
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public static void setLogger(ILog logger) {
        ZLog4J.logger = logger;
    }

    public static void d(String msg) {
        d(TAG, getMethodInfo() + msg);
    }

    public static void d(String msg, boolean showMethodInfo) {
        if (showMethodInfo) {
            d(TAG, getMethodInfo() + msg);
        } else {
            d(TAG, msg);
        }
    }

    public static void d(String tag, String msg, boolean showMethodInfo) {
        if (isDebug) {
            if (showMethodInfo) {
                d(tag, getMethodInfo() + msg);
            } else {
                d(tag, msg);
            }
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            logger.d(tag, msg);
        }
    }

    public static void i(String msg) {
        i(TAG, getMethodInfo() + msg);
    }

    public static void i(String msg, boolean showMethodInfo) {
        if (showMethodInfo) {
            i(TAG, getMethodInfo() + msg);
        } else {
            i(TAG, msg);
        }
    }

    public static void i(String tag, String msg, boolean showMethodInfo) {
        if (isDebug) {
            if (showMethodInfo) {
                i(tag, getMethodInfo() + msg);
            } else {
                i(tag, msg);
            }
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            logger.i(tag, msg);
        }
    }

    public static void w(String msg) {
        w(TAG, getMethodInfo() + msg, null);
    }

    public static void w(String msg, Throwable throwable) {
        w(TAG, getMethodInfo() + msg, throwable);
    }

    public static void w(String msg, Throwable throwable, boolean showMethodInfo) {
        if (showMethodInfo) {
            w(TAG, getMethodInfo() + msg, throwable);
        } else {
            w(TAG, msg, throwable);
        }
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if (isDebug) {
            logger.w(tag, msg, throwable);
        }
    }

    public static void e(String msg) {
        e(TAG, getMethodInfo() + msg, null);
    }

    public static void e(String msg, Throwable throwable) {
        e(TAG, getMethodInfo() + msg, throwable);
    }

    public static void e(String msg, Throwable throwable, boolean showMethodInfo) {
        if (showMethodInfo) {
            e(TAG, getMethodInfo() + msg, throwable);
        } else {
            e(TAG, msg, throwable);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (isDebug) {
            logger.e(tag, msg, throwable);
        }
    }

}
