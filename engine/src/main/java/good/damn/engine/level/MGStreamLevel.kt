package good.damn.engine.level

import good.damn.engine.flow.MGFlowLevel
import good.damn.engine.loaders.MGLoaderLevelLibrary
import good.damn.engine.loaders.MGLoaderLevelMatrices
import good.damn.engine.loaders.MGLoaderLevelMatrices.Companion.fillModelMatrix
import good.damn.engine.loaders.mesh.MGLoaderLevelMeshA3D
import good.damn.engine.loaders.MGLoaderLevelTextures
import good.damn.engine.models.MGMInformator
import good.damn.engine.models.MGMInstanceMesh
import good.damn.engine.models.json.MGMLevelInfoMesh
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.mapimporter.MIImportMap
import good.damn.mapimporter.models.MIMProp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.InputStream

object MGStreamLevel {

    @JvmStatic
    fun readBin(
        flow: MGFlowLevel<MGMInstanceMesh>,
        input: InputStream,
        informator: MGMInformator,
        bufferMap: ByteArray
    ) {
        val stream = DataInputStream(
            input
        )

        val map = MIImportMap.createFromStream(
            stream,
            bufferMap
        )

        val scope = CoroutineScope(
            Dispatchers.IO
        )

        val libName = map.atlases[0].rects[0].libraryName
        val localPathLibTextures = "textures/$libName"
        val localPathLibObj = "objs/$libName"
        val loaderLib = MGLoaderLevelLibrary(
            scope,
            informator,
            localPathLibTextures,
            "levels/$libName/library.txt",
            "levels/$libName/culling.txt"
        )

        val loaderTextures = MGLoaderLevelTextures(
            scope,
            informator.glHandler,
            informator.poolTextures,
            localPathLibTextures
        )

        loaderTextures.loadTextures(
            map
        )

        if (!loaderLib.loadLibrary()) {
            return
        }

        loaderLib.readProps()

        loaderLib.loadNonCullFaceList()

        while (loaderLib.meshes == null) {}
        val meshes = loaderLib.meshes!!

        val loaderMatrices = MGLoaderLevelMatrices(
            scope
        )

        loaderMatrices.loadMatrices(
            meshes,
            map
        )

        val loaderMeshes = MGLoaderLevelMeshA3D(
            localPathLibObj,
            informator.glHandler
        )

        while (
            !(loaderMatrices.isLoadMatrices ||
                loaderTextures.isLoadCompleted
                )
        ) {}

        meshes.forEach {
            scope.launch {
                val prop = it.value
                val v = loaderMeshes.loadInstanceArray(
                    prop.fileNameA3d,
                    prop.matrices
                ) ?: return@launch

                prop.materialTexture.load(
                    informator.poolTextures,
                    localPathLibTextures,
                    informator.glHandler
                )

                flow.emit(
                    MGMInstanceMesh(
                        prop.shaderOpaque,
                        v.vertexArray,
                        arrayOf(
                            MGMaterial(
                                prop.materialTexture
                            )
                        ),
                        prop.enableCullFace,
                        v.modelMatrices
                    )
                )
            }
        }

        scope.launch {
            while (loaderLib.terrain == null) { }
            val terrain = loaderLib.terrain!!

            val mapProp = map.props.find {
                it.name == terrain.a3dMesh
            }

            loadLandscape(
                terrain,
                loaderMeshes,
                loaderLib,
                informator,
                localPathLibTextures,
                mapProp
            )?.run {
                flow.emit(
                    this
                )
            }
        }

    }

    private inline fun loadLandscape(
        landscape: MGMLevelInfoMesh,
        loaderMesh: MGLoaderLevelMeshA3D,
        loaderProp: MGLoaderLevelLibrary,
        informator: MGMInformator,
        localLibPathTextures: String,
        mapProp: MIMProp?,
    ): MGMInstanceMesh? {
        val instanceArray = loaderMesh.loadInstanceArray(
            "${landscape.a3dMesh}.a3d",
            arrayListOf(
                MGMatrixTransformationNormal(
                    MGMatrixScaleRotation()
                ).apply {
                    mapProp ?: return@apply
                    fillModelMatrix(
                        model,
                        mapProp
                    )

                    normal.apply {
                        calculateInvertModel()
                        calculateNormalMatrix()
                    }
                }
            )
        ) ?: return null

        val prop = loaderProp.readProp(
            landscape,
            "png"
        )

        prop.materialTexture.load(
            informator.poolTextures,
            localLibPathTextures,
            informator.glHandler
        )

        return MGMInstanceMesh(
            prop.shaderOpaque,
            instanceArray.vertexArray,
            arrayOf(
                MGMaterial(
                    prop.materialTexture
                )
            ),
            true,
            instanceArray.modelMatrices
        )
    }

    fun read(
        input: InputStream,
        poolTextures: MGPoolTextures
    ): Array<MGMInstanceMesh>? {
        return null
        /*val bufferedReader = BufferedReader(
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

            return@Array loaderMesh.createVertexArrayInstance(
                MGEnumArrayVertexConfiguration.INT,
                obj.vertices,
                obj.indices,
                modelMatrices,
                material
            )
        }
        bufferedReader.close()

        return output*/
    }

    private fun BufferedReader.readLineValueInt() =
        readLine().toIntOrNull()
}