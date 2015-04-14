#include <string.h>
#include <jni.h>
#include <android/log.h>
#include "duktape.h"
#include "canvas.h"
#include "xmlhttprequest.h"

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    return JNI_VERSION_1_6;
}

void myFatal (duk_context *ctx, duk_errcode_t code, const char *msg) {
    printf("Error code: %dn", code);
    printf("Error message: %sn", msg);
    exit(-1);
}

//Run script on context
jstring Java_com_ohtu_wearable_canvas_DuktapeWrapper_runScriptOnContext
(JNIEnv *env, jobject thisObj, jlong context_pointer, jstring script) {
    //Cast long to duk_context-pointer
    duk_context *ctx = (duk_context*)context_pointer;

    char peval[255];
    const char *real_script = (*env)->GetStringUTFChars(env, script, 0);
    duk_peval_string(ctx, real_script);
    char ret[2550];
    if (duk_get_type(ctx, -1) == DUK_TYPE_NUMBER) {
        sprintf(ret, "%lf", (double)duk_get_number(ctx, -1));
    }
    if (duk_get_type(ctx, -1) == DUK_TYPE_STRING) {
        sprintf(ret, "%s", (char*) duk_get_string(ctx, -1));
    }
    return (*env)->NewStringUTF(env, ret);
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
    duk_push_c_function(ctx, jni_line_to, 2);
    duk_put_prop_string(ctx, -2, "jni_line_to");
    duk_pop(ctx);  /* pop global */

    //Push reference to jni_move_to function to DukTape heap so it can be called from javascripts
    duk_push_global_object(ctx);
    duk_push_c_function(ctx, jni_move_to, 2);
    duk_put_prop_string(ctx, -2, "jni_move_to");
    duk_pop(ctx);  /* pop global */

    //Push reference to jni_draw_rect function to DukTape heap so it can be called from javascripts
   	duk_push_global_object(ctx);
   	duk_push_c_function(ctx, jni_fill_rect, 5);
   	duk_put_prop_string(ctx, -2, "jni_fill_rect");
   	duk_pop(ctx);  /* pop global */

   	duk_push_global_object(ctx);
    duk_push_c_function(ctx, jni_clear_rect, 4);
    duk_put_prop_string(ctx, -2, "jni_clear_rect");
    duk_pop(ctx);

   	//Push reference to jni_draw_rect function to DukTape heap so it can be called from javascripts
    duk_push_global_object(ctx);
    duk_push_c_function(ctx, jni_begin_path, 0);
    duk_put_prop_string(ctx, -2, "jni_begin_path");
    duk_pop(ctx);  /* pop global */

    duk_push_global_object(ctx);
    duk_push_c_function(ctx, jni_stroke, 1);
    duk_put_prop_string(ctx, -2, "jni_stroke");
    duk_pop(ctx);  /* pop global */

        //I need access to the native_xmlhttprequest()
        duk_push_global_object(ctx);
        duk_push_c_function(ctx, native_xmlhttprequest, 8);
        duk_put_prop_string(ctx, -2, "native_xmlhttprequest");
        duk_pop(ctx); /* pop global */

        //I need access to the native_abort()
        duk_push_global_object(ctx);
        duk_push_c_function(ctx, native_abort, 1);
        duk_put_prop_string(ctx, -2, "native_abort");
        duk_pop(ctx); /* pop global */

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

    //duk_destroy_heap(ctx);
}