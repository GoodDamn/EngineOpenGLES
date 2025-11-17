package good.damn.mapimporter.models

import good.damn.mapimporter.creators.MICreatorCollisionBox
import good.damn.mapimporter.misc.MIMVector3

data class MIMCollisionBox(
    val position: MIMVector3,
    val rotation: MIMVector3,
    val scale: MIMVector3,
)