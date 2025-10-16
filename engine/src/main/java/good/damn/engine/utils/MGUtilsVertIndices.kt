package good.damn.engine.utils

import android.util.Log
import good.damn.engine.opengl.MGVector
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.math.cos
import kotlin.math.sin

class MGUtilsVertIndices {
    companion object {

        fun createSphere(
            countSteps: Int
        ): Pair<IntBuffer, FloatBuffer> {
            val radianStep = 3.14159f * 2 / countSteps
            val output = MGUtilsBuffer.allocateFloat(
                countSteps * 3
            )

            val indRows = countSteps - 2
            val indices = MGUtilsBuffer.allocateInt(
                (indRows + 1) * 3
            )

            var currentRadian = 0f
            var currentStep = 0
            var currentIndex = 0

            while (currentStep < countSteps) {
                Log.d("TAG", "createSphere: $currentStep -> $currentRadian")
                // X
                output.put(
                    currentIndex++,
                    sin(currentRadian)
                )
                // Y
                output.put(
                    currentIndex++,
                    .0f
                )
                // Z
                output.put(
                    currentIndex++,
                    cos(currentRadian)
                )
                currentRadian += radianStep
                currentStep++
            }

            currentStep = 0
            currentIndex = 0
            while (currentStep < indRows) {
                indices.put(
                    currentIndex++,
                    currentStep
                )

                indices.put(
                    currentIndex++,
                    currentStep + 1
                )

                indices.put(
                    currentIndex++,
                    currentStep + 2
                )

                currentStep++
            }

            indices.put(
                currentIndex,
                currentStep
            )

            indices.put(
                currentIndex + 1,
                currentStep + 1
            )

            indices.put(
                currentIndex + 2,
                0
            )


            return Pair(
                indices,
                output
            );
        }

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