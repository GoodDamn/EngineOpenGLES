#include <jni.h>
#include "APIOpengl.h"

inline jlong descriptor(void* native_app) {
    return reinterpret_cast<intptr_t>(native_app);
}

template<typename T>
inline T* extractFromDescriptor(jlong ptr) {
    return reinterpret_cast<T*>(ptr);
}

inline APIOpengl* extractApi(jlong ptr) {
    return extractFromDescriptor<APIOpengl>(ptr);
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
    
    return extractApi(api)->createProgram(
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
    extractApi(api)->drawMesh(
        vertex_array,
        mode,
        type_indices,
        indices_count
    );
}
extern "C"
JNIEXPORT void JNICALL
Java_good_damn_apigl_GLApi_setModelMatrix(
    JNIEnv *env,
    jobject thiz,
    jlong api,
    jint uniform_location,
    jfloatArray model
) {
    jfloat* data = env->GetFloatArrayElements(
        model,
        nullptr
    );
    
    extractApi(api)->setModelMatrix(
        uniform_location,
        data
    );
    
    env->ReleaseFloatArrayElements(
        model,
        data,
        JNI_ABORT
    );
}
extern "C"
JNIEXPORT jlong JNICALL
Java_good_damn_apigl_GLApi_createVertexAttribute(
    JNIEnv *env,
    jobject thiz,
    jlong api,
    jintArray attrs
) {
    jint* data = env->GetIntArrayElements(
        attrs,
        nullptr
    );
    
    unsigned int length = env->GetArrayLength(
        attrs
    );
    
    APIVertexAttribute* attributes = extractApi(
        api
    )->createVertexAttributes(
        data,
        length
    );
    
    env->ReleaseIntArrayElements(
        attrs,
        data,
        JNI_ABORT
    );
    
    if (attributes == nullptr) {
        return -1;
    }
    
    return descriptor(
        attributes
    );
}
extern "C"
JNIEXPORT jint JNICALL
Java_good_damn_apigl_GLApi_createVertexArray(
    JNIEnv *env,
    jobject thiz,
    jlong api,
    jlong vertex_attribute,
    jfloatArray vertices,
    jintArray indices
) {
    auto* attributes = extractFromDescriptor<APIVertexAttribute>(
        vertex_attribute
    );
    
    if (attributes == nullptr) {
        return -1;
    }
    
    jfloat* dataVertices = env->GetFloatArrayElements(
        vertices,
        nullptr
    );
    
    unsigned int lengthVertices = env->GetArrayLength(
        vertices
    );
    
    jint* dataIndices = env->GetIntArrayElements(
        indices,
        nullptr
    );
    
    unsigned int lengthIndices = env->GetArrayLength(
        indices
    );
    
    GLuint vertexArray = extractApi(
        api
    )->createVertexArray(
        dataVertices,
        lengthVertices,
        dataIndices,
        lengthVertices,
        attributes
    );
    
    env->ReleaseIntArrayElements(
        indices,
        dataIndices,
        JNI_ABORT
    );
    
    env->ReleaseFloatArrayElements(
        vertices,
        dataVertices,
        JNI_ABORT
    );
    
    return vertexArray;
}