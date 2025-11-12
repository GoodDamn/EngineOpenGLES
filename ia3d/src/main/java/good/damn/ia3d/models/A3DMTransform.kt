package good.damn.ia3d.models

import good.damn.ia3d.misc.A3DMVector3

data class A3DMTransform(
    val position: A3DMVector3,
    val rotation: A3DMVector3,
    val scale: A3DMVector3
)
