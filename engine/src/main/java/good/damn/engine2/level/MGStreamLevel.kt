package good.damn.engine2.level

import good.damn.engine2.flow.MGFlowLevel
import good.damn.engine2.loaders.MGLoaderLevelLibrary
import good.damn.engine2.loaders.MGLoaderLevelMatrices
import good.damn.engine2.loaders.MGLoaderLevelMatrices.Companion.fillModelMatrix
import good.damn.engine2.loaders.mesh.MGLoaderLevelMeshA3D
import good.damn.engine2.loaders.MGLoaderLevelTextures
import good.damn.engine2.models.MGMInstanceMesh
import good.damn.engine2.models.json.MGMLevelInfoMesh
import good.damn.common.matrices.COMatrixScaleRotation
import good.damn.common.matrices.COMatrixTransformationNormal
import good.damn.engine2.providers.MGMProviderGL
import good.damn.mapimporter.MIImportMap
import good.damn.mapimporter.models.MIMProp
import java.io.DataInputStream
import java.io.InputStream

object MGStreamLevel {

    private const val SLEVELS = "levels"
    private const val STEXTURES = "textures"
    private const val SOBJECTS = "objs"
    private const val SDIVISOR = "/"
    private const val FILENAME_LIBRARY = "library.txt"
    private const val FILENAME_CULLING = "culling.txt"
    private const val FILE_EXTENSION_A3D = ".a3d"

    @JvmStatic
    fun readBin(
        flow: MGFlowLevel<MGMInstanceMesh>,
        input: InputStream,
        bufferMap: ByteArray,
        glProvider: MGMProviderGL,
        additionalImports: List<MGIImportMapAdditional>?
    ) {
        val stream = DataInputStream(
            input
        )

        val map = MIImportMap.createFromStream(
            stream,
            bufferMap
        )

        val libName = map.atlases[0].rects[0].libraryName
        val localPathDir = SLEVELS + SDIVISOR + libName
        val localPathLibTextures = STEXTURES + SDIVISOR + libName
        val localPathLibObj = SOBJECTS + SDIVISOR + libName

        additionalImports?.forEach {
            it.import(
                map,
                localPathDir
            )
        }

        val loaderLib = MGLoaderLevelLibrary(
            localPathLibTextures,
            localPathDir + SDIVISOR + FILENAME_LIBRARY,
            localPathDir + SDIVISOR + FILENAME_CULLING,
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
            landscape.a3dMesh + FILE_EXTENSION_A3D,
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