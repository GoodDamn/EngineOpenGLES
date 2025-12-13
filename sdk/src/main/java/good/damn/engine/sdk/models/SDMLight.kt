package good.damn.engine.sdk.models

import good.damn.engine.sdk.SDVector3

data class SDMLight(
    val colorAmbient: SDVector3,
    val colorLight: SDVector3,
    val normalDirection: SDVector3
)