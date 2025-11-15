package good.damn.engine.level

import android.util.Log
import good.damn.engine.models.MGMMeshInstance
import good.damn.engine.opengl.arrays.MGArrayVertexConfigurator
import good.damn.engine.opengl.arrays.MGArrayVertexInstanced
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.engine.opengl.enums.MGEnumTextureType
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.textures.MGTexture
import good.damn.engine.utils.MGUtilsA3D
import good.damn.engine.utils.MGUtilsBuffer
import good.damn.engine.utils.MGUtilsFile
import good.damn.ia3d.A3DImport
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
            val localPathLibTextures = "textures/$libName"
            val localPathLibObj = "objs/$libName"
            val libJson = MGUtilsFile.getPublicFile(
                "levels/$libName/library.txt"
            )

            for (j in map.atlases) {
                for (r in j.rects) {
                    if (poolTextures.get(r.name) == null) {
                        poolTextures.add(
                            r.name,
                            MGTexture.createDefaultAsset(
                                "$localPathLibTextures/${r.name}.png",
                                MGEnumTextureType.DIFFUSE
                            )
                        )

                        val metallicName = "${r.name}_m"
                        val metallic = "$localPathLibTextures/$metallicName.jpg"
                        MGUtilsFile.getPublicFile(
                            metallic
                        ).run {
                            if (exists()) {
                                poolTextures.add(
                                    metallicName,
                                    MGTexture.createDefaultAsset(
                                        metallic,
                                        MGEnumTextureType.METALLIC
                                    )
                                )
                            }
                        }

                        val emissiveName = "${r.name}_e"
                        val emissive = "$localPathLibTextures/$emissiveName.jpg"
                        MGUtilsFile.getPublicFile(
                            emissive
                        ).run {
                            if (exists()) {
                                poolTextures.add(
                                    emissiveName,
                                    MGTexture.createDefaultAsset(
                                        emissive,
                                        MGEnumTextureType.EMISSIVE
                                    )
                                )
                            }
                        }
                    }
                }
            }

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

                val diffuse = mesh.getJSONArray(
                    "textures"
                ).getJSONObject(0).getString(
                    "diffuseMap"
                )

                meshes[name] = MGProp(
                    mesh.getString(
                        "file"
                    ),
                    diffuse,
                    "${diffuse}_m",
                    "${diffuse}_e",
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

            val arrayInstanced = Array<MGMMeshInstance?>(
                meshes.size
            ) { null }

            var currentInstance = 0

            meshes.forEach {
                val file = MGUtilsFile.getPublicFile(
                    "$localPathLibObj/${it.value.fileNameA3d}"
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
                    it.value.fileNameMetallic,
                    it.value.fileNameEmissive,
                    localPathLibTextures
                )

                val mesh = obj.meshes[0]
                val configIndices = mesh.subMeshes[0].indices
                val configurator = MGArrayVertexConfigurator(
                    MGUtilsA3D.createConfigurationArrayVertex(
                        configIndices
                    )
                )

                configurator.configure(
                    MGUtilsA3D.createMergedVertexBuffer(
                        mesh
                    ),
                    configIndices.buffer,
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
            val fileNameMetallic: String?,
            val fileNameEmissive: String?,
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