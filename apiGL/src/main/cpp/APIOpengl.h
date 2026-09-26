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
        std::string& src
    );
    
    GLuint createProgram(
        std::string& srcVertex,
        std::string& srcFragment
    );
    
    

};


#endif //ENGINEOPENGLES_APIOPENGL_H
