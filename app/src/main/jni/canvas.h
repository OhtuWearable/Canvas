#ifndef CANVAS_H_INCLUDED
#define CANVAS_H_INCLUDED

duk_ret_t jni_fill_rect(duk_context *ctx);

duk_ret_t jni_line_to(duk_context *ctx);

duk_ret_t jni_move_to(duk_context *ctx);

duk_ret_t jni_begin_path(duk_context *ctx);

duk_ret_t jni_stroke(duk_context *ctx);

#endif