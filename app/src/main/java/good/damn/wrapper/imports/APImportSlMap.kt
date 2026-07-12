package good.damn.wrapper.imports

import android.util.Log
import good.damn.apigl.arrays.GLArrayVertexConfigurator
import good.damn.apigl.arrays.pointers.GLPointerAttribute
import good.damn.apigl.drawers.GLDrawerMaterialTexture
import good.damn.apigl.drawers.GLDrawerMesh
import good.damn.apigl.drawers.GLDrawerMeshMaterial
import good.damn.apigl.drawers.GLDrawerMeshMaterialNormals
import good.damn.apigl.drawers.GLDrawerNormalMatrix
import good.damn.apigl.drawers.GLDrawerPositionEntity
import good.damn.apigl.drawers.GLDrawerVertexArray
import good.damn.apigl.drawers.GLMaterial
import good.damn.apigl.enums.GLEnumFaceOrder
import good.damn.apigl.runnables.GLRunglConfigVertexArray
import good.damn.apigl.shaders.GLShaderMaterial
import good.damn.apigl.shaders.base.GLBinderAttribute
import good.damn.common.utils.COUtilsFile
import good.damn.engine.ASObject3d
import good.damn.engine2.logic.MGMGeometryFrustrumMesh
import good.damn.engine2.logic.MGVolumeTriggerMesh
import good.damn.engine2.models.MGMMeshDrawer
import good.damn.engine2.providers.MGProviderGL
import good.damn.logic.utils.LGUtilsTrigger
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream

class APImportSlMap
: MGProviderGL(),
APIProcessTempFile {

    companion object {
        private val TAG = APImportSlMap::class.simpleName
    }

    private val mBinderAttribute = GLBinderAttribute.Builder()
        .bindPosition()
        .bindTextureCoordinates()
        .bindNormal()
        .build()

    override fun isValidExtension(
        fileName: String
    ) = fileName.contains(
        ".slmap"
    )

    override fun onProcessTempFile(
        rootFile: File,
        contextFiles: Array<File?>
    ) {
        val input = DataInputStream(
            FileInputStream(
                rootFile
            )
        )

        val buffer = ByteArray(512)

        repeat(
            input.readInt()
        ) {
            val nameBytesLength = input.readInt()

            input.read(
                buffer,
                0,
                nameBytesLength
            )

            val objName = String(
                buffer,
                0,
                nameBytesLength,
                Charsets.UTF_8
            )

            val positionX = input.readFloat()
            val positionY = input.readFloat()
            val positionZ = input.readFloat()

            Log.d(TAG, "onProcessTempFile: $objName ($positionX, $positionY, $positionZ)")

            /*val objFile = COUtilsFile.getPublicFile(
                "objs/$objName.mgobj"
            )

            if (!objFile.exists()) {
                return@repeat
            }*/

            val obj = ASObject3d.createFromFile(
                COUtilsFile.getPublicFile(
                    "objs/$objName.fbx"
                )
            )?.get(0) ?: return@repeat

            
            /*val objInp = DataInputStream(
                FileInputStream(
                    objFile
                )
            )

            val countVertices = objInp.readInt() * 8
            val countIndices = (objFile.length().toInt() - 4 - countVertices * 4) / 4

            val bufferVertices = ASUtilsBuffer.allocateFloat(
                countVertices
            )

            val bufferIndices = ASUtilsBuffer.allocateInt(
                countIndices
            )

            Log.d(TAG, "onProcessTempFile: $countVertices::::$countIndices::::${objFile.length()}")
            var ii = 3
            var iii = 4
            var i = 0
            while (i < countVertices) {
                var f = objInp.readFloat()
                if (i == ii) {
                    val u = 1.0f - objInp.readFloat()
                    val v = f
                    ii += 8
                   // Log.d(TAG, "onProcessTempFile: $i::: U: $f")

                    bufferVertices.put(
                        i,
                        u
                    )
                    i++

                    bufferVertices.put(
                        i,
                        v
                    )

                    i++

                    continue
                }

                bufferVertices.put(
                    i,
                    f
                )

                i++
            }

            for (j in 0 until countIndices) {
                bufferIndices.put(
                    j,
                    objInp.readInt()
                )
            }

            bufferVertices.position(0)
            bufferIndices.position(0)

            objInp.close()*/

            val provider = glProvider

            val triggerPoint = LGUtilsTrigger.createTriggerPoint(
                obj.vertices
            )

            val triggerMatrix = LGUtilsTrigger.createTriggerPointMatrix(
                triggerPoint
            ).apply {
                setPosition(
                    positionX,
                    positionZ,
                    -positionY
                )

                addRotation(
                    90f,
                    0f,
                    0f
                )

                invalidateScaleRotation()
                invalidatePosition()
                calculateInvertTrigger()
                calculateNormals()
            }

            val materialShader = provider.pools.materials.loadOrGetFromCache(
                objName,
                "textures/$objName"
            )

            val shaders = provider.shaders
            val shader = shaders.cacheGeometryPass.loadOrGetFromCache(
                materialShader.srcCodeMaterial,
                shaders.source.vert,
                mBinderAttribute,
                arrayOf(
                    GLShaderMaterial(
                        materialShader.shaderTextures
                    )
                )
            )

            val configurator = GLArrayVertexConfigurator(
                obj.config
            )

            provider.glHandler.post(
                GLRunglConfigVertexArray(
                    configurator,
                    obj.vertices,
                    obj.indices,
                    GLPointerAttribute.defaultNoTangent
                )
            )

            val drawerMesh = GLDrawerMeshMaterialNormals(
                GLDrawerMeshMaterial(
                    arrayOf(
                        GLMaterial(
                            GLDrawerMaterialTexture(
                                materialShader.textures
                            )
                        )
                    ),
                    GLDrawerMesh(
                        GLDrawerVertexArray(
                            configurator
                        ),
                        GLDrawerPositionEntity(
                            triggerMatrix.matrixMesh.model
                        ),
                        GLEnumFaceOrder.COUNTER_CLOCK_WISE
                    )
                ),
                GLDrawerNormalMatrix(
                    triggerMatrix.matrixMesh.normal
                )
            )

            val frustrumMesh = MGMGeometryFrustrumMesh(
                false,
                drawerMesh
            )

            provider.geometry.meshesNormals.add(
                MGMMeshDrawer(
                    shader,
                    frustrumMesh
                )
            )

            val volume = MGVolumeTriggerMesh(
                triggerMatrix.matrixTrigger.model,
                frustrumMesh
            )

            provider.managers.apply {
                managerFrustrum.volumes.add(
                    volume
                )

                managerTrigger.addTrigger(
                    volume
                )
            }
        }

        input.close()
        rootFile.delete()
    }
}