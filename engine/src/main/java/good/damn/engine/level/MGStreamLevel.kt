package good.damn.engine.level

import android.opengl.GLES30
import good.damn.engine.models.MGMMeshInstance
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.MGObject3d
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.opengl.shaders.MGIShaderNormal
import good.damn.engine.opengl.triggers.MGMatrixTriggerMesh
import good.damn.engine.utils.MGUtilsBuffer
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.FloatBuffer

class MGStreamLevel(
    private val input: InputStream
) {

    fun read(): Array<MGMMeshInstance>? {
        val bufferedReader = BufferedReader(
            InputStreamReader(
                input
            )
        )

        val meshesCount = bufferedReader.readLineValueInt()
            ?: return null

        val output = Array(
            meshesCount
        ) {
            val meshNameCount = bufferedReader
                .readLine()!!
                .split("\\s+".toRegex())

            val meshCount = meshNameCount[1]
                .toInt()

            val meshName = meshNameCount[0]

            val modelMatrices = Array(
                meshCount
            ) {
                MGMatrixScaleRotation().apply {
                    val strPosition = bufferedReader
                        .readLine()!!
                        .split("\\s+".toRegex())

                    setPosition(
                        strPosition[0].toIntOrNull()?.toFloat() ?: 0f,
                        strPosition[1].toIntOrNull()?.toFloat() ?: 0f,
                        strPosition[2].toIntOrNull()?.toFloat() ?: 0f,
                    )
                    val scale =
                        strPosition[3].toFloatOrNull() ?: 1f
                    setScale(
                        scale,
                        scale,
                        scale
                    )

                    invalidatePosition()
                    invalidateScaleRotation()
                }
            }

            val obj = MGObject3d.createFromAssets(
                "objs/$meshName"
            )?.get(0)!!

            val vertexArray = MGArrayVertex()
            vertexArray.configure(
                obj.vertices,
                obj.indices
            )

            val buffers = intArrayOf(1)
            GLES30.glGenBuffers(
                1,
                buffers,
                0
            )

            GLES30.glBindBuffer(
                GLES30.GL_ARRAY_BUFFER,
                buffers[0]
            )

            GLES30.glBufferData(
                GLES30.GL_ARRAY_BUFFER,
                meshCount * 64,
                convertMatricesToBuffer(
                    modelMatrices
                ),
                GLES30.GL_STATIC_DRAW
            )
            vertexArray.setupInstanceDrawing()

            return@Array MGMMeshInstance(
                vertexArray,
                obj,
                modelMatrices
            )
        }
        bufferedReader.close()

        return output
    }

    private fun BufferedReader.readLineValueInt() =
        readLine().toIntOrNull()

    private inline fun convertMatricesToBuffer(
        v: Array<MGMatrixScaleRotation>
    ): FloatBuffer {
        var i = 0
        val output = FloatArray(
            v.size * 16
        )

        v.forEach {
            it.model.forEach { vv ->
                output[i] = vv
                i++
            }
        }

        return MGUtilsBuffer.createFloat(
            output
        )
    }
}