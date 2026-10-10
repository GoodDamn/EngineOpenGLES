//
// Created by gooddamn on 9/27/26.
//

#ifndef ENGINEOPENGLES_APIUTILS_H
#define ENGINEOPENGLES_APIUTILS_H

#include "android/log.h"
#define LOG_TAG "APIUtils"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

inline void copyShort(
    int* a,
    unsigned short *b,
    unsigned int amount
) {
    for (int i = 0; i < amount; i++) {
        b[i] = a[i];
    }
}

inline void copyByte(
    int* a,
    unsigned char *b,
    unsigned int amount
) {
    for (int i = 0; i < amount; i++) {
        b[i] = a[i];
    }
}

#endif //ENGINEOPENGLES_APIUTILS_H
