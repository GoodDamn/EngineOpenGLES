package good.damn.ia3d.models


data class A3DMMesh(
    val vertexBuffers: Array<A3DMBufferVertex?>,
    val subMeshes: Array<A3DMSubMesh>
)