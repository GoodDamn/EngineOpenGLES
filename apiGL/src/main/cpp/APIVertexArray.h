//
// Created by gooddamn on 10/10/26.
//

#ifndef ENGINEOPENGLES_APIVERTEXARRAY_H
#define ENGINEOPENGLES_APIVERTEXARRAY_H
#include "GLES3/gl3.h"

struct APIVertexArray {
    GLuint descriptor;
    GLenum typeIndices;
    GLsizei indicesCount;
};

#endif //ENGINEOPENGLES_APIVERTEXARRAY_H
