#include <jni.h>
#include "COMatrix4x4.h"

template<typename T>
inline T* asPointer(jlong ptr) {
    return reinterpret_cast<T*>(ptr);
}


inline long asPointer(
    void* ptr
) {
    return reinterpret_cast<intptr_t>(ptr);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_good_damn_common_matrices_COMatrix4x4_createMatrix(
    JNIEnv *env,
    jobject thiz
) {
    return asPointer(
        new COMatrix4x4()
    );
}
extern "C"
JNIEXPORT void JNICALL
Java_good_damn_common_matrices_COMatrix4x4_releaseMatrix(
    JNIEnv *env,
    jobject thiz,
    jlong ptr
) {
    delete asPointer<COMatrix4x4>(
        ptr
    );
}
extern "C"
JNIEXPORT void JNICALL
Java_good_damn_common_matrices_COMatrix4x4_multiplyMatrix(
    JNIEnv *env,
    jobject thiz,
    jlong ptr1,
    jlong ptr2
) {
    auto* matrix1 = asPointer<COMatrix4x4>(
        ptr1
    );
    
    auto* matrix2 = asPointer<COMatrix4x4>(
        ptr2
    );
    
    COMatrix4x4 result = (*matrix1) * (*matrix2);
    matrix1->copyFrom(&result);
}