package good.damn.engine.utils

import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.ia3d.enums.A3DEnumTypeBufferVertex
import good.damn.ia3d.models.A3DMAsset
import good.damn.ia3d.models.A3DMConfigIndices
import good.damn.ia3d.models.A3DMMesh

object MGUtilsA3D {

    fun createConfigurationArrayVertex(
        config: A3DMConfigIndices
    ) = when (
        config.indexSize
    ) {
        2.toByte() -> MGEnumArrayVertexConfiguration.SHORT
        1.toByte() -> MGEnumArrayVertexConfiguration.BYTE
        else -> MGEnumArrayVertexConfiguration.INT
    }

    inline fun createMergedVertexBuffer(
        mesh: A3DMMesh,
        uvScale: Float
    ) = MGUtilsArray.createMergedVertexBuffer(
        mesh.vertexBuffers[
            A3DEnumTypeBufferVertex.POSITION.type - 1
        ]!!.vertices,
        mesh.vertexBuffers[
            A3DEnumTypeBufferVertex.UV1.type - 1
        ]!!.vertices,
        mesh.vertexBuffers[
            A3DEnumTypeBufferVertex.NORMAL1.type - 1
        ]!!.vertices,
        uvScale
    )
}