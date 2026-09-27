//
// Created by gooddamn on 9/27/26.
//

#ifndef ENGINEOPENGLES_APIBINDERATTRIBUTE_H
#define ENGINEOPENGLES_APIBINDERATTRIBUTE_H

#include <vector>
#include "GLES3/gl3.h"

class APIBinderAttribute {
private:
    struct APIAttributeBind {
        const char* name;
        GLuint location;
    };
    
    std::vector<APIAttributeBind>* mAttributes;
    APIBinderAttribute(
        std::vector<APIAttributeBind>* attributes
    );

public:
    
    void bindAttributes(
        GLuint program
    );
    
    class Builder {
    private:
        std::vector<APIAttributeBind> mList;
    public:
        
        Builder* bindPosition();
        Builder* bindTextureCoordinates();
        Builder* bindNormal();
        
        APIBinderAttribute* build();
    };
    
};


#endif //ENGINEOPENGLES_APIBINDERATTRIBUTE_H
