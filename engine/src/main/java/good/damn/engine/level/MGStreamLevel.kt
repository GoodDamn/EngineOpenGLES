package good.damn.engine.level

import android.graphics.Color
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
import good.damn.engine.models.json.spawn.MGMLevelSpawnLight
import good.damn.engine.opengl.drawers.MGDrawerMeshMaterialMutable
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.matrices.MGMatrixScaleRotation
import good.damn.engine.opengl.matrices.MGMatrixTransformationNormal
import good.damn.engine.opengl.models.MGMMeshMaterial
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGShaderMaterial
import good.damn.engine.opengl.shaders.base.binder.MGBinderAttribute
import good.damn.engine.opengl.triggers.MGTriggerLight
import good.damn.engine.opengl.triggers.MGTriggerMesh
import good.damn.engine.opengl.triggers.MGTriggerSimple
import good.damn.engine.sdk.SDVector3
import good.damn.engine.sdk.models.SDMLightPoint
import good.damn.engine.sdk.models.SDMLightPointInterpolation
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
                mapProp
            )?.run {
                flow.emit(
                    this
                )
            }
        //}

    }

    private fun processSpawnPoints(
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

        val poolMesh = informator.poolMeshes.loadOrGetFromCache(
            json.mesh,
            informator
        ) ?: return

        val triggerAction = MGTriggerSimple(
            informator.drawerLightDirectional
        )

        val lightInterpolation = SDMLightPointInterpolation(
            json.lightConstant,
            json.lightLinear,
            0f,
            json.lightRadius
        )

        val binderAttr = MGBinderAttribute.Builder()
            .bindPosition()
            .bindTextureCoordinates()
            .bindNormal()
            .build()
        val pointsInfo = Array(
            json.info.size
        ) {
            val info = json.info[it]
            val material = informator.poolMaterials.loadOrGetFromCache(
                info.texture,
                "textures/${info.texture}",
                informator
            )

            val lightJson = MGMLevelSpawnLight.createFromJson(
                MGUtilsJson.createFromFile(
                    MGUtilsFile.getPublicFile(
                        "lights/${info.light}.txt"
                    )
                )
            )

            return@Array Pair(
                Pair(
                    material,
                    SDMLightPoint(
                        SDVector3(
                            Color.red(
                                lightJson.color
                            ) / 255f,
                            Color.green(
                                lightJson.color
                            ) / 255f,
                            Color.blue(
                                lightJson.color
                            ) / 255f
                        ),
                        lightInterpolation,
                        1.0f
                    )
                ),
                informator.shaders.cacheGeometryPass.loadOrGetFromCache(
                    material.srcCodeMaterial,
                    informator.shaders.source.vert,
                    binderAttr,
                    arrayOf(
                        MGShaderMaterial(
                            material.shaderTextures
                        )
                    )
                )
            )
        }

        map.spawnPoints.forEachIndexed { i, it ->
            val triggerMesh = MGTriggerMesh.createFromMeshPool(
                poolMesh[0],
                triggerAction
            )

            val pointInfo = pointsInfo[
                i % pointsInfo.size
            ]

            val triggerLight = MGTriggerLight.createFromLight(
                pointInfo.first.second
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

            triggerLight.matrix.run {
                setPosition(
                    it.position.x,
                    it.position.z+json.positionYDt,
                    it.position.y,
                )
                radius = 20000f
                invalidatePosition()
                invalidateRadius()
                calculateInvertTrigger()
            }

            informator.meshes.add(
                MGMMeshMaterial(
                    pointInfo.second,
                    MGDrawerMeshMaterialMutable(
                        arrayOf(
                            MGMaterial(
                                pointInfo.first.first.materialTexture
                            )
                        ),
                        triggerMesh.mesh
                    )
                )
            )

            informator.managerTriggerLight.addTrigger(
                triggerLight.triggerState
            )
        }
    }

    private fun loadLandscape(
        landscape: MGMLevelInfoMesh,
        loaderMesh: MGLoaderLevelMeshA3D,
        loaderProp: MGLoaderLevelLibrary,
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
}