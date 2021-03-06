#include <string.h>
#include <jni.h>
#include <android/log.h>
#include "duktape.h"

duk_ret_t jni_fill_rect(duk_context *ctx){

    //get parameters from Duktape heap
    const char *fillstyle = duk_require_string(ctx, 0);
    const char *x = duk_require_string(ctx, 1);
    const char *y = duk_require_string(ctx, 2);
    const char *width = duk_require_string(ctx, 3);
    const char *height = duk_require_string(ctx, 4);

    //get pointers to JNIEnv and JNIObj from DukTape heap
    (void) duk_get_global_string(ctx, "JNIEnv");
    JNIEnv *env = (JNIEnv *)duk_require_pointer(ctx, -1);
    (void) duk_get_global_string(ctx, "JNIObj");
    jobject *obj = (jobject *)duk_require_pointer(ctx, -1);

    jclass duktape_wrapper_jclass = (*env)->FindClass(env, "com/ohtu/wearable/canvas/DuktapeWrapper");

    //parameters (string fillstyle, string x, string y, string width, string height) and return parameter (string)
    const char *signature = "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;";
    //get DuktapeWrapper.fillRect methodID
    jmethodID perform_fillRect_jmethodID = (*env)->GetStaticMethodID(env, duktape_wrapper_jclass, "fillRect", signature);

    //jstrings from parameter strings
    jstring jfillstyle = (*env)->NewStringUTF(env, fillstyle);
    jstring jx = (*env)->NewStringUTF(env, x);
    jstring jy = (*env)->NewStringUTF(env, y);
    jstring jwidth = (*env)->NewStringUTF(env, width);
    jstring jheight = (*env)->NewStringUTF(env, height);

    //call DuktapeWrapper.fillRect method with parameters and save response to response_jstring
    jstring response_jstring = (jstring) (*env)->CallStaticObjectMethod(env, duktape_wrapper_jclass, perform_fillRect_jmethodID, jfillstyle, jx, jy, jwidth, jheight);

    //delete local references to parameter + response strings
    (*env)->DeleteLocalRef(env, jfillstyle);
    (*env)->DeleteLocalRef(env, jx);
    (*env)->DeleteLocalRef(env, jy);
    (*env)->DeleteLocalRef(env, jwidth);
    (*env)->DeleteLocalRef(env, jheight);
    (*env)->DeleteLocalRef(env, response_jstring);

    duk_pop(ctx);

    return 1;
}

duk_ret_t jni_clear_rect(duk_context *ctx){

    const char *x = duk_require_string(ctx, 0);
    const char *y = duk_require_string(ctx, 1);
    const char *width = duk_require_string(ctx, 2);
    const char *height = duk_require_string(ctx, 3);

    (void) duk_get_global_string(ctx, "JNIEnv");
    JNIEnv *env = (JNIEnv *)duk_require_pointer(ctx, -1);
    (void) duk_get_global_string(ctx, "JNIObj");
    jobject *obj = (jobject *)duk_require_pointer(ctx, -1);

    jclass duktape_wrapper_jclass = (*env)->FindClass(env, "com/ohtu/wearable/canvas/DuktapeWrapper");

    const char *signature = "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;";
    jmethodID perform_clearRect_jmethodID = (*env)->GetStaticMethodID(env, duktape_wrapper_jclass, "clearRect", signature);

    jstring jx = (*env)->NewStringUTF(env, x);
    jstring jy = (*env)->NewStringUTF(env, y);
    jstring jwidth = (*env)->NewStringUTF(env, width);
    jstring jheight = (*env)->NewStringUTF(env, height);

    jstring response_jstring = (jstring) (*env)->CallStaticObjectMethod(env, duktape_wrapper_jclass, perform_clearRect_jmethodID, jx, jy, jwidth, jheight);

    (*env)->DeleteLocalRef(env, jx);
    (*env)->DeleteLocalRef(env, jy);
    (*env)->DeleteLocalRef(env, jwidth);
    (*env)->DeleteLocalRef(env, jheight);
    (*env)->DeleteLocalRef(env, response_jstring);
    (*env)->DeleteLocalRef(env, duktape_wrapper_jclass);

    duk_pop(ctx);

    return 1;
}

duk_ret_t jni_line_to(duk_context *ctx){

    const char *x = duk_require_string(ctx, 0);
    const char *y = duk_require_string(ctx, 1);

    (void) duk_get_global_string(ctx, "JNIEnv");
    JNIEnv *env = (JNIEnv *)duk_require_pointer(ctx, -1);

    (void) duk_get_global_string(ctx, "JNIObj");
    jobject *obj = (jobject *)duk_require_pointer(ctx, -1);

    jclass duktape_wrapper_jclass = (*env)->FindClass(env, "com/ohtu/wearable/canvas/DuktapeWrapper");

    const char *signature = "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;";

    jmethodID perform_lineTo_jmethodID = (*env)->GetStaticMethodID(env, duktape_wrapper_jclass, "lineTo", signature);

    jstring jx = (*env)->NewStringUTF(env, x);
    jstring jy = (*env)->NewStringUTF(env, y);

    jstring response_jstring = (jstring) (*env)->CallStaticObjectMethod(env, duktape_wrapper_jclass, perform_lineTo_jmethodID, jx, jy);

    (*env)->DeleteLocalRef(env, jx);
    (*env)->DeleteLocalRef(env, jy);
    (*env)->DeleteLocalRef(env, response_jstring);
    (*env)->DeleteLocalRef(env, duktape_wrapper_jclass);

    duk_pop(ctx);

    return 1;
}

