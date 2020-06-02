package com.zougy.zio;

import android.util.Log;

public class JniTest {

    static {
        System.loadLibrary("test");
    }

    private JniTest() {
    }

    private static class JniTestHolder {
        private static JniTest instance = new JniTest();
    }

    public static JniTest getInstance() {
        return JniTestHolder.instance;
    }

    public native void test();

    public void jniTest(String test) {
        Log.d("JniTest", "ZLog jniTest:" + test);
    }
}
