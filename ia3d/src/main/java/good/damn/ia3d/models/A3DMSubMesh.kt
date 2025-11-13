package good.damn.ia3d.models

import java.nio.IntBuffer
import java.nio.ShortBuffer

data class A3DMSubMesh(
    val indices: ShortBuffer,
    val smoothGroups: IntArray,
    val materialId: Int
)
