#include <string.h>
#include <jni.h>
#include <android/log.h>
#include "duktape.h"

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    return JNI_VERSION_1_6;
}


void myFatal (duk_context *ctx, duk_errcode_t code, const char *msg) {
    printf("Error code: %dn", code);
    printf("Error message: %sn", msg);
    exit(-1);
}

duk_ret_t native_request_send(duk_context *ctx){

    __android_log_write(ANDROID_LOG_DEBUG, "JNI", "native request send called");

    //String fillStyle, int x, int y, int width, int height
    const char *fillstyle = duk_require_string(ctx, 0);
    const char *x = duk_require_string(ctx, 1);
    __android_log_write(ANDROID_LOG_DEBUG, "JNI", "x");
    const char *y = duk_require_string(ctx, 2);
    __android_log_write(ANDROID_LOG_DEBUG, "JNI", "y");
    const char *width = duk_require_string(ctx, 3);
    __android_log_write(ANDROID_LOG_DEBUG, "JNI", "width");
    const char *height = duk_require_string(ctx, 4);



    (void) duk_get_global_string(ctx, "JNIEnv");
    JNIEnv *env = (JNIEnv *)duk_require_pointer(ctx, -1);
    //jclass duktape_wrapper_jclass = (*env)->FindClass(env, "DuktapeWrapper");

    (void) duk_get_global_string(ctx, "JNIObj");
    jobject *obj = (JNIEnv *)duk_require_pointer(ctx, -1);

    jclass duktape_wrapper_jclass = (*env)->GetObjectClass(env, obj);

    const char *signature = "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;";

    jmethodID perform_drawCanvas_jmethodID = (*env)->GetStaticMethodID(env, duktape_wrapper_jclass, "drawCanvas", signature);



    jstring jfillstyle = (*env)->NewStringUTF(env, fillstyle);

    jstring jx = (*env)->NewStringUTF(env, x);
    jstring jy = (*env)->NewStringUTF(env, y);

    jstring jwidth = (*env)->NewStringUTF(env, width);
    jstring jheight = (*env)->NewStringUTF(env, height);

    jstring response_jstring = (jstring) (*env)->CallStaticObjectMethod(env, duktape_wrapper_jclass, perform_drawCanvas_jmethodID, jfillstyle, jx, jy, jwidth, jheight);
    __android_log_write(ANDROID_LOG_DEBUG, "JNI", "kaatuuko");
    //__android_log_write(ANDROID_LOG_DEBUG, "JNI", response_jstring);
    duk_pop(ctx);

    return 1;
}

JNIEXPORT void JNICALL Java_com_ohtu_wearable_canvas_DuktapeWrapper_runScript
(JNIEnv *env, jobject thisObj, jstring canvas, jstring script) {

    duk_context *ctx = duk_create_heap(NULL, NULL, NULL, NULL, &myFatal);
    if (!ctx) {
        printf("Failed to create a Duktape heap.n");
        return;
    }

   	//I need access to the JNIEnv in my native_request_send
   	duk_push_global_object(ctx);
   	duk_push_pointer(ctx, env);
   	duk_put_prop_string(ctx, -2, "JNIEnv");
   	duk_pop(ctx);  /* pop global */

    duk_push_global_object(ctx);
    duk_push_pointer(ctx, thisObj);
    duk_put_prop_string(ctx, -2, "JNIObj");
    duk_pop(ctx); /* pop global */

   	duk_push_global_object(ctx);
   	duk_push_c_function(ctx, native_request_send, 5);
   	duk_put_prop_string(ctx, -2, "native_request_send");
   	duk_pop(ctx);  /* pop global */

   	const char *canvasScript = (*env)->GetStringUTFChars(env, canvas, 0);
    __android_log_write(ANDROID_LOG_DEBUG, "JNI canvasScript", canvasScript);

    if (duk_peval_string(ctx, canvasScript) != 0) {
    	__android_log_write(ANDROID_LOG_ERROR, "JNI canvasScript", duk_safe_to_string(ctx, -1));
    } else {
        __android_log_write(ANDROID_LOG_DEBUG, "JNI canvasScript", duk_safe_to_string(ctx, -1));
    }

    duk_pop(ctx);

   	const char *nativeScript = (*env)->GetStringUTFChars(env, script, 0);
    __android_log_write(ANDROID_LOG_DEBUG, "JNI drawScript", nativeScript);
   	if (duk_peval_string(ctx, nativeScript) != 0) {
   		__android_log_write(ANDROID_LOG_ERROR, "JNI drawScript", duk_safe_to_string(ctx, -1));
   	} else {
   	    __android_log_write(ANDROID_LOG_DEBUG, "JNI drawScript", duk_safe_to_string(ctx, -1));
   	}
   	duk_pop(ctx);

    duk_destroy_heap(ctx);

}