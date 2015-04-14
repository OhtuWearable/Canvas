#ifndef XMLHTTPREQUEST_H
#define XMLHTTPREQUEST_H

#include <string.h>
#include <jni.h>
#include "duktape.h"

duk_ret_t native_xmlhttprequest(duk_context *ctx);
duk_ret_t native_abort(duk_context *ctx);

#endif