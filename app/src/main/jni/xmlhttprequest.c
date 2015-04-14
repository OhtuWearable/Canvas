#include "xmlhttprequest.h"

duk_ret_t native_abort(duk_context *ctx){
    const char *reqid = duk_require_string(ctx, 0);

    //Get JNIENv from stack
    (void) duk_get_global_string(ctx, "JNIEnv");
    JNIEnv *env = (JNIEnv *)duk_require_pointer(ctx, -1);

    //Get JNIObj(=instance of DuktapeWrapper) from stack
    (void) duk_get_global_string(ctx, "JNIObj");
    jobject *obj = (JNIEnv *)duk_require_pointer(ctx, -1);

    //Get DuktapeWrapper(the class)
    jclass duktape_wrapper_jclass=(*env)->GetObjectClass(env, obj);

    //This is the signature of performJavaHttpAbort()(in DuktapeWrapper)
    const char *signature ="(Ljava/lang/String;)Ljava/lang/String;";

    //Get performJavaHttpAbort()(in DuktapeWrapper)
    jmethodID perform_java_http_abort =(*env)->GetStaticMethodID(env, duktape_wrapper_jclass, "performJavaHttpAbort", signature);

    //Convert parameters
    jstring jreqid = (*env)->NewStringUTF(env, reqid);

    //Get the response
    jstring json_response_jstring = (jstring) (*env)->CallStaticObjectMethod(env,
                                                            duktape_wrapper_jclass,
                                                            perform_java_http_abort,
                                                            jreqid);

    const char *response = (*env)->GetStringUTFChars(env, json_response_jstring, 0);
    duk_pop(ctx);
    duk_idx_t obj_idx = duk_push_object(ctx);
    duk_push_string(ctx, response);
    duk_put_prop_string(ctx, obj_idx, "response");
    (*env)->ReleaseStringUTFChars(env, json_response_jstring, response);
    return 1;
}

duk_ret_t native_xmlhttprequest(duk_context *ctx){
    //Get parameters from JavaScript
    const char *reqid = duk_require_string(ctx, 0);
    const char *method = duk_require_string(ctx, 1);
    const char *url = duk_require_string(ctx, 2);
    const char *data = duk_get_string(ctx, 3);
    const char *headers = duk_get_string(ctx, 4);
    const char *username = duk_get_string(ctx, 5);
    const char *password = duk_get_string(ctx, 6);
    const int *async = duk_get_boolean(ctx, 7);

    //Get JNIENv from stack
    (void) duk_get_global_string(ctx, "JNIEnv");
    JNIEnv *env = (JNIEnv *)duk_require_pointer(ctx, -1);

    //Get JNIObj(=instance of DuktapeWrapper) from stack
    (void) duk_get_global_string(ctx, "JNIObj");
    jobject *obj = (JNIEnv *)duk_require_pointer(ctx, -1);

    //Get DuktapeWrapper(the class)
    jclass duktape_wrapper_jclass=(*env)->GetObjectClass(env, obj);

    //This is the signature of performJavaHttpRequest()(in DuktapeWrapper)
    const char *signature ="(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Ljava/lang/String;";

    //Get performJavaHttpRequest()(in DuktapeWrapper)
    jmethodID perform_java_http_request =(*env)->GetStaticMethodID(env, duktape_wrapper_jclass, "performJavaHttpRequest", signature);

    //Convert parameters
    jstring jreqid = (*env)->NewStringUTF(env, reqid);
    jstring jmethod = (*env)->NewStringUTF(env, method);
    jstring jurl = (*env)->NewStringUTF(env, url);
    jstring jdata = (*env)->NewStringUTF(env, data);
    jstring jheaders = (*env)->NewStringUTF(env, headers);
    jstring jusername = (*env)->NewStringUTF(env, username);
    jstring jpassword = (*env)->NewStringUTF(env, password);
    jboolean jasync=1;
    if(async==0){
        jasync=0;
    }

    //Get the response
    jstring json_response_jstring = (jstring) (*env)->CallStaticObjectMethod(env,
                                                            duktape_wrapper_jclass,
                                                            perform_java_http_request,
                                                            jmethod,
                                                            jurl,
                                                            jdata,
                                                            jreqid,
                                                            (jlong)ctx,
                                                            jheaders,
                                                            jusername,
                                                            jpassword,
                                                            jasync);

    const char *response = (*env)->GetStringUTFChars(env, json_response_jstring, 0);
    duk_pop(ctx);
    duk_idx_t obj_idx = duk_push_object(ctx);
    duk_push_string(ctx, response);
    duk_put_prop_string(ctx, obj_idx, "response");
    (*env)->ReleaseStringUTFChars(env, json_response_jstring, response);
    return 1;
}
