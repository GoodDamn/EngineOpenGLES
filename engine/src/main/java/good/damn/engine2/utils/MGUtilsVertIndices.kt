package good.damn.engine2.utils

import good.damn.engine.ASUtilsBuffer
import good.damn.engine.sdk.SDVector3
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.sin

object MGUtilsVertIndices {

    @JvmStatic
    private val PI2 = 3.14159f * 2

    @JvmStatic
    fun createSphere(
        countStepsHorizontal: Int,
    ): Pair<ByteBuffer, FloatBuffer> {

        val stepsVertical = 3
        val radianStep = PI2 / countStepsHorizontal
        val output = ASUtilsBuffer.allocateFloat(
            countStepsHorizontal * stepsVertical * 3 //+ 6 // 6 = 3 (top) + 3(bottom)
        )

        val indRows = countStepsHorizontal - 2
        val indices = ASUtilsBuffer.allocateByte(
            (indRows + 1) * 3 * stepsVertical
        )

        var currentStepVertical = 0
        var currentIndex = 0

        fun drawCircleXY(
            output: FloatBuffer,
            cosRad: Float,
            sinRad: Float
        ) {
            // X
            output.put(
                currentIndex++,
                sinRad
            )
            // Y
            output.put(
                currentIndex++,
                cosRad
            )
            // Z
            output.put(
                currentIndex++,
                0f
            )
        }

        fun drawCircleXZ(
            output: FloatBuffer,
            cosRad: Float,
            sinRad: Float
        ) {
            // X
            output.put(
                currentIndex++,
                sinRad
            )
            // Y
            output.put(
                currentIndex++,
                0f
            )
            // Z
            output.put(
                currentIndex++,
                cosRad
            )
        }

        fun drawCircleYZ(
            output: FloatBuffer,
            cosRad: Float,
            sinRad: Float
        ) {
            // X
            output.put(
                currentIndex++,
                0f
            )
            // Y
            output.put(
                currentIndex++,
                sinRad
            )
            // Z
            output.put(
                currentIndex++,
                cosRad
            )
        }

        while (currentStepVertical < stepsVertical) {
            var currentRadian = 0f
            var currentStepHorizontal = 0

            while (currentStepHorizontal < countStepsHorizontal) {
                val cos = cos(currentRadian)
                val sin = sin(currentRadian)

                when (currentStepVertical) {
                    0 -> {
                        drawCircleXY(
                            output,
                            cos,
                            sin
                        )
                    }

                    1 -> {
                        drawCircleYZ(
                            output,
                            cos,
                            sin
                        )
                    }

                    2 -> {
                        drawCircleXZ(
                            output,
                            cos,
                            sin
                        )
                    }
                }

                currentRadian += radianStep
                currentStepHorizontal++
            }
            currentStepVertical++
        }

        var dtIndices = 0
        currentIndex = 0
        currentStepVertical = 0
        while (currentStepVertical < stepsVertical) {
            var currentStepHorizontal = 0
            while (currentStepHorizontal < indRows) {
                indices.put(
                    currentIndex++,
                    (currentStepHorizontal + dtIndices).toByte()
                )

                indices.put(
                    currentIndex++,
                    (currentStepHorizontal + dtIndices + 1).toByte()
                )

                indices.put(
                    currentIndex++,
                    (currentStepHorizontal + dtIndices + 2).toByte()
                )

                currentStepHorizontal++
            }

            indices.put(
                currentIndex++,
                (currentStepHorizontal + dtIndices).toByte()
            )

            indices.put(
                currentIndex++,
                (currentStepHorizontal + dtIndices + 1).toByte()
            )

            indices.put(
                currentIndex++,
                dtIndices.toByte()
            )
            currentStepVertical++
            dtIndices += currentStepHorizontal + 1

        }


        return Pair(
            indices,
            output
        );
    }


    @JvmStatic
    fun createCubeVertices(
        min: SDVector3,
        max: SDVector3
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


    @JvmStatic
    fun createCubeIndices() = byteArrayOf(
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


    @JvmStatic
    fun createQuadVertices() = floatArrayOf(
        -1.0f, 1.0f,     0.0f, 1.0f,//0.0f, 0.0f,   // top left
        -1.0f, -1.0f,    0.0f, 0.0f,//0.0f, 1.0f,   // bottom left
        1.0f, -1.0f,     1.0f, 0.0f,//1.0f, 1.0f,   // bottom right
        1.0f, 1.0f,      1.0f, 1.0f//1.0f, 0.0f   // top right
    )

    @JvmStatic
    fun createQuadIndices() = byteArrayOf(
        0, 1, 2,
        0, 2, 3
    )
}