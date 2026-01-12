package good.damn.engine.loaders.mesh

import good.damn.common.COHandlerGl
import good.damn.engine.models.MGMInstanceArray
import good.damn.common.matrices.MGMatrixScaleRotation
import good.damn.common.matrices.MGMatrixTransformationNormal
import good.damn.engine.utils.MGUtilsA3D
import good.damn.engine.utils.MGUtilsFile
import good.damn.engine.utils.MGUtilsInstancedMesh
import good.damn.ia3d.A3DImport
import good.damn.ia3d.stream.A3DInputStream
import java.io.FileInputStream

class MGLoaderLevelMeshA3D(
    private val localPathLibObj: String,
    private val handlerGl: COHandlerGl,
    private val buffer: ByteArray
) {
    fun loadInstanceArray(
        fileNameA3D: String,
        matrices: List<
            MGMatrixTransformationNormal<MGMatrixScaleRotation>
        >,
        uvScale: Float
    ): MGMInstanceArray? {
        val file = MGUtilsFile.getPublicFile(
            "$localPathLibObj/$fileNameA3D"
        )

        if (!file.exists()) {
            return null
        }

        val obj = A3DImport.createFromStream(
            A3DInputStream(
                buffer,
                FileInputStream(
                    file
                )
            ),
            buffer
        ) ?: return null

        val mesh = obj.meshes[0]
        val configIndices = mesh.subMeshes[0].indices

        return MGUtilsInstancedMesh.createVertexArrayInstance(
            MGUtilsA3D.createConfigurationArrayVertex(
                configIndices
            ),
            MGUtilsA3D.createMergedVertexBuffer(
                mesh,
                uvScale
            ),
            configIndices.buffer,
            matrices,
            handlerGl
        )
    }
}