//
// Created by gooddamn on 10/3/26.
//

#include "APIImage.h"

APIImage* APIImage::setSource(
    int fileDescriptor
) {
    mFileDescriptor = fileDescriptor;
    return this;
}

APIImage* APIImage::load() {
    AImageDecoder* decoder;
    
    int result = AImageDecoder_createFromFd(
        mFileDescriptor,
        &decoder
    );
    
    if (result != ANDROID_IMAGE_DECODER_SUCCESS) {
        return nullptr;
    }
    
    const AImageDecoderHeaderInfo* infoHeader = AImageDecoder_getHeaderInfo(
        decoder
    );
    
    int32_t width = AImageDecoderHeaderInfo_getWidth(infoHeader);
    int32_t height = AImageDecoderHeaderInfo_getHeight(infoHeader);
    
    auto format = (AndroidBitmapFormat)
        AImageDecoderHeaderInfo_getAndroidBitmapFormat(
            infoHeader
        );
    
    size_t stride = AImageDecoder_getMinimumStride(
        decoder
    );
    
    size_t size = height * stride;
    void* pixels = malloc(size);
    
    result = AImageDecoder_decodeImage(
        decoder,
        pixels,
        stride,
        size
    );
    
    AImageDecoder_delete(
        decoder
    );
    
    if (result != ANDROID_IMAGE_DECODER_SUCCESS) {
        free(pixels);
        return nullptr;
    }
    
    mWidth = width;
    mHeight = height;
    mPixels = pixels;
    
    return this;
}

int APIImage::getWidth() {
    return mWidth;
}

int APIImage::getHeight() {
    return mHeight;
}

void* APIImage::getPixels() {
    return mPixels;
}
