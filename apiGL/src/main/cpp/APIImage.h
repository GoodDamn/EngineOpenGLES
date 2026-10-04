//
// Created by gooddamn on 10/3/26.
//

#ifndef ENGINEOPENGLES_APIIMAGE_H
#define ENGINEOPENGLES_APIIMAGE_H

#include "android/imagedecoder.h"
#include "malloc.h"
#include "APIUtils.h"

class APIImage {
private:
    int mFileDescriptor;
    void* mPixels = nullptr;
    
    int mWidth;
    int mHeight;
public:
    
    int getWidth();
    int getHeight();
    
    void* getPixels();
    
    APIImage* setSource(
        int fileDescriptor
    );
    
    APIImage* load();
    
    /*~APIImage() {
        if (mPixels == nullptr) {
            return;
        }
        
        free(mPixels);
        mPixels = nullptr;
    }*/
};


#endif //ENGINEOPENGLES_APIIMAGE_H
