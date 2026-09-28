//
// Created by gooddamn on 9/26/26.
//

#include "APIOpengl.h"
#include "GLES3/gl3.h"
#include "APIUtils.h"
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

inline GLuint createBuffer(
    GLenum target,
    const void* data,
    GLsizeiptr dataSize
) {
    GLuint buffer;
    glGenBuffers(
        1,
        &buffer
    );
    
    glBindBuffer(
        target,
        buffer
    );
    
    LOGD("DATA_SIZE: %i,", dataSize);
    
    glBufferData(
        target,
        dataSize,
        data,
        GL_STATIC_DRAW
    );
    
    return buffer;
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
        char log[255];
        GLsizei length;
        glGetShaderInfoLog(
            shaderFragment,
            sizeof(log),
            &length,
            log
            );
        LOGD("SHADER_ERROR_FRAGMENT: %s", log);
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
        char log[255];
        GLsizei length;
        glGetShaderInfoLog(
            shaderVertex,
            sizeof(log),
            &length,
            log
        );
        LOGD("SHADER_ERROR_VERTEX: %s", log);
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
    
    glLinkProgram(
        program
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

void APIOpengl::setModelMatrix(
    GLint uniformLocation,
    float *model
) {
    glUniformMatrix4fv(
        uniformLocation,
        1,
        GL_FALSE,
        model
    );
}

APIVertexAttribute* APIOpengl::createVertexAttributes(
    int* attrs,
    char size
) {
    if (size < 1) {
        return nullptr;
    }
    
    APIVertexAttribute::Builder builder;
    
    for (char i = 0; i < size; i++) {
        int attr = attrs[i];
        
        switch (attr) {
            case 0:
                builder.pointPosition();
                continue;
            case 1:
                builder.pointTextureCoordinates();
                continue;
            case 2:
                builder.pointNormal();
                continue;
        }
    }
    
    return builder.build();
}

GLuint APIOpengl::createVertexArray(
    float* vertices,
    unsigned int verticesSize,
    int* indices,
    unsigned int indicesCount,
    APIVertexAttribute* vertexAttribute
) {
    GLuint vertexArray;
    glGenVertexArrays(
        1,
        &vertexArray
    );
    
    glBindVertexArray(
        vertexArray
    );
    
    LOGD("createVertexArray: COUNT(V:::I): %i:::%i", verticesSize, indicesCount);
    
    GLuint bufferVertices = createBuffer(
        GL_ARRAY_BUFFER,
        vertices,
        verticesSize * sizeof(float)
    );
    
    LOGD("createVertexArray: buffer_vertices");
    
    GLuint bufferIndices = createBuffer(
        GL_ELEMENT_ARRAY_BUFFER,
        indices,
        indicesCount * sizeof(int)
    );
    
    
    LOGD("createVertexArray: buffer_indices: vert_attr: %i", vertexAttribute);
    vertexAttribute->bindPointers();
    
    LOGD("bindPointers");
    
    glBindVertexArray(
        0
    );
    
    
    LOGD("vertexArray: %i", vertexArray);
    
    return vertexArray;
}

void APIOpengl::useProgram(
    GLuint program
) {
    glUseProgram(
        program
    );
}

APIBinderAttribute* APIOpengl::createBinderAttributes(
    int *attrs,
    char size
) {
    if (size < 1) {
        return nullptr;
    }
    
    APIBinderAttribute::Builder builder;
    
    for (char i = 0; i < size; i++) {
        int attr = attrs[i];
        
        switch (attr) {
            case 0:
                builder.bindPosition();
                continue;
            case 1:
                builder.bindTextureCoordinates();
                continue;
            case 2:
                builder.bindNormal();
                continue;
        }
    }
    
    return builder.build();
}

GLint APIOpengl::getUniformLocation(
    GLuint program,
    const char* name
) {
    return glGetUniformLocation(
        program,
        name
    );
}
