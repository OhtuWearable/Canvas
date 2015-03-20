#include <string.h>
#include <jni.h>
#include "duktape.h"

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    return JNI_VERSION_1_6;
}


void myFatal (duk_context *ctx, duk_errcode_t code, const char *msg) {
    printf("Error code: %dn", code);
    printf("Error message: %sn", msg);
    exit(-1);
}

JNIEXPORT void JNICALL Java_com_ohtu_wearable_javascriptapp_DuktapeWrapper_runScript
(JNIEnv *env, jobject thisObj, jstring canvas, jstring script) {

    duk_context *ctx = duk_create_heap(NULL, NULL, NULL, NULL, &myFatal);
    if (!ctx) {
        printf("Failed to create a Duktape heap.n");
        return;
    }

    duk_destroy_heap(ctx);

}