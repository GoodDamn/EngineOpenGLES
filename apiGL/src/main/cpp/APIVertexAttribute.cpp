//
// Created by gooddamn on 9/27/26.
//

#include "APIVertexAttribute.h"
#include "GLES3/gl3.h"

APIVertexAttribute::APIVertexAttribute(
    std::vector<APIAttribute> *attributes,
    unsigned short stride
): mAttributes(
    attributes
), mStride(
    stride
) {}

void APIVertexAttribute::bindPointers() {
    for (auto& it : *mAttributes) {
        glEnableVertexAttribArray(
            it.attrib
        );
        
        glVertexAttribPointer(
            it.attrib,
            it.size,
            GL_FLOAT,
            false,
            mStride,
            (void*)it.offset
        );
    }
}

APIVertexAttribute::~APIVertexAttribute() {
    mAttributes->clear();
    delete mAttributes;
    mAttributes = nullptr;
}


APIVertexAttribute* APIVertexAttribute::Builder::build() {
    auto* attr = new APIVertexAttribute(
        mList,
        mOffset
    );
    
    return attr;
}

APIVertexAttribute::Builder* APIVertexAttribute::Builder::pointPosition() {
    APIAttribute attribute;
    attribute.attrib = 0;
    attribute.offset = mOffset;
    attribute.size = 3;
    
    mList->push_back(
        attribute
    );
    
    mOffset += 3 * 4;
    return this;
}

APIVertexAttribute::Builder *APIVertexAttribute::Builder::pointTextureCoordinates() {
    APIAttribute attribute;
    attribute.attrib = 1;
    attribute.offset = mOffset;
    attribute.size = 2;
    
    mList->push_back(
        attribute
    );
    
    mOffset += 2 * 4;
    return this;
}

APIVertexAttribute::Builder *APIVertexAttribute::Builder::pointNormal() {
    APIAttribute attribute;
    attribute.attrib = 2;
    attribute.offset = mOffset;
    attribute.size = 3;
    
    mList->push_back(
        attribute
    );
    
    mOffset += 3 * 4;
    return nullptr;
}

APIVertexAttribute::Builder::~Builder() {
    mList = nullptr;
}
