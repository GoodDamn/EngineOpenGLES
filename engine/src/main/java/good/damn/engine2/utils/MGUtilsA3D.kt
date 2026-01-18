package good.damn.engine2.utils

import good.damn.apigl.enums.GLEnumArrayVertexConfiguration
import good.damn.ia3d.enums.A3DEnumTypeBufferVertex
import good.damn.ia3d.models.A3DMConfigIndices
import good.damn.ia3d.models.A3DMMesh

object MGUtilsA3D {

    @JvmStatic
    fun createConfigurationArrayVertex(
        config: A3DMConfigIndices
    ) = when (
        config.indexSize
    ) {
        2.toByte() -> GLEnumArrayVertexConfiguration.SHORT
        1.toByte() -> GLEnumArrayVertexConfiguration.BYTE
        else -> GLEnumArrayVertexConfiguration.INT
    }

    fun createMergedVertexBuffer(
        mesh: A3DMMesh,
        uvScale: Float
    ) = MGUtilsArray.createMergedVertexBuffer(
        getBufferPosition(mesh),
        getBufferUv(mesh),
        mesh.vertexBuffers[
            A3DEnumTypeBufferVertex.NORMAL1.type - 1
        ]!!.vertices,
        mesh.subMeshes[0].indices.tangent,
        uvScale
    )

    private inline fun getBufferPosition(
        mesh: A3DMMesh
    ) = mesh.vertexBuffers[
        A3DEnumTypeBufferVertex.POSITION.type - 1
    ]!!.vertices

    private inline fun getBufferUv(
        mesh: A3DMMesh
    ) = mesh.vertexBuffers[
        A3DEnumTypeBufferVertex.UV1.type - 1
    ]!!.vertices
}