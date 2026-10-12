//
// Created by gooddamn on 10/12/26.
//

#ifndef ENGINEOPENGLES_COMATRIX4X4_H
#define ENGINEOPENGLES_COMATRIX4X4_H
#include <array>

class COMatrix4x4 {
private:
    float m[4][4] = { // Identity matrix
        {1.0f, 0.0f, 0.0f, 0.0f},
        {0.0f, 1.0f, 0.0f, 0.0f},
        {0.0f, 0.0f, 1.0f, 0.0f},
        {0.0f, 0.0f, 0.0f, 1.0f}
    };
public:
    
    COMatrix4x4 operator*(
        const COMatrix4x4& right
    );
    
    std::array<float, 4> operator*(
        const std::array<float, 4>& vec
    );
    
    void copyFrom(
        COMatrix4x4* src
    );
};


#endif //ENGINEOPENGLES_COMATRIX4X4_H
