//
// Created by gooddamn on 9/26/26.
//

#include "APIOpengl.h"
#include "GLES2/gl2ext.h"
#include "APIUtils.h"
#include <string>
#include "APIImage.h"

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
    APIVertexArray* vertexArray,
    GLenum mode
) {
    glBindVertexArray(
        vertexArray->descriptor
    );
    
    glDrawElements(
        mode,
        vertexArray->indicesCount,
        vertexArray->typeIndices,
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

APIVertexArray* APIOpengl::createVertexArray(
    float* vertices,
    unsigned int verticesSize,
    int* indices,
    unsigned int indicesCount,
    APIVertexAttribute* vertexAttribute
) {
    auto* outVertexArray = new APIVertexArray;
    outVertexArray->typeIndices = GL_UNSIGNED_INT;
    outVertexArray->indicesCount = indicesCount;
    
    void* data = indices;
    unsigned int dataSize = indicesCount * sizeof(int);
    if (indicesCount < UINT8_MAX) {
        outVertexArray->typeIndices = GL_UNSIGNED_BYTE;
        dataSize = indicesCount;
        auto* cc = new unsigned char[indicesCount];
        copyByte(indices, cc, indicesCount);
        data = cc;
    } else if (indicesCount < UINT16_MAX) {
        outVertexArray->typeIndices = GL_UNSIGNED_SHORT;
        dataSize = indicesCount * sizeof(short);
        auto* ss = new unsigned short[indicesCount];
        copyShort(indices, ss, indicesCount);
        data = ss;
    }
    
    glGenVertexArrays(
        1,
        &(outVertexArray->descriptor)
    );
    
    glBindVertexArray(
        outVertexArray->descriptor
    );
    
    GLuint bufferVertices = createBuffer(
        GL_ARRAY_BUFFER,
        vertices,
        verticesSize * sizeof(float)
    );
    
    GLuint bufferIndices = createBuffer(
        GL_ELEMENT_ARRAY_BUFFER,
        data,
        dataSize
    );
    
    vertexAttribute->bindPointers();
    
    glBindVertexArray(
        0
    );
    
    return outVertexArray;
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

APITexture* APIOpengl::createTexture(
    int fileDescriptor
) {
    APIImage* image = APIImage()
        .setSource(fileDescriptor)
        ->load();
    
    if (image == nullptr) {
        LOGD("createTexture: NULL");
        return nullptr;
    }
    
    APITexture* outTexture = new APITexture;
    
    outTexture->activeSlot = GL_TEXTURE0;
    
    glGenTextures(
        1,
        &(outTexture->descriptor)
    );
    
    glBindTexture(
        GL_TEXTURE_2D,
        outTexture->descriptor
    );
    
    glTexParameteri(
        GL_TEXTURE_2D,
        GL_TEXTURE_MIN_FILTER,
        GL_LINEAR_MIPMAP_NEAREST
    );
    
    glTexParameteri(
        GL_TEXTURE_2D,
        GL_TEXTURE_MAG_FILTER,
        GL_LINEAR
    );
    
    glTexParameteri(
        GL_TEXTURE_2D,
        GL_TEXTURE_WRAP_S,
        GL_CLAMP_TO_EDGE
    );
    
    glTexParameteri(
        GL_TEXTURE_2D,
        GL_TEXTURE_WRAP_T,
        GL_CLAMP_TO_EDGE
    );
    
    glTexImage2D(
        GL_TEXTURE_2D,
        0,
        GL_RGBA,
        image->getWidth(),
        image->getHeight(),
        0,
        GL_RGBA,
        GL_UNSIGNED_BYTE,
        image->getPixels()
    );
    
    glGenerateMipmap(
        GL_TEXTURE_2D
    );
    
    /*glTexParameteri(
        GL_TEXTURE_2D,
        GL_TEXTURE_MAX_ANISOTROPY_EXT,
        16.0f
    );*/
    
    return outTexture;
}

void APIOpengl::setCullface() {
    glEnable(
        GL_CULL_FACE
    );
    
    glCullFace(
        GL_BACK
    );
    
    glFrontFace(
        GL_CW
    );
}

void APIOpengl::putTexture(
    GLint uniformTexture,
    APITexture* texture
) {
    glBindTexture(
        GL_TEXTURE_2D,
        texture->descriptor
    );
    
    glActiveTexture(
        texture->activeSlot
    );
    
    glUniform1i(
        uniformTexture,
        texture->activeSlot
    );
}
