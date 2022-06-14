package com.zougy.commons;

import android.util.Log;

public final class ZLog4J {

    public static String TAG = "ZLog";

    public static boolean isDebug = true;

    private static String getMethodInfo() {
        try {
            String className = Thread.currentThread().getStackTrace()[4].getClassName();
            String methodName = Thread.currentThread().getStackTrace()[4].getMethodName();
            int lineNum = Thread.currentThread().getStackTrace()[4].getLineNumber();
            return "[" + className + ":" + methodName + " " + lineNum + "]:";
        } catch (Exception e) {
            return "";
        }
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

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
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

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
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
            if (throwable != null) {
                Log.w(tag, msg, throwable);
            } else {
                Log.w(tag, msg);
            }
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
            if (throwable != null) {
                Log.e(tag, msg, throwable);
            } else {
                Log.e(tag, msg);
            }
        }
    }

}
