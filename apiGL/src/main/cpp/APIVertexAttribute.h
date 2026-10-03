//
// Created by gooddamn on 9/27/26.
//

#ifndef ENGINEOPENGLES_APIVERTEXATTRIBUTE_H
#define ENGINEOPENGLES_APIVERTEXATTRIBUTE_H

#include "GLES3/gl3.h"
#include <vector>

class APIVertexAttribute {
private:
    struct APIAttribute {
        GLuint attrib;
        unsigned int offset;
        unsigned int size;
    };
    
    std::vector<APIAttribute>* mAttributes;
    unsigned short mStride;
    APIVertexAttribute(
        std::vector<APIAttribute>* attributes,
        unsigned short stride
    );
    
public:
    
    void bindPointers();
    
    ~APIVertexAttribute();
    
    class Builder {
    private:
        std::vector<APIAttribute>* mList = new std::vector<APIAttribute>();
        unsigned short mOffset = 0;
    public:
        
        Builder* pointPosition();
        Builder* pointTextureCoordinates();
        Builder* pointNormal();
        
        ~Builder();
        
        APIVertexAttribute* build();
    };
    
};


#endif //ENGINEOPENGLES_APIVERTEXATTRIBUTE_H
