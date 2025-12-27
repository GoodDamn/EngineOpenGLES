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
import good.damn.engine.models.json.spawn.MGMLevelSpawnInfo
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialMutable
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.opengl.models.MGMMeshMaterial
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.triggers.MGTriggerMesh
import good.damn.engine.opengl.triggers.MGTriggerSimple
import good.damn.engine.shader.generators.MGMMaterialShader
import good.damn.engine.utils.MGUtilsFile
import good.damn.engine.utils.MGUtilsJson
import good.damn.mapimporter.MIImportMap
import good.damn.mapimporter.models.MIMMap
import good.damn.mapimporter.models.MIMProp
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

        val libName = map.atlases[0].rects[0].libraryName
        val localPathDir = "levels/$libName"
        val localPathLibTextures = "textures/$libName"
        val localPathLibObj = "objs/$libName"

        processSpawnPoints(
            map,
            localPathDir,
            informator
        )

        val loaderLib = MGLoaderLevelLibrary(
            informator,
            localPathLibTextures,
            "$localPathDir/library.txt",
            "$localPathDir/culling.txt"
        )

        val loaderTextures = MGLoaderLevelTextures(
            informator.glHandler,
            informator.poolTextures,
            localPathLibTextures
        )

        loaderTextures.loadTextures(
            map
        )
        loaderLib.loadLibrary()
        loaderLib.loadNonCullFaceList()

        loaderLib.readProps()

        val meshes = loaderLib.meshes!!

        val loaderMatrices = MGLoaderLevelMatrices()

        loaderMatrices.loadMatrices(
            meshes,
            map
        )

        val loaderMeshes = MGLoaderLevelMeshA3D(
            localPathLibObj,
            informator.glHandler,
            bufferMap
        )

        meshes.forEach {
            val prop = it.value
            val v = loaderMeshes.loadInstanceArray(
                prop.fileNameA3d,
                prop.matrices,
                1f
            ) ?: return

            flow.emit(
                MGMInstanceMesh(
                    prop.shaderOpaque,
                    v.vertexArray,
                    prop.materials,
                    prop.enableCullFace,
                    v.modelMatrices
                )
            )
        }

        //scope.launch {
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
        //}

    }

    private inline fun processSpawnPoints(
        map: MIMMap,
        localPathDir: String,
        informator: MGMInformator
    ) {
        val json = MGMLevelSpawnInfo.createFromJson(
            MGUtilsJson.createFromFile(
                MGUtilsFile.getPublicFile(
                    "$localPathDir/spawnpoints.txt"
                )
            )
        )
        // it only processes with cached object, material and shader
        // may be you won't spawn present boxes :)
        val mesh = informator.poolMeshes[
            json.mesh
        ] ?: return

        val meshes = Array(
            json.info.size
        ) {
            val material = informator.poolMaterials[
                json.info[it].texture
            ] ?: MGMMaterialShader.getDefault(
                informator.shaders.source
            )

            return@Array Pair(
                material,
                informator.shaders.cacheGeometryPass[
                    material.srcCodeMaterial
                ]
            )
        }

        val triggerAction = MGTriggerSimple(
            informator.drawerLightDirectional
        )

        map.spawnPoints.forEachIndexed { i, it ->
            val triggerMesh = MGTriggerMesh.createFromMeshPool(
                mesh[0],
                triggerAction
            )

            triggerMesh.matrix.run {
                setPosition(
                    it.position.x,
                    it.position.z+json.positionYDt,
                    it.position.y,
                )
                addRotation(
                    it.rotation.x+json.rotX,
                    it.rotation.z,
                    it.rotation.y,
                )
                invalidateScaleRotation()
                invalidatePosition()

                calculateInvertTrigger()
                calculateNormals()
            }

            val meshh = meshes[
                i % meshes.size
            ]

            informator.meshes.add(
                MGMMeshMaterial(
                    meshh.second,
                    MGDrawerMeshMaterialMutable(
                        arrayOf(
                            MGMaterial(
                                meshh.first.materialTexture
                            )
                        ),
                        triggerMesh.mesh
                    )
                )
            )
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
            ),
            105f
        ) ?: return null

        val prop = loaderProp.readProp(
            landscape
        )

        return MGMInstanceMesh(
            prop.shaderOpaque,
            instanceArray.vertexArray,
            prop.materials,
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