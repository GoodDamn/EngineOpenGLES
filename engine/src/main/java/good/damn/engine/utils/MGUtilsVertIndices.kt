package good.damn.engine.utils

import good.damn.engine.opengl.MGVector

class MGUtilsVertIndices {
    companion object {
        fun createCubeVertices(
            min: MGVector,
            max: MGVector
        ) = floatArrayOf(
            // top-left
            min.x, min.y, min.z, // down 0
            min.x, max.y, min.z, // up 1

            // top-right
            max.x, min.y, min.z, // down 2
            max.x, max.y, min.z, // up 3

            // bottom-left
            min.x, min.y, max.z, // down 4
            min.x, max.y, max.z, // up 5

            // bottom-right
            max.x, min.y, max.z, // down 6
            max.x, max.y, max.z, // up 7
        )

        fun createCubeIndices() = intArrayOf(
            // Left
            0, 1, 4,
            4, 5, 1,

            // Top
            1, 3, 7,
            7, 5, 1,

            // right
            3, 2, 6,
            6, 7, 3,

            // bottom
            0, 2, 4,
            4, 6, 2,

            // front
            0, 1, 2,
            2, 3, 1,

            // back
            4, 5, 6,
            6, 7, 5
        )
    }
}