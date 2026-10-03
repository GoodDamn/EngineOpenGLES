//
// Created by gooddamn on 9/27/26.
//

#include "APIBinderAttribute.h"

APIBinderAttribute::APIBinderAttribute(
    std::vector<APIAttributeBind> *attributes
) {
    mAttributes = attributes;
}

void APIBinderAttribute::bindAttributes(
    GLuint program
) {
    for (auto& it: *mAttributes) {
        glBindAttribLocation(
            program,
            it.location,
            it.name
        );
    }
}

APIBinderAttribute::~APIBinderAttribute() {
    mAttributes->clear();
    delete mAttributes;
    mAttributes = nullptr;
}


APIBinderAttribute::Builder* APIBinderAttribute::Builder::bindPosition() {
    APIAttributeBind attr;
    attr.location = 0;
    attr.name = "position";
    
    mList->push_back(
        attr
    );
    
    return this;
}

APIBinderAttribute::Builder* APIBinderAttribute::Builder::bindTextureCoordinates() {
    APIAttributeBind attr;
    attr.location = 1;
    attr.name = "texCoord";
    
    mList->push_back(
        attr
    );
    
    return this;
}

APIBinderAttribute::Builder* APIBinderAttribute::Builder::bindNormal() {
    APIAttributeBind attr;
    attr.location = 2;
    attr.name = "normal";
    
    mList->push_back(
        attr
    );
    
    return this;
}

APIBinderAttribute* APIBinderAttribute::Builder::build() {
    return new APIBinderAttribute(
        mList
    );
}

APIBinderAttribute::Builder::~Builder() {
    mList = nullptr;
}
