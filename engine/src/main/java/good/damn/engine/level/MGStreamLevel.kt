package good.damn.engine.level

import android.opengl.GLES30
import android.util.Log
import good.damn.engine.models.MGMMeshInstance
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.arrays.MGArrayVertexManager
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.utils.MGUtilsArray
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsFile
import good.damn.ia3d.A3DImport
import good.damn.ia3d.enums.A3DEnumTypeBufferVertex
import good.damn.ia3d.stream.A3DInputStream
import good.damn.mapimporter.MIImportMap
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.FloatBuffer
import java.nio.charset.Charset
import java.util.LinkedList

class MGStreamLevel {

    companion object {
        fun readBin(
            input: InputStream,
            poolTextures: MGPoolTextures
        ): Array<MGMMeshInstance?>? {
            val stream = DataInputStream(
                input
            )
            val buffer = ByteArray(
                2048
            )

            val map = MIImportMap.createFromStream(
                stream,
                buffer
            )
            val TAG = "MGStreamLevel"
            val libName = map.atlases[0].rects[0].libraryName

            for (j in map.atlases) {
                for (r in j.rects) {
                    if (poolTextures.get(r.name) == null) {
                        poolTextures.add(
                            r.name,
                            MGTexture.createDefaultAsset(
                                "textures/$libName/${r.name}.png",
                                MGEnumTextureType.DIFFUSE
                            )
                        )
                    }
                }
            }

            val libJson = MGUtilsFile.getPublicFile(
                "textures/$libName/library.txt"
            )

            if (!libJson.exists()) {
                return null
            }

            val json = JSONObject(
                String(
                    libJson.inputStream().run {
                        val b = readBytes()
                        close()
                        return@run b
                    },
                    Charset.forName("UTF-8")
                )
            ).getJSONArray(
                "groups"
            ).getJSONObject(
                0
            ).getJSONArray(
                "props"
            )

            Log.d(TAG, "readBin: JSON_LIB: ${json.length()}")

            val meshes = HashMap<
                String, MGProp
            >(json.length())

            for (i in 0 until json.length()) {
                val lJson = json.getJSONObject(i)
                val name = lJson.getString(
                    "name"
                )

                val mesh = lJson.getJSONObject(
                    "mesh"
                )

                meshes[name] = MGProp(
                    mesh.getString(
                        "file"
                    ),
                    mesh.getJSONArray(
                        "textures"
                    ).getJSONObject(0).getString(
                        "diffuseMap"
                    ),
                    LinkedList()
                )
            }

            for (i in map.props) {
                meshes[
                    i.name
                ]?.run {
                    matrices.add(
                        MGMatrixTransformationNormal(
                            MGMatrixScaleRotation()
                        ).apply { 
                            model.apply {
                                var roll = 0f
                                var yaw = 0f
                                var pitch = 0f

                                i.rotation?.let {
                                    roll += Math.toDegrees(it.x.toDouble()).toFloat()
                                    yaw += Math.toDegrees(it.y.toDouble()).toFloat()
                                    pitch += Math.toDegrees(it.z.toDouble()).toFloat()
                                }

                                setRotation(
                                    roll,
                                    pitch,
                                    yaw
                                )

                                setPosition(
                                    i.position.x,
                                    i.position.z,
                                    i.position.y,
                                )
                                
                                i.scale?.let { 
                                    setScale(
                                        it.x,
                                        it.z,
                                        it.y
                                    )
                                }
                                
                                invalidatePosition()
                                invalidateScaleRotation()
                            }
                            
                            normal.apply {
                                calculateInvertModel()
                                calculateNormalMatrix()
                            }
                        }
                    )
                }
            }

            val localPathLib = "textures/$libName"
            val arrayInstanced = Array<MGMMeshInstance?>(
                meshes.size
            ) { null }

            var currentInstance = 0

            meshes.forEach {
                val folderName = it.value.fileNameA3d.run {
                    val i = indexOf(".")
                    return@run if (
                        i == -1
                    ) null else substring(
                        0, i
                    )
                }

                val file = MGUtilsFile.getPublicFile(
                    "$localPathLib/$folderName/${it.value.fileNameA3d}"
                )

                if (!file.exists()) {
                    return@forEach
                }
                
                val obj = A3DImport.createFromStream(
                    A3DInputStream(
                        FileInputStream(
                            file
                        )
                    ),
                    buffer
                ) ?: return@forEach

                val material = MGMaterial.createWithPath(
                    poolTextures,
                    it.value.fileNameDiffuse,
                    null,
                    null,
                    localPathLib
                )

                val mesh = obj.meshes[0]
                val configurator = MGArrayVertexConfigurator(
                    MGEnumArrayVertexConfiguration.SHORT
                )

                configurator.configure(
                    MGUtilsArray.createMergedVertexBuffer(
                        mesh.vertexBuffers[
                            A3DEnumTypeBufferVertex.POSITION.type - 1
                        ]!!.vertices,
                        mesh.vertexBuffers[
                            A3DEnumTypeBufferVertex.UV1.type - 1
                        ]!!.vertices,
                        mesh.vertexBuffers[
                            A3DEnumTypeBufferVertex.NORMAL1.type - 1
                        ]!!.vertices
                    ),
                    mesh.subMeshes[0].indices,
                    MGArrayVertexConfigurator.STRIDE
                )

                val vertexArray = MGArrayVertexInstanced(
                    configurator
                )


                val modelMatrices = it.value.matrices
                    .toTypedArray()

                val matrices = convertMatricesToBuffer(
                    modelMatrices
                )

                vertexArray.setupMatrixBuffer(
                    modelMatrices.size,
                    matrices.model,
                    matrices.rotation
                )

                vertexArray.setupInstanceDrawing(
                    MGArrayVertexInstanced.INDEX_ATTRIB_INSTANCE_MODEL,
                    MGArrayVertexInstanced.INDEX_BUFFER_MODEL
                )

                vertexArray.setupInstanceDrawing(
                    MGArrayVertexInstanced.INDEX_ATTRIB_INSTANCE_ROTATION,
                    MGArrayVertexInstanced.INDEX_BUFFER_ROTATION
                )

                arrayInstanced[
                    currentInstance
                ] = MGMMeshInstance(
                    vertexArray,
                    material,
                    modelMatrices
                )

                currentInstance++
            }

            return arrayInstanced
        }

        private data class MGProp(
            val fileNameA3d: String,
            val fileNameDiffuse: String,
            val matrices: LinkedList<
                MGMatrixTransformationNormal<
                    MGMatrixScaleRotation
                >
            >
        )

        fun read(
            input: InputStream,
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
                    MGMatrixTransformationNormal(
                        MGMatrixScaleRotation()
                    ).apply {
                        model.run {
                            val strPosition = bufferedReader
                                .readLine()!!
                                .split("\\s+".toRegex())

                            setPosition(
                                strPosition.getOrNull(0)?.toFloatOrNull() ?: 0f,
                                strPosition.getOrNull(1)?.toFloatOrNull() ?: 0f,
                                strPosition.getOrNull(2)?.toFloatOrNull() ?: 0f,
                            )

                            val scale = strPosition.getOrNull(3)?.toFloatOrNull() ?: 0f
                            setScale(
                                scale,
                                scale,
                                scale
                            )

                            setRotation(
                                strPosition.getOrNull(4)?.toFloatOrNull() ?: 0f,
                                strPosition.getOrNull(5)?.toFloatOrNull() ?: 0f,
                                strPosition.getOrNull(6)?.toFloatOrNull() ?: 0f,
                            )

                            invalidatePosition()
                            invalidateScaleRotation()
                        }

                        normal.run {
                            calculateInvertModel()
                            calculateNormalMatrix()
                        }
                    }
                }

                val obj = MGObject3d.createFromAssets(
                    "objs/$meshName"
                )?.get(0)!!

                val material = MGMaterial.createWithPath(
                    poolTextures,
                    obj.texturesDiffuseFileName?.get(0),
                    obj.texturesMetallicFileName?.get(0),
                    obj.texturesEmissiveFileName?.get(0),
                    "textures"
                )

                val configurator = MGArrayVertexConfigurator(
                    MGEnumArrayVertexConfiguration.INT
                )

                configurator.configure(
                    obj.vertices,
                    obj.indices,
                    MGArrayVertexConfigurator.STRIDE
                )

                val vertexArray = MGArrayVertexInstanced(
                    configurator
                )

                val matrices = convertMatricesToBuffer(
                    modelMatrices
                )

                vertexArray.setupMatrixBuffer(
                    meshCount,
                    matrices.model,
                    matrices.rotation
                )

                vertexArray.setupInstanceDrawing(
                    MGArrayVertexInstanced.INDEX_ATTRIB_INSTANCE_MODEL,
                    MGArrayVertexInstanced.INDEX_BUFFER_MODEL
                )

                vertexArray.setupInstanceDrawing(
                    MGArrayVertexInstanced.INDEX_ATTRIB_INSTANCE_ROTATION,
                    MGArrayVertexInstanced.INDEX_BUFFER_ROTATION
                )

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

        private fun convertMatricesToBuffer(
            v: Array<
                MGMatrixTransformationNormal<
                    MGMatrixScaleRotation
                >
            >
        ): MGMatrixBuffer {
            var i = 0
            val outputModel = FloatArray(
                v.size * 16
            )
            val outputRotation = FloatArray(
                outputModel.size
            )

            v.forEach {
                for (indexMat in it.model.model.indices) {
                    outputModel[i] = it.model.model[indexMat]
                    outputRotation[i] = it.normal.normalMatrix[indexMat]
                    i++
                }
            }

            return MGMatrixBuffer(
                MGUtilsBuffer.createFloat(
                    outputModel
                ),
                MGUtilsBuffer.createFloat(
                    outputRotation
                )
            )
        }

        private data class MGMatrixBuffer(
            val model: FloatBuffer,
            val rotation: FloatBuffer
        )
    }
}