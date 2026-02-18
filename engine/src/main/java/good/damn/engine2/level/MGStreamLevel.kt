package good.damn.engine2.level

import good.damn.apigl.drawers.GLDrawerLightPoint
import good.damn.apigl.drawers.GLDrawerMaterialTexture
import good.damn.apigl.drawers.GLDrawerMesh
import good.damn.apigl.drawers.GLDrawerMeshMaterial
import good.damn.apigl.drawers.GLDrawerMeshMaterialNormals
import good.damn.apigl.drawers.GLDrawerNormalMatrix
import good.damn.apigl.drawers.GLDrawerPositionEntity
import good.damn.apigl.drawers.GLMaterial
import good.damn.apigl.drawers.GLVolumeLight
import good.damn.apigl.enums.GLEnumFaceOrder
import good.damn.apigl.shaders.GLShaderMaterial
import good.damn.apigl.shaders.base.GLBinderAttribute
import good.damn.engine2.flow.MGFlowLevel
import good.damn.engine2.loaders.MGLoaderLevelLibrary
import good.damn.engine2.loaders.MGLoaderLevelMatrices
import good.damn.engine2.loaders.MGLoaderLevelMatrices.Companion.fillModelMatrix
import good.damn.engine2.loaders.mesh.MGLoaderLevelMeshA3D
import good.damn.engine2.loaders.MGLoaderLevelTextures
import good.damn.engine2.models.MGMInstanceMesh
import good.damn.engine2.models.json.MGMLevelInfoMesh
import good.damn.engine2.models.json.spawn.MGMLevelSpawnInfo
import good.damn.engine2.models.json.spawn.MGMLevelSpawnLight
import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.common.matrices.COMatrixTransformationNormal
import good.damn.common.utils.COUtilsFile
import good.damn.engine2.opengl.models.MGMMeshDrawer
import good.damn.engine.sdk.models.SDMLightPoint
import good.damn.engine.sdk.models.SDMLightPointInterpolation
import good.damn.engine2.logic.MGMGeometryFrustrumMesh
import good.damn.engine2.logic.MGVolumeTriggerMesh
import good.damn.engine2.providers.MGMProviderGL
import good.damn.engine2.utils.MGUtilsJson
import good.damn.engine2.utils.MGUtilsVector3
import good.damn.logic.triggers.LGTriggerMesh
import good.damn.logic.triggers.stateables.LGTriggerStateableLight
import good.damn.mapimporter.MIImportMap
import good.damn.mapimporter.models.MIMMap
import good.damn.mapimporter.models.MIMProp
import java.io.DataInputStream
import java.io.InputStream

object MGStreamLevel {

    @JvmStatic
    fun readBin(
        flow: MGFlowLevel<MGMInstanceMesh>,
        input: InputStream,
        bufferMap: ByteArray,
        glProvider: MGMProviderGL,
        additionalImports: Array<MGIProviderMapImport>?
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

        additionalImports?.forEach {
            it.import(
                map,
                localPathDir
            )
        }

        val loaderLib = MGLoaderLevelLibrary(
            localPathLibTextures,
            "$localPathDir/library.txt",
            "$localPathDir/culling.txt",
            glProvider
        )

        val loaderTextures = MGLoaderLevelTextures(
            glProvider.pools.textures,
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
            glProvider.glHandler,
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

    private fun loadLandscape(
        landscape: MGMLevelInfoMesh,
        loaderMesh: MGLoaderLevelMeshA3D,
        loaderProp: MGLoaderLevelLibrary,
        mapProp: MIMProp?,
    ): MGMInstanceMesh? {
        val instanceArray = loaderMesh.loadInstanceArray(
            "${landscape.a3dMesh}.a3d",
            arrayListOf(
                COMatrixTransformationNormal(
                    COMatrixScaleRotation()
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