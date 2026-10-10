//
// Created by gooddamn on 10/10/26.
//

#ifndef ENGINEOPENGLES_APITEXTURE_H
#define ENGINEOPENGLES_APITEXTURE_H
#include "GLES3/gl3.h"

struct APITexture {
    GLuint descriptor = 0;
    GLuint activeSlot = GL_TEXTURE0;
};

#endif //ENGINEOPENGLES_APITEXTURE_H
