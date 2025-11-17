package good.damn.mapimporter.models

import good.damn.mapimporter.creators.MICreatorCollisionPlane
import good.damn.mapimporter.misc.MIMVector3

data class MIMCollisionPlane(
    val len: Double,
    val position: MIMVector3,
    val rotation: MIMVector3,
    val width: Double
)
