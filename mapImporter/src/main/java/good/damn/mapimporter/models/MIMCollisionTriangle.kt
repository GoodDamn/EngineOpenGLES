package good.damn.mapimporter.models

import good.damn.mapimporter.creators.MICreatorCollisionTriangle
import good.damn.mapimporter.misc.MIMVector3

data class MIMCollisionTriangle(
    val len: Double,
    val position: MIMVector3,
    val rotation: MIMVector3,
    val v0: MIMVector3,
    val v1: MIMVector3,
    val v2: MIMVector3
)
