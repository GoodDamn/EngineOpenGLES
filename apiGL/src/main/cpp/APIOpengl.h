//
// Created by gooddamn on 9/26/26.
//

#ifndef ENGINEOPENGLES_APIOPENGL_H
#define ENGINEOPENGLES_APIOPENGL_H

#include "GLES3/gl3.h"
#include <string>

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

};


#endif //ENGINEOPENGLES_APIOPENGL_H
