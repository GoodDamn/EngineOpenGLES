package good.damn.engine.sdk.models

import good.damn.engine.sdk.MGVector3

data class SDMLight(
    val colorAmbient: MGVector3,
    val colorLight: MGVector3,
    val normalDirection: MGVector3
)