package good.damn.engine.utils

import good.damn.engine.opengl.enums.MGEnumArrayVertexConfiguration
import good.damn.engine.opengl.objects.MGObject3d
import good.damn.engine.sdk.MGVector3
import good.damn.ia3d.enums.A3DEnumTypeBufferVertex
import good.damn.ia3d.misc.A3DMVector2
import good.damn.ia3d.misc.A3DMVector3
import good.damn.ia3d.models.A3DMAsset
import good.damn.ia3d.models.A3DMConfigIndices
import good.damn.ia3d.models.A3DMMesh
import kotlin.math.tan

object MGUtilsA3D {

    @JvmStatic
    fun createConfigurationArrayVertex(
        config: A3DMConfigIndices
    ) = when (
        config.indexSize
    ) {
        2.toByte() -> MGEnumArrayVertexConfiguration.SHORT
        1.toByte() -> MGEnumArrayVertexConfiguration.BYTE
        else -> MGEnumArrayVertexConfiguration.INT
    }

    fun createMergedVertexBuffer(
        mesh: A3DMMesh,
        tangentBi: Pair<FloatArray, FloatArray>,
        uvScale: Float
    ) = MGUtilsArray.createMergedVertexBuffer(
        getBufferPosition(mesh),
        getBufferUv(mesh),
        mesh.vertexBuffers[
            A3DEnumTypeBufferVertex.NORMAL1.type - 1
        ]!!.vertices,
        tangentBi.first,
        tangentBi.second,
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