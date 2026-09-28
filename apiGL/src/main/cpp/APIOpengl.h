//
// Created by gooddamn on 9/26/26.
//

#ifndef ENGINEOPENGLES_APIOPENGL_H
#define ENGINEOPENGLES_APIOPENGL_H

#include "GLES3/gl3.h"
#include <string>
#include "APIVertexAttribute.h"
#include "APIBinderAttribute.h"

class APIOpengl {
public:

    static GLuint createShader(
        GLenum type,
        const char* src
    );
    
    GLuint createProgram(
        const char* srcVertex,
        const char* srcFragment
    );
    
    void drawMesh(
        GLuint vertexArray,
        GLenum mode,
        GLenum typeIndices,
        GLsizei indicesCount
    );
    
    void setModelMatrix(
        GLint uniformLocation,
        float *model
    );
    
    GLint getUniformLocation(
        GLuint program,
        const char* name
    );
    
    void useProgram(
        GLuint program
    );
    
    APIVertexAttribute* createVertexAttributes(
        int* attrs,
        char size
    );
    
    APIBinderAttribute* createBinderAttributes(
        int* attrs,
        char size
    );
    

    GLuint createVertexArray(
        float* vertices,
        unsigned int verticesSize,
        int* indices,
        unsigned int indicesCount,
        APIVertexAttribute* vertexAttribute
    );
};


#endif //ENGINEOPENGLES_APIOPENGL_H