duk_ret_t jni_move_to(duk_context *ctx){

    const char *x = duk_require_string(ctx, 0);
    const char *y = duk_require_string(ctx, 1);

    (void) duk_get_global_string(ctx, "JNIEnv");
    JNIEnv *env = (JNIEnv *)duk_require_pointer(ctx, -1);

    (void) duk_get_global_string(ctx, "JNIObj");
    jobject *obj = (jobject *)duk_require_pointer(ctx, -1);

    jclass duktape_wrapper_jclass = (*env)->FindClass(env, "com/ohtu/wearable/canvas/DuktapeWrapper");

    const char *signature = "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;";

    jmethodID perform_moveTo_jmethodID = (*env)->GetStaticMethodID(env, duktape_wrapper_jclass, "moveTo", signature);

    jstring jx = (*env)->NewStringUTF(env, x);
    jstring jy = (*env)->NewStringUTF(env, y);

    jstring response_jstring = (jstring) (*env)->CallStaticObjectMethod(env, duktape_wrapper_jclass, perform_moveTo_jmethodID, jx, jy);

    (*env)->DeleteLocalRef(env, jx);
    (*env)->DeleteLocalRef(env, jy);
    (*env)->DeleteLocalRef(env, response_jstring);
    (*env)->DeleteLocalRef(env, duktape_wrapper_jclass);

    duk_pop(ctx);

    return 1;
}

duk_ret_t jni_begin_path(duk_context *ctx){


    (void) duk_get_global_string(ctx, "JNIEnv");
    JNIEnv *env = (JNIEnv *)duk_require_pointer(ctx, -1);

    (void) duk_get_global_string(ctx, "JNIObj");
    jobject *obj = (jobject *)duk_require_pointer(ctx, -1);

    jclass duktape_wrapper_jclass = (*env)->FindClass(env, "com/ohtu/wearable/canvas/DuktapeWrapper");

    const char *signature = "()Ljava/lang/String;";

    jmethodID perform_beginPath_jmethodID = (*env)->GetStaticMethodID(env, duktape_wrapper_jclass, "beginPath", signature);

    jstring response_jstring = (jstring) (*env)->CallStaticObjectMethod(env, duktape_wrapper_jclass, perform_beginPath_jmethodID);

    (*env)->DeleteLocalRef(env, response_jstring);
    (*env)->DeleteLocalRef(env, duktape_wrapper_jclass);

    duk_pop(ctx);

    return 1;
}

duk_ret_t jni_stroke(duk_context *ctx){

    const char *strokeStyle = duk_require_string(ctx, 0);

    (void) duk_get_global_string(ctx, "JNIEnv");
    JNIEnv *env = (JNIEnv *)duk_require_pointer(ctx, -1);

    (void) duk_get_global_string(ctx, "JNIObj");
    jobject *obj = (jobject *)duk_require_pointer(ctx, -1);

    jclass duktape_wrapper_jclass = (*env)->FindClass(env, "com/ohtu/wearable/canvas/DuktapeWrapper");

    const char *signature = "(Ljava/lang/String;)Ljava/lang/String;";

    jmethodID perform_stroke_jmethodID = (*env)->GetStaticMethodID(env, duktape_wrapper_jclass, "stroke", signature);

    jstring strokestyle = (*env)->NewStringUTF(env, strokeStyle);

    jstring response_jstring = (jstring) (*env)->CallStaticObjectMethod(env, duktape_wrapper_jclass, perform_stroke_jmethodID, strokestyle);
    (*env)->DeleteLocalRef(env, response_jstring);
    (*env)->DeleteLocalRef(env, strokestyle);
    (*env)->DeleteLocalRef(env, duktape_wrapper_jclass);

    duk_pop(ctx);

    return 1;
}

/*
duk_ret_t jni_get_width(duk_context *ctx){
     (void) duk_get_global_string(ctx, "JNIEnv");
     JNIEnv *env = (JNIEnv *)duk_require_pointer(ctx, -1);

     (void) duk_get_global_string(ctx, "JNIObj");
     jobject *obj = (jobject *)duk_require_pointer(ctx, -1);

     jclass duktape_wrapper_jclass = (*env)->GetObjectClass(env, obj);

     const char *signature = "()I";

     jmethodID perform_getWidth_jmethodID = (*env)->GetStaticMethodID(env, duktape_wrapper_jclass, "getWidth", signature);

     int response = (*env)->CallStaticIntMethod(env, duktape_wrapper_jclass, perform_getWidth_jmethodID);
     __android_log_write(ANDROID_LOG_DEBUG, "GETWIDTH: ", "called");
     duk_push_int(ctx, (duk_int_t) response);

     duk_pop(ctx);

     return 1;
}

duk_ret_t jni_get_height(duk_context *ctx){
     (void) duk_get_global_string(ctx, "JNIEnv");
     JNIEnv *env = (JNIEnv *)duk_require_pointer(ctx, -1);

     (void) duk_get_global_string(ctx, "JNIObj");
     jobject *obj = (jobject *)duk_require_pointer(ctx, -1);

     jclass duktape_wrapper_jclass = (*env)->GetObjectClass(env, obj);

     const char *signature = "()I";

     jmethodID perform_getHeight_jmethodID = (*env)->GetStaticMethodID(env, duktape_wrapper_jclass, "getHeight", signature);

     int response = (*env)->CallStaticIntMethod(env, duktape_wrapper_jclass, perform_getHeight_jmethodID);
     __android_log_write(ANDROID_LOG_DEBUG, "getHeight ", "called");
     duk_push_int(ctx, (duk_int_t) response);

     duk_pop(ctx);

     return 1;
}*/