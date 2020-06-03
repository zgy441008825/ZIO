//
// Created by AFCar-zougy on 06/02 002.
//

#include <jni.h>
#include <dlfcn.h>
#include <string.h>
#include <malloc.h>
#include <android/log.h>
#include <pthread.h>

#include <android/log.h>

JavaVM *mJavaVM;
JNIEnv *jniEnv;
jobject mJobj;

static jmethodID mIdSubProgramJniTest;

void D_Log(const char *fmt, ...) {
    va_list ap;
    char buf[4096];
    va_start(ap, fmt);
    vsnprintf(buf, sizeof(buf) - 2, fmt, ap);
    va_end(ap);
    __android_log_print(ANDROID_LOG_DEBUG, "JNI Log", "%s", buf);
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    mJavaVM = vm;
    D_Log("JNI_OnLoad called");
    if ((*mJavaVM)->GetEnv(mJavaVM, (void **) &jniEnv, JNI_VERSION_1_4) != JNI_OK) {
        D_Log("Failed to get the environment using GetEnv()");
        return -1;
    }
    D_Log("JNI OnLoad OK:%p", jniEnv);
    return JNI_VERSION_1_4;
}

void test(const char *abc) {
    JNIEnv *env;
    (*mJavaVM)->GetEnv(mJavaVM, (void **) &env, JNI_VERSION_1_4);
//    jclass testClass = (*env)->FindClass(env, "java/lang/String");
//    D_Log("test:%s", testClass);
    D_Log("test mJavaVM:%p  env:%p", mJavaVM, env);
    jstring text = (*env)->NewStringUTF(env, abc);
//    D_Log("sendMsg2App text:%s", text);
    (*env)->CallVoidMethod(env, mJobj, mIdSubProgramJniTest, text);
    (*env)->DeleteLocalRef(env, text);
}

JNIEXPORT void JNICALL
Java_com_zougy_zio_JniTest_test(JNIEnv *env, jobject thiz) {
    mJobj = (*env)->NewGlobalRef(env, thiz);
    jclass jClass = (*env)->GetObjectClass(env, thiz);

    mIdSubProgramJniTest = (*env)->GetMethodID(env, jClass, "jniTest", "(Ljava/lang/String;)V");
    test("9999999999");
}