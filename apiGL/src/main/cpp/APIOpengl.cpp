//
// Created by gooddamn on 9/26/26.
//

#include "APIOpengl.h"
#include "GLES3/gl3.h"
#include <string>

inline void getCompileStatus(
    GLuint shader,
    GLint* status
) {
    glGetShaderiv(
        shader,
        GL_COMPILE_STATUS,
        status
    );
}

GLuint APIOpengl::createShader(
    GLenum type,
    std::string& src
) {
    GLuint shader = glCreateShader(
        type
    );
    
    const char* cStr = src.c_str();
    
    glShaderSource(
        shader,
        1,
        &cStr,
        nullptr
    );
    
    glCompileShader(
        shader
    );
    
    return shader;
}

GLuint APIOpengl::createProgram(
    std::string& srcVertex,
    std::string& srcFragment
) {
    GLuint shaderFragment = createShader(
        GL_FRAGMENT_SHADER,
        srcFragment
    );
    
    GLint status;
    getCompileStatus(
        shaderFragment,
        &status
    );
    
    if (status == GL_FALSE) {
        return -1;
    }
    
    GLuint shaderVertex = createShader(
        GL_VERTEX_SHADER,
        srcVertex
    );
    
    getCompileStatus(
        shaderVertex,
        &status
    );
    
    if (status == GL_FALSE) {
        return -1;
    }
    
    GLuint program = glCreateProgram();
    
    glAttachShader(
        program,
        shaderFragment
    );
    
    glAttachShader(
        program,
        shaderVertex
    );
    
    return program;
}