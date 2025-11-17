package good.damn.ia3d.models

import good.damn.ia3d.misc.A3DMVector3
import good.damn.ia3d.misc.A3DMVector4

data class A3DMTransform(
    val position: A3DMVector3,
    val rotation: A3DMVector4,
    val scale: A3DMVector3
)
