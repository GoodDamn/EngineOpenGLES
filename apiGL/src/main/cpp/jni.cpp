#include <jni.h>
#include "APIOpengl.h"

inline jlong descriptor(void* native_app) {
    return reinterpret_cast<intptr_t>(native_app);
}

template<typename T>
inline T* extractFromDescriptor(jlong ptr) {
    return reinterpret_cast<T*>(ptr);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_good_damn_apigl_GLApi_create(
    JNIEnv *env,
    jobject thiz
) {
    return descriptor(
        new APIOpengl()
    );
}


extern "C"
JNIEXPORT jint JNICALL
Java_good_damn_apigl_GLApi_createProgram(
    JNIEnv *env,
    jobject thiz,
    jlong api,
    jstring src_vertex,
    jstring src_fragment
) {
    const char* srcVertex = env->GetStringUTFChars(
        src_vertex,
        nullptr
    );
    
    const char* srcFragment = env->GetStringUTFChars(
        src_fragment,
        nullptr
    );
    
    return extractFromDescriptor<
        APIOpengl
    >(api)->createProgram(
        srcVertex,
        srcFragment
    );
}
extern "C"
JNIEXPORT void JNICALL
Java_good_damn_apigl_GLApi_drawMesh(
    JNIEnv *env,
    jobject thiz,
    jlong api,
    jint vertex_array,
    jint mode,
    jint type_indices,
    jint indices_count
) {
    extractFromDescriptor<
        APIOpengl
    >(api)->drawMesh(
        vertex_array,
        mode,
        type_indices,
        indices_count
    );
}