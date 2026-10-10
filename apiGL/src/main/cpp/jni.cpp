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
    jstring src_fragment,
    jlong binderAttribute
) {
    const char* srcVertex = env->GetStringUTFChars(
        src_vertex,
        nullptr
    );
    
    const char* srcFragment = env->GetStringUTFChars(
        src_fragment,
        nullptr
    );
    
    GLuint program = extractApi(api)->createProgram(
        srcVertex,
        srcFragment
    );
    
    extractFromDescriptor<APIBinderAttribute>(
        binderAttribute
    )->bindAttributes(
        program
    );
    
    env->ReleaseStringUTFChars(
        src_vertex,
        srcVertex
    );
    
    env->ReleaseStringUTFChars(
        src_fragment,
        srcFragment
    );
    
    return program;
}
extern "C"
JNIEXPORT void JNICALL
Java_good_damn_apigl_GLApi_drawMesh(
    JNIEnv *env,
    jobject thiz,
    jlong api,
    jlong vertex_array,
    jint mode
) {
    auto* vertexArray = extractFromDescriptor<APIVertexArray>(
        vertex_array
    );
    
    if (vertexArray == nullptr) {
        return;
    }
    
    extractApi(api)->drawMesh(
        vertexArray,
        mode
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
JNIEXPORT jlong JNICALL
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
    
    APIVertexArray* vertexArray = extractApi(
        api
    )->createVertexArray(
        dataVertices,
        lengthVertices,
        dataIndices,
        lengthIndices,
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
    
    if (vertexArray == nullptr) {
        return -1;
    }
    
    return descriptor(
        vertexArray
    );
}

extern "C"
JNIEXPORT jint JNICALL
Java_good_damn_apigl_GLApi_getUniformLocation(
    JNIEnv *env,
    jobject thiz,
    jlong api,
    jint program,
    jstring name
) {
    const char* data = env->GetStringUTFChars(
        name,
        nullptr
    );
    
    GLint uniformLocation = extractApi(api)->getUniformLocation(
        program,
        data
    );
    
    env->ReleaseStringUTFChars(
        name,
        data
    );
    
    return uniformLocation;
}
extern "C"
JNIEXPORT void JNICALL
Java_good_damn_apigl_GLApi_useProgram(
    JNIEnv *env,
    jobject thiz,
    jlong api,
    jint program
) {
    extractApi(api)->useProgram(
        program
    );
}

extern "C"
JNIEXPORT jlong JNICALL
Java_good_damn_apigl_GLApi_createBinderAttribute(
    JNIEnv *env,
    jobject thiz,
    jlong api,
    jintArray attrs
) {
    jint* data = env->GetIntArrayElements(
        attrs,
        nullptr
    );
    
    APIBinderAttribute* attributes = extractApi(api)->createBinderAttributes(
        data,
        env->GetArrayLength(
            attrs
        )
    );
    
    env->ReleaseIntArrayElements(
        attrs,
        data,
        JNI_ABORT
    );
    
    return descriptor(
        attributes
    );
}

extern "C"
JNIEXPORT void JNICALL
Java_good_damn_apigl_GLApi_releaseBinderAttribute(
    JNIEnv *env,
    jobject thiz,
    jlong reference
) {
    delete extractFromDescriptor<APIBinderAttribute>(
        reference
    );
}

extern "C"
JNIEXPORT void JNICALL
Java_good_damn_apigl_GLApi_releaseVertexAttribute(
    JNIEnv *env,
    jobject thiz,
    jlong reference
) {
    delete extractFromDescriptor<APIVertexAttribute>(
        reference
    );
}
extern "C"
JNIEXPORT jlong JNICALL
Java_good_damn_apigl_GLApi_createTexture(
    JNIEnv *env,
    jobject thiz,
    jlong api,
    jint file_descriptor
) {
    APITexture* texture = extractApi(api)->createTexture(
        file_descriptor
    );
    
    if (texture == nullptr) {
        return -1;
    }
    
    return descriptor(
        texture
    );
}
extern "C"
JNIEXPORT void JNICALL
Java_good_damn_apigl_GLApi_setCullface(
    JNIEnv *env,
    jobject thiz,
    jlong api
) {
    extractApi(api)->setCullface();
}
extern "C"
JNIEXPORT void JNICALL
Java_good_damn_apigl_GLApi_putTexture(
    JNIEnv *env,
    jobject thiz,
    jlong api,
    jlong texture,
    jint uniform_texture
) {
    auto* t = extractFromDescriptor<APITexture>(
        texture
    );
    
    if (t == nullptr) {
        return;
    }
    
    extractApi(
        api
    )->putTexture(
        uniform_texture,
        t
    );
}