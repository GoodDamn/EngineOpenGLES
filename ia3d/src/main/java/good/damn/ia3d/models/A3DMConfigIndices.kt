package good.damn.ia3d.models

import java.nio.Buffer

data class A3DMConfigIndices(
    val indexSize: Byte,
    val buffer: Buffer,
    val tangentBi: Pair<FloatArray, FloatArray>
)
