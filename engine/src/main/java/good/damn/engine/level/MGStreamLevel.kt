package good.damn.engine.level

import android.opengl.GLES30
import good.damn.engine.models.MGMMeshInstance
import good.damn.engine.opengl.MGArrayVertex
import good.damn.engine.opengl.MGArrayVertexInstanced
import good.damn.engine.opengl.MGObject3d
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationInvert
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.opengl.pools.MGPoolMeshesStatic
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGIShaderNormal
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.triggers.MGMatrixTriggerMesh
import good.damn.engine.utils.MGUtilsBuffer
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.FloatBuffer

class MGStreamLevel {

    companion object {
        fun read(
            input: InputStream,
            shaderMaterial: MGShaderMaterial,
            poolTextures: MGPoolTextures
        ): Array<MGMMeshInstance>? {
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

                val material = MGMaterial.createWithPath(
                    poolTextures,
                    obj.texturesDiffuseFileName?.get(0),
                    obj.texturesMetallicFileName?.get(0),
                    obj.texturesEmissiveFileName?.get(0)
                )

                val vertexArray = MGArrayVertexInstanced()
                vertexArray.configure(
                    obj.vertices,
                    obj.indices
                )

                vertexArray.setupMatrixBuffer(
                    meshCount,
                    convertMatricesToBuffer(
                        modelMatrices
                    )
                )
                vertexArray.setupInstanceDrawing()

                return@Array MGMMeshInstance(
                    vertexArray,
                    material,
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
}