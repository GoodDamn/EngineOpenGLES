package good.damn.engine.loaders.mesh

import good.damn.engine.models.MGMMeshInstance
import good.damn.engine.models.MGProp
import good.damn.engine.opengl.entities.MGMaterial
import good.damn.engine.opengl.entities.MGMaterialTexture
import good.damn.engine.opengl.pools.MGPoolTextures
import good.damn.engine.opengl.shaders.MGShaderOpaque
import good.damn.engine.opengl.thread.MGHandlerGl
import good.damn.engine.utils.MGUtilsA3D
import good.damn.engine.utils.MGUtilsFile
import good.damn.ia3d.A3DImport
import good.damn.ia3d.stream.A3DInputStream
import java.io.FileInputStream

class MGLoaderLevelMeshA3D(
    private val poolTextures: MGPoolTextures,
    private val buffer: ByteArray,
    private val localPathLibObj: String,
    private val localPathLibTextures: String,
    private val handlerGl: MGHandlerGl
): MGILoaderMesh<MGProp> {

    override fun loadMeshInstance(
        it: MGProp
    ): Pair<MGShaderOpaque, MGMMeshInstance?>? {
        val file = MGUtilsFile.getPublicFile(
            "$localPathLibObj/${it.fileNameA3d}"
        )

        if (!file.exists()) {
            return null
        }

        val obj = A3DImport.createFromStream(
            A3DInputStream(
                FileInputStream(
                    file
                )
            ),
            buffer
        ) ?: return null

        it.materialTexture.load(
            poolTextures,
            localPathLibTextures,
            handlerGl
        )

        val material = MGMaterial(
            it.materialTexture
        )

        val mesh = obj.meshes[0]
        val configIndices = mesh.subMeshes[0].indices

        return Pair(
            it.shaderOpaque,
            MGILoaderMesh.createVertexArrayInstance(
                MGUtilsA3D.createConfigurationArrayVertex(
                    configIndices
                ),
                MGUtilsA3D.createMergedVertexBuffer(
                    mesh,
                    if (it.fileNameA3d.contains("errain"))
                        105f
                    else 1f
                ),
                configIndices.buffer,
                it.matrices.toTypedArray(),
                material,
                handlerGl,
                it.enableCullFace
            )
        )
    }
}