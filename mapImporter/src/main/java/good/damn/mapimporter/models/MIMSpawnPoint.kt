package good.damn.mapimporter.models

import good.damn.mapimporter.misc.MIMVector3

data class MIMSpawnPoint(
    val position: MIMVector3,
    val rotation: MIMVector3,
    val type: Int
)