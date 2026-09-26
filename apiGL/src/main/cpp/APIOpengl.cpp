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
    const char* src
) {
    GLuint shader = glCreateShader(
        type
    );
    
    glShaderSource(
        shader,
        1,
        &src,
        nullptr
    );
    
    glCompileShader(
        shader
    );
    
    return shader;
}

GLuint APIOpengl::createProgram(
    const char* srcVertex,
    const char* srcFragment
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

void APIOpengl::drawMesh(
    GLuint vertexArray,
    GLenum mode,
    GLenum typeIndices,
    GLsizei indicesCount
) {
    glBindVertexArray(
        vertexArray
    );
    
    glDrawElements(
        mode,
        indicesCount,
        typeIndices,
        nullptr
    );
    
    glBindVertexArray(
        0
    );
}