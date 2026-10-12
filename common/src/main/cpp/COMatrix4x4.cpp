//
// Created by gooddamn on 10/12/26.
//

#include "COMatrix4x4.h"

COMatrix4x4 COMatrix4x4::operator*(
    const COMatrix4x4 &right
) {
    COMatrix4x4 result;
    for (int i = 0; i < 4; ++i) {
        for (int j = 0; j < 4; ++j) {
            result.m[i][j] = 0.0f;
            for (int k = 0; k < 4; ++k) {
                result.m[i][j] += this->m[k][j] * right.m[i][k];
            }
        }
    }
    return result;
}

std::array<float, 4> COMatrix4x4::operator*(
    const std::array<float, 4> &vec
) {
    std::array<float, 4> result;
    for (int i = 0; i < 4; ++i) {
        result[i] = 0;
        for (int k = 0; k < 4; ++k) {
            result[i] += this->m[k][i] * vec[k];
        }
    }
    return result;
}

void COMatrix4x4::copyFrom(
    COMatrix4x4 *src
) {
    std::memcpy(
        m,
        src->m,
        sizeof(m)
    );
}
