package good.damn.ia3d.models

import good.damn.ia3d.enums.A3DEnumTypeBufferVertex

data class A3DMBufferVertex(
    val vertices: FloatArray,
    val bufferType: A3DEnumTypeBufferVertex
)