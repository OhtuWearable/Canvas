#include <string.h>
#include <jni.h>
#include <android/log.h>
#include "duktape.h"
#include "canvas.h"

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    return JNI_VERSION_1_6;
}

void myFatal (duk_context *ctx, duk_errcode_t code, const char *msg) {
    printf("Error code: %dn", code);
    printf("Error message: %sn", msg);
    exit(-1);
}

JNIEXPORT void JNICALL Java_com_ohtu_wearable_canvas_DuktapeWrapper_runScript
(JNIEnv *env, jobject thisObj, jstring canvas, jstring script) {

    duk_context *ctx = duk_create_heap(NULL, NULL, NULL, NULL, &myFatal);
    if (!ctx) {
        printf("Failed to create a Duktape heap.n");
        return;
    }

    //push JNIEnv DukTape heap so it can be used on functions
   	duk_push_global_object(ctx);
   	duk_push_pointer(ctx, env);
   	duk_put_prop_string(ctx, -2, "JNIEnv");
   	duk_pop(ctx);  /* pop global */

    //push JNIObj DukTape heap so it can be used on functions
    duk_push_global_object(ctx);
    duk_push_pointer(ctx, thisObj);
    duk_put_prop_string(ctx, -2, "JNIObj");
    duk_pop(ctx); /* pop global */

    //Push reference to jni_line_to function to DukTape heap so it can be called from javascripts
    duk_push_global_object(ctx);
    duk_push_c_function(ctx, jni_line_to, 3);
    duk_put_prop_string(ctx, -2, "jni_line_to");
    duk_pop(ctx);  /* pop global */

    //Push reference to jni_draw_rect function to DukTape heap so it can be called from javascripts
   	duk_push_global_object(ctx);
   	duk_push_c_function(ctx, jni_draw_rect, 5);
   	duk_put_prop_string(ctx, -2, "jni_draw_rect");
   	duk_pop(ctx);  /* pop global */

    //get canvas script string from jstring given as parameter and evaluate(=run) it with DukTape
   	const char *canvasScript = (*env)->GetStringUTFChars(env, canvas, 0);
    __android_log_write(ANDROID_LOG_DEBUG, "JNI canvasScript", canvasScript);
    if (duk_peval_string(ctx, canvasScript) != 0) {
    	__android_log_write(ANDROID_LOG_ERROR, "JNI canvasScript", duk_safe_to_string(ctx, -1));
    } else {
        __android_log_write(ANDROID_LOG_DEBUG, "JNI canvasScript", duk_safe_to_string(ctx, -1));
    }
    duk_pop(ctx); /* pop global */

    //get script string from jstring given as parameter and evaluate(=run) it with DukTape
   	const char *nativeScript = (*env)->GetStringUTFChars(env, script, 0);
    __android_log_write(ANDROID_LOG_DEBUG, "JNI drawScript", nativeScript);
   	if (duk_peval_string(ctx, nativeScript) != 0) {
   		__android_log_write(ANDROID_LOG_ERROR, "JNI drawScript", duk_safe_to_string(ctx, -1));
   	} else {
   	    __android_log_write(ANDROID_LOG_DEBUG, "JNI drawScript", duk_safe_to_string(ctx, -1));
   	}
   	duk_pop(ctx); /* pop global */

    duk_destroy_heap(ctx);
}